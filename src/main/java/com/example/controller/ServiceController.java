package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.example.model.Rider;
import com.example.model.TokenSession;
import com.example.model.Usuario;
import com.example.service.LoginService;
import com.example.service.RegisterService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@RestController
@RequestMapping("/ticomo")
@CrossOrigin(origins = {"https://ticomo01man.web.app", "http://localhost:4200"})
/*
 * Esta clase actúa como un patrón de
 * diseño encargado de llamar al servicio para que registre tanto usuarios como riders y para loggear
 * gente a la aplicación.
 * */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ServiceController {
	
	/*=============================================================================================*/
	//VARIABLES
	
	//registerService : Propiedad que solo conoce el controlador del servicio. Llama al servicio de registro
	//Se instancia sola al arrancar SpringFramework con la anotación Autowired
	@Autowired
	private RegisterService regService;
	//logService : Propiedad que solo conoce el controlador del servicio. Llama al servicio de Loggeo.
	private static LoginService logService;
	/*=============================================================================================*/
	//MÉTODOS
	
	//Inicializar el servicio del login
	@Autowired
	public void setLoginService(LoginService loginServ) {
		ServiceController.logService = loginServ;
	}
    
	
	/*
	 * Este método es llamado desde la vista por tipo PostMapping , se accede vía
	 * "/Registro/newUsuario" para diferenciar que tiene que registrar un usuario
	 * y toma como entrada un usuario que se le pasa al servicio para que lo registre.
	 *
	 * Controla excepciones y retorna un FORBIDDEN en caso de error.	 
	 * */
	@PostMapping("/Registro/newUsuario")
	public void signUpUsuario(@RequestBody Usuario user){
		try {
			regService.signUpUsuario(user);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	/*
	 * Este método es llamado desde la vista por tipo PostMapping , se accede vía
	 * "/Registro/newRider" para diferenciar que tiene que registrar un rider
	 * y toma como entrada un rider que se le pasa al servicio para que lo registre.
	 *
	 * Controla excepciones y retorna un FORBIDDEN en caso de error.
	 * */
	@PostMapping("/Registro/newRider")
	public void signUpRider(@RequestBody Rider rider){
		try {
			regService.signUpRider(rider);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	
	/* Este método es llamado desde la vista por tipo PostMapping , se accede vía "/Login" para Loggear todo tipo de usuarios de la aplicación
	 * Toma como entrada la información de loggeo : Email y Contraseña
	 * Controla excepciones y retorna un FORBIDDEN en caso de error.
	 */
	
	@PostMapping("/Login")
	public TokenSession signIn(@RequestBody Usuario info) {
		try {
			TokenSession loginInfo;
			loginInfo = logService.signIn(info);
			return loginInfo;
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		
	}
	
	@PostMapping("/buscarUsuarioComoTelefonista")
	public Usuario buscarUsuarioComoTelefonista(@RequestBody Usuario info) {
		try {
			Usuario usuario;
			usuario = logService.buscarUsuarioComoTelefonista(info);
			return usuario;
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		
	}
}