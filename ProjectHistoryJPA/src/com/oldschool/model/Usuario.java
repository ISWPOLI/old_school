package com.oldschool.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Table(name="usuario")
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u ORDER BY u.activo DESC"),
	@NamedQuery(name="Usuario.findByEstado", query="SELECT u FROM Usuario u WHERE u.activo = :estado"),
	@NamedQuery(name="Usuario.findIdByUser", query="SELECT u.id_usuario FROM Usuario u WHERE u.login = :login"),
	@NamedQuery(name="Usuario.login", query="SELECT u FROM Usuario u WHERE u.login = :login AND u.contraseña = :pass AND u.activo = 1"),
	@NamedQuery(name="Usuario.findByNombres", query="SELECT u FROM Usuario u WHERE LOWER(u.nombre_Usuario) LIKE :nombre OR LOWER(u.apellido) LIKE :apellido"),
	@NamedQuery(name="Usuario.findByNombresYEstado", query="SELECT u FROM Usuario u WHERE (LOWER(u.nombre_Usuario) LIKE :nombre OR LOWER(u.apellido) LIKE :apellido) AND u.activo = :estado "),
})
@NamedNativeQueries({
	@NamedNativeQuery(name="Usuario.updatePassword", query="UPDATE usuario SET contraseña = ? WHERE Id_usuario = ?")
})
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final byte ESTADO_ACTIVO = 1; 
	public static final byte ESTADO_INACTIVO = 0;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_usuario;

	private byte activo;

	private String apellido;

	private String contraseña;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha_Ultimo_Acceso;

	private String genero;

	private String login;

	private String nombre_Usuario;

	//bi-directional many-to-one association to DocumentoAsociado
	@OneToMany(mappedBy="usuario")
	private List<DocumentoAsociado> documentoAsociados;

	//bi-directional many-to-one association to Proyecto
	@OneToMany(mappedBy="usuario")
	private List<Proyecto> proyectos;

	//bi-directional many-to-many association to Rol
	//@ManyToMany(mappedBy="usuarios")
	@ManyToMany(mappedBy="usuarios", fetch=FetchType.LAZY)
	private List<Rol> rols;

	public Usuario() {
	}

	public int getId_usuario() {
		return this.id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public byte getActivo() {
		return this.activo;
	}

	public void setActivo(byte activo) {
		this.activo = activo;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getContraseña() {
		return this.contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public Date getFecha_Ultimo_Acceso() {
		return this.fecha_Ultimo_Acceso;
	}

	public void setFecha_Ultimo_Acceso(Date fecha_Ultimo_Acceso) {
		this.fecha_Ultimo_Acceso = fecha_Ultimo_Acceso;
	}

	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNombre_Usuario() {
		return this.nombre_Usuario;
	}

	public void setNombre_Usuario(String nombre_Usuario) {
		this.nombre_Usuario = nombre_Usuario;
	}

	public List<DocumentoAsociado> getDocumentoAsociados() {
		return this.documentoAsociados;
	}

	public void setDocumentoAsociados(List<DocumentoAsociado> documentoAsociados) {
		this.documentoAsociados = documentoAsociados;
	}

	public DocumentoAsociado addDocumentoAsociado(DocumentoAsociado documentoAsociado) {
		getDocumentoAsociados().add(documentoAsociado);
		documentoAsociado.setUsuario(this);

		return documentoAsociado;
	}

	public DocumentoAsociado removeDocumentoAsociado(DocumentoAsociado documentoAsociado) {
		getDocumentoAsociados().remove(documentoAsociado);
		documentoAsociado.setUsuario(null);

		return documentoAsociado;
	}

	public List<Proyecto> getProyectos() {
		return this.proyectos;
	}

	public void setProyectos(List<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	public Proyecto addProyecto(Proyecto proyecto) {
		getProyectos().add(proyecto);
		proyecto.setUsuario(this);

		return proyecto;
	}

	public Proyecto removeProyecto(Proyecto proyecto) {
		getProyectos().remove(proyecto);
		proyecto.setUsuario(null);

		return proyecto;
	}

	public List<Rol> getRols() {
		return this.rols;
	}

	public void setRols(List<Rol> rols) {
		this.rols = rols;
	}

	public String getNombreCompleto() {
		return nombre_Usuario + " " + apellido;
	}
	
	public String getNombreRoles(){
		StringBuilder nombresRoles = new StringBuilder();
		for (Rol rol : this.rols) {
			if(nombresRoles.length() != 0){
				nombresRoles.append(", ");
			}
			nombresRoles.append(rol.getNombre_Rol());
		}
		return nombresRoles.toString();
	}
	
	/*Roles*/
	public boolean isAdmin(){
		for (Rol rol : this.rols) {
			if(rol.getNombre_Rol().toUpperCase().equals(Rol.ROL_ADMINISTRADOR.toUpperCase())){
				return true;
			}
		}
		return false;
	}
	
	public boolean isConsultor(){
		for (Rol rol : this.rols) {
			if(rol.getNombre_Rol().toUpperCase().equals(Rol.ROL_CONSULTOR.toUpperCase())){
				return true;
			}
		}
		return false;
	}
	
	public boolean isAprobador(){
		for (Rol rol : this.rols) {
			if(rol.getNombre_Rol().toUpperCase().equals(Rol.ROL_APROBADOR.toUpperCase())){
				return true;
			}
		}
		return false;
	}
	
	
}