package com.oldschool.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the area database table.
 * 
 */
@Entity
@Table(name="area")
@NamedQueries({
	@NamedQuery(name="Area.findAll", query="SELECT a FROM Area a"),
	@NamedQuery(name="Area.findByNombre", query="SELECT a FROM Area a WHERE LOWER(a.nombre_Area) LIKE :nombre_Area")
})
public class Area implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_Area;

	private String nombre_Area;

	//bi-directional many-to-one association to Proyecto
	@OneToMany(mappedBy="area")
	private List<Proyecto> proyectos;

	public Area() {
	}
	
	public Area(int idArea) {
		this.id_Area = idArea;
	}

	public int getId_Area() {
		return this.id_Area;
	}

	public void setId_Area(int id_Area) {
		this.id_Area = id_Area;
	}

	public String getNombre_Area() {
		return this.nombre_Area;
	}

	public void setNombre_Area(String nombre_Area) {
		this.nombre_Area = nombre_Area;
	}

	public List<Proyecto> getProyectos() {
		return this.proyectos;
	}

	public void setProyectos(List<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	public Proyecto addProyecto(Proyecto proyecto) {
		getProyectos().add(proyecto);
		proyecto.setArea(this);

		return proyecto;
	}

	public Proyecto removeProyecto(Proyecto proyecto) {
		getProyectos().remove(proyecto);
		proyecto.setArea(null);

		return proyecto;
	}

}