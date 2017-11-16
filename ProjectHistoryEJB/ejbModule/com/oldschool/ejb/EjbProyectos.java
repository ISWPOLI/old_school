package com.oldschool.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.oldschool.model.DocumentoAsociado;
import com.oldschool.model.Proyecto;

@Stateless(name="EjbProyectosLocal", mappedName="ejb/EjbProyectosLocal")
public class EjbProyectos implements EjbProyectosLocal {

	@PersistenceContext(unitName = "ProjectHistoryJPA")
	EntityManager em;

	@Override
	public int crearProyecto(Proyecto proyecto) throws Exception {
		em.persist(proyecto);
		em.flush();
		if(proyecto.getId_Proyecto()!=0){
			return proyecto.getId_Proyecto();
		}
		return 0;
	}

	@Override
	public boolean asociarDocumentos(List<DocumentoAsociado> lista) throws Exception {
		int cont = 0;
		for (DocumentoAsociado doc : lista) {
			em.persist(doc);
			cont++;
		}
		if(cont==lista.size()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean quitarDocumentos(List<DocumentoAsociado> lista) throws Exception {
		int cont = 0;
		for (DocumentoAsociado doc : lista) {
			Query query = em.createNamedQuery("DocumentoAsociado.eliminarPorId");
			query.setParameter("ID", doc.getId_Documento_Asociado());
			if(query.executeUpdate()>0){
				cont++;
			}
		}
		if(cont==lista.size()){
			return true;
		}else{
			return false;
		}
	}
}
