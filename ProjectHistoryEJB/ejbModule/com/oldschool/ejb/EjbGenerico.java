package com.oldschool.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name="EjbGenerico", mappedName="ejb/EjbGenerico")
public class EjbGenerico implements EjbGenericoLocal {

	@PersistenceContext(unitName = "ProjectHistoryJPA")
	EntityManager em;

	public EjbGenerico() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> listarTodo(Object instanciaClase) throws Exception {

		List<Object> lista = new ArrayList<Object>();

		try {
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + ".findAll");
			lista = query.getResultList();

			return lista;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> listarTodoLazy(Object instanciaClase, int firstRecord, int maxResult) throws Exception {

		List<Object> lista = new ArrayList<Object>();

		try {
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + ".findAll");
			query.setFirstResult(firstRecord);
			query.setMaxResults(maxResult);

			lista = query.getResultList();

			return lista;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> listarPorQuery(Object instanciaClase, String nombreQuery, Map<String, Object> parametros)
			throws Exception {

		List<Object> lista = new ArrayList<Object>();

		try {
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + "." + nombreQuery, instanciaClase.getClass());
			for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
				query.setParameter(parametro.getKey(), parametro.getValue());
			}
			lista = query.getResultList();

			return lista;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> listarPorNativeQuery(Object instanciaClase, String nombreQuery, Map<Integer, Object> parametros)
			throws Exception {

		List<Object> lista = new ArrayList<Object>();

		try {
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + "." + nombreQuery, instanciaClase.getClass());
			for (Map.Entry<Integer, Object> parametro : parametros.entrySet()) {
				query.setParameter(parametro.getKey(), parametro.getValue());
			}
			lista = query.getResultList();

			return lista;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> listarPorQueryLazy(Object instanciaClase, String nombreQuery, Map<String, Object> parametros,
			int firstRecord, int maxResult) throws Exception {
		List<Object> lista = new ArrayList<Object>();

		try {
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + "." + nombreQuery, instanciaClase.getClass());
			for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
				query.setParameter(parametro.getKey(), parametro.getValue());
			}
			query.setFirstResult(firstRecord);
			query.setMaxResults(maxResult);

			lista = query.getResultList();

			return lista;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public boolean agregarObjeto(Object instanciaClase) throws Exception{
		try {
//			em.clear();
			em.persist(instanciaClase);
			em.flush();
			return true;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public boolean actualizarObjeto(Object instanciaClase) throws Exception{
		try {
			em.merge(instanciaClase);
			em.flush();
			return true;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public boolean EjecutarActualizacionPorQuery(Object instanciaClase, String nombreQuery,
			Map<String, Object> parametros) throws Exception {
		String nombreEntidad = nombreInstanciaClase(instanciaClase);

		Query query = em.createNamedQuery(nombreEntidad + "." + nombreQuery);

		for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
			query.setParameter(parametro.getKey(), parametro.getValue());
		}

		int resultado = query.executeUpdate();

		if (resultado > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean eliminarObjeto(Object instanciaClase) throws Exception {
		try {
			instanciaClase = em.merge(instanciaClase);
			em.remove(instanciaClase);
			return true;
		} catch (Exception e) {
			// throw new Exception(e);
			return false;
		}
	}

	@Override
	public boolean eliminarObjectoPorQuery(Object instanciaClase, long id) throws Exception {
		
		try {
			String queryGeneral = "eliminarPorId";
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + "." + queryGeneral);
			query.setParameter("ID", Long.valueOf(id));
			int resultado = query.executeUpdate();

			if (resultado > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public boolean eliminarObjectoPorQuery(Object instanciaClase, int id) throws Exception {
		try {
			String queryGeneral = "eliminarPorId";
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + "." + queryGeneral);
			query.setParameter("ID", Integer.valueOf(id));
			int resultado = query.executeUpdate();

			if (resultado > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public boolean eliminarObjectoPorQuery(Object instanciaClase, String nombreQuery, Map<String, Object> parametros)
			throws Exception {
		try {
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + "." + nombreQuery);

			for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
				query.setParameter(parametro.getKey(), parametro.getValue());
			}

			int resultado = query.executeUpdate();

			if (resultado > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// throw new Exception(e);
			return false;
		}
	}

	@Override
	public boolean eliminarObjetoSinMerge(Object instanciaClase) throws Exception {
		try {
			em.remove(instanciaClase);
			return true;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public Object obtenerObjetoPorId(Object instanciaClase, Object id) throws Exception {

		Object objeto = new Object();
		try {
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + ".findById");
			query.setParameter("id", id);
			objeto = query.getSingleResult();

			return objeto;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object obtenerObjetoPorQuery(Object instanciaClase, String nombreQuery, Map<String, Object> parametros)
			throws Exception {

		Object objeto = new Object();
		try {
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + "." + nombreQuery);
			for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
				query.setParameter(parametro.getKey(), parametro.getValue());
			}

			List lista = query.getResultList();

			if (!lista.isEmpty()) {
				objeto = query.getSingleResult();
			} else {
				return null;
			}

			return objeto;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object obtenerObjetoPorNativeQuery(Object instanciaClase, String nombreQuery, Map<Integer, Object> parametros) throws Exception {
		Object objeto = new Object();
		try {
			String nombreEntidad = nombreInstanciaClase(instanciaClase);

			Query query = em.createNamedQuery(nombreEntidad + "." + nombreQuery);
			for (Map.Entry<Integer, Object> parametro : parametros.entrySet()) {
				query.setParameter(parametro.getKey(), parametro.getValue());
			}

			List lista = query.getResultList();

			if (lista!=null && !lista.isEmpty()) {
				objeto = lista.get(0);
			} else {
				return null;
			}

			return objeto;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	private String nombreInstanciaClase(Object instanciaClase) {

		String[] claseSplit = instanciaClase.getClass().getName().toString().split("\\.");
		String nombreEntidad = claseSplit[claseSplit.length - 1];

		return nombreEntidad;
	}

	@Override
	public void limpiarCacheJPA() throws Exception {
		try {
			// VACIAR CAHE DE JPA
			em.getEntityManagerFactory().getCache().evictAll();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

}
