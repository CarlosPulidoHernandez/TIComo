package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.mongodb.lang.NonNull;
/*Clase representativa del objeto rider, que hereda atributos de Persona*/
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Rider extends Persona{
	/*==========================================================*/
	//VARIABLES
	//tipoVehiculo : variable que refiere al tipo de vehículo que tiene el rider
	//Anota NonNull para que no pueda coger nulos en la BBDD
	@NonNull
	private String tipoVehiculo;
	//matricula : refiere a la matrícula del vehículo
	private String matricula;
	//carnet : refiere al carnet del rider
	private String carnet;
	//telefono:  telefono del rider.
	private String telefono;
	//valoraciónMedia : varable que indica la valoración que tiene un rider
	private double valoracionMedia;
	//numeroPedidos: número de pedidos que puede repartir 
	private int numeroPedidos;
	
	
	public Rider(String nif, String nombre, String email, String contrasenia,
			String contraseniaDoble, int intentos, String tipoVehiculo,
			String matricula, String carnet, String telefono, int numeroPedidos) {
		super(nif, nombre, email, contrasenia, contraseniaDoble, intentos);
		this.tipoVehiculo = tipoVehiculo;
		this.matricula = matricula;
		this.carnet = carnet;
		this.telefono = telefono;
		this.valoracionMedia = 0.0;
		this.numeroPedidos=numeroPedidos;
	}

	/*Métodos Getter y Setter de la clase*/
	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public int getNumeroPedidos() {
		return numeroPedidos;
	}

	public void setNumeroPedidos(int numeroPedidos) {
		this.numeroPedidos = numeroPedidos;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCarnet() {
		return carnet;
	}

	public void setCarnet(String carnet) {
		this.carnet = carnet;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
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
		return "Rider [tipoVehiculo=" + tipoVehiculo + ", matricula=" + matricula + ", carnet=" + carnet + ", telefono="
				+ telefono + ", valoracionMedia=" + valoracionMedia + ", numeroPedidos=" +numeroPedidos+"]";
	}

	
	
	
	
}
