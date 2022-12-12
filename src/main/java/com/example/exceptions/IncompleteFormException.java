package com.example.exceptions;
/*Esta excepción controla forms vacíos*/
@SuppressWarnings("serial")
public class IncompleteFormException extends Exception{
	
	public IncompleteFormException(String err) {
		super(err);
	}

}
