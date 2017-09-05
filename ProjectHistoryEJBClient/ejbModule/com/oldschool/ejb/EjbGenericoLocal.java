package com.oldschool.ejb;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface EjbGenericoLocal {
	
	public List<Object> listarTodo(Object instanciaClase) throws Exception;
	public List<Object> listarTodoLazy(Object instanciaClase, int firstRecord, int maxResult) throws Exception;
	public List<Object> listarPorQuery(Object instanciaClase, String nombreQuery, Map<String , Object> parametros) throws Exception;
	public List<Object> listarPorNativeQuery(Object instanciaClase, String nombreQuery, Map<Integer , Object> parametros) throws Exception;
	public List<Object> listarPorQueryLazy(Object instanciaClase, String nombreQuery, Map<String , Object> parametros, int firstRecord, int maxResult) throws Exception;
	
	public boolean agregarObjeto(Object instanciaClase) throws Exception;
	public boolean EjecutarActualizacionPorQuery(Object instanciaClase, String nombreQuery, Map<String, Object> parametros) throws Exception;
	public boolean actualizarObjeto(Object instanciaClase) throws Exception;
	public boolean eliminarObjeto(Object instanciaClase) throws Exception;
	public boolean eliminarObjectoPorQuery(Object instanciaClase, long id) throws Exception;
	public boolean eliminarObjectoPorQuery(Object instanciaClase, String nombreQuery, Map<String, Object> parametros) throws Exception;
	public boolean eliminarObjetoSinMerge(Object instanciaClase) throws Exception;
	
	public Object obtenerObjetoPorId(Object instanciaClase, Object id) throws Exception;
	public Object obtenerObjetoPorQuery(Object instanciaClase, String nombreQuery, Map<String, Object> parametros) throws Exception;
	public Object obtenerObjetoPorNativeQuery(Object instanciaClase, String nombreQuery, Map<Integer, Object> parametros) throws Exception;
//	public Object obtenerSingleObjetoPorQuery(Object instanciaClase, String nombreQuery, Map<String, Object> parametros) throws PNegocioExcepcion;
	
	//LIMPIAR CACHE DE JPA
	public void limpiarCacheJPA() throws Exception;
	boolean eliminarObjectoPorQuery(Object instanciaClase, int id) throws Exception;
	
}