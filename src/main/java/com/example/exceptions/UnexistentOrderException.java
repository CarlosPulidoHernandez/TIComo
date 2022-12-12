package com.example.exceptions;
/*Esta clase representa la excepción personalizada para decir que un pedidos no existe*/
@SuppressWarnings("serial")
public class UnexistentOrderException extends Exception{
	
	public UnexistentOrderException(String error) {
		super(error);
	}
}