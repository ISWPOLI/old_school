package com.oldschool.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

import com.oldschool.ejb.EjbGenericoLocal;
import com.oldschool.model.Area;
import com.oldschool.model.Proyecto;
import com.oldschool.model.ReportePlantilla;

@ManagedBean(name= ReportePlantillasBean.BEAN_NAME)
@ViewScoped
public class ReportePlantillasBean implements Serializable{

	/*Variables Bean*/
	public static final String BEAN_NAME = "reportePlantillasBean";
	private static final long serialVersionUID = 1461272576618969285L;
	@EJB private EjbGenericoLocal ejbGenerico;
	
	/*Variables*/
	private List<Proyecto> listaProyectos;
	private List<Area> listaAreas;
	private List<ReportePlantilla> listaReporte;
	private PieChartModel reportePie;
	//Filtros
	private Date fechaInicio;
	private Date fechaFin;
	private Proyecto proyectoSeleccionado;
	private Area areaSeleccionada;
	//
	private Area nuevaArea;
	private Proyecto nuevaProyecto;
	
	/*Métodos privados*/
	@SuppressWarnings("unchecked")
	private void cargarProyectos(){
		try {
			this.listaProyectos = (List<Proyecto>)(List<?>)ejbGenerico.listarTodo(new Proyecto());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarAreas(){
		try {
			this.listaAreas = (List<Area>)(List<?>)ejbGenerico.listarTodo(new Area());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Métodos públicos*/
	@PostConstruct
	public void init(){
		try {
			limpiar();
			cargarAreas();
			cargarProyectos();
			cargarReporte();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void limpiar(){
		this.listaProyectos = new ArrayList<>();
		this.listaAreas = new ArrayList<>();
		this.listaReporte = new ArrayList<>();
		this.reportePie = new PieChartModel();
		//filtros
//		Calendar cal = Calendar.getInstance();
//		int mes = cal.get(Calendar.MONTH)+1;
//		int anio = cal.get(Calendar.YEAR);
		//this.fechaInicio = Fecha.construirDate(cal.getActualMinimum(Calendar.DAY_OF_MONTH), mes, anio, 0, 0, 0);
		//this.fechaFin = Fecha.construirDate(cal.getActualMaximum(Calendar.DAY_OF_MONTH), mes, anio, 0, 0, 0);
		this.fechaInicio = null;
		this.fechaFin = null;
		this.proyectoSeleccionado = new Proyecto();
		this.areaSeleccionada = new Area();
		
		nuevaArea = new Area();
		nuevaProyecto = new Proyecto();
	}
	
	@SuppressWarnings("unchecked")
	public void cargarReporte(){
		try {
			
			if(this.areaSeleccionada!=null && this.areaSeleccionada.getId_Area()!=0){
				
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("area", this.areaSeleccionada.getId_Area());
				
				if(this.proyectoSeleccionado!=null & this.proyectoSeleccionado.getId_Proyecto()!=0){
					parametros.put("proyecto", this.proyectoSeleccionado.getId_Proyecto());
					
					if(this.fechaInicio!=null && this.fechaFin!=null && this.fechaInicio.compareTo(fechaFin) < 0){
						parametros.put("fechaIni", this.fechaInicio);
						parametros.put("fechaFin", this.fechaFin);
						this.listaReporte = (List<ReportePlantilla>)(List<?>)ejbGenerico.listarPorQuery(new ReportePlantilla(), "findByAreaProyectoFechas", parametros);
					}else{
						this.listaReporte = (List<ReportePlantilla>)(List<?>)ejbGenerico.listarPorQuery(new ReportePlantilla(), "findByAreaProyecto", parametros);
					}
					
				} else if(this.fechaInicio!=null && this.fechaFin!=null && this.fechaInicio.compareTo(fechaFin) < 0){
					parametros.put("fechaIni", this.fechaInicio);
					parametros.put("fechaFin", this.fechaFin);
					this.listaReporte = (List<ReportePlantilla>)(List<?>)ejbGenerico.listarPorQuery(new ReportePlantilla(), "findByAreaFechas", parametros);
				} else{
					this.listaReporte = (List<ReportePlantilla>)(List<?>)ejbGenerico.listarPorQuery(new ReportePlantilla(), "findByArea", parametros);
				}
				
			} else if(this.proyectoSeleccionado!=null & this.proyectoSeleccionado.getId_Proyecto()!=0){
				
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("proyecto", this.proyectoSeleccionado.getId_Proyecto());
				
				if(this.fechaInicio!=null && this.fechaFin!=null && this.fechaInicio.compareTo(fechaFin) < 0){
					parametros.put("fechaIni", this.fechaInicio);
					parametros.put("fechaFin", this.fechaFin);
					this.listaReporte = (List<ReportePlantilla>)(List<?>)ejbGenerico.listarPorQuery(new ReportePlantilla(), "findByProyectoFechas", parametros);
				}else{
					this.listaReporte = (List<ReportePlantilla>)(List<?>)ejbGenerico.listarPorQuery(new ReportePlantilla(), "findByProyecto", parametros);
				}
				
			}else if(this.fechaInicio!=null && this.fechaFin!=null && this.fechaInicio.compareTo(fechaFin) < 0){
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("fechaIni", this.fechaInicio);
				parametros.put("fechaFin", this.fechaFin);
				
				this.listaReporte = (List<ReportePlantilla>)(List<?>)ejbGenerico.listarPorQuery(new ReportePlantilla(), "findByFechas", parametros);
			}else{
				this.listaReporte = (List<ReportePlantilla>)(List<?>)ejbGenerico.listarTodo(new ReportePlantilla());
			}
			
			generarReporte();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generarReporte(){
		reportePie = new PieChartModel();
		
		if(listaReporte!=null && !listaReporte.isEmpty()){
			for (ReportePlantilla reporte: listaReporte) {
				String titulo = reporte.getNombre_Tipo_Documento() + "[" + reporte.getCantidad().toString() + "]";
				reportePie.set(titulo, reporte.getCantidad());
			}
		}else{
			reportePie.set("Sin resultados", 0);
		}
		 
		reportePie.setTitle("Reporte");
		reportePie.setLegendPosition("e");
		reportePie.setShowDataLabels(true);
	 }
	
	/*Get & Set*/
	public List<Proyecto> getListaProyectos() {
		return listaProyectos;
	}
	public void setListaProyectos(List<Proyecto> listaProyectos) {
		this.listaProyectos = listaProyectos;
	}
	public List<Area> getListaAreas() {
		return listaAreas;
	}
	public void setListaAreas(List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}
	public List<ReportePlantilla> getListaReporte() {
		return listaReporte;
	}
	public void setListaReporte(List<ReportePlantilla> listaReporte) {
		this.listaReporte = listaReporte;
	}
	public PieChartModel getReportePie() {
		return reportePie;
	}
	public void setReportePie(PieChartModel reportePie) {
		this.reportePie = reportePie;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Proyecto getProyectoSeleccionado() {
		return proyectoSeleccionado;
	}
	public void setProyectoSeleccionado(Proyecto proyectoSeleccionado) {
		this.proyectoSeleccionado = proyectoSeleccionado;
	}
	public Area getAreaSeleccionada() {
		return areaSeleccionada;
	}
	public void setAreaSeleccionada(Area areaSeleccionada) {
		this.areaSeleccionada = areaSeleccionada;
	}
	public Area getNuevaArea() {
		return nuevaArea;
	}
	public void setNuevaArea(Area nuevaArea) {
		this.nuevaArea = nuevaArea;
	}
	public void setNuevaProyecto(Proyecto nuevaProyecto) {
		this.nuevaProyecto = nuevaProyecto;
	}
	public Proyecto getNuevaProyecto() {
		return nuevaProyecto;
	}

}
