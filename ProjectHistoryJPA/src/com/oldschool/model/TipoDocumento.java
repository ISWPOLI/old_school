package com.oldschool.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_documento database table.
 * 
 */
@Entity
@Table(name="tipo_documento")
@NamedQuery(name="TipoDocumento.findAll", query="SELECT t FROM TipoDocumento t")
public class TipoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_Tipo_Documento;

	private String nombre_Tipo_Documento;

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

}