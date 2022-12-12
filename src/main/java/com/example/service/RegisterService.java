package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exceptions.ContraseniaIncorrectaException;
import com.example.exceptions.IlegalNumberException;
import com.example.exceptions.InvalidEmailException;
import com.example.exceptions.YaEnUsoException;
import com.example.model.Rider;
import com.example.model.Usuario;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Service
@RequestMapping("/Registro")
/*
 * 
 * Esta clase representa el servicio de registro, tanto para riders como usuarios
 * 
 * */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RegisterService {
	/*=================================================*/
	//VARIABLES
	//riderServ : variable que representa al servicio de riders y que permite su registro
	@Autowired
	private RiderService riderServ;
	//userServ : variable que representa al servicio de usuarios y que permite su registro
	@Autowired
	private UsuarioService userServ;
	/*=======================================================*/
	//MÉTODOS
	/*
	 * Método que llama a la funcionalidad del servicio de riders para registrar uno en la BBDD. 
	 * Controla excepciones de YaEnUsoException, contraseniaIncorrecta e InvalidEmailException
	 * 
	 * */
	public Rider signUpRider(Rider rider) throws YaEnUsoException, ContraseniaIncorrectaException, InvalidEmailException, IlegalNumberException{		
		rider.setIntentos(5);
		return riderServ.saveRider(rider);
	}
	/*
	 * Método que llama a la funcionalidad del servicio de usuarios para registrar uno en la BBDD. 
	 * Controla excepciones de YaEnUsoException, contraseniaIncorrecta , InvalidEmailException e IlegalNumberException
	 * 
	 * */
	public Usuario signUpUsuario(Usuario user) throws YaEnUsoException, ContraseniaIncorrectaException, IlegalNumberException, InvalidEmailException {
		user.setIntentos(5);
		return userServ.saveUseR(user);
	}

}
