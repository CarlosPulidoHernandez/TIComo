package com.example.exceptions;
/*Esta clase representa la excepción personalizada para decir que un usuario no existe*/
@SuppressWarnings("serial")
public class UnexistentUser extends Exception{
	
	public UnexistentUser(String error) {
		super(error);
	}
}