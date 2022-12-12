package com.example.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/*Clase del modelo representativa a una comanda realizada por un usuario*/
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class PedidoComanda {
	/*====================================================*/
	//VARIABLES
	//comanda : un diccionario que usaremos para anotar el id del plato junto al número de unidades del mismo
	private Map<String, String> comanda = new HashMap<>();
	//idPedido : id para identificar el pedido
	@Id
	private String id;
	//estadoPedido : estado inicial del pedido, asignado como Pedido Realizado al principio, y En Preparacion/En reparto o Pedido Finalizado
	//Según avanza su estado
	private String estadoPedido;
	//precio : precio total de la comanda
	private double precioComanda;
	//cliente : variable que permite relacionar al cliente con el pedido. Es el primer elemento de la comanda
	private String nifCliente;
	//restaurante : variable que permite relacionar al restaurante del que se piden los platos con el pedido. Es el segundo elemento de la comanda
	private String idRestaurante;
	//fechaPedido : variable que indica cual es la fecha en la que se crea el pedido
	private Date fecha;
	//idRider : variable que identifica al rider asociado al pedido
	private String idRider;

	
	/*====================================================*/
	//MÉTODOS
	
	
	/*Constructor de la clase*/
	public PedidoComanda(String id,Map<String,String> comanda,String estadoPedido, 
	double precioComanda, String nifCliente,String idRestaurante,Date fecha,String idRider)
	{
		
		this.comanda = comanda;
		this.id = id;
		this.estadoPedido = estadoPedido;
		this.precioComanda = precioComanda;
		this.nifCliente = nifCliente;
		this.idRestaurante = idRestaurante;
		this.fecha = fecha;
		this.idRider =idRider;
		
	}
	
	/*
	 * Getters y setters útiles para modificar la comanda
	 * */

	public PedidoComanda() {
	}

	public Map<String, String> getComanda() {
		return comanda;
	}

	public void setComanda(Map<String, String> comanda) {
		this.comanda = comanda;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedido(String estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public double getPrecioComanda() {
		return precioComanda;
	}

	public void setPrecioComanda(double precioComanda) {
		this.precioComanda = precioComanda;
	}

	public String getNifCliente() {
		return nifCliente;
	}

	public void setNifCliente(String nifCliente) {
		this.nifCliente = nifCliente;
	}

	public String getIdRestaurante() {
		return idRestaurante;
	}

	public void setIdRestaurante(String idRestaurante) {
		this.idRestaurante = idRestaurante;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getIdRider() {
		return idRider;
	}

	public void setIdRider(String idRider) {
		this.idRider = idRider;
	}
	
	/*
	 * Método toString de la clase
	 * */
	
	@Override
	public String toString() {
		return "PedidoComanda [comanda=" + comanda + ", id=" + id + ", estadoPedido=" + estadoPedido
				+ ", precioComanda=" + precioComanda + ", nifCliente=" + nifCliente + ", idRestaurante=" + idRestaurante
				+ ", fecha=" + fecha + ", idRider=" + idRider + "]";
	}
	
	
		
}
