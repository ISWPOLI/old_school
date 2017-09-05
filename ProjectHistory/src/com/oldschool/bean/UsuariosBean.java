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
	private List<Usuario> listaUsuarios;
	private Usuario usuarioSeleccionado;
	//Formulario de registro
	private String username;
	private String pass;
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
	
	/*Métodos públicos*/
	@PostConstruct
	public void init(){
		limpiar();
		cargarListaUsuarios(Usuario.ESTADO_ACTIVO);
	}
	
	public void limpiar(){
		this.listaUsuarios = new ArrayList<>();
		this.usuarioSeleccionado = new Usuario();
		this.username = null;
		this.pass = null;
		this.filtroNombre = null;
	}
	
	public void crearUsuario(){
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
				if(Util.isEmpty(this.username)){
					validar = true;
				}
				if(Util.isEmpty(this.pass)){
					validar = true;
				}
			}
			//Validar si el nombre de usuario ya existe
			if(!validar){
				boolean existeUsuario = ejbUsuarios.existeUsuarioConLogin(this.username);
				if(existeUsuario){
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ya existe un recurso registrado con ese nombre usuario.");
				}else{
					Usuario usuario = this.usuarioSeleccionado;
					usuario.setActivo(Usuario.ESTADO_ACTIVO);
					usuario.setLogin(this.username);
					String encPass = Util.encriptarPass(this.username, this.pass);
					usuario.setContraseña(encPass);
					//Crear usuario
					boolean resultado = ejbGenerico.agregarObjeto(usuario);
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
		
	}
	
	/*Get & Set*/
	public SesionBean getSesionBean() {
		return sesionBean;
	}
	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
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
}
