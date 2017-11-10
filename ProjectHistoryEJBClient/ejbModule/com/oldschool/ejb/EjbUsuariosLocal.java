package com.oldschool.ejb;

import java.util.List;

import javax.ejb.Remote;

import com.oldschool.model.Usuario;

@Remote
public interface EjbUsuariosLocal {

	public boolean existeUsuarioConLogin(String login) throws Exception;
	public List<Usuario> listarUsuarios(byte estado) throws Exception;
	public List<Usuario> filtrarUsuariosPorNombre(String nombre) throws Exception;
	public List<Usuario> filtrarUsuariosPorNombreYEstado(String nombre, byte estado) throws Exception;
	public boolean actualizarDatosUsuario(Usuario usuario) throws Exception;
	public boolean cambiarEstadoDeUsuario(int idUsuario, byte estado) throws Exception;

	public boolean registrarUsuario(Usuario usuario) throws Exception;

}
