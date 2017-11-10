package com.oldschool.bean;

import java.io.Serializable;
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

import com.oldschool.ejb.EjbGenericoLocal;
import com.oldschool.model.Area;
import com.oldschool.model.Cliente;
import com.oldschool.model.Proyecto;
import com.oldschool.util.Mensaje;
import com.oldschool.util.Util;

@ManagedBean(name= ProyectosBean.BEAN_NAME)
@ViewScoped
public class ProyectosBean implements Serializable {

	/*Variables Bean*/
	public static final String BEAN_NAME = "proyectosBean";
	private static final long serialVersionUID = 1461272576618969285L;
	@EJB private EjbGenericoLocal ejbGenerico;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	/*Variables*/
	private List<Proyecto> listaProyectos;
	private List<Cliente> listaClientes;
	private List<Area> listaAreas;
	private Proyecto proyectoSeleccionado;
	
	//Formulario de registro
	private String nombre;
	private String descripcion;
	private String estado;
	private int idArea;
	private int idCliente;
	//Formulario de edicion
	private int idAreaMod;
	private int idClienteMod;
	
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
		this.nombre = null;
		this.descripcion = null;
		this.estado = null;
		this.idArea = 0;
		this.idCliente = 0;
		this.filtroNombre = null;
	}
	
	public void crearProyecto(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(Util.isEmpty(this.nombre) || Util.isEmpty(this.descripcion) || Util.isEmpty(this.estado)){
				validar = true;
			}
			if(Util.isEmpty(this.idArea) || Util.isEmpty(this.idCliente)){
				validar = true;
			}
			if(!validar){
				//Validar si el nombre del proyecto ya existe
				if(existeProyecto(listaProyectos, this.nombre)){
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ya existe un proyecto con ese nombre.");
				}else{
					Proyecto proyecto = new Proyecto();
					proyecto.setNombre_Proyecto(this.nombre);
					proyecto.setDescripcion(this.descripcion);
					proyecto.setEstado(this.estado);
					proyecto.setCliente(new Cliente(this.idCliente));
					proyecto.setArea(new Area(this.idArea));
					proyecto.setFecha_Creacion_Proyecto(new Date());
					if(sesionBean.getUsuario() != null){
						proyecto.setUsuario(sesionBean.getUsuario());						
					}
					//Registrar proyecto
					boolean resultado = ejbGenerico.agregarObjeto(proyecto);
					if(resultado){
						Mensaje.mostrarMensaje(Mensaje.INFO, "Se registró el proyecto correctamente");
						limpiar();
						cargarListaProyectos();
					}else{
						Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error creando el proyecto, por favor intentelo de nuevo más tarde.");
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
	
	public void editarProyecto(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(this.proyectoSeleccionado!=null){
				if(Util.isEmpty(this.proyectoSeleccionado.getNombre_Proyecto())
						|| Util.isEmpty(this.proyectoSeleccionado.getDescripcion())
						|| Util.isEmpty(this.proyectoSeleccionado.getEstado())){
					validar = true;
				}
				if(Util.isEmpty(this.idAreaMod) || Util.isEmpty(this.idClienteMod)){
					validar = true;
				}
			}
			//Actualizar
			if(!validar){
				//Re-asignar los valores de los combos
				this.proyectoSeleccionado.setArea(new Area(this.idAreaMod));
				this.proyectoSeleccionado.setCliente(new Cliente(this.idClienteMod));
				this.proyectoSeleccionado.setFecha_Creacion_Proyecto(new Date());
				//Ejecutar la actualizacion en BD
				boolean result = ejbGenerico.actualizarObjeto(this.proyectoSeleccionado);
				if(result){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se actualizaron los datos del proyecto exitosamente.");
					limpiar();
					cargarListaProyectos();
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error actualizando los datos del proyecto, por favor intentelo de nuevo más tarde.");
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
//			this.idArea = proyectoSeleccionado.getArea().getId_Area();
			this.idAreaMod = proyectoSeleccionado.getArea().getId_Area();
		}
		if(proyectoSeleccionado.getCliente()!=null){
//			this.idCliente = proyectoSeleccionado.getCliente().getId_Cliente();
			this.idClienteMod = proyectoSeleccionado.getCliente().getId_Cliente();
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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
	public int getIdAreaMod() {
		return idAreaMod;
	}
	public void setIdAreaMod(int idAreaMod) {
		this.idAreaMod = idAreaMod;
	}
	public int getIdClienteMod() {
		return idClienteMod;
	}
	public void setIdClienteMod(int idClienteMod) {
		this.idClienteMod = idClienteMod;
	}
}
