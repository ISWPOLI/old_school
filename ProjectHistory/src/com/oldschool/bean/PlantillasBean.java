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
import javax.faces.bean.ViewScoped;
import javax.sql.rowset.serial.SerialBlob;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.oldschool.ejb.EjbGenericoLocal;
import com.oldschool.model.TipoDocumento;
import com.oldschool.util.Mensaje;
import com.oldschool.util.Util;

@ManagedBean(name= PlantillasBean.BEAN_NAME)
@ViewScoped
public class PlantillasBean implements Serializable {

	/*Variables Bean*/
	public static final String BEAN_NAME = "plantillasBean";
	private static final long serialVersionUID = 1461272576618969285L;
	@EJB private EjbGenericoLocal ejbGenerico;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	/*Variables*/
	private List<TipoDocumento> listaPlantillas;
	private TipoDocumento plantillaSeleccionada;
	//Formulario de registro
	private String nombre;
//	private UploadedFile plantilla;
	private String nombeArchivoCargado;
	private byte[] tempBlob;
	private long tamanioDocumento;
	//Filtro usuario
	private String filtroNombre;
	//Descargable
	private StreamedContent plantillaDescargable;
	
	/*Métodos privados*/
	@SuppressWarnings("unchecked")
	private void cargarListaPlantillas(){
		try {
			listaPlantillas = (List<TipoDocumento>)(List<?>)ejbGenerico.listarTodo(new TipoDocumento());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean existePlantilla(List<TipoDocumento> lista, String nuevaPlantilla){
		for (TipoDocumento plantilla : lista) {
			if(plantilla.getNombre_Tipo_Documento().equals(nuevaPlantilla)){
				return true;
			}
		}
		return false;
	}
	
	/*Métodos públicos*/
	@PostConstruct
	public void init(){
		limpiar();
		cargarListaPlantillas();
	}
	
	public void limpiar(){
		this.listaPlantillas = new ArrayList<>();
		this.plantillaSeleccionada = new TipoDocumento();
		this.nombre = null;
		this.nombeArchivoCargado = null;
		this.tempBlob = null;
		this.tamanioDocumento = 0;
		this.filtroNombre = null;
		this.plantillaDescargable = null;
	}
	
	public void crearPlantilla(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(Util.isEmpty(this.nombre)){
				validar = true;
			}if(this.tempBlob==null){
				validar = true;
			}
			//Validar si el nombre de usuario ya existe
			if(!validar){
				
				if(existePlantilla(this.listaPlantillas, this.nombre)){
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ya existe un área con ese nombre.");
				}else{
					//Obtener extension de documentol
					String[] nombreDoc = nombeArchivoCargado.split("\\.");
					String extension = nombreDoc.length > 0 ? nombreDoc[nombreDoc.length-1] : "txt";
					//Sobre cargar el objeto antes de guardarlo
					TipoDocumento plantilla = new TipoDocumento();
					plantilla.setNombre_Tipo_Documento(this.nombre);
					plantilla.setPlantilla(this.tempBlob);
					plantilla.setTipo_archivo(extension);
					plantilla.setFecha_modificacion(new Date());
					plantilla.setTamanio_documento(tamanioDocumento);
					//Registrar plantilla
					boolean resultado = ejbGenerico.agregarObjeto(plantilla);
					if(resultado){
						Mensaje.mostrarMensaje(Mensaje.INFO, "Se registró la plantilla correctamente");
						limpiar();
						cargarListaPlantillas();
					}else{
						Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error cargando la plantilla, por favor intentelo de nuevo más tarde.");
					}
				}
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Hay campos vacíos, por favor verifique y vuelva a intentarlo");
			}
			
			
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
			e.printStackTrace();
		}
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
	
	@SuppressWarnings("unchecked")
	public void filtrarPlantillas(){
		try {
			//Filtro de nombre
			if(!Util.isEmpty(filtroNombre)){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("nombre_Tipo_Documento", "%"+this.filtroNombre.toLowerCase()+"%");
				this.listaPlantillas = (List<TipoDocumento>)(List<?>)ejbGenerico.listarPorQuery(new TipoDocumento(), "findByNombre", parametros);
			}else{
				//Mostrar todo
				cargarListaPlantillas();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	
	public void editarPlantilla(){
		
		try {
			//Validar campos vacios
			boolean validar = false;
			if(this.plantillaSeleccionada==null){
				validar = true;
			}if(Util.isEmpty(this.plantillaSeleccionada.getNombre_Tipo_Documento())){
				validar = true;
			}if(this.tempBlob==null){
				validar = true;
			}
			//Validar si el nombre de usuario ya existe
			if(!validar){
				//Obtener extension de documentol
				String[] nombreDoc = nombeArchivoCargado.split("\\.");
				String extension = nombreDoc.length > 0 ? nombreDoc[nombreDoc.length-1] : "txt";
				//Sobre cargar el objeto antes de guardarlo
//				TipoDocumento plantilla = new TipoDocumento();
				plantillaSeleccionada.setPlantilla(this.tempBlob);
				plantillaSeleccionada.setTipo_archivo(extension);
				plantillaSeleccionada.setFecha_modificacion(new Date());
				plantillaSeleccionada.setTamanio_documento(tamanioDocumento);
				//Registrar plantilla
				boolean resultado = ejbGenerico.actualizarObjeto(plantillaSeleccionada);
				if(resultado){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se actualizó la plantilla correctamente");
					limpiar();
					cargarListaPlantillas();
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error actualizando la plantilla, por favor intentelo de nuevo más tarde.");
				}
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Hay campos vacíos, por favor verifique y vuelva a intentarlo");
			}
			
			
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
			e.printStackTrace();
		}
	
		
		
		
//		try {
//			//Validar campos vacios
//			boolean validar = false;
//			if(this.areaSeleccionada!=null){
//				if(Util.isEmpty(this.areaSeleccionada.getNombre_Area())){
//					validar = true;
//				}
//			}
//			//Actualizar
//			if(!validar){
//				boolean result = ejbGenerico.actualizarObjeto(this.areaSeleccionada);
//				if(result){
//					Mensaje.mostrarMensaje(Mensaje.INFO, "Se actualizaron los datos del area exitosamente.");
//					limpiar();
//					cargarListaAreas();
//				}else{
//					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error actualizando los datos del area, por favor intentelo de nuevo más tarde.");
//				}
//			}else{
//				Mensaje.mostrarMensaje(Mensaje.WARN, "Hay campos vacíos, por favor verifique y vuelva a intentarlo");
//			}
//		} catch (Exception e) {
//			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
//			e.printStackTrace();
//		}
	}
	
	@SuppressWarnings("resource")
	public StreamedContent descargarPlantilla(){
		StreamedContent download = new DefaultStreamedContent();
		try {
			if(this.plantillaSeleccionada!=null && this.plantillaSeleccionada.getPlantilla()!=null){
				
				Blob blob = new SerialBlob( this.plantillaSeleccionada.getPlantilla() );
				InputStream is = blob.getBinaryStream();
				
				String nombreArchivo = this.plantillaSeleccionada.getNombre_Tipo_Documento();
				String pathArchivo = System.getProperty("java.io.tmpdir") + File.separator + nombreArchivo + "." + this.plantillaSeleccionada.getTipo_archivo();
				FileOutputStream fos = new FileOutputStream( pathArchivo );
				int b = 0;
				while ((b = is.read()) != -1){
				    fos.write(b); 
				}
				
				is.close();
				fos.flush();

				InputStream stream = new FileInputStream( pathArchivo );
				download = new DefaultStreamedContent(stream, Files.probeContentType( Paths.get(pathArchivo) ), this.plantillaSeleccionada.getNombre_Tipo_Documento() + "." + this.plantillaSeleccionada.getTipo_archivo());
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return download;
	}
	
	/*Get & Set*/
	public SesionBean getSesionBean() {
		return sesionBean;
	}
	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}
	public List<TipoDocumento> getListaPlantillas() {
		return listaPlantillas;
	}
	public void setListaPlantillas(List<TipoDocumento> listaPlantillas) {
		this.listaPlantillas = listaPlantillas;
	}
	public TipoDocumento getPlantillaSeleccionada() {
		return plantillaSeleccionada;
	}
	public void setPlantillaSeleccionada(TipoDocumento plantillaSeleccionada) {
		this.plantillaSeleccionada = plantillaSeleccionada;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
//	public UploadedFile getPlantilla() {
//		return plantilla;
//	}
//	public void setPlantilla(UploadedFile plantilla) {
//		this.plantilla = plantilla;
//	}
	public String getFiltroNombre() {
		return filtroNombre;
	}
	public void setFiltroNombre(String filtroNombre) {
		this.filtroNombre = filtroNombre;
	}
	public String getNombeArchivoCargado() {
		return nombeArchivoCargado;
	}
	public void setNombeArchivoCargado(String nombeArchivoCargado) {
		this.nombeArchivoCargado = nombeArchivoCargado;
	}
	public StreamedContent getPlantillaDescargable() {
		return plantillaDescargable;
	}
	public void setPlantillaDescargable(StreamedContent plantillaDescargable) {
		this.plantillaDescargable = plantillaDescargable;
	}
}
