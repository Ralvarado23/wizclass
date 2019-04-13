package com.wizclass.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Paleta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String colorBaseHex;
	private String colorBaseCss;
	private String colorSecundarioHex;
	private String colorSecundarioCss;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="paleta_id")
	private List<Pagina> paginas = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getColorBaseHex() {
		return colorBaseHex;
	}

	public void setColorBaseHex(String colorBaseHex) {
		this.colorBaseHex = colorBaseHex;
	}

	public String getColorBaseCss() {
		return colorBaseCss;
	}

	public void setColorBaseCss(String colorBaseCss) {
		this.colorBaseCss = colorBaseCss;
	}

	public String getColorSecundarioHex() {
		return colorSecundarioHex;
	}

	public void setColorSecundarioHex(String colorSecundarioHex) {
		this.colorSecundarioHex = colorSecundarioHex;
	}

	public String getColorSecundarioCss() {
		return colorSecundarioCss;
	}

	public void setColorSecundarioCss(String colorSecundarioCss) {
		this.colorSecundarioCss = colorSecundarioCss;
	}

	public List<Pagina> getPaginas() {
		return paginas;
	}

	public void setPaginas(List<Pagina> paginas) {
		this.paginas = paginas;
	}

	@Override
	public String toString() {
		return "Paleta [id=" + id + ", nombre=" + nombre + ", colorBaseHex=" + colorBaseHex + ", colorBaseCss="
				+ colorBaseCss + ", colorSecundarioHex=" + colorSecundarioHex + ", colorSecundarioCss="
				+ colorSecundarioCss + "]";
	}
}
