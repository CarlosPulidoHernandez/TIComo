package com.example.exceptions;
/*Esta clase representa la excepción personalizada para un Perfil que ha sido
 * bloqueado por fallar múltiples veces en los inicios de sesión*/
@SuppressWarnings("serial")
public class PerfilBloqueadoException extends Exception {
	
	public PerfilBloqueadoException(String err) 
	{
		super(err);

	}

}
