package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.model.Administrator;
import com.example.model.PedidoComanda;
import com.example.model.Usuario;
import com.example.service.AdminService;
import com.example.service.UsuarioService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@RestController
@RequestMapping("/ticomo")
@CrossOrigin(origins = {"https://ticomo01man.web.app", "http://localhost:4200"})

/* Clase controller del administrador.
 * Desde esta clase llamamos al servicio de los administradores
 * para ejecutar sus funcionalidades.
 **/
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class AdministratorController {
	/*============================================== */
	//VARIABLES
	
	//AdminService : Propiedad que solo conoce el controlador del servicio de Administrador
	//Se instancia sola al arrancar SpringFramework con la anotación Autowired
	@Autowired
	private AdminService adminServ;
	//userServ : los admin pueden acceder al servicio de usuario para hacer consultas
	@Autowired
	private UsuarioService userServ;
	/*============================================== */
	//MÉTODOS

	/*
	 * Este método es llamado desde la vista por tipo PostMapping y toma
	 * como entrada un administrador que se le pasa al servicio para que lo registre y lo guarde.
	 * 
	 * Controla excepciones y retorna un BAD_REQUEST en caso de error.
	 * */
	@PostMapping("/administrators")
	public void saveAdministrator(@RequestBody Administrator administrator) {	
		try {
			adminServ.saveAdmin(administrator);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	/*
	 * Este método es utilizado por administradores para consultar los usuarios que tienen perfil en la aplicación
	 * Retorna una lista de esos usuarios si y solo si la persona que lo ordena es autorizada haciendo
	 * uso de un token de administrador.
	 * 
	 * Controla excepciones y retorna un FORBIDDEN en caso de error.	  
	 * */
	@GetMapping("/consultarUsers/{tokenAdmin}")
	public List<Usuario> consultarUsers(@PathVariable String tokenAdmin){
		try {
			if(!tokenAdmin.contains("Admin")) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN , "Usted no tiene autorización para realizar esta acción");
			}
			return userServ.consultarUsuarios();
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/*
	 * Método para actualizar un administrador si previamente ha verificado su token en Frontend
	 * */
	@PostMapping("/updateAdmin")
	public void updateAdmn(@RequestBody Administrator admn) {
		try {
			adminServ.updateAdmnForm(admn);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/*
	 * Método que consulta los pedidos para el administrador, según el restaurante que le pase
	 * */
	@PostMapping("/consultarPedidosPorRestaurante")
	public List<PedidoComanda> consultarPedidosAdmin(@RequestBody String idRestaurante){
		try {
			return adminServ.findByidRestaurante(idRestaurante);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	/*Método para actualizar los intentos del Admin. Se puede llamar a mano también para actualizarlo aunque es común su uso desde el login*/
	@PostMapping("/updateAdminIntentos")
	public void updateAdminIntentos(String email, int intentos) {
		try {
			adminServ.updateAdmnIntentos(email,intentos);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/consultarPedidosAdmin/{estado}")
    public List<PedidoComanda> consultarTodosLosPedidosPorEstado(@PathVariable String estado){
        try {
            return adminServ.consultarPedidosPorEstado(estado);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
	
	
	
}
