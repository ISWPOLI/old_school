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
import org.primefaces.model.chart.PieChartModel;

import com.oldschool.ejb.EjbGenericoLocal;
import com.oldschool.model.Documento;
import com.oldschool.model.DocumentoAsociado;
import com.oldschool.model.Proyecto;
import com.oldschool.util.Mensaje;

@ManagedBean(name= HistoricoDocumentosBean.BEAN_NAME)
@ViewScoped
public class HistoricoDocumentosBean implements Serializable{

	/*Variables Bean*/
	public static final String BEAN_NAME = "historicoDocumentosBean";
	private static final long serialVersionUID = 1461272576618969285L;
	@EJB private EjbGenericoLocal ejbGenerico;
	
	/*Variables de sesión*/
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	/*Variables*/
	private Proyecto proyectoSeleccionado;
	private List<DocumentoAsociado> documentosAsociados;
	private PieChartModel reportePie;
	private DocumentoAsociado docAsociadoSeleccionado;
	private Documento documentoSeleccionado;
	
	/*Métodos privados*/
	@SuppressWarnings("unchecked")
	private void cargarDocumentosAsociados(){
		try {
			if(this.proyectoSeleccionado!=null && this.proyectoSeleccionado.getId_Proyecto()!=0){
				//Buscar los documentos asociados y obtener el último
				Map<String, Object> parametros = new HashMap<>();
				parametros.put("id_Proyecto", this.proyectoSeleccionado.getId_Proyecto());
				
				documentosAsociados = (List<DocumentoAsociado>)(List<?>)ejbGenerico.listarPorQuery(new DocumentoAsociado(), "findByProyecto", parametros);
				
			}else{
				System.err.println("No se seleccionó un proyecto");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Métodos públicos*/
	@PostConstruct
	public void init(){
		limpiar();
		if(this.proyectoSeleccionado==null || this.proyectoSeleccionado.getId_Proyecto()==0){
			this.proyectoSeleccionado = this.sesionBean.getProyectoSeleccinado();
		}
		cargarDocumentosAsociados();
		cargarReporte();
	}
	
	public void limpiar(){
		reportePie = new PieChartModel();
		proyectoSeleccionado = new Proyecto();
		documentosAsociados = new ArrayList<>();
	}
	
	public void cargarReporte(){
		try {
			reportePie = new PieChartModel();
			
			if(this.documentosAsociados!=null && this.documentosAsociados.size()>0){
				for (DocumentoAsociado documento: documentosAsociados) {
					String titulo = documento.getTipoDocumento().getNombre_Tipo_Documento() + "[" + documento.getCantDocumentos() + "]";
					reportePie.set(titulo, documento.getCantDocumentos());
				}
			}else{
				reportePie.set("Sin resultados", 0);
			}
			
			reportePie.setTitle("Reporte");
			reportePie.setLegendPosition("e");
			reportePie.setShowDataLabels(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public StreamedContent descargarDocumento(){
		StreamedContent download = new DefaultStreamedContent();
		try {
			
			if(documentoSeleccionado!=null && documentoSeleccionado.getIdDocumento()!=0){
				
				Blob blob = new SerialBlob( documentoSeleccionado.getDocumento() );
				InputStream is = blob.getBinaryStream();
				
				String nombreArchivo = documentoSeleccionado.getNombreDescarga();
				String pathArchivo = System.getProperty("java.io.tmpdir") + File.separator + nombreArchivo + "." + documentoSeleccionado.getTipoArchivo();
				FileOutputStream fos = new FileOutputStream( pathArchivo );
				int b = 0;
				while ((b = is.read()) != -1){
					fos.write(b); 
				}
				
				is.close();
				fos.flush();
				
				InputStream stream = new FileInputStream( pathArchivo );
				download = new DefaultStreamedContent(stream, Files.probeContentType( Paths.get(pathArchivo) ), nombreArchivo + "." + documentoSeleccionado.getTipoArchivo());
				
			}else{
				Mensaje.mostrarMensaje(Mensaje.ERROR, "Ha ocurrido un error, por favor intentelo de nuevo más tarde.");
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
	public Proyecto getProyectoSeleccionado() {
		return proyectoSeleccionado;
	}
	public void setProyectoSeleccionado(Proyecto proyectoSeleccionado) {
		this.proyectoSeleccionado = proyectoSeleccionado;
	}
	public List<DocumentoAsociado> getDocumentosAsociados() {
		return documentosAsociados;
	}
	public void setDocumentosAsociados(List<DocumentoAsociado> documentosAsociados) {
		this.documentosAsociados = documentosAsociados;
	}
	public PieChartModel getReportePie() {
		return reportePie;
	}
	public void setReportePie(PieChartModel reportePie) {
		this.reportePie = reportePie;
	}
	public DocumentoAsociado getDocAsociadoSeleccionado() {
		return docAsociadoSeleccionado;
	}
	public void setDocAsociadoSeleccionado(DocumentoAsociado docAsociadoSeleccionado) {
		this.docAsociadoSeleccionado = docAsociadoSeleccionado;
	}
	public Documento getDocumentoSeleccionado() {
		return documentoSeleccionado;
	}
	public void setDocumentoSeleccionado(Documento documentoSeleccionado) {
		this.documentoSeleccionado = documentoSeleccionado;
	}
}
