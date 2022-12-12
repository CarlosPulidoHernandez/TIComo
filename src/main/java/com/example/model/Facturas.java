package com.example.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*Clase que  genera las facturas*/
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Facturas {
	/*===================================================*/
	//VARIABLES
	// titulo : titulo de la factura
	private String titulo;
	// id : identificador de la factura
	@Id
	private String idPedido;
	
	//fecha : fecha de emisión
	private Date fechaFactura;
	
	//cliente : quien realiza el pedido
	private String nifCliente;

	/*===================================*/
	//MÉTODOS
	
	/*Constructor de la clase*/
	public Facturas(String titulo, String idPedido, Date fechaFactura, String nifCliente) {
		this.titulo = titulo;
		this.idPedido = idPedido;
		this.fechaFactura = fechaFactura;
		this.nifCliente = nifCliente;
		
	}

	/*Getters y setters de la clase*/

	public String getTitulo() {
		return titulo;
	}



	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}



	public String getId() {
		return idPedido;
	}



	public void setId(String id) {
		this.idPedido = id;
	}



	public Date getFechaFactura() {
		return fechaFactura;
	}



	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	

	public String getCliente() {
		return nifCliente;
	}

	public void setCliente(String cliente) {
		this.nifCliente = cliente;
	}
	/*Método toString de la clase*/
	@Override
	public String toString() {
		return "Facturas [titulo=" + titulo + ", idPedido=" + idPedido + ", fechaFactura=" + fechaFactura + ", cliente="
				+ nifCliente + "]";
	}
	
	
	
	
}
