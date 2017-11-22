package com.oldschool.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the documento database table.
 * 
 */
@Entity
@Table(name="documento")
@NamedQueries({
	@NamedQuery(name="Documento.findAll", query="SELECT d FROM Documento d"),
	@NamedQuery(name="Documento.findByIdDocAsociado", query="SELECT d FROM Documento d WHERE d.documentoAsociado.id_Documento_Asociado = :id_Documento_Asociado ORDER BY d.fechaCargue DESC"),
	@NamedQuery(name="Documento.findByIdDocAsociadoYEstado", query="SELECT d FROM Documento d WHERE d.documentoAsociado.id_Documento_Asociado = :id_Documento_Asociado AND d.estado = :estado ORDER BY d.fechaCargue DESC"),
	@NamedQuery(name="Documento.findAprobadosByIdDocAsociado", query="SELECT d FROM Documento d WHERE d.documentoAsociado.id_Documento_Asociado = :id_Documento_Asociado AND d.estado = 'A' ORDER BY d.fechaAprobacion DESC"),
	@NamedQuery(name="Documento.findByEstado", query="SELECT d FROM Documento d WHERE d.estado = :estado ORDER BY d.fechaCargue DESC"),
	@NamedQuery(name="Documento.findByNombreYEstado", query="SELECT d FROM Documento d WHERE LOWER(d.nombreDocumento) LIKE :nombreDocumento AND d.estado = :estado ORDER BY d.fechaCargue DESC"),
	@NamedQuery(name="Documento.findByIdProyecto", query="SELECT d FROM Documento d WHERE d.documentoAsociado.proyecto.id_Proyecto = :id_Proyecto AND d.estado = 'A' ORDER BY d.documentoAsociado.id_Documento_Asociado ASC "),
	@NamedQuery(name="Documento.eliminarPorId", query="DELETE FROM Documento d WHERE d.idDocumento = :ID"),
})
public class Documento implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ESTADO_PENDIENTE = "P";
	public static final String ESTADO_APROBADO = "A";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_documento")
	private int idDocumento;

	@Lob
	@Column(name="documento")
	private byte[] documento;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_cargue")
	private Date fechaCargue;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_aprobacion")
	private Date fechaAprobacion;

	@Column(name="nombre_documento")
	private String nombreDocumento;
	
	@Column(name="tamanio_documento")
	private long tamanioDocumento;

	@Column(name="tipo_archivo")
	private String tipoArchivo;

	//bi-directional many-to-one association to DocumentoAsociado
	@ManyToOne
	@JoinColumn(name="id_documento_asociado")
	private DocumentoAsociado documentoAsociado;

	public Documento() {
	}

	public int getIdDocumento() {
		return this.idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public byte[] getDocumento() {
		return this.documento;
	}

	public void setDocumento(byte[] documento) {
		this.documento = documento;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaCargue() {
		return this.fechaCargue;
	}

	public void setFechaCargue(Date fechaCargue) {
		this.fechaCargue = fechaCargue;
	}
	
	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}
	
	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public String getNombreDocumento() {
		return nombreDocumento;
	}
	
	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}
	
	public long getTamanioDocumento() {
		return this.tamanioDocumento;
	}

	public void setTamanioDocumento(long tamanioDocumento) {
		this.tamanioDocumento = tamanioDocumento;
	}

	public String getTipoArchivo() {
		return this.tipoArchivo;
	}

	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}

	public DocumentoAsociado getDocumentoAsociado() {
		return this.documentoAsociado;
	}

	public void setDocumentoAsociado(DocumentoAsociado documentoAsociado) {
		this.documentoAsociado = documentoAsociado;
	}
	
	public String getEstadoString(){
		if(ESTADO_APROBADO.equals(estado)){
			return "Aprobado";
		}else if(ESTADO_PENDIENTE.equals(estado)){
			return "Pendiente";
		}else{
			return "";
		}
	}
	
	public String getNombreDescarga(){
		return nombreDocumento + "[" + getEstadoString() + "]";
	}

}