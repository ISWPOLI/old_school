package com.oldschool.ejb;

import javax.ejb.Remote;

@Remote
public interface EjbUsuariosLocal {

	public boolean existeUsuarioConLogin(String login) throws Exception;
}
