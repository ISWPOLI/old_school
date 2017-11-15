package com.oldschool.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the proyecto database table.
 * 
 */
@Entity
@Table(name="proyecto")
@NamedQueries({
	@NamedQuery(name="Proyecto.findAll", query="SELECT p FROM Proyecto p"),
	@NamedQuery(name="Proyecto.findByNombre", query="SELECT p FROM Proyecto p WHERE LOWER(p.nombre_Proyecto) LIKE :nombre_Proyecto")
	
})
public class Proyecto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_Proyecto;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha_Creacion_Proyecto;

	private String nombre_Proyecto;
	
	private String descripcion;
	
	private String estado;

	//bi-directional many-to-one association to DocumentoAsociado
	@OneToMany(mappedBy="proyecto")
	private List<DocumentoAsociado> documentoAsociados;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="Id_Usuario")
	private Usuario usuario;

	//bi-directional many-to-one association to Area
	@ManyToOne
	@JoinColumn(name="Id_Area")
	private Area area;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="Id_Cliente")
	private Cliente cliente;

	public Proyecto() {
	}
	
	public Proyecto(int idProyecto) {
		this.id_Proyecto = idProyecto;
	}

	public int getId_Proyecto() {
		return this.id_Proyecto;
	}

	public void setId_Proyecto(int id_Proyecto) {
		this.id_Proyecto = id_Proyecto;
	}

	public Date getFecha_Creacion_Proyecto() {
		return this.fecha_Creacion_Proyecto;
	}

	public void setFecha_Creacion_Proyecto(Date fecha_Creacion_Proyecto) {
		this.fecha_Creacion_Proyecto = fecha_Creacion_Proyecto;
	}

	public String getNombre_Proyecto() {
		return this.nombre_Proyecto;
	}

	public void setNombre_Proyecto(String nombre_Proyecto) {
		this.nombre_Proyecto = nombre_Proyecto;
	}

	public List<DocumentoAsociado> getDocumentoAsociados() {
		return this.documentoAsociados;
	}

	public void setDocumentoAsociados(List<DocumentoAsociado> documentoAsociados) {
		this.documentoAsociados = documentoAsociados;
	}

	public DocumentoAsociado addDocumentoAsociado(DocumentoAsociado documentoAsociado) {
		getDocumentoAsociados().add(documentoAsociado);
		documentoAsociado.setProyecto(this);

		return documentoAsociado;
	}

	public DocumentoAsociado removeDocumentoAsociado(DocumentoAsociado documentoAsociado) {
		getDocumentoAsociados().remove(documentoAsociado);
		documentoAsociado.setProyecto(null);

		return documentoAsociado;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Area getArea() {
		return this.area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

}