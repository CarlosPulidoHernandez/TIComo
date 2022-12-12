package com.example.service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.exceptions.ContraseniaIncorrectaException;
import com.example.exceptions.IlegalNumberException;
import com.example.model.Administrator;
import com.example.model.Persona;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*Clase abstracta que sirve para alojar métodos referentes a la seguridad del sistema*/
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class SecurityMethods {
	
	/*====================================================*/
	//MÉTODOS
	/*
	 * 
	 * Este método permite comprobar si la contraseña de un 
	 * determinado usuario incumple las condiciones de seguridad establecidas
	 * 
	 * */
	public void restriccionesContrasenia(Persona usuario) throws ContraseniaIncorrectaException {
		
		/*restricciones de seguridad de contraseña*/
		
		if(usuario.getContrasenia().length() < 8)
			throw new ContraseniaIncorrectaException("Error. La contraseña tiene que tener un mínimo de 8 caracteres");
		
		boolean contieneMayus = false;
		boolean contieneNumero = false;
		for(int i = 0; i < usuario.getContrasenia().length() || (!contieneMayus && !contieneNumero); i++)
		{
			if(Character.isUpperCase(usuario.getContrasenia().charAt(i)))
				contieneMayus = true;
			
			if(Character.isDigit(usuario.getContrasenia().charAt(i)))
				contieneNumero = true;	
		}
		
		if(!contieneMayus)
			throw new ContraseniaIncorrectaException("Error. La contraseña tiene que tener mínimo una mayúscula");
		
		if(!contieneNumero)
			throw new ContraseniaIncorrectaException("Error. La contraseña tiene que tener mínimo un número");
	}
	
	/*
	 * Comprobamos que la contraseña del administrador sea valida.
	 */
	
	public void restriccionesContraseniaAdmin(Administrator admin) throws ContraseniaIncorrectaException {
		
		/*restricciones de seguridad de contraseña*/
		
		if(admin.getContrasenia().length() < 8)
			throw new ContraseniaIncorrectaException("Error. La contraseña tiene que tener un mínimo de 8 caracteres");
		
		boolean contieneMayus = false;
		boolean contieneNumero = false;
		for(int i = 0; i < admin.getContrasenia().length() || (!contieneMayus && !contieneNumero); i++)
		{
			if(Character.isUpperCase(admin.getContrasenia().charAt(i)))
				contieneMayus = true;
			
			if(Character.isDigit(admin.getContrasenia().charAt(i)))
				contieneNumero = true;	
		}
		
		if(!contieneMayus)
			throw new ContraseniaIncorrectaException("Error. La contraseña tiene que tener mínimo una mayúscula");
		
		if(!contieneNumero)
			throw new ContraseniaIncorrectaException("Error. La contraseña tiene que tener mínimo un número");
	}
	
	/*
	 * Método para cifrar contraseñas
	 * 
	 * */
	public String cifradoContrasenia(String pwd){
		PasswordEncoder ncoder = codificador();
		return ncoder.encode(pwd);
		
	}
	
	/*Método para "decodificar" la contraseña*/
	public boolean decoder(String pass, String passMongo) {
		PasswordEncoder ncoder = codificador();
		return ncoder.matches(pass, passMongo);
	}
	
	
	/*Método que retorna el codificador y que permite
	 *  hacer .encode() para codificar o .matches() 
	 *  para saber si una contraseña coincide con otra ya codificada*/
	
	public PasswordEncoder codificador() {
		return new BCryptPasswordEncoder();
	}

	
	/*
	 * Método que verifica si un número de teléfono es válido
	 * 
	 * */
	public Boolean verificarNumero(String numero) throws IlegalNumberException {
		
		if(numero.length() < 9)
			throw new IlegalNumberException("El número de teléfono introducido no es válido. Introduzca nueve dígitos");
		
		if(numero.charAt(0) != '6' && numero.charAt(0) != '7' && numero.charAt(0)!= '8' && numero.charAt(0) != '9')
			throw new IlegalNumberException("El número de teléfono introducido no es válido. Debe estar en el rango 6-9 de los números de teléfono");
		
		for(int i = 0; i < numero.length(); i++) 
		{
			if(!Character.isDigit(numero.charAt(i)))
				throw new IlegalNumberException("El número de teléfono introducido no es válido.");
			
		}	
		return true;
	}
	/*
	 * 
	 * *Método que verifica si un email es válido
	 * 
	 * */
	public Boolean validEmail(String email) {
		String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		return email.toLowerCase().matches(regex.toLowerCase());
	}
	
	/*Método para validar que dos contraseñas son iguales*/
	public Boolean equalPass(String pass1, String pass2) throws ContraseniaIncorrectaException {
		
		if(!pass1.equals(pass2))
			throw new ContraseniaIncorrectaException("Las contraseñas no coinciden");
		
		return true;
		
	}
	
	/*Método para validar un NIF*/
	public Boolean validNif(String nif) throws IlegalNumberException {
		
		String err = "Invalid NIF. No son 8 letras y un caracter";
		if(nif.length() != 9) {
			throw new IlegalNumberException(err);
		}
		for(int i = 0; i < nif.length()-1; i++) {
			if(!Character.isDigit(nif.charAt(i)))
				throw new IlegalNumberException(err);
		}
		
		if(!Character.isAlphabetic(nif.charAt(nif.length()-1)))
			throw new IlegalNumberException(err);
		
		return true;
	}
	
}
