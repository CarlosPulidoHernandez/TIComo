package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*Clase representativa del objeto administrador, que hereda de la clase Persona*/
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Administrator {
	/*===================================================*/
	//VARIABLES
	//id : id para mongo
	private String id;
	//nombre : variable tipo string que refiere al nombre del Admin
	private String nombre;
	//apellidos : variable tipo string que refiere al apellido del admin
	private String apellidos;
	//zonaGeografica : variable de tipo String que indica a que zona refiere un administrador.
	private String zonaGeografica;
	//email : variable que refiere al email del admin. Es tipo string
	private String email;
	//contrasenia : variable tipo string que refiere a la contraseña y luego se cifra
	private String contrasenia;
	//contraseniaDoble : variable tipo string que refiere a la repeticion para comprobar la contraseña
	private String contraseniaDoble;
	
	//intentos : variable que anota los intentos de inicio de sesión que tiene un usuario
	private int intentos = 5;
	/*====================================================*/
	//MÉTODOS
	/*
	 * Método Constructor de la clase Administrador
	 * */
	public Administrator(String id,String nombre, String apellidos, String zonaGeografica, String email, String contrasenia,
			String contraseniaDoble, int intentos) {
		
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.zonaGeografica = zonaGeografica;
		this.email = email;
		this.contrasenia = contrasenia;
		this.contraseniaDoble = contraseniaDoble;
		this.intentos = intentos;
	}
	
	/*
	 * Métodos Getter y Setter para manipular las variables del objeto  
	 * */
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getZonaGeografica() {
		return zonaGeografica;
	}

	public void setZonaGeografica(String zonaGeografica) {
		this.zonaGeografica = zonaGeografica;
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/*
	 * Método toSting de la clase
	 * */
	@Override
	public String toString() {
		return "Administrator [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", zonaGeografica="
				+ zonaGeografica + ", email=" + email + ", contrasenia=" + contrasenia + ", contraseniaDoble="
				+ contraseniaDoble + ", intentos=" + intentos + "]";
	}

	

	
	
	



}
