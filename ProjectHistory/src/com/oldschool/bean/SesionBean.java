package com.oldschool.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.oldschool.model.Proyecto;
import com.oldschool.model.Usuario;

@ManagedBean(name = SesionBean.BEAN_NAME)
@SessionScoped
public class SesionBean implements Serializable{

	/*VARIABLES BEAN*/
	public static final String BEAN_NAME = "sesionBean";
	private static final long serialVersionUID = 6218749905502974994L;
	
	/*CONSTRUCTOR*/
	public SesionBean() {
		usuario = new Usuario();
	}
	
	/*VARIABLES*/
	private Usuario usuario;
	private String nombreUser;
	
	private Proyecto proyectoSeleccinado; //Historial
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getNombreUser() {
		return nombreUser;
	}
	public void setNombreUser(String nombreUser) {
		this.nombreUser = nombreUser;
	}
	
	public Proyecto getProyectoSeleccinado() {
		return proyectoSeleccinado;
	}
	public void setProyectoSeleccinado(Proyecto proyectoSeleccinado) {
		this.proyectoSeleccinado = proyectoSeleccinado;
	}
	
}
