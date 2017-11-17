package com.oldschool.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.sql.rowset.serial.SerialBlob;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.oldschool.ejb.EjbGenericoLocal;
import com.oldschool.model.DocumentoAsociado;
import com.oldschool.util.Util;

@ManagedBean(name= AprobacionesBean.BEAN_NAME)
@ViewScoped
public class AprobacionesBean implements Serializable {

	/*Variables Bean*/
	public static final String BEAN_NAME = "aprobacionesBean";
	private static final long serialVersionUID = 1461272576618969285L;
	@EJB private EjbGenericoLocal ejbGenerico;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	/*Variables*/
	private List<DocumentoAsociado> listaDocumentos;
	private DocumentoAsociado documentoSeleccionado;
	//Filtro usuario
	private String filtroDocumento;
	
	/*Métodos privados*/
	@SuppressWarnings("unchecked")
	private void cargarListaDocumentosPorAprobar(){
		try {
			listaDocumentos = (List<DocumentoAsociado>)(List<?>)ejbGenerico.listarPorQuery(new DocumentoAsociado(), "findPendientes", new HashMap<>());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Métodos públicos*/
	@PostConstruct
	public void init(){
		limpiar();
		cargarListaDocumentosPorAprobar();
	}
	
	public void limpiar(){
		this.listaDocumentos = new ArrayList<>();
		this.filtroDocumento = null;
	}
	
	@SuppressWarnings("unchecked")
	public void filtrarDocumento(){
		try {
			//Filtro de nombre
			if(!Util.isEmpty(filtroDocumento)){
				Map<String, Object> parametros = new HashMap<>();
				parametros.put("nombre_Documento", "%"+this.filtroDocumento+"%");
				this.listaDocumentos = (List<DocumentoAsociado>)(List<?>)ejbGenerico.listarPorQuery(new DocumentoAsociado(), "findPendientesPorNombre", parametros);
			}else{
				//Mostrar todo
				cargarListaDocumentosPorAprobar();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public StreamedContent descargarDocumento(){
		StreamedContent download = new DefaultStreamedContent();
		try {
			if(this.documentoSeleccionado!=null && this.documentoSeleccionado.getDocumentoAsociado()!=null){
				
				Blob blob = new SerialBlob( this.documentoSeleccionado.getDocumentoAsociado() );
				InputStream is = blob.getBinaryStream();
				
				String nombreArchivo = this.documentoSeleccionado.getNombre_Documento();
				String pathArchivo = System.getProperty("java.io.tmpdir") + File.separator + nombreArchivo + "." + this.documentoSeleccionado.getTipo_archivo();
				FileOutputStream fos = new FileOutputStream( pathArchivo );
				int b = 0;
				while ((b = is.read()) != -1){
				    fos.write(b); 
				}
				
				is.close();
				fos.flush();

				InputStream stream = new FileInputStream( pathArchivo );
				download = new DefaultStreamedContent(stream, Files.probeContentType( Paths.get(pathArchivo) ), this.documentoSeleccionado.getNombre_Documento() + "." + this.documentoSeleccionado.getTipo_archivo());
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return download;
	}
	
	/*Get & Set*/
	public SesionBean getSesionBean() {
		return sesionBean;
	}
	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}
	public List<DocumentoAsociado> getListaDocumentos() {
		return listaDocumentos;
	}
	public void setListaDocumentos(List<DocumentoAsociado> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}
	public String getFiltroDocumento() {
		return filtroDocumento;
	}
	public void setFiltroDocumento(String filtroDocumento) {
		this.filtroDocumento = filtroDocumento;
	}
	public DocumentoAsociado getDocumentoSeleccionado() {
		return documentoSeleccionado;
	}
	public void setDocumentoSeleccionado(DocumentoAsociado documentoSeleccionado) {
		this.documentoSeleccionado = documentoSeleccionado;
	}
}
