package com.example.exceptions;
/*Una clase que representa la excepción personalizada para la invalidación de administradores*/
@SuppressWarnings("serial")
public class NonAdminValidationException extends Exception {
	
	public NonAdminValidationException(String err) {
		
		super(err);
		
	}
}
