package com.oldschool.ejb;

import javax.ejb.Remote;

import com.oldschool.model.Usuario;

@Remote
public interface EjbLoginLocal {

	public int getIdByUser(String user) throws Exception;
	public Usuario iniciarSesion(String user, String pass) throws Exception;
	
}
