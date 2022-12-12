package com.example.model;

import java.net.URL;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Restaurante {
	@Id
	private String id;
	
	private String cif;	
	private String nombre;
	private String direccion;
	private String emailContacto;
	private String categoria;
	private URL imagen;
	private double valoracionMedia;
	

	public Restaurante(String cif, String nombre, String direccion, String emailContacto, String categoria, URL imagen ) {
		
		this.cif = cif;
		this.nombre = nombre;
		this.direccion = direccion;
		this.emailContacto = emailContacto;
		this.categoria = categoria;
		this.imagen=imagen;
		this.valoracionMedia = 0.0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmailContacto() {
		return emailContacto;
	}

	public void setEmailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public URL getImagen() {
		return imagen;
	}

	public void setImagen(URL imagen) {
		this.imagen = imagen;
	}
	

	public double getValoracionMedia() {
		return valoracionMedia;
	}

	public void setValoracionMedia(double valoracionMedia) {
		this.valoracionMedia = valoracionMedia;
	}
	/*Método toString de la clase*/
	@Override
	public String toString() {
		return "Restaurante [id=" + id + ", cif=" + cif + ", nombre=" + nombre + ", direccion=" + direccion + ", emailContacto="
				+ emailContacto + ", categoria=" + categoria + ", imagen=" + imagen + "]";
	}
	
	
	
}
