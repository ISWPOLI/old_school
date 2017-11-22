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
import java.util.Date;
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
import com.oldschool.model.Documento;
import com.oldschool.model.DocumentoAsociado;
import com.oldschool.util.Mensaje;
import com.oldschool.util.Util;

@ManagedBean(name= AprobacionesBean.BEAN_NAME)
@ViewScoped
public class AprobacionesBean implements Serializable {

	/*Variables Bean*/
	public static final String BEAN_NAME = "aprobacionesBean";
	private static final long serialVersionUID = 1461272576618969285L;
	//$ANALYSIS-IGNORE
	@EJB private EjbGenericoLocal ejbGenerico;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	//$ANALYSIS-IGNORE
	private List<Documento> listaDocumentos;
	private Documento documentoSeleccionado;
	//Filtro usuario
	private String filtroDocumento;
	
	/*Métodos privados*/
	@SuppressWarnings("unchecked")
	private void cargarListaDocumentosPorAprobar(){
		try {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("estado", Documento.ESTADO_PENDIENTE);
			listaDocumentos = (List<Documento>)(List<?>)ejbGenerico.listarPorQuery(new Documento(), "findByEstado", parametros);
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
				parametros.put("nombreDocumento", "%"+this.filtroDocumento+"%");
				parametros.put("estado", Documento.ESTADO_PENDIENTE);
				this.listaDocumentos = (List<Documento>)(List<?>)ejbGenerico.listarPorQuery(new Documento(), "findByNombreYEstado", parametros);
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
				
				Blob blob = new SerialBlob( this.documentoSeleccionado.getDocumento() );
				InputStream is = blob.getBinaryStream();
				
				String nombreArchivo = this.documentoSeleccionado.getNombreDocumento();
				String pathArchivo = System.getProperty("java.io.tmpdir") + File.separator + nombreArchivo + "." + this.documentoSeleccionado.getTipoArchivo();
				FileOutputStream fos = new FileOutputStream( pathArchivo );
				int b = 0;
				while ((b = is.read()) != -1){
				    fos.write(b); 
				}
				
				is.close();
				fos.flush();

				InputStream stream = new FileInputStream( pathArchivo );
				download = new DefaultStreamedContent(stream, Files.probeContentType( Paths.get(pathArchivo) ), this.documentoSeleccionado.getNombreDocumento() + "." + this.documentoSeleccionado.getTipoArchivo());
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return download;
	}
	
	public void aprobarDocumento(){
		try {
			//Validar que no esté vacío
			if(this.documentoSeleccionado!=null && this.documentoSeleccionado.getIdDocumento()!=0){
				
				//Sobre cargar
				Date fechaActual = new Date();
				this.documentoSeleccionado.setEstado(Documento.ESTADO_APROBADO);
				this.documentoSeleccionado.setFechaAprobacion(fechaActual);
				DocumentoAsociado docAsociado = this.documentoSeleccionado.getDocumentoAsociado();
				docAsociado.setFechaModificacion(fechaActual);
				
				//Actualizar datos del documento - Aprobacion
				if(ejbGenerico.actualizarObjeto(this.documentoSeleccionado)){
					System.out.println("Se aprobó el documento. Falta actualizar el documento asociado.");
					//Actualizar el documento asociado - Fecha de cargue reciente.
					if(ejbGenerico.actualizarObjeto(docAsociado)){
						System.out.println("Se actualizó el documento asociado.");
						Mensaje.mostrarMensaje(Mensaje.INFO, "Se aprobó el documento. Ya está disponible para su consulta/descarga.");
					}else{
						System.err.println("No se logró actualizar la fecha del documento asociado");
					}
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error aprobando el documento, por favor intentelo de nuevo más tarde.");
				}
				
				//Limpiar y re-cargar lista
				limpiar();
				cargarListaDocumentosPorAprobar();
				
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Hay campos vacíos, por favor verifique y vuelva a intentarlo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void descartarDocumento(){
		try {
			//Validar que no esté vacío
			if(this.documentoSeleccionado!=null && this.documentoSeleccionado.getIdDocumento()!=0){
				
				//Ejecutar la eliminar documento
				boolean resultado = ejbGenerico.eliminarObjectoPorQuery(documentoSeleccionado, documentoSeleccionado.getIdDocumento());
				if(resultado){
					Mensaje.mostrarMensaje(Mensaje.INFO, "Se descartó el documento y eliminó del sistema.");
				}else{
					Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error descartando el documento, por favor intentelo de nuevo más tarde.");
				}
				//Limpiar y re-cargar lista
				limpiar();
				cargarListaDocumentosPorAprobar();
				
			}else{
				Mensaje.mostrarMensaje(Mensaje.WARN, "Hay campos vacíos, por favor verifique y vuelva a intentarlo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Get & Set*/
	public SesionBean getSesionBean() {
		return sesionBean;
	}
	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}
	public List<Documento> getListaDocumentos() {
		return listaDocumentos;
	}
	public void setListaDocumentos(List<Documento> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}
	public String getFiltroDocumento() {
		return filtroDocumento;
	}
	public void setFiltroDocumento(String filtroDocumento) {
		this.filtroDocumento = filtroDocumento;
	}
	public Documento getDocumentoSeleccionado() {
		return documentoSeleccionado;
	}
	public void setDocumentoSeleccionado(Documento documentoSeleccionado) {
		this.documentoSeleccionado = documentoSeleccionado;
	}
}
