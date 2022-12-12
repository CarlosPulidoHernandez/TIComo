package com.example.model;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.mongodb.lang.NonNull;
 /*Clase representativa del objeto Persona, que actua de Padre del resto*/
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Persona {
	/*=====================================*/
	//VARIABLES
	
	//nif : Al igual que el resto de variables, es de tipo String. Refiere al NIF de la persona
	//Lleva consigo la anotación Id, para hacer saber a la BBDD que es un tipo ID incremental
	@Id
	private String nif;
	//nombre : Refiere al nombre de la persona
	//Se anota con NonNull para que no anote nulos en la BBDD
	@NonNull
	private String nombre;
	//email : Refiere al email de la persona
	//Anota NonNull
	@NonNull
	private String email;
	//contrasenia : Refiere a la contrasenia de la persona
	@NonNull
	private String contrasenia;
	@NonNull
	private String contraseniaDoble;
	//intentos : variable que anota los intentos de inicio de sesión que tiene un usuario
	private int intentos = 5;
	
	/*Método constructor de la clase*/
	public Persona(String nif, String nombre, String email, String contrasenia,String contraseniaDoble,int intentos) {
		
		this.nif = nif;
		this.nombre = nombre;
		this.email = email;
		this.contrasenia = contrasenia;
		this.contraseniaDoble= contraseniaDoble;
		this.intentos = intentos;
	}
	
	public Persona() {
		this.nombre = "";
		this.email = "";
		this.contrasenia = "";
		this.contraseniaDoble = "";
	}
	
	/*Métodos Getter y Setter de la clase*/
	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}
	public int getIntentos() {
		return intentos;
	}
	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}
	
	public String getContraseniaDoble() {
		return contraseniaDoble;
	}
	public void setContraseniaDoble(String contraseniaDoble) {
		this.contraseniaDoble = contraseniaDoble;
	}
	/*Método toString de la clase*/
	@Override
	public String toString() {
		return "Persona [nif=" + nif + ", nombre=" + nombre + ", email=" + email + ", contrasenia=" + contrasenia
				+ ", contraseniaDoble=" + contraseniaDoble + ", intentos=" + intentos + "]";
	}
	
	
	
	
}
