package com.oldschool.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.sql.rowset.serial.SerialBlob;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.oldschool.ejb.EjbGenericoLocal;
import com.oldschool.ejb.EjbProyectosLocal;
import com.oldschool.model.Area;
import com.oldschool.model.Cliente;
import com.oldschool.model.Documento;
import com.oldschool.model.DocumentoAsociado;
import com.oldschool.model.Proyecto;
import com.oldschool.model.TipoDocumento;
import com.oldschool.util.Mensaje;
import com.oldschool.util.Util;

@ManagedBean(name= ProyectosBean.BEAN_NAME)
@SessionScoped
public class ProyectosBean implements Serializable {

	/*Variables Bean*/
	public static final String BEAN_NAME = "proyectosBean";
	private static final long serialVersionUID = 1461272576618969285L;
	@EJB private EjbGenericoLocal ejbGenerico;
	@EJB private EjbProyectosLocal ejbProyectos;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	/*Variables*/
	private List<Proyecto> listaProyectos;
	private List<Cliente> listaClientes;
	private List<Area> listaAreas;
	private Proyecto proyectoSeleccionado;
	
	//Formulario de registro
	private char caracterControl;
	private int idArea;
	private int idCliente;
	private TipoDocumento tipoDocumentalSeleccionado;
	private TipoDocumento tipoDocAgregar;
	private List<DocumentoAsociado> listaDocumentosAsocEliminar; //Se eliminara la relacion en BD
	
	//Formulario de edicion
	private List<TipoDocumento> listaTipoDocumentos;
	private List<DocumentoAsociado> listaDocumentosAsociados;
	private DocumentoAsociado documentoSeleccionado;
	private String nombreNuevoDocumento;
	private String nombeArchivoCargado;
	private byte[] tempBlob;
	private long tamanioDocumento;
	
	//Filtro formulario principal
	private String filtroNombre;
	//Filtro formulario asociacion
	private int filtroTipoDoc;
	private String filtroNombreDoc;
	
	/*Métodos privados*/
	@SuppressWarnings("unchecked")
	private void cargarListaAreas(){
		try {
			listaAreas = (List<Area>)(List<?>)ejbGenerico.listarTodo(new Area());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarListaProyectos(){
		try {
			listaProyectos = (List<Proyecto>)(List<?>)ejbGenerico.listarTodo(new Proyecto());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarListaClientes(){
		try {
			listaClientes = (List<Cliente>)(List<?>)ejbGenerico.listarTodo(new Cliente());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarListaTipoDocumentos(){
		try {
			listaTipoDocumentos = (List<TipoDocumento>)(List<?>)ejbGenerico.listarTodo(new TipoDocumento());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarListaDocumentos(int idProyecto){
		try {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("id_Proyecto", idProyecto);
			this.listaDocumentosAsociados = (List<DocumentoAsociado>)(List<?>)ejbGenerico.listarPorQuery(new DocumentoAsociado(), "findByProyecto", parametros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean existeProyecto(List<Proyecto> lista, String nuevoProyecto){
		for (Proyecto proyecto: lista) {
			if(proyecto.getNombre_Proyecto().equals(nuevoProyecto)){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private Cliente buscarClienteEnLista(int idCliente){
		for (Cliente cliente : listaClientes) {
			if(cliente.getId_Cliente() == idCliente){
				return cliente;
			}
		}
		return null;
	}
	
	private boolean editarProyecto(){
		try {
			proyectoSeleccionado.setCliente(new Cliente(this.idCliente));
			proyectoSeleccionado.setArea(new Area(this.idArea));
			proyectoSeleccionado.setFecha_Creacion_Proyecto(new Date());
			if(sesionBean.getUsuario() != null){
				proyectoSeleccionado.setUsuario(sesionBean.getUsuario());						
			}
			
			//Registrar proyecto
			boolean resActualizacion = ejbGenerico.actualizarObjeto(proyectoSeleccionado);
			if(resActualizacion){
				//Eliminar relaciones de documentos asociados
				if(ejbProyectos.quitarDocumentos(listaDocumentosAsocEliminar)){
					System.out.println("Se quitaron los documentos asociados al proyecto");
				}
				//Agregar los nuevos documentos
				List<DocumentoAsociado> temp = new ArrayList<>();
				for (int j = 0; j < listaDocumentosAsociados.size(); j++) {
					DocumentoAsociado doc = listaDocumentosAsociados.get(j);
					//Solo se deben agregar los nuevos docuemtnos
					if(doc.getId_Documento_Asociado()==0){
						doc.setProyecto(new Proyecto(proyectoSeleccionado.getId_Proyecto()));
						doc.setUsuario(sesionBean.getUsuario());
						temp.add(doc);
					}
				}
				boolean resultado = ejbProyectos.asociarDocumentos(temp);
				if(resultado){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se actualizó el proyecto correctamente");
					limpiar();
					cargarListaProyectos();
					
					return true;
					
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error asociando documentos del proyecto, por favor intentelo de nuevo más tarde.");
				}
			}else{
				Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error actualizando el proyecto, por favor intentelo de nuevo más tarde.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	private boolean registrarProyecto(){
		try {
			//Validar si el nombre del proyecto ya existe
			if(existeProyecto(listaProyectos, proyectoSeleccionado.getNombre_Proyecto())){
				Mensaje.mostrarMensaje(Mensaje.ERROR, "Ya existe un proyecto con ese nombre.");
			}else{
				Proyecto proyecto = new Proyecto();
				proyecto.setNombre_Proyecto(this.proyectoSeleccionado.getNombre_Proyecto());
				proyecto.setDescripcion(this.proyectoSeleccionado.getDescripcion());
				proyecto.setEstado(this.proyectoSeleccionado.getEstado());
				proyecto.setCliente(new Cliente(this.idCliente));
				proyecto.setArea(new Area(this.idArea));
				proyecto.setFecha_Creacion_Proyecto(new Date());
				if(sesionBean.getUsuario() != null){
					proyecto.setUsuario(sesionBean.getUsuario());						
				}
				//Registrar proyecto
				int id = ejbProyectos.crearProyecto(proyecto);
				if(id!=0){
					for (int j = 0; j < listaDocumentosAsociados.size(); j++) {
						DocumentoAsociado doc = listaDocumentosAsociados.get(j);
						doc.setProyecto(new Proyecto(id));
						doc.setUsuario(sesionBean.getUsuario());
						listaDocumentosAsociados.set(j, doc);
					}
					boolean resultado = ejbProyectos.asociarDocumentos(listaDocumentosAsociados);
					if(resultado){
						Mensaje.mostrarMensaje(Mensaje.INFO, "Se registró el proyecto correctamente");
						limpiar();
						cargarListaProyectos();
						
						return true;
						
					}else{
						Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error asociando los documentos, por favor intentelo de nuevo más tarde.");
					}
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error creando el proyecto, por favor intentelo de nuevo más tarde.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	/*Métodos públicos*/
	@PostConstruct
	public void init(){
		limpiar();
		cargarListaProyectos();
		cargarListaClientes();
		cargarListaAreas();
	}
	
	public void limpiar(){
		this.listaProyectos = new ArrayList<>();
		this.proyectoSeleccionado = new Proyecto(); 
		this.idArea = 0;
		this.idCliente = 0;
		this.filtroNombre = null;
	}
	
	public void limpiarProyectosForm(){
		this.idArea = 0;
		this.idCliente = 0;
		this.tipoDocumentalSeleccionado = new TipoDocumento();
		this.tipoDocAgregar = new TipoDocumento();
		this.listaDocumentosAsocEliminar = new ArrayList<>();
	}
	
	public void limpiarAsociacionForm(){
		this.documentoSeleccionado = null;
		this.nombeArchivoCargado = null;
		this.nombreNuevoDocumento = null;
		this.tempBlob = null;
		this.tamanioDocumento = 0;
		this.filtroTipoDoc = 0;
		this.filtroNombreDoc = null;
		this.listaDocumentosAsociados = new ArrayList<>();
		this.listaTipoDocumentos = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	public void filtrarProyectos(){
		try {
			//Filtro de nombre
			if(!Util.isEmpty(filtroNombre)){
				Map<String, Object> parametros = new HashMap<>();
				parametros.put("nombre_Proyecto", "%"+this.filtroNombre+"%");
				this.listaProyectos = (List<Proyecto>)(List<?>)ejbGenerico.listarPorQuery(new Proyecto(), "findByNombre", parametros);
			}else{
				//Mostrar todo
				cargarListaProyectos();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Formulario registro documentos*/
	public String irControlProyecto(){
		try {
			limpiarProyectosForm();
			limpiarAsociacionForm();
			cargarListaTipoDocumentos();
			//Si es edicion
			if(this.caracterControl == 'E' && this.proyectoSeleccionado!=null && !Util.isEmpty(this.proyectoSeleccionado.getId_Proyecto())){
				cargarListaDocumentos(this.proyectoSeleccionado.getId_Proyecto());
				this.idArea = this.proyectoSeleccionado.getArea().getId_Area();
				this.idCliente = this.proyectoSeleccionado.getCliente().getId_Cliente();
			}
			//Si es creacion
			else{
				this.proyectoSeleccionado = new Proyecto();
				this.proyectoSeleccionado.setUsuario(this.sesionBean.getUsuario());
				this.proyectoSeleccionado.setFecha_Creacion_Proyecto(new Date());
			}
			return "irControlProyectos";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public void quitarTipoDocumento(){
		if(this.tipoDocumentalSeleccionado!=null && !Util.isEmpty(this.tipoDocumentalSeleccionado.getId_Tipo_Documento())){
			for (int i = 0; i < listaDocumentosAsociados.size(); i++) {
				DocumentoAsociado doc = listaDocumentosAsociados.get(i);
				if(doc.getTipoDocumento().getId_Tipo_Documento() == this.tipoDocumentalSeleccionado.getId_Tipo_Documento()){
					//Validar si ya existía antes de la modificación
					if(doc.getId_Documento_Asociado()!=0){
						//Debe ir a una lista diferente, pues ya existía y hay que eliminar el registro de BD
						listaDocumentosAsocEliminar.add(doc);
					}
					listaDocumentosAsociados.remove(i);
					break;
				}
			}
		}
	}

	public void agregaTipoDocumentoLista(){
		if(this.tipoDocAgregar!=null && !Util.isEmpty(this.tipoDocAgregar.getId_Tipo_Documento())){
			
			//Validar que no haya duplicidad
			boolean validar = false;
			for (DocumentoAsociado doc : listaDocumentosAsociados) {
				if(doc.getTipoDocumento().getId_Tipo_Documento() == this.tipoDocAgregar.getId_Tipo_Documento()){
					validar = true;
					break;
				}
			}
			if(!validar){
				DocumentoAsociado tempDoc = new DocumentoAsociado();
				tempDoc.setTipoDocumento(this.tipoDocAgregar);
				listaDocumentosAsociados.add(tempDoc);
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Ya hay un tipo documental igual en la lista.");
			}
		}
	}
	
	public String guardarCambiosProyecto(){
		try {

			//Validar campos vacios
			boolean validar = false;
			if(Util.isEmpty(this.proyectoSeleccionado.getNombre_Proyecto()) || Util.isEmpty(this.proyectoSeleccionado.getEstado())){
				validar = true;
			}
			if(Util.isEmpty(this.idArea) || Util.isEmpty(this.idCliente)){
				validar = true;
			}
			
			if(this.caracterControl == 'E' && !validar && editarProyecto()){
				return "control-proyectos";
				
			}else if(this.caracterControl == 'R' && !validar && registrarProyecto()){
				return "control-proyectos";
				
			}else{
				Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error, por favor intentelo de nuevo más tarde.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*Formulario de carga de documentos*/
	public String irAsociarDocumentos(){
		if(this.proyectoSeleccionado!=null && !Util.isEmpty(this.proyectoSeleccionado.getId_Proyecto())){
			//Cargar página
			limpiarAsociacionForm();
			cargarListaTipoDocumentos();
			cargarListaDocumentos(this.proyectoSeleccionado.getId_Proyecto());
			//Redireccionar
			return "irAsociarDocumentos";
		}
		return "";
	}
	
	@SuppressWarnings({ "unchecked", "resource" })
	public StreamedContent descargarDocumento(){
		StreamedContent download = new DefaultStreamedContent();
		try {
			
			if(documentoSeleccionado!=null && documentoSeleccionado.getId_Documento_Asociado()!=0){
				
				//Buscar los documentos asociados y obtener el último
				Map<String, Object> parametros = new HashMap<>();
				parametros.put("id_Documento_Asociado", this.documentoSeleccionado.getId_Documento_Asociado());
				//parametros.put("estado", Documento.ESTADO_APROBADO);
				List<Documento> documentos = (List<Documento>)(List<?>)ejbGenerico.listarPorQuery(new Documento(), "findAprobadosByIdDocAsociado", parametros);
				
				if(documentos!=null && !documentos.isEmpty()){
					//La lista esta ordenada por fecha, y se toma el registro aprobado mas reciente como predeterminado.
					Documento docPredeterminado = documentos.get(0);
					
					Blob blob = new SerialBlob( docPredeterminado.getDocumento() );
					InputStream is = blob.getBinaryStream();
					
					String nombreArchivo = docPredeterminado.getNombreDocumento();
					String pathArchivo = System.getProperty("java.io.tmpdir") + File.separator + nombreArchivo + "." + docPredeterminado.getTipoArchivo();
					FileOutputStream fos = new FileOutputStream( pathArchivo );
					int b = 0;
					while ((b = is.read()) != -1){
						fos.write(b); 
					}
					
					is.close();
					fos.flush();
					
					InputStream stream = new FileInputStream( pathArchivo );
					download = new DefaultStreamedContent(stream, Files.probeContentType( Paths.get(pathArchivo) ), docPredeterminado.getNombreDocumento() + "." + docPredeterminado.getTipoArchivo());
					
				}else{
					Mensaje.mostrarMensaje("No se puede descargar el documento debido a que esta en proceso de aprobación.");
				}
				
			}else{
				Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error, por favor intentelo de nuevo más tarde.");
				System.err.println("El objeto 'documentoSeleccionado' está vacío ");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return download;
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		try {
			//System.out.println("Llego al evento que maneja la carga");
			this.nombeArchivoCargado = event.getFile().getFileName();
			this.tempBlob = event.getFile().getContents();
			this.tamanioDocumento = event.getFile().getSize();
			if(this.tamanioDocumento > 1000000){
				this.tamanioDocumento *= 0.001;
			}else if(this.tamanioDocumento > 1000){
				this.tamanioDocumento *= 0.001;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void cargarDocumento(){
		
		try {
			//Validar campos vacios
			boolean validar = false;
			if(this.documentoSeleccionado==null || Util.isEmpty(this.nombreNuevoDocumento) || this.tempBlob==null){
				validar = true;
			}
			//Validar si el nombre de usuario ya existe
			if(!validar){
				//Obtener extension de documentol
				String[] nombreDoc = nombeArchivoCargado.split("\\.");
				String extension = nombreDoc.length > 0 ? nombreDoc[nombreDoc.length-1] : "txt";
				
				Date fechaActual = new Date();
				
				//Registrar documento y asociarlo al tipo de doc
				Documento documento = new Documento();
				documento.setNombreDocumento(this.nombreNuevoDocumento);
				documento.setFechaCargue(fechaActual);
				documento.setDocumento(this.tempBlob);
				documento.setTamanioDocumento(this.tamanioDocumento);
				documento.setTipoArchivo(extension);
				documento.setEstado(Documento.ESTADO_PENDIENTE);
				documento.setDocumentoAsociado(documentoSeleccionado);
				
				//Actualizar Usuario creador del documento
				documentoSeleccionado.setUsuario(sesionBean.getUsuario());
				//documentoSeleccionado.setFechaModificacion(fechaActual); //No se actualizará la fecha hasta que se apruebe el documento

				//Registrar documento
				if(ejbGenerico.agregarObjeto(documento)){
					//Actualizar usuario creador
					if(ejbGenerico.actualizarObjeto(documentoSeleccionado)){
						Mensaje.mostrarMensaje(Mensaje.INFO, "Se cargó el documento, estará disponible para su descarga despues del proceso de aprobación.");
					}
					limpiarAsociacionForm();
					cargarListaDocumentos(this.proyectoSeleccionado.getId_Proyecto());
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error actualizando el documento, por favor intentelo de nuevo más tarde.");
				}
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Hay campos vacíos, por favor verifique y vuelva a intentarlo");
			}
			
			
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
			e.printStackTrace();
		}
	
	}
	
	/*Get & Set*/
	public SesionBean getSesionBean() {
		return sesionBean;
	}
	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}
	public List<Proyecto> getListaProyectos() {
		return listaProyectos;
	}
	public void setListaProyectos(List<Proyecto> listaProyectos) {
		this.listaProyectos = listaProyectos;
	}
	public List<Cliente> getListaClientes() {
		return listaClientes;
	}
	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}
	public List<Area> getListaAreas() {
		return listaAreas;
	}
	public void setListaAreas(List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}
	public Proyecto getProyectoSeleccionado() {
		return proyectoSeleccionado;
	}
	public void setProyectoSeleccionado(Proyecto proyectoSeleccionado) {
		this.proyectoSeleccionado = proyectoSeleccionado;
		if(proyectoSeleccionado.getArea()!=null){
			this.idArea = proyectoSeleccionado.getArea().getId_Area();
		}
		if(proyectoSeleccionado.getCliente()!=null){
			this.idCliente = proyectoSeleccionado.getCliente().getId_Cliente();
		}
	}
	public int getIdArea() {
		return idArea;
	}
	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}
	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	public String getFiltroNombre() {
		return filtroNombre;
	}
	public void setFiltroNombre(String filtroNombre) {
		this.filtroNombre = filtroNombre;
	}
	public int getFiltroTipoDoc() {
		return filtroTipoDoc;
	}
	public void setFiltroTipoDoc(int filtroTipoDoc) {
		this.filtroTipoDoc = filtroTipoDoc;
	}
	public String getFiltroNombreDoc() {
		return filtroNombreDoc;
	}
	public void setFiltroNombreDoc(String filtroNombreDoc) {
		this.filtroNombreDoc = filtroNombreDoc;
	}
	public List<TipoDocumento> getListaTipoDocumentos() {
		return listaTipoDocumentos;
	}
	public void setListaTipoDocumentos(List<TipoDocumento> listaTipoDocumentos) {
		this.listaTipoDocumentos = listaTipoDocumentos;
	}
	public List<DocumentoAsociado> getListaDocumentosAsociados() {
		return listaDocumentosAsociados;
	}
	public void setListaDocumentosAsociados(List<DocumentoAsociado> listaDocumentosAsociados) {
		this.listaDocumentosAsociados = listaDocumentosAsociados;
	}
	public DocumentoAsociado getDocumentoSeleccionado() {
		return documentoSeleccionado;
	}
	public void setDocumentoSeleccionado(DocumentoAsociado documentoSeleccionado) {
		this.documentoSeleccionado = documentoSeleccionado;
//		if(!Util.isEmpty(this.documentoSeleccionado.getNombre_Documento())){
//			this.nombreNuevoDocumento = this.documentoSeleccionado.getNombre_Documento();
//		}
	}
	public char getCaracterControl() {
		return caracterControl;
	}
	public void setCaracterControl(char caracterControl) {
		this.caracterControl = caracterControl;
	}
	public String getNombeArchivoCargado() {
		return nombeArchivoCargado;
	}
	public void setNombeArchivoCargado(String nombeArchivoCargado) {
		this.nombeArchivoCargado = nombeArchivoCargado;
	}
	public byte[] getTempBlob() {
		return tempBlob;
	}
	public void setTempBlob(byte[] tempBlob) {
		this.tempBlob = tempBlob;
	}
	public long getTamanioDocumento() {
		return tamanioDocumento;
	}
	public void setTamanioDocumento(long tamanioDocumento) {
		this.tamanioDocumento = tamanioDocumento;
	}
	public String getNombreNuevoDocumento() {
		return nombreNuevoDocumento;
	}
	public void setNombreNuevoDocumento(String nombreNuevoDocumento) {
		this.nombreNuevoDocumento = nombreNuevoDocumento;
	}
	public TipoDocumento getTipoDocumentalSeleccionado() {
		return tipoDocumentalSeleccionado;
	}
	public void setTipoDocumentalSeleccionado(TipoDocumento tipoDocumentalSeleccionado) {
		this.tipoDocumentalSeleccionado = tipoDocumentalSeleccionado;
	}
	public TipoDocumento getTipoDocAgregar() {
		return tipoDocAgregar;
	}
	public void setTipoDocAgregar(TipoDocumento tipoDocAgregar) {
		this.tipoDocAgregar = tipoDocAgregar;
	}
}
