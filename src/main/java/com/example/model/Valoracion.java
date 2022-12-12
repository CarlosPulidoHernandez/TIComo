package com.example.model;

import java.util.List;

import org.springframework.data.annotation.Id;

/*Clase que representa las valoraciones tanto de rider como de restaurantes*/
public class Valoracion {
	/* ============================ */
	// VARIABLES

	// idValoracion : id de la valoracion para distinguir en los repositorios
	@Id
	private String idValoracion;
	// notaRestaurante : nota que un usuario le da en la valoración
	private int notaRestaurante;
	// notaRider : nota que un usuario le da en la valoración
	private int notaRider;
	// comentario : comentario que deja un usuario
	private String comentario;
	// idValoradoRestaurante : id que funciona como clave ajena para buscar en cada repositorio
	// dependiendo de quien se trate
	private String idValoradoRestaurante;
	// idValoradoRider : id que funciona como clave ajena para buscar en cada repositorio
	// dependiendo de quien se trate
	private String idValoradoRider;
	/* ==================================== */
	// MÉTODOS

	/* Constructor */
	public Valoracion(int notaRestaurante, int notaRider, String comentario, String idValoradoRestaurante, String idValoradoRider) {
		super();
		this.notaRestaurante = notaRestaurante;
		this.notaRider = notaRider;
		this.comentario = comentario;
		this.idValoradoRestaurante = idValoradoRestaurante;
		this.idValoradoRider = idValoradoRider;
	}

	/* Métodos getter y setter */
	public String getIdValoracion() {
		return idValoracion;
	}

	public void setIdValoracion(String idValoracion) {
		this.idValoracion = idValoracion;
	}

	public int getNotaRestaurante() {
		return notaRestaurante;
	}

	public void setNotaRestaurante(int nota) {
		this.notaRestaurante = nota;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getIdValoradoRestaurante() {
		return idValoradoRestaurante;
	}

	public void setIdValoradoRestaurante(String idValorado) {
		this.idValoradoRestaurante = idValorado;
	}

	public int getNotaRider() {
		return notaRider;
	}

	public void setNotaRider(int notaRider) {
		this.notaRider = notaRider;
	}

	public String getIdValoradoRider() {
		return idValoradoRider;
	}

	public void setIdValoradoRider(String idValoradoRider) {
		this.idValoradoRider = idValoradoRider;
	}
	
	@Override
	public String toString() {
		return "Valoracion [idValoracion=" + idValoracion + ", notaRestaurante=" + notaRestaurante + ", notaRider="
				+ notaRider + ", comentario=" + comentario + ", idValoradoRestaurante=" + idValoradoRestaurante
				+ ", idValoradoRider=" + idValoradoRider + "]";
	}

	/* Método que calcula la media para ambas clases */
	public double calcularMedia(List<Integer> notas) {
		double sum = 0;
		if(notas.isEmpty()) {
			return 0;
		}else {
		    for (int i=0; i<notas.size(); i++) {
		    	sum += notas.get(i);
		    }	    
		}    
	    return (sum/notas.size());
	}

}
