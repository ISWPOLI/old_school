package com.oldschool.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
	
}
