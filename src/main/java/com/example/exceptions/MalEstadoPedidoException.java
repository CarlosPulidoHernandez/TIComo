package com.example.exceptions;
/*En ésta excepción vamos a controlar que el estado que el rider cambie de cualquier pedido
 * no éste ni en reparto ni en ya entregado.*/
@SuppressWarnings("serial")
public class MalEstadoPedidoException extends Exception {
	
	public MalEstadoPedidoException(String err) 
	{
		super(err);

	}

}
