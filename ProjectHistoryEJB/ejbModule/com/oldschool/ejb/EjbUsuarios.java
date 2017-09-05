package com.oldschool.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.oldschool.model.Usuario;

@Stateless(name="EjbUsuariosLocal", mappedName="ejb/EjbUsuariosLocal")
public class EjbUsuarios implements EjbUsuariosLocal {

	@PersistenceContext(unitName = "ProjectHistoryJPA")
	EntityManager em;
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean existeUsuarioConLogin(String login) throws Exception {
		Query query = em.createNamedQuery("Usuario.findIdByUser");
		query.setParameter("login", login);
		
		List<Object> temp = query.getResultList();
		if(temp!=null && !temp.isEmpty()){
			return true;
		}
		return false;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Usuario> listarUsuarios(byte estado) throws Exception {
		Query query = em.createNamedQuery("Usuario.findByEstado");
		query.setParameter("estado", estado);
		
		List temp = query.getResultList();
		if(temp!=null && !temp.isEmpty()){
			List<Usuario> lista = temp;
			return lista;
		}
		return null;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Usuario> filtrarUsuariosPorNombre(String nombre) throws Exception {
		Query query = em.createNamedQuery("Usuario.findByNombres");
		query.setParameter("nombre", "%" + nombre.toLowerCase() + "%");
		query.setParameter("apellido", "%" + nombre.toLowerCase() + "%");
		
		List temp = query.getResultList();
		if(temp!=null && !temp.isEmpty()){
			List<Usuario> lista = temp;
			return lista;
		}
		return null;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Usuario> filtrarUsuariosPorNombreYEstado(String nombre, byte estado) throws Exception {
		Query query = em.createNamedQuery("Usuario.findByNombresYEstado");
		query.setParameter("nombre", "%" + nombre.toLowerCase() + "%");
		query.setParameter("apellido", "%" + nombre.toLowerCase() + "%");
		query.setParameter("estado", estado);
		
		List temp = query.getResultList();
		if(temp!=null && !temp.isEmpty()){
			List<Usuario> lista = temp;
			return lista;
		}
		return null;
	}
	
}
