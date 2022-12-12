package com.example.exceptions;
/*Esta clase representa las excepciones que hacen referencia a
 * números de teléfono que han sido introducidos y que no cumplen con 
 * el rango de teléfonos válidos*/
@SuppressWarnings("serial")
public class IlegalNumberException extends Exception{
	
	public IlegalNumberException(String error) {
		super(error);
	}
}