package com.oldschool.bean;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
	private UploadedFile plantilla;
	private String nombeArchivoCargado;
	private byte[] tempBlob;
	//Filtro usuario
	private String filtroNombre;
	
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
		this.plantilla = null;
		this.filtroNombre = null;
	}
	
	public void crearPlantilla(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(Util.isEmpty(this.nombre)){
				validar = true;
			}if(this.plantilla==null){
				validar = true;
			}
			//Validar si el nombre de usuario ya existe
			if(!validar){
				
				if(existePlantilla(this.listaPlantillas, this.nombre)){
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ya existe un área con ese nombre.");
				}else{
					//Convertir FILE a BLOB para guardarlo en BD
//					byte[] blob = Util.convertirFileABlob(this.plantilla.getInputstream());
//					byte[] blob = this.plantilla.getContents();
					//Sobre cargar el objeto antes de guardarlo
					TipoDocumento plantilla = new TipoDocumento();
					plantilla.setNombre_Tipo_Documento(this.nombre);
					plantilla.setPlantilla(this.tempBlob);
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
			System.out.println("Llego al evento que maneja la carga");
			this.plantilla = event.getFile();
			this.nombeArchivoCargado = this.plantilla.getFileName();
			byte[] blob = event.getFile().getContents();
			this.tempBlob = blob;
			System.out.println(blob);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@SuppressWarnings("unchecked")
	public void filtrarArea(){
//		try {
//			//Filtro de nombre
//			if(!Util.isEmpty(filtroNombre)){
//				Map<String, Object> parametros = new HashMap<>();
//				parametros.put("nombre_Area", "%"+this.filtroNombre+"%");
//				this.listaAreas = (List<Area>)(List<?>)ejbGenerico.listarPorQuery(new Area(), "findByNombre", parametros);
//			}else{
//				//Mostrar todo
//				cargarListaAreas();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
//	
	public void editarArea(){
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
//	
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
	public UploadedFile getPlantilla() {
		return plantilla;
	}
	public void setPlantilla(UploadedFile plantilla) {
		this.plantilla = plantilla;
	}
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
}
