package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/*Clase representativa del objeto Telefonista*/

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Telefonista extends Persona{
	
	public Telefonista(String nif, String nombre, String email, String contrasenia, String contraseniaDoble, int intentos) {
		super(nif, nombre, email, contrasenia,contraseniaDoble,intentos);
	}

	public Telefonista() {
		super();
	}

	
	
}
