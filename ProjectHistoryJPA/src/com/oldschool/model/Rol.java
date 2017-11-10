package com.oldschool.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the rol database table.
 * 
 */
@Entity
@Table(name="rol")
@NamedQuery(name="Rol.findAll", query="SELECT r FROM Rol r")
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	public static String ROL_ADMINISTRADOR = "Administrador";
	public static String ROL_CONSULTOR = "Consultor";
	public static String ROL_APROBADOR = "Aprobador";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_Rol;

	private byte activo;

	private String descripcion_Rol;

	private String nombre_Rol;

	//bi-directional many-to-many association to Usuario
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(
		name="rol_usuario"
		, joinColumns={
			@JoinColumn(name="Id_Rol")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Id_Usuario")
			}
		)
	private List<Usuario> usuarios;

	public Rol() {
	}

	public int getId_Rol() {
		return this.id_Rol;
	}

	public void setId_Rol(int id_Rol) {
		this.id_Rol = id_Rol;
	}

	public byte getActivo() {
		return this.activo;
	}

	public void setActivo(byte activo) {
		this.activo = activo;
	}

	public String getDescripcion_Rol() {
		return this.descripcion_Rol;
	}

	public void setDescripcion_Rol(String descripcion_Rol) {
		this.descripcion_Rol = descripcion_Rol;
	}

	public String getNombre_Rol() {
		return this.nombre_Rol;
	}

	public void setNombre_Rol(String nombre_Rol) {
		this.nombre_Rol = nombre_Rol;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}