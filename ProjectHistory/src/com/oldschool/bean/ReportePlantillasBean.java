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
import com.oldschool.model.TipoDocumento;

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
//	private List<ReportePlantilla> listaReporte;
	List<TipoDocumento> listaReporte;
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
	
	private List<TipoDocumento> analizarLista(List<TipoDocumento> lista){
		List<TipoDocumento> listaSalida = new ArrayList<>();
		//Se valida que la lista no esté vacía
		if(lista!=null && !lista.isEmpty()){
			TipoDocumento temp = lista.get(0);	//Empezar en el primer registro de la lista
			int contador = 0;
			for (int i = 0; i < lista.size(); i++) {
				TipoDocumento tipoDocumento = lista.get(i);
				if(temp.getId_Tipo_Documento() != tipoDocumento.getId_Tipo_Documento()){
					//Asignar el objeto a la lista
					listaSalida.add(temp);
					//Cambiar de temporal
					temp = tipoDocumento;
					//reiniciar contador en 0
					contador = 0;
				}
				//En cada iteración va aumentando el contador
				temp.setCantVecesUsada(++contador);
			}
			//La última iteracion tambien se debe agregar a la lista de salida
			listaSalida.add(temp);
		}
		//Se retorna la lista
		return listaSalida;
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
			Map<Integer, Object> parametros = new HashMap<Integer, Object>();
			//Comprobar si hay filtro de area
			if(this.areaSeleccionada!=null && this.areaSeleccionada.getId_Area()!=0){
				
				parametros.put(1, this.areaSeleccionada.getId_Area());
				
				if(this.proyectoSeleccionado!=null & this.proyectoSeleccionado.getId_Proyecto()!=0){
					parametros.put(2, this.proyectoSeleccionado.getId_Proyecto());
					
					//Busca por con todos los filtros
					if(this.fechaInicio!=null && this.fechaFin!=null && this.fechaInicio.compareTo(fechaFin) < 0){
						parametros.put(3, this.fechaInicio);
						parametros.put(4, this.fechaFin);
						this.listaReporte = (List<TipoDocumento>)(List<?>)ejbGenerico.listarPorNativeQuery(new TipoDocumento(), "findByAreaProyectoFechas", parametros);
					}
					//Busca por área y proyecto
					else{
						this.listaReporte = (List<TipoDocumento>)(List<?>)ejbGenerico.listarPorNativeQuery(new TipoDocumento(), "findByAreaProyecto", parametros);
					}
					
				}
				// Busca por área y fecha de modificación
				else if(this.fechaInicio!=null && this.fechaFin!=null && this.fechaInicio.compareTo(fechaFin) < 0){
					parametros.put(2, this.fechaInicio);
					parametros.put(3, this.fechaFin);
					this.listaReporte = (List<TipoDocumento>)(List<?>)ejbGenerico.listarPorNativeQuery(new TipoDocumento(), "findByAreaFechas", parametros);
				}
				//Solo busca el por área
				else{
					this.listaReporte = (List<TipoDocumento>)(List<?>)ejbGenerico.listarPorNativeQuery(new TipoDocumento(), "findByArea", parametros);
				}
			}
			//Comprobar si hay filtro de proyecto
			else if(this.proyectoSeleccionado!=null & this.proyectoSeleccionado.getId_Proyecto()!=0){
				
				parametros.put(1, this.proyectoSeleccionado.getId_Proyecto());
				
				if(this.fechaInicio!=null && this.fechaFin!=null && this.fechaInicio.compareTo(fechaFin) < 0){
					parametros.put(2, this.fechaInicio);
					parametros.put(3, this.fechaFin);
					this.listaReporte = (List<TipoDocumento>)(List<?>)ejbGenerico.listarPorNativeQuery(new TipoDocumento(), "findByProyectoFechas", parametros);
				}else{
					this.listaReporte = (List<TipoDocumento>)(List<?>)ejbGenerico.listarPorNativeQuery(new TipoDocumento(), "findByProyecto", parametros);
				}
				
			}
			//Comprobar si hay filtro de fechas
			else if(this.fechaInicio!=null && this.fechaFin!=null && this.fechaInicio.compareTo(fechaFin) < 0){
				parametros.put(1, this.fechaInicio);
				parametros.put(2, this.fechaFin);
				this.listaReporte = (List<TipoDocumento>)(List<?>)ejbGenerico.listarPorNativeQuery(new TipoDocumento(), "findByFechas", parametros);
			}
			//No hay ningun filtro aplicado, listar todo
			else{
				this.listaReporte = (List<TipoDocumento>)(List<?>)ejbGenerico.listarPorNativeQuery(new TipoDocumento(), "findEveryReference", new HashMap<>());
				//listaReporte = (List<TipoDocumento>)(List<?>) ejbGenerico.listarTodo(new TipoDocumento());
			}
			
			this.listaReporte = analizarLista(this.listaReporte);
			
			generarReporte();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generarReporte(){
		reportePie = new PieChartModel();
		
		if(listaReporte!=null && !listaReporte.isEmpty()){
			for (TipoDocumento reporte: listaReporte) {
				String titulo = reporte.getNombre_Tipo_Documento() + "[" + reporte.getCantVecesUsada() + "]";
				reportePie.set(titulo, reporte.getCantVecesUsada());
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
	public List<TipoDocumento> getListaReporte() {
		return listaReporte;
	}
	public void setListaReporte(List<TipoDocumento> listaReporte) {
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
