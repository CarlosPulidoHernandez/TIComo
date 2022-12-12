package com.example.model;
import java.net.URL;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.mongodb.lang.NonNull;
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Plato {
	
	@Id
	private String id;
	@NonNull
	private String nombre;
	@NonNull
	private URL foto;
	@NonNull
	private String descripcion;
	@NonNull
	private double precio;
	@NonNull
	private boolean aptoVeganos;
	@NonNull
	private String idRestaurante;
	
	public Plato(String nombre, URL foto, String descripcion, double precio, boolean aptoVeganos, String idRestaurante) {
		
		
		this.nombre = nombre;
		this.foto = foto;
		this.descripcion = descripcion;
		this.precio = precio;
		this.aptoVeganos = aptoVeganos;
		this.idRestaurante = idRestaurante;
	}
	public String getIdRestaurante() {
		return idRestaurante;
	}
	public void setIdRestaurante(String idRestaurante) {
		this.idRestaurante = idRestaurante;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public URL getFoto() {
		return foto;
	}
	public void setFoto(URL foto) {
		this.foto = foto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public boolean isAptoVeganos() {
		return aptoVeganos;
	}
	public void setAptoVeganos(boolean aptoVeganos) {
		this.aptoVeganos = aptoVeganos;
	}
	@Override
	public String toString() {
		return "Plato [id=" + id + ", nombre=" + nombre + ", foto=" + foto + ", descripcion=" + descripcion
				+ ", precio=" + precio + ", aptoVeganos=" + aptoVeganos + ", idRestaurante=" + idRestaurante + "]";
	}
	
}