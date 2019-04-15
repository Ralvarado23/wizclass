package com.wizclass.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Pagina {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre; // NOMBRE DEL CENTRO
	private String titulo; // TITULO DE LA PAGINA
	private String calle;
	private String numero;
	private String localidad;
	private int codigoPostal;
	private String emailContacto;
	private String telefonoContacto;
	private String fechaModificacion;
	private Double precio = 99.90; //PRECIO INICIAL DE CADA PÁGINA
	private Boolean enCarrito; //INDICA SI LA PAGINA SE ENCUENTRA EN EL CARRITO
	private Boolean comprado; //INDICA SI LA PAGINA ESTA COMPRADA

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "pagina_ensenanza", joinColumns = @JoinColumn(name = "pagina_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ensenanza_id", referencedColumnName = "id"))
	private Set<Ensenanza> ensenanzas;
	
	@ManyToOne
    private Paleta paleta;
	
	@ManyToOne
    private User user;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="pagina_id")
	private List<Noticia> noticias = new ArrayList<>();
	
	private String picture;
	
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
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getCalle() {
		return calle;
	}
	
	public void setCalle(String calle) {
		this.calle = calle;
	}
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getLocalidad() {
		return localidad;
	}
	
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	
	public int getCodigoPostal() {
		return codigoPostal;
	}
	
	public void setCodigoPostal(int codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	
	public String getEmailContacto() {
		return emailContacto;
	}

	public void setEmailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}

	public String getTelefonoContacto() {
		return telefonoContacto;
	}

	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

	public Set<Ensenanza> getEnsenanzas() {
		return ensenanzas;
	}
	
	public void setEnsenanzas(Set<Ensenanza> ensenanzas) {
		this.ensenanzas = ensenanzas;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Paleta getPaleta() {
		return paleta;
	}

	public void setPaleta(Paleta paleta) {
		this.paleta = paleta;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public List<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Boolean getEnCarrito() {
		return enCarrito;
	}

	public void setEnCarrito(Boolean enCarrito) {
		this.enCarrito = enCarrito;
	}

	public Boolean getComprado() {
		return comprado;
	}

	public void setComprado(Boolean comprado) {
		this.comprado = comprado;
	}

	@Override
	public String toString() {
		return "Pagina [id=" + id + ", nombre=" + nombre + ", titulo=" + titulo + ", calle=" + calle + ", numero="
				+ numero + ", localidad=" + localidad + ", codigoPostal=" + codigoPostal + ", emailContacto="
				+ emailContacto + ", telefonoContacto=" + telefonoContacto + ", fechaModificacion=" + fechaModificacion
				+ ", precio=" + precio + ", enCarrito=" + enCarrito + ", comprado=" + comprado + ", ensenanzas="
				+ ensenanzas + ", paleta=" + paleta + ", user=" + user + ", noticias=" + noticias + ", picture="
				+ picture + "]";
	}
}
