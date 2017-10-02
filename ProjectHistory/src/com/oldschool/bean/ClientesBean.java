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
import com.oldschool.model.Cliente;
import com.oldschool.util.Mensaje;
import com.oldschool.util.Util;

@ManagedBean(name= ClientesBean.BEAN_NAME)
@ViewScoped
public class ClientesBean implements Serializable {

	/*Variables Bean*/
	public static final String BEAN_NAME = "clientesBean";
	private static final long serialVersionUID = 1461272576618969285L;
	@EJB private EjbGenericoLocal ejbGenerico;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	/*Variables*/
	private List<Cliente> listaClientes;
	private Cliente clienteSeleccionado;
	//Formulario de registro
	private String nombre;
	private long nit;
	private String direccion;
	private long telefono;
	private String email;
	//Filtro usuario
	private String filtroNombre;
	private String filtroEstado;
	
	/*Métodos privados*/
	@SuppressWarnings("unchecked")
	private void cargarListaClientes(byte estado){
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("estado", estado);
			listaClientes = (List<Cliente>)(List<?>)ejbGenerico.listarPorQuery(new Cliente(), "findByEstado", parametros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean existeCliente(String nombre, long nit){
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("nombre", nombre);
			parametros.put("nit", nit);
			Object obj = ejbGenerico.obtenerObjetoPorQuery(new Cliente(), "findByNombreOrNit", parametros);
			if(obj!=null){
				return true;
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
		cargarListaClientes(Cliente.ESTADO_ACTIVO);
	}
	
	public void limpiar(){
		this.listaClientes = new ArrayList<>();
		this.clienteSeleccionado = new Cliente();
		this.nombre = null;
		this.nit = 0;
		this.direccion = null;
		this.telefono = 0;
		this.email = null;
		this.filtroNombre = null;
		//this.filtroEstado = Cliente.ESTADO_ACTIVO;
	}
	
	public void crearCliente(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(Util.isEmpty(this.nombre)){
				validar = true;
			}
			if(Util.isEmpty(this.nit)){
				validar = true;
			}
			if(Util.isEmpty(this.direccion)){
				validar = true;
			}
			if(Util.isEmpty(this.telefono)){
				validar = true;
			}
			if(Util.isEmpty(this.email)){
				validar = true;
			}
			//Validar si el nombre de usuario ya existe
			if(!validar){
				boolean existeCliente = existeCliente(this.nombre, this.nit);
				if(existeCliente){
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ya existe un cliente registrado con ese NOMBRE o NIT.");
				}else{
					Cliente cliente = new Cliente();
					cliente.setRazon_Social(this.nombre);
					cliente.setNit(this.nit);
					cliente.setDireccion(this.direccion);
					cliente.setTelefono(this.telefono);
					cliente.setEmail(this.email);
					cliente.setActivo(Cliente.ESTADO_ACTIVO);
					//Crear cliente
					boolean resultado = ejbGenerico.agregarObjeto(cliente);
					if(resultado){
						Mensaje.mostrarMensaje(Mensaje.INFO, "Se registró el cliente correctamente");
						limpiar();
						cargarListaClientes(Cliente.ESTADO_ACTIVO);
					}else{
						Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error creando el cliente, por favor intentelo de nuevo más tarde.");
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
	public void filtrarClientes(){
		try {
			//Por defecto busca los activos
			byte estado = Cliente.ESTADO_ACTIVO;
			//En caso de que se seleccione inactivo
			if(this.filtroEstado.equals("I")){
				estado = Cliente.ESTADO_INACTIVO;
			}
			//Filtro de nombre
			if(!Util.isEmpty(filtroNombre)){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("estado", estado);
				parametros.put("nombre", "%" + filtroNombre + "%");
				this.listaClientes = (List<Cliente>)(List<?>)ejbGenerico.listarPorQuery(new Cliente(), "findByNombreYEstado", parametros);
			}else{
				//Mostrar todo
				cargarListaClientes(estado);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editarCliente(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(this.clienteSeleccionado!=null){
				if(Util.isEmpty(this.clienteSeleccionado.getRazon_Social())){
					validar = true;
				}
				if(Util.isEmpty(this.clienteSeleccionado.getNit())){
					validar = true;
				}
				if(Util.isEmpty(this.clienteSeleccionado.getDireccion())){
					validar = true;
				}
				if(Util.isEmpty(this.clienteSeleccionado.getTelefono())){
					validar = true;
				}
				if(Util.isEmpty(this.clienteSeleccionado.getEmail())){
					validar = true;
				}
				
			}
			//Actualizar
			if(!validar){
				boolean result = ejbGenerico.actualizarObjeto(this.clienteSeleccionado);
				if(result){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se actualizaron los datos del cliente exitosamente.");
					limpiar();
					cargarListaClientes(Cliente.ESTADO_ACTIVO);
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error actualizando los datos del cliente, por favor intentelo de nuevo más tarde.");
				}
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Hay campos vacíos, por favor verifique y vuelva a intentarlo");
			}
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
			e.printStackTrace();
		}
	}
	
	public void inhabilitarCliente(){
		try {
			if(this.clienteSeleccionado!=null){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("idCliente", this.clienteSeleccionado.getId_Cliente());
				boolean result = ejbGenerico.EjecutarActualizacionPorQuery(new Cliente(), "inactivarCliente", parametros);
				if(result){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se Inhabilitó el cliente correctamente.");
					limpiar();
					cargarListaClientes(Cliente.ESTADO_ACTIVO);
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error inhabilitando el cliente, por favor intentelo de nuevo más tarde.");
				}
			}
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
		}
	}
	
	public void reactivarCliente(){
		try {
			if(this.clienteSeleccionado!=null){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("idCliente", this.clienteSeleccionado.getId_Cliente());
				boolean result = ejbGenerico.EjecutarActualizacionPorQuery(new Cliente(), "activarCliente", parametros);
				if(result){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se re-activó el cliente correctamente.");
					limpiar();
					cargarListaClientes(Cliente.ESTADO_INACTIVO);
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error re-activando el cliente, por favor intentelo de nuevo más tarde.");
				}
			}
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
		}
	}
	
	/*Get & Set*/
	public SesionBean getSesionBean() {
		return sesionBean;
	}
	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}
	public List<Cliente> getListaClientes() {
		return listaClientes;
	}
	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}
	public Cliente getClienteSeleccionado() {
		return clienteSeleccionado;
	}
	public void setClienteSeleccionado(Cliente clienteSeleccionado) {
		this.clienteSeleccionado = clienteSeleccionado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public long getNit() {
		return nit;
	}
	public void setNit(long nit) {
		this.nit = nit;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public long getTelefono() {
		return telefono;
	}
	public void setTelefono(long telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFiltroNombre() {
		return filtroNombre;
	}
	public void setFiltroNombre(String filtroNombre) {
		this.filtroNombre = filtroNombre;
	}
	public String getFiltroEstado() {
		return filtroEstado;
	}
	public void setFiltroEstado(String filtroEstado) {
		this.filtroEstado = filtroEstado;
	}
	
}
