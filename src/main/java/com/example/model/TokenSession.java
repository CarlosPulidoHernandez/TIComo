package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/*Esta clase es representativa del token de inicio de sesión*/
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class TokenSession {
	
	/*Estas variables representan el usuario de la sesión, su nif y el token que se le asigna*/
	private String usuario;
	private String tokenType;
	private String nif;
	private String email;
	/*Este es el constructor de la clase*/
	public TokenSession(String usuario, String tokenType, String nif,String email) {
		super();
		this.usuario = usuario;
		this.tokenType = tokenType;
		this.nif = nif;
		this.email = email;
	}
	/*Estos son los getter y setter del objeto*/
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	/*Este método es el toString de la información del objeto*/
	@Override
	public String toString() {
		return "TokenSession [usuario=" + usuario + ", tokenType=" + tokenType + ", nif=" + nif + ", email=" + email
				+ "]";
	}
	
}