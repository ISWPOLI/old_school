package com.oldschool.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@Table(name="cliente")
@NamedQueries({
	@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c"),
	@NamedQuery(name="Cliente.findByEstado", query="SELECT c FROM Cliente c WHERE c.activo = :estado"),
	@NamedQuery(name="Cliente.findByNombreOrNit", query="SELECT c FROM Cliente c WHERE c.razon_Social = :nombre OR c.nit = :nit"),
	@NamedQuery(name="Cliente.findByNombreYEstado", query="SELECT c FROM Cliente c WHERE c.activo = :estado AND LOWER(c.razon_Social) LIKE :nombre"),
	@NamedQuery(name="Cliente.inactivarCliente", query="UPDATE Cliente c SET c.activo = 0 WHERE c.id_Cliente = :idCliente"),
	@NamedQuery(name="Cliente.activarCliente", query="UPDATE Cliente c SET c.activo = 1 WHERE c.id_Cliente = :idCliente")
})
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final byte ESTADO_ACTIVO = 1; 
	public static final byte ESTADO_INACTIVO = 0;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_Cliente;

	private byte activo;

	private String direccion;

	private String email;

	private long nit;

	private String razon_Social;

	private long telefono;

	//bi-directional many-to-one association to Proyecto
	@OneToMany(mappedBy="cliente")
	private List<Proyecto> proyectos;

	public Cliente() {
	}
	
	public Cliente(int idCliente) {
		this.id_Cliente = idCliente;
	}

	public int getId_Cliente() {
		return this.id_Cliente;
	}

	public void setId_Cliente(int id_Cliente) {
		this.id_Cliente = id_Cliente;
	}

	public byte getActivo() {
		return this.activo;
	}

	public void setActivo(byte activo) {
		this.activo = activo;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getNit() {
		return this.nit;
	}

	public void setNit(long nit) {
		this.nit = nit;
	}

	public String getRazon_Social() {
		return this.razon_Social;
	}

	public void setRazon_Social(String razon_Social) {
		this.razon_Social = razon_Social;
	}

	public long getTelefono() {
		return this.telefono;
	}

	public void setTelefono(long telefono) {
		this.telefono = telefono;
	}

	public List<Proyecto> getProyectos() {
		return this.proyectos;
	}

	public void setProyectos(List<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	public Proyecto addProyecto(Proyecto proyecto) {
		getProyectos().add(proyecto);
		proyecto.setCliente(this);

		return proyecto;
	}

	public Proyecto removeProyecto(Proyecto proyecto) {
		getProyectos().remove(proyecto);
		proyecto.setCliente(null);

		return proyecto;
	}

}