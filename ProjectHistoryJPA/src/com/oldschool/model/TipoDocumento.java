package com.oldschool.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the tipo_documento database table.
 * 
 */
@Entity
@Table(name="tipo_documento")
@NamedQueries({
	@NamedQuery(name="TipoDocumento.findAll", query="SELECT t FROM TipoDocumento t"),
	@NamedQuery(name="TipoDocumento.findByNombre", query="SELECT t FROM TipoDocumento t WHERE LOWER(t.nombre_Tipo_Documento) LIKE :nombre_Tipo_Documento"),
	@NamedQuery(name="TipoDocumento.eliminarPorId", query="DELETE FROM TipoDocumento t WHERE t.id_Tipo_Documento = :ID"),
	/*Reporte*/
//	@NamedQuery(name="TipoDocumento.findByArea", query="SELECT t FROM TipoDocumento t JOIN t.documentoAsociados da ON t.id_Tipo_Documento = da.tipoDocumento "), //id_Area = :area
//	@NamedQuery(name="TipoDocumento.findByAreaFechas", query="SELECT d FROM DocumentoAsociado d WHERE d.proyecto.area.id_Area = :area AND d.fechaModificacion BETWEEN :fechaIni AND :fechaFin"),
//	@NamedQuery(name="TipoDocumento.findByAreaProyecto", query="SELECT d FROM DocumentoAsociado d WHERE d.proyecto.area.id_Area = :area AND d.proyecto.id_Proyecto = :proyecto"),
//	@NamedQuery(name="TipoDocumento.findByAreaProyectoFechas", query="SELECT d FROM DocumentoAsociado d WHERE d.proyecto.area.id_Area = :area AND d.proyecto.id_Proyecto = :proyecto AND d.fechaModificacion BETWEEN :fechaIni AND :fechaFin"),
//	@NamedQuery(name="TipoDocumento.findByProyectoFechas", query="SELECT d FROM DocumentoAsociado d WHERE d.proyecto.id_Proyecto = :ID AND d.fechaModificacion BETWEEN :fechaIni AND :fechaFin"),
//	@NamedQuery(name="TipoDocumento.findByFechas", query="SELECT d FROM DocumentoAsociado d WHERE d.fechaModificacion BETWEEN :fechaIni AND :fechaFin"),
})
@NamedNativeQueries({
	@NamedNativeQuery(name="TipoDocumento.findEveryReference", query="SELECT td.* FROM tipo_documento td JOIN documento_asociado da ON td.Id_Tipo_Documento = da.Id_Tipo_Documento ORDER BY td.Id_Tipo_Documento", resultClass=TipoDocumento.class),
	@NamedNativeQuery(name="TipoDocumento.findByArea", query="SELECT td.* FROM tipo_documento td JOIN documento_asociado da ON td.Id_Tipo_Documento = da.Id_Tipo_Documento JOIN proyecto p ON da.Id_Proyecto = p.Id_Proyecto WHERE p.Id_Area = ? ORDER BY td.Id_Tipo_Documento", resultClass=TipoDocumento.class),
	@NamedNativeQuery(name="TipoDocumento.findByAreaFechas", query="SELECT td.* FROM tipo_documento td JOIN documento_asociado da ON td.Id_Tipo_Documento = da.Id_Tipo_Documento JOIN proyecto p ON da.Id_Proyecto = p.Id_Proyecto  WHERE p.Id_Area = ? AND da.fecha_modificacion BETWEEN ? AND ? ORDER BY td.Id_Tipo_Documento", resultClass=TipoDocumento.class),
	@NamedNativeQuery(name="TipoDocumento.findByAreaProyecto", query="SELECT td.* FROM tipo_documento td JOIN documento_asociado da ON td.Id_Tipo_Documento = da.Id_Tipo_Documento JOIN proyecto p ON da.Id_Proyecto = p.Id_Proyecto WHERE p.Id_Area = ? AND da.Id_Proyecto = ? ORDER BY td.Id_Tipo_Documento", resultClass=TipoDocumento.class),
	@NamedNativeQuery(name="TipoDocumento.findByAreaProyectoFechas", query="SELECT td.* FROM tipo_documento td JOIN documento_asociado da ON td.Id_Tipo_Documento = da.Id_Tipo_Documento JOIN proyecto p ON da.Id_Proyecto = p.Id_Proyecto WHERE p.Id_Area = ? AND da.Id_Proyecto = ? AND da.fecha_modificacion BETWEEN ? AND ? ORDER BY td.Id_Tipo_Documento", resultClass=TipoDocumento.class),
	@NamedNativeQuery(name="TipoDocumento.findByProyecto", query="SELECT td.* FROM tipo_documento td JOIN documento_asociado da ON td.Id_Tipo_Documento = da.Id_Tipo_Documento JOIN proyecto p ON da.Id_Proyecto = p.Id_Proyecto  WHERE da.Id_Proyecto = ? ORDER BY td.Id_Tipo_Documento", resultClass=TipoDocumento.class),
	@NamedNativeQuery(name="TipoDocumento.findByProyectoFechas", query="SELECT td.* FROM tipo_documento td JOIN documento_asociado da ON td.Id_Tipo_Documento = da.Id_Tipo_Documento JOIN proyecto p ON da.Id_Proyecto = p.Id_Proyecto WHERE da.Id_Proyecto = ? AND da.fecha_modificacion BETWEEN ? AND ? ORDER BY td.Id_Tipo_Documento", resultClass=TipoDocumento.class),
	@NamedNativeQuery(name="TipoDocumento.findByFechas", query="SELECT td.* FROM tipo_documento td JOIN documento_asociado da ON td.Id_Tipo_Documento = da.Id_Tipo_Documento JOIN proyecto p ON da.Id_Proyecto = p.Id_Proyecto WHERE da.fecha_modificacion BETWEEN ? AND ? ORDER BY td.Id_Tipo_Documento", resultClass=TipoDocumento.class),
})
public class TipoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_Tipo_Documento;

	private String nombre_Tipo_Documento;

	@Column(name="tipo_archivo")
	private String tipo_archivo;
	
	@Column(name="fecha_modificacion")
	private Date fecha_modificacion;
	
	@Column(name="tamanio_documento")
	private long tamanio_documento;
	
	@Transient
	private int cantVecesUsada;
	
	@Lob
	private byte[] plantilla;

	//bi-directional many-to-one association to DocumentoAsociado
	@OneToMany(mappedBy="tipoDocumento")
	private List<DocumentoAsociado> documentoAsociados;

	public TipoDocumento() {
	}

	public int getId_Tipo_Documento() {
		return this.id_Tipo_Documento;
	}

	public void setId_Tipo_Documento(int id_Tipo_Documento) {
		this.id_Tipo_Documento = id_Tipo_Documento;
	}

	public String getNombre_Tipo_Documento() {
		return this.nombre_Tipo_Documento;
	}

	public void setNombre_Tipo_Documento(String nombre_Tipo_Documento) {
		this.nombre_Tipo_Documento = nombre_Tipo_Documento;
	}

	public byte[] getPlantilla() {
		return this.plantilla;
	}

	public void setPlantilla(byte[] plantilla) {
		this.plantilla = plantilla;
	}
	
	public String getTipo_archivo() {
		return tipo_archivo;
	}
	public void setTipo_archivo(String tipo_archivo) {
		this.tipo_archivo = tipo_archivo;
	}

	public Date getFecha_modificacion() {
		return fecha_modificacion;
	}
	public void setFecha_modificacion(Date fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}
	
	public long getTamanio_documento() {
		return tamanio_documento;
	}
	public void setTamanio_documento(long tamanio_documento) {
		this.tamanio_documento = tamanio_documento;
	}
	
	public List<DocumentoAsociado> getDocumentoAsociados() {
		return this.documentoAsociados;
	}

	public void setDocumentoAsociados(List<DocumentoAsociado> documentoAsociados) {
		this.documentoAsociados = documentoAsociados;
	}

	public DocumentoAsociado addDocumentoAsociado(DocumentoAsociado documentoAsociado) {
		getDocumentoAsociados().add(documentoAsociado);
		documentoAsociado.setTipoDocumento(this);

		return documentoAsociado;
	}

	public DocumentoAsociado removeDocumentoAsociado(DocumentoAsociado documentoAsociado) {
		getDocumentoAsociados().remove(documentoAsociado);
		documentoAsociado.setTipoDocumento(null);

		return documentoAsociado;
	}
	
	public void setCantVecesUsada(int cantVecesUsada) {
		this.cantVecesUsada = cantVecesUsada;
	}
	public int getCantVecesUsada() {
		return cantVecesUsada;
	}

}