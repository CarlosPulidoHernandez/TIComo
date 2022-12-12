package com.example.exceptions;
/*Esta clase representa las excepciones personalizadas que 
 * se utilizan para señalar que un email introducido no tiene el formato
 * correcto y no es válido.*/
@SuppressWarnings("serial")
public class InvalidEmailException extends Exception {
	
	public InvalidEmailException(String error) {
		super(error);
	}

}
