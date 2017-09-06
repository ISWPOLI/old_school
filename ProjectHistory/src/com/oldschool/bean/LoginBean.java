package com.oldschool.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.oldschool.ejb.EjbGenericoLocal;
import com.oldschool.ejb.EjbLoginLocal;
import com.oldschool.model.Usuario;
import com.oldschool.util.Mensaje;
import com.oldschool.util.Util;

@ManagedBean(name= LoginBean.BEAN_NAME)
@ViewScoped
public class LoginBean implements Serializable {

	/*Variables Bean*/
	public static final String BEAN_NAME = "loginBean";
	private static final long serialVersionUID = 1461272576618969285L;
	@EJB private EjbLoginLocal ejbLogin;
	@EJB private EjbGenericoLocal ejbGenerico;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	/*Variables*/
	private String user;
	private String pass;
	
	/*Métodos privados*/
	private void actualizarFechaDeAcceso(int idUsuario){
		try {
//			Usuario temp = new Usuario();
//			temp.setId_usuario(idUsuario);
//			temp.setFecha_Ultimo_Acceso(new Date());
			if(ejbLogin.actualizarFechaAcceso(idUsuario)){
//			if(ejbGenerico.actualizarObjeto(temp)){
				System.out.println("Se actualizó la última fecha de acceso correctamente");
			}else{
				System.out.println("No se logró actualizar la última fecha de acceso");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Métodos públicos*/
	@PostConstruct
	public void init(){
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String iniciarSesion(){
		try {
			String encPass = Util.encriptarPass(this.user, this.pass);
			Usuario usuario = ejbLogin.iniciarSesion(user, encPass);
			if(usuario!=null){
				//Session Bean
				sesionBean.setNombreUser(user);
				sesionBean.setUsuario(usuario);
				//Actualizar
				actualizarFechaDeAcceso(usuario.getId_usuario());
				//Mensaje de confirmacion
				Mensaje.mostrarMensaje(Mensaje.INFO, "Bienvenido " + user);
				//Retorno
				return "home-page";
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Usuario o contraseña incorrecta.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String cerrarSesion(){
		this.sesionBean.setUsuario(null);
		this.sesionBean.setNombreUser(null);
		return "login-page";
	}
	
	/*Get & Set*/
	public SesionBean getSesionBean() {
		return sesionBean;
	}
	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
}
