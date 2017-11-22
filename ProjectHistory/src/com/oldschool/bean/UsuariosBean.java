package com.oldschool.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.oldschool.ejb.EjbGenericoLocal;
import com.oldschool.ejb.EjbLoginLocal;
import com.oldschool.ejb.EjbUsuariosLocal;
import com.oldschool.model.Rol;
import com.oldschool.model.Usuario;
import com.oldschool.util.Mensaje;
import com.oldschool.util.Util;

@ManagedBean(name= UsuariosBean.BEAN_NAME)
@ViewScoped
public class UsuariosBean implements Serializable {

	/*Variables Bean*/
	public static final String BEAN_NAME = "usuariosBean";
	private static final long serialVersionUID = 1461272576618969285L;
	@EJB private EjbGenericoLocal ejbGenerico;
	@EJB private EjbUsuariosLocal ejbUsuarios;
	@EJB private EjbLoginLocal ejbLogin;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	/*Variables*/
	private List<Rol> listaRoles;
	private List<Usuario> listaUsuarios;
	private Usuario usuarioSeleccionado;
	//Formulario de registro
	private String nombre;
	private String apellido;
	private String genero;
	private String username;
	private String pass;
	private String rolesSeleccionados;
	private String rolesSeleccionadosMod;
	//Cambiar contraseña
	private String confirmarPass;
	//Filtro usuario
	private String filtroNombre;
	private String filtroEstado;
	
	/*Métodos privados*/
	private void cargarListaUsuarios(byte estado){
		try {
			listaUsuarios = ejbUsuarios.listarUsuarios(estado);
//			listaUsuarios = (List<Usuario>)(List<?>)ejbGenerico.listarTodo(new Usuario());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarListaRoles(){
		try {
			this.listaRoles = (List<Rol>)(List<?>)ejbGenerico.listarTodo(new Rol()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Rol obtenerRolPorNombre(String nombreRol){
		for (Rol rol : listaRoles) {
			if(rol.getNombre_Rol().equals(nombreRol)){
				return rol;
			}
		}
		return null;
	}
	
	/*Métodos públicos*/
	@PostConstruct
	public void init(){
		limpiar();
		cargarListaUsuarios(Usuario.ESTADO_ACTIVO);
		cargarListaRoles();
	}
	
	public void limpiar(){
		this.listaUsuarios = new ArrayList<>();
		this.usuarioSeleccionado = new Usuario();
		this.nombre = null;
		this.apellido = null;
		this.genero = null;
		this.username = null;
		this.pass = null;
		this.confirmarPass = null;
		this.filtroNombre = null;
		this.rolesSeleccionados = null;
		this.rolesSeleccionadosMod = null;
	}
	
	public void crearUsuario(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(this.usuarioSeleccionado!=null){
				if(Util.isEmpty(this.nombre)){
					validar = true;
				}
				if(Util.isEmpty(this.apellido)){
					validar = true;
				}
				if(Util.isEmpty(this.genero)){
					validar = true;
				}
				if(Util.isEmpty(this.username)){
					validar = true;
				}
				if(Util.isEmpty(this.pass)){
					validar = true;
				}
				if(Util.isEmpty(this.rolesSeleccionados)){
					validar = true;
				}
			}
			//Validar si el nombre de usuario ya existe
			if(!validar){
				boolean existeUsuario = ejbUsuarios.existeUsuarioConLogin(this.username);
				if(existeUsuario){
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ya existe un recurso registrado con ese nombre usuario.");
				}else{
					//Roles
					List<Rol> roles = new ArrayList<>();
					String[] rolesStr = rolesSeleccionados.split(",");
					for (String nombreRol : rolesStr) {
						roles.add(obtenerRolPorNombre(nombreRol));
					}
					
					//Usuario
					Usuario usuario = new Usuario();
					usuario.setNombre_Usuario(nombre);
					usuario.setApellido(apellido);
					usuario.setGenero(genero);
					usuario.setActivo(Usuario.ESTADO_ACTIVO);
					usuario.setLogin(this.username);
					String encPass = Util.encriptarPass(this.username, this.pass);
					usuario.setContraseña(encPass);
					usuario.setRols(roles);
					//Crear usuario
					boolean resultado = ejbUsuarios.registrarUsuario(usuario);
					if(resultado){
						Mensaje.mostrarMensaje(Mensaje.INFO, "Se registró el usuario correctamente");
						limpiar();
						cargarListaUsuarios(Usuario.ESTADO_ACTIVO);
					}else{
						Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error creando el usuario, por favor intentelo de nuevo más tarde.");
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
	
	public void filtrarUsuario(){
		try {
			//Por defecto busca los activos
			byte estado = Usuario.ESTADO_ACTIVO;
			//En caso de que se seleccione inactivo
			if(this.filtroEstado.equals("I")){
				estado = Usuario.ESTADO_INACTIVO;
			}
			//Filtro de nombre
			if(!Util.isEmpty(filtroNombre)){
				this.listaUsuarios = ejbUsuarios.filtrarUsuariosPorNombreYEstado(filtroNombre, estado);
			}else{
				//Mostrar todo
				cargarListaUsuarios(estado);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editarUsuario(){
		try {
			//Validar campos vacios
			boolean validar = false;
			if(this.usuarioSeleccionado!=null){
				if(Util.isEmpty(this.usuarioSeleccionado.getNombre_Usuario())){
					validar = true;
				}
				if(Util.isEmpty(this.usuarioSeleccionado.getApellido())){
					validar = true;
				}
				if(Util.isEmpty(this.usuarioSeleccionado.getGenero())){
					validar = true;
				}
			}
			//Actualizar
			if(!validar){
				//Roles
				List<Rol> roles = new ArrayList<>();
				String[] rolesStr = rolesSeleccionadosMod.split(",");
				for (String nombreRol : rolesStr) {
					roles.add(obtenerRolPorNombre(nombreRol));
				}
				usuarioSeleccionado.setRols(roles);
				
				boolean result = ejbUsuarios.actualizarDatosUsuario(usuarioSeleccionado);
				if(result){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se actualizaron los datos del usuario exitosamente.");
					limpiar();
					cargarListaUsuarios(Usuario.ESTADO_ACTIVO);
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error actualizando los datos del usuario, por favor intentelo de nuevo más tarde.");
				}
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Hay campos vacíos, por favor verifique y vuelva a intentarlo");
			}
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
			e.printStackTrace();
		}
	}
	
	public void inhabilitarUsuario(){
		try {
			if(this.usuarioSeleccionado!=null){
				boolean result = ejbUsuarios.cambiarEstadoDeUsuario(usuarioSeleccionado.getId_usuario(), Usuario.ESTADO_INACTIVO);
				if(result){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se Inhabilitó el usuario correctamente.");
					limpiar();
					cargarListaUsuarios(Usuario.ESTADO_ACTIVO);
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error inhabilitando el usuario, por favor intentelo de nuevo más tarde.");
				}
			}
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
		}
	}
	
	public void reactivarUsuario(){
		try {
			if(this.usuarioSeleccionado!=null){
				boolean result = ejbUsuarios.cambiarEstadoDeUsuario(usuarioSeleccionado.getId_usuario(), Usuario.ESTADO_ACTIVO);
				if(result){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se re-activó el usuario correctamente.");
					limpiar();
					cargarListaUsuarios(Usuario.ESTADO_INACTIVO);
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error re-activando el usuario, por favor intentelo de nuevo más tarde.");
				}
			}
		} catch (Exception e) {
			Mensaje.mostrarMensaje(Mensaje.FATAL, "Ha ocurrido una excepción, intentelo de nuevo más tarde. Si el error persiste contacte a su administrador.");
		}
	}
	
	public void cambiarPassword(){
		try {
			if(this.usuarioSeleccionado!=null && this.usuarioSeleccionado.getId_usuario()!=0 && !Util.isEmpty(this.usuarioSeleccionado.getLogin())){
				//Validar contraseñas
				if(Util.isEmpty(pass) || Util.isEmpty(confirmarPass)){
					Mensaje.mostrarMensaje(Mensaje.WARN, "No pueden haber campos vacíos.");
				}else{
					//Comparar si las contraseñas son iguales
					if(pass.equals(confirmarPass)){
						String encPass = Util.encriptarPass(this.usuarioSeleccionado.getLogin(), this.pass);
						this.usuarioSeleccionado.setContraseña(encPass);
						boolean resultado = ejbUsuarios.cambiarPassword(usuarioSeleccionado);
						if(resultado){
							Mensaje.mostrarMensaje(Mensaje.INFO, "Se cambió la contraseña correctamente.");
						}else{
							Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error cambiando la contraseña, por favor intente de nuevo.");
						}
					}else{
						Mensaje.mostrarMensaje(Mensaje.WARN, "Las contraseñas no coinciden, intentelo de nuevo.");
					}
				}
			}
			
			limpiar();
			cargarListaUsuarios(Usuario.ESTADO_ACTIVO);
			
		} catch (Exception e) {
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
	public List<Rol> getListaRoles() {
		return listaRoles;
	}
	public void setListaRoles(List<Rol> listaRoles) {
		this.listaRoles = listaRoles;
	}
	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}
	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}
	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
		if(this.usuarioSeleccionado.getRols()!=null){
			StringBuilder nombresRoles = new StringBuilder();
			for (Rol rol : this.usuarioSeleccionado.getRols()) {
				if(nombresRoles.length() != 0){
					nombresRoles.append(",");
				}
				nombresRoles.append( rol.getNombre_Rol() );
			}
			this.rolesSeleccionadosMod = nombresRoles.toString();
		}
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
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
	public String getRolesSeleccionados() {
		return rolesSeleccionados;
	}
	public void setRolesSeleccionados(String rolesSeleccionados) {
		this.rolesSeleccionados = rolesSeleccionados;
	}
	public String getRolesSeleccionadosMod() {
		return rolesSeleccionadosMod;
	}
	public void setRolesSeleccionadosMod(String rolesSeleccionadosMod) {
		this.rolesSeleccionadosMod = rolesSeleccionadosMod;
	}
	public String getConfirmarPass() {
		return confirmarPass;
	}
	public void setConfirmarPass(String confirmarPass) {
		this.confirmarPass = confirmarPass;
	}
	
}
