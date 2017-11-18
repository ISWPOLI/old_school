package com.oldschool.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the documento_asociado database table.
 * 
 */
@Entity
@Table(name="documento_asociado")
@NamedQueries({
	@NamedQuery(name="DocumentoAsociado.findAll", query="SELECT d FROM DocumentoAsociado d"),
	@NamedQuery(name="DocumentoAsociado.findByProyecto", query="SELECT d FROM DocumentoAsociado d WHERE d.proyecto.id_Proyecto = :id_Proyecto"),
	@NamedQuery(name="DocumentoAsociado.eliminarPorId", query="DELETE FROM DocumentoAsociado d WHERE d.id_Documento_Asociado = :ID"),
	@NamedQuery(name="DocumentoAsociado.findByTipoDocumento", query="SELECT d FROM DocumentoAsociado d WHERE d.tipoDocumento.id_Tipo_Documento = :id_Tipo_Documento"),
//	@NamedQuery(name="DocumentoAsociado.findPendientes", query="SELECT d FROM DocumentoAsociado d WHERE d.estado = 'P'"),
})
public class DocumentoAsociado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_Documento_Asociado;

	@Column(name="fecha_modificacion")
	private Date fechaModificacion;
	
//	@Transient
//	private boolean tieneDocAsociados;
	
	//bi-directional many-to-one association to Documento
	@OneToMany(mappedBy="documentoAsociado")
	private List<Documento> documentos;

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

	public List<Documento> getDocumentos() {
		return this.documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}

	public Documento addDocumento(Documento documento) {
		getDocumentos().add(documento);
		documento.setDocumentoAsociado(this);

		return documento;
	}

	public Documento removeDocumento(Documento documento) {
		getDocumentos().remove(documento);
		documento.setDocumentoAsociado(null);

		return documento;
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

	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	/*public boolean isTieneDocAsociados() {
		if(this.documentos!=null && !this.documentos.isEmpty()){
			tieneDocAsociados = true;
		}
		return tieneDocAsociados;
	}
	
	public void setTieneDocAsociados(boolean tieneDocAsociados) {
		this.tieneDocAsociados = tieneDocAsociados;
	}*/
	
}