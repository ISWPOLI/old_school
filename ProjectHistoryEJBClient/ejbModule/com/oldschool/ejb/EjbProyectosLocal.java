package com.oldschool.ejb;

import java.util.List;

import javax.ejb.Remote;

import com.oldschool.model.DocumentoAsociado;
import com.oldschool.model.Proyecto;

@Remote
public interface EjbProyectosLocal {

	public int crearProyecto(Proyecto proyecto) throws Exception;
	public boolean asociarDocumentos(List<DocumentoAsociado> lista) throws Exception;
	public boolean quitarDocumentos(List<DocumentoAsociado> lista) throws Exception;
	
}
