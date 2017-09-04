package com.oldschool.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the documento_asociado database table.
 * 
 */
@Entity
@Table(name="documento_asociado")
@NamedQuery(name="DocumentoAsociado.findAll", query="SELECT d FROM DocumentoAsociado d")
public class DocumentoAsociado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_Documento_Asociado;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha_Cargue_Documento;

	private String nombre_Documento;

	//bi-directional many-to-one association to Proyecto
	@ManyToOne
	@JoinColumn(name="Id_Proyecto")
	private Proyecto proyecto;

	//bi-directional many-to-one association to TipoDocumento
	@ManyToOne
	@JoinColumn(name="Id_Tipo_Documento")
	private TipoDocumento tipoDocumento;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="Id_Usuario")
	private Usuario usuario;

	public DocumentoAsociado() {
	}

	public int getId_Documento_Asociado() {
		return this.id_Documento_Asociado;
	}

	public void setId_Documento_Asociado(int id_Documento_Asociado) {
		this.id_Documento_Asociado = id_Documento_Asociado;
	}

	public Date getFecha_Cargue_Documento() {
		return this.fecha_Cargue_Documento;
	}

	public void setFecha_Cargue_Documento(Date fecha_Cargue_Documento) {
		this.fecha_Cargue_Documento = fecha_Cargue_Documento;
	}

	public String getNombre_Documento() {
		return this.nombre_Documento;
	}

	public void setNombre_Documento(String nombre_Documento) {
		this.nombre_Documento = nombre_Documento;
	}

	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}