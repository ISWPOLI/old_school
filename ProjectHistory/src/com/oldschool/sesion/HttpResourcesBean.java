package com.oldschool.sesion;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpResourcesBean {

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.
				getCurrentInstance().
				getExternalContext().getRequest();
	}

	public static String getAtributoEnSesion(String atributo)
	{
		HttpSession session = getSession();
		if ( session != null ){
			return (String) session.getAttribute(atributo);
		}else{
			return null;
		}
	}

}
