package com.example.exceptions;
/*Una excepción personalizada que representa la
 *  invalidez de las contraseñas al no ser correctas*/
@SuppressWarnings("serial")
public class ContraseniaIncorrectaException extends Exception {
	
	public ContraseniaIncorrectaException(String error) {
		super(error);
	}

}
