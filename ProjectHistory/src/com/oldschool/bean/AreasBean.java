package com.oldschool.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.oldschool.ejb.EjbGenericoLocal;
import com.oldschool.model.Area;
import com.oldschool.model.Proyecto;
import com.oldschool.util.Mensaje;
import com.oldschool.util.Util;

@ManagedBean(name= AreasBean.BEAN_NAME)
@ViewScoped
public class AreasBean implements Serializable {

	/*Variables Bean*/
	public static final String BEAN_NAME = "areasBean";
	private static final long serialVersionUID = 1461272576618969285L;
	//$ANALYSIS-IGNORE
	@EJB private EjbGenericoLocal ejbGenerico;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	//$ANALYSIS-IGNORE
	private List<Area> listaAreas;
	private Area areaSeleccionada;
	//Formulario de registro
	private String nombre;
	//Filtro usuario
	private String filtroNombre;
	
	/*Métodos privados*/
	@SuppressWarnings("unchecked")
	private void cargarListaAreas(){
		try {
			listaAreas = (List<Area>)(List<?>)ejbGenerico.listarTodo(new Area());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean existeArea(List<Area> lista, String nuevaArea){
		for (Area area : lista) {
			if(area.getNombre_Area().equals(nuevaArea)){
				return true;
			}
		}
		return false;
	}
	
	/*Métodos públicos*/
	@PostConstruct
	public void init(){
		limpiar();
		cargarListaAreas();
	}
	
	public void limpiar(){
		this.listaAreas = new ArrayList<>();
		this.areaSeleccionada = new Area();
		this.nombre = null;
		this.filtroNombre = null;
	}
	
	public void crearArea(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(Util.isEmpty(this.nombre)){
				validar = true;
			}
			//Validar si el nombre de usuario ya existe
			if(!validar){
				
				if(existeArea(this.listaAreas, this.nombre)){
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ya existe un área con ese nombre.");
				}else{
					Area area = new Area();
					area.setNombre_Area(this.nombre);
					//Registrar area
					boolean resultado = ejbGenerico.agregarObjeto(area);
					if(resultado){
						Mensaje.mostrarMensaje(Mensaje.INFO, "Se registró el area correctamente");
						limpiar();
						cargarListaAreas();
					}else{
						Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error creando el area, por favor intentelo de nuevo más tarde.");
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
	
	@SuppressWarnings("unchecked")
	public void filtrarArea(){
		try {
			//Filtro de nombre
			if(!Util.isEmpty(filtroNombre)){
				Map<String, Object> parametros = new HashMap<>();
				parametros.put("nombre_Area", "%"+this.filtroNombre+"%");
				this.listaAreas = (List<Area>)(List<?>)ejbGenerico.listarPorQuery(new Area(), "findByNombre", parametros);
			}else{
				//Mostrar todo
				cargarListaAreas();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editarArea(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(this.areaSeleccionada!=null){
				if(Util.isEmpty(this.areaSeleccionada.getNombre_Area())){
					validar = true;
				}
			}
			//Actualizar
			if(!validar){
				boolean result = ejbGenerico.actualizarObjeto(this.areaSeleccionada);
				if(result){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se actualizaron los datos del area exitosamente.");
					limpiar();
					cargarListaAreas();
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error actualizando los datos del area, por favor intentelo de nuevo más tarde.");
				}
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Hay campos vacíos, por favor verifique y vuelva a intentarlo");
			}
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void eliminarArea(){
		try {
			//Validar campos vacios
			if(this.areaSeleccionada!=null && !Util.isEmpty(this.areaSeleccionada.getId_Area())){
				//Revisar si el área tiene relacion con proyectos
				Map<String, Object> parametros = new HashMap<>();
				parametros.put("area", this.areaSeleccionada.getId_Area());
				List<Proyecto> listaProyectos = null;
				listaProyectos = (List<Proyecto>)(List<?>)ejbGenerico.listarPorQuery(new Proyecto(), "findByIdArea", parametros);
				
				//Si no hay resultados de la consulta se puede eliminar
				if(listaProyectos==null || listaProyectos.isEmpty()){
					//Puede eliminar
					boolean result = ejbGenerico.eliminarObjectoPorQuery(areaSeleccionada, areaSeleccionada.getId_Area());
					if(result){
						Mensaje.mostrarMensaje(Mensaje.INFO, "Se eliminó el area exitosamente.");
					}else{
						Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error eliminando el area, por favor intentelo de nuevo más tarde.");
					}
				}else{
					Mensaje.mostrarMensaje(Mensaje.WARN, "Esta área ya está asociada a proyectos activos, no se puede borrar.");
				}
				
				//Limpiar y cargar lista
				limpiar();
				cargarListaAreas();
				
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
	public List<Area> getListaAreas() {
		return listaAreas;
	}
	public void setListaAreas(List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}
	public Area getAreaSeleccionada() {
		return areaSeleccionada;
	}
	public void setAreaSeleccionada(Area areaSeleccionada) {
		this.areaSeleccionada = areaSeleccionada;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFiltroNombre() {
		return filtroNombre;
	}
	public void setFiltroNombre(String filtroNombre) {
		this.filtroNombre = filtroNombre;
	}
}
