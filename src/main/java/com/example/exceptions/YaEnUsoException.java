package com.example.exceptions;
/*Excepción personalizada que indica que algo ya está en uso*/
@SuppressWarnings("serial")
public class YaEnUsoException extends Exception{
	
	public YaEnUsoException(String error) {
		super(error);
	}

}
