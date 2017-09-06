package com.oldschool.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oldschool.bean.SesionBean;
import com.oldschool.util.Util;

//@WebFilter("/*.xhtml")
@WebFilter("/pages/*")
public class LoginFilter implements Filter {

	private static final String APLICACION = "/ProjectHistory";
	private static final String HOME_URL = "/pages/login.xhtml";
	private static final String ADMIN_URL = "/pages/home.xhtml";
	private static final String CONSULTOR_URL = "/pages/home.xhtml";
	private static final String APROBADOR_URL = "/pages/home.xhtml";
	private static final String ACCESO_DENEGADO_URL = "/pages/acceso_denegado.xhtml";
	/*PERMISO SOBRE CARPETAS*/
	private static final String PATH_USUARIOS_ADMIN = "/pages/Usuario/";

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			
			//Bean de sesi�n
			SesionBean sesionBean = (SesionBean)((HttpServletRequest)request).getSession().getAttribute("sesionBean");

			// Validar si la sesion est� asignada
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			//HttpSession ses = req.getSession(false);

			// Permitir al usuario acceder o devolver al login si no est� logeado
			String reqURI = req.getRequestURI();

			if(sesionBean!=null && !Util.isEmpty(sesionBean.getNombreUser())){
				
				/*Cuando est�n logueados y vayan a la p�gina de LOGIN los retornar� autom�ticamente al Home*/
				if (reqURI.equals(APLICACION + HOME_URL) || reqURI.equals(APLICACION)) {
					if(sesionBean.getUsuario().isAdmin()){
						res.sendRedirect(req.getContextPath() + ADMIN_URL);
					}
					else if(sesionBean.getUsuario().isAprobador()){
						res.sendRedirect(req.getContextPath() + APROBADOR_URL);
					}
					else if(sesionBean.getUsuario().isConsultor()){
						res.sendRedirect(req.getContextPath() + CONSULTOR_URL);
					}
				}
				/*Permiso sobre carpeta de usurios*/
				else if( reqURI.contains(APLICACION + PATH_USUARIOS_ADMIN) ){
					if(sesionBean.getUsuario().isAdmin()){
						chain.doFilter(request, response);
					}else{
						res.sendRedirect(req.getContextPath() + ACCESO_DENEGADO_URL);
					}
				}
				/*Cualquier otra petici�n*/
				else {
					chain.doFilter(request, response);
				}

			} else if (reqURI.indexOf(HOME_URL) >= 0 || reqURI.equals(APLICACION + ACCESO_DENEGADO_URL) ) {
				chain.doFilter(request, response);
			} else {
				// El usuario no se logeo, se redirige a la pagina de inicio
				// como usuario anonimo
				res.sendRedirect(req.getContextPath() + HOME_URL);
			}
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}