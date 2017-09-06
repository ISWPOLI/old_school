package com.oldschool.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.oldschool.model.Usuario;

@Stateless(name="EjbLoginLocal", mappedName="ejb/EjbLoginLocal")
public class EjbLogin implements EjbLoginLocal {

	@PersistenceContext(unitName = "ProjectHistoryJPA")
	EntityManager em;
	
	@Override
	@SuppressWarnings("unchecked")
	public int getIdByUser(String user) throws Exception {
		Query query = em.createNamedQuery("Usuario.findIdByUser");
		query.setParameter("login", user);
		
		List<Object> temp = query.getResultList();
		if(temp!=null && !temp.isEmpty()){
			return (int) temp.get(0);
		}
			
		return 0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Usuario iniciarSesion(String user, String pass) throws Exception {
		Query query = em.createNamedQuery("Usuario.login");
		query.setParameter("login", user);
		query.setParameter("pass", pass);
		List<Object> temp = query.getResultList();
		
		if(temp!=null && !temp.isEmpty()){
			return (Usuario)temp.get(0);
		}
		
		return null;
	}
	
	@Override
	public boolean actualizarFechaAcceso(int idUsuario) throws Exception {
		Query query = em.createNativeQuery("UPDATE usuario SET Fecha_Ultimo_Acceso = ? WHERE Id_usuario = ?");
		query.setParameter(1, new Date());
		query.setParameter(2, idUsuario);
		
		int result = query.executeUpdate();
		if(result>=1){
			return true;
		}
		
		return false;
	}
	
}
