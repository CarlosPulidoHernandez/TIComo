package com.example.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.mongodb.lang.NonNull;
/*Clase que representa al objeto usuario, y que extiende de la clase Persona*/
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Usuario extends Persona{
	/*==========================================*/
	//VARIABLES
	//direccion : refiere a la dirección donde pide el usuario
	//Anota NonNull para evitar nulos en la BBDD
	@NonNull
	private String direccion;
	//telefono : refiere a un teléfono válido del usuario
	@NonNull
	private String telefono;
	/*Método constructor de la clase*/	
	public Usuario(String nif, String nombre, String email, String contrasenia,
			String contraseniaDoble,int intentos, String direccion, String telefono) {
		super(nif, nombre, email, contrasenia,contraseniaDoble,intentos);
		this.direccion = direccion;
		this.telefono = telefono;
	}

	public Usuario() {
		super();
		this.direccion = "";
		this.telefono = "";
	}

	/*Métodos Getter y Setter de la clase*/
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	/*Método toString de la clase*/
	@Override
	public String toString() {
		return "Usuario [ direccion=" + direccion + ", telefono=" + telefono + "]";
	}
	
	
	
}
