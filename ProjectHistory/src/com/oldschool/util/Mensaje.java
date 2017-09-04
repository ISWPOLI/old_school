package com.oldschool.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class Mensaje {

	public static final Severity INFO = FacesMessage.SEVERITY_INFO;
	public static final Severity WARN = FacesMessage.SEVERITY_WARN;
	public static final Severity ERROR = FacesMessage.SEVERITY_ERROR;
	public static final Severity FATAL = FacesMessage.SEVERITY_FATAL;
	
	public Mensaje() {}
	
	public static void mostrarMensaje(String mensaje) {
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(mensaje));
        escribirLog(INFO, mensaje);
	}
	
	public static void mostrarMensaje(String titulo, String mensaje) {
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(titulo, mensaje));
        escribirLog(INFO, mensaje);
	}
	
	public static void mostrarMensaje(Severity tipo, String mensaje) {
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(tipo, "", mensaje));
        escribirLog(tipo, mensaje);
	}
	
	public static void mostrarMensaje(Severity tipo, String titulo, String mensaje) {
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(tipo, titulo, mensaje));
        escribirLog(tipo, mensaje);
	}
	
	private static void escribirLog(Severity tipo, String mensaje){
		if(tipo == INFO){
			System.out.println("Mostando mensaje Info: "+mensaje);
		}else if(tipo == WARN){
			System.out.println("Mostando mensaje Warn: "+mensaje);
		}else if(tipo == ERROR){
			System.out.println("Mostando mensaje Error: "+mensaje);
		}else if(tipo == FATAL){
			System.out.println("Mostando mensaje Fatal: "+mensaje);
		}
	}
	
}
