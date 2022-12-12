package com.example.exceptions;
/*Una excepción personalizada que representa la
 *  invalidez de la generación de facturas*/
@SuppressWarnings("serial")
public class InvoiceGenException extends Exception {
	
	public InvoiceGenException(String error) {
		super(error);
	}

}
