package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.model.Telefonista;
import com.example.service.TelefonistaService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/* Clase controller del telefonista.
 * Desde esta clase llamamos al servicio de los administradores
 * para ejecutar sus funcionalidades.
 **/

@RestController
@RequestMapping("/telefonistas")
@CrossOrigin(origins = {"https://ticomo01man.web.app", "http://localhost:4200"})

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class TelefonistaController {

	@Autowired
	private TelefonistaService telServ;
	
	@PostMapping("/saveTelefonista")
	public void saveTelefonista(@RequestBody Telefonista telefonista) {	
		try {
			telServ.saveTelefonista(telefonista);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	
}
