package com.oldschool.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the reporteplantillas database table.
 * 
 */
@Entity
@Table(name="reporteplantillas")
@NamedQueries({
	@NamedQuery(name="ReportePlantilla.findAll", query="SELECT r FROM ReportePlantilla r"),
	@NamedQuery(name="ReportePlantilla.findByAreaProyectoFechas", query="SELECT r FROM ReportePlantilla r WHERE r.id_Area = :area AND r.id_Proyecto = :proyecto AND r.fecha_Creacion_Proyecto BETWEEN :fechaIni AND :fechaFin"),
	@NamedQuery(name="ReportePlantilla.findByAreaProyecto", query="SELECT r FROM ReportePlantilla r WHERE r.id_Area = :area AND r.id_Proyecto = :proyecto"),
	@NamedQuery(name="ReportePlantilla.findByAreaFechas", query="SELECT r FROM ReportePlantilla r WHERE r.id_Area = :area AND r.fecha_Creacion_Proyecto BETWEEN :fechaIni AND :fechaFin"),
	@NamedQuery(name="ReportePlantilla.findByArea", query="SELECT r FROM ReportePlantilla r WHERE r.id_Area = :area"),
	@NamedQuery(name="ReportePlantilla.findByProyectoFechas", query="SELECT r FROM ReportePlantilla r WHERE r.id_Proyecto = :proyecto AND r.fecha_Creacion_Proyecto BETWEEN :fechaIni AND :fechaFin"),
	@NamedQuery(name="ReportePlantilla.findByProyecto", query="SELECT r FROM ReportePlantilla r WHERE r.id_Proyecto = :proyecto"),
	@NamedQuery(name="ReportePlantilla.findByFechas", query="SELECT r FROM ReportePlantilla r WHERE r.fecha_Creacion_Proyecto BETWEEN :fechaIni AND :fechaFin")
})
public class ReportePlantilla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id_Tipo_Documento;
	
	private String nombre_Tipo_Documento;
	
	private BigInteger cantidad;
	
	private int id_Proyecto;
	
	private String nombre_Proyecto;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha_Creacion_Proyecto;
	
	private int id_Cliente;
	
	private String razon_Social;
	
	private int nit;

	private int id_Area;

	private String nombre_Area;
	
	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="fecha_modificacion")
//	private Date fechaModificacion;

//	private Object plantilla;
//	private byte[] plantilla;

//	@Column(name="tamanio_documento")
//	private int tamanioDocumento;

//	@Column(name="tipo_archivo")
//	private String tipoArchivo;

	public ReportePlantilla() {
	}

	public BigInteger getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(BigInteger cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFecha_Creacion_Proyecto() {
		return this.fecha_Creacion_Proyecto;
	}

	public void setFecha_Creacion_Proyecto(Date fecha_Creacion_Proyecto) {
		this.fecha_Creacion_Proyecto = fecha_Creacion_Proyecto;
	}

//	public Date getFechaModificacion() {
//		return this.fechaModificacion;
//	}
//	public void setFechaModificacion(Date fechaModificacion) {
//		this.fechaModificacion = fechaModificacion;
//	}

	public int getId_Area() {
		return this.id_Area;
	}

	public void setId_Area(int id_Area) {
		this.id_Area = id_Area;
	}

	public int getId_Cliente() {
		return this.id_Cliente;
	}

	public void setId_Cliente(int id_Cliente) {
		this.id_Cliente = id_Cliente;
	}

	public int getId_Proyecto() {
		return this.id_Proyecto;
	}

	public void setId_Proyecto(int id_Proyecto) {
		this.id_Proyecto = id_Proyecto;
	}

	public int getId_Tipo_Documento() {
		return this.id_Tipo_Documento;
	}

	public void setId_Tipo_Documento(int id_Tipo_Documento) {
		this.id_Tipo_Documento = id_Tipo_Documento;
	}

	public int getNit() {
		return this.nit;
	}

	public void setNit(int nit) {
		this.nit = nit;
	}

	public String getNombre_Area() {
		return this.nombre_Area;
	}

	public void setNombre_Area(String nombre_Area) {
		this.nombre_Area = nombre_Area;
	}

	public String getNombre_Proyecto() {
		return this.nombre_Proyecto;
	}

	public void setNombre_Proyecto(String nombre_Proyecto) {
		this.nombre_Proyecto = nombre_Proyecto;
	}

	public String getNombre_Tipo_Documento() {
		return this.nombre_Tipo_Documento;
	}

	public void setNombre_Tipo_Documento(String nombre_Tipo_Documento) {
		this.nombre_Tipo_Documento = nombre_Tipo_Documento;
	}

//	public Object getPlantilla() {
//		return this.plantilla;
//	}
//	public void setPlantilla(Object plantilla) {
//		this.plantilla = plantilla;
//	}
	
//	public byte[] getPlantilla() {
//		return this.plantilla;
//	}
//
//	public void setPlantilla(byte[] plantilla) {
//		this.plantilla = plantilla;
//	}

	public String getRazon_Social() {
		return this.razon_Social;
	}

	public void setRazon_Social(String razon_Social) {
		this.razon_Social = razon_Social;
	}

//	public int getTamanioDocumento() {
//		return this.tamanioDocumento;
//	}
//	public void setTamanioDocumento(int tamanioDocumento) {
//		this.tamanioDocumento = tamanioDocumento;
//	}
//	public String getTipoArchivo() {
//		return this.tipoArchivo;
//	}
//	public void setTipoArchivo(String tipoArchivo) {
//		this.tipoArchivo = tipoArchivo;
//	}

}