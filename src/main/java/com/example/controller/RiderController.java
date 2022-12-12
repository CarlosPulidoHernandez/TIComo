package com.example.controller;

import java.util.List;
import java.util.Optional;

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

import com.example.exceptions.MalEstadoPedidoException;
import com.example.exceptions.NonAdminValidationException;
import com.example.model.PedidoComanda;
import com.example.model.Rider;
import com.example.service.RiderService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@RestController
@RequestMapping("/ticomo")
@CrossOrigin(origins = {"https://ticomo01man.web.app", "http://localhost:4200"})
/*
 * Esta clase actúa como un patrón de 
 * diseño encargado de llamar al servicio de los Rider
 * para ejecutar sus funcionalidades.
 * */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RiderController {
	/*=============================================================================================*/
	//VARIABLES
	
	//riderService : Propiedad que solo conoce el controlador del servicio de Riders
	private static RiderService riderServ;
	/*=============================================================================================*/

	// MÉTODOS
	
	//Inicializar el servicio del rider
	@Autowired
	public void setRiderService(RiderService riderService) {
		RiderController.riderServ = riderService;
	}
	
	/*
	 * Este método es llamado desde la vista por tipo PostMapping y toma
	 * como entrada un Rider que se le pasa al servicio para que lo registre y lo guarde.
	 * Es un método que se llama en caso de que un rider quiera actualizar su información.
	 * 
	 * Controla excepciones y retorna un FORBIDDEN en caso de error.
	 * 
	 * */
	@PostMapping("/riders")
	public void saveRider(@RequestBody Rider rider) {
		try {
			riderServ.saveRider(rider);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	
	}
	/*Método que retorna una lista como consulta de todos los rider*/
	@GetMapping("/consultarRiders/{token}")
	public List<Rider> consultarRiders(@PathVariable String token) {
		
		try {
			if(!token.equalsIgnoreCase("admin"))
				throw new NonAdminValidationException("No hay un token de Administrador válido para consultar los riders");
				
			return riderServ.consultarRiders();
			
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	
	}
	
	/*
	 * Método para actualizar un rider
	 * 
	 * */
	@PostMapping("/updateRider")
	public void updateRider(@RequestBody Rider rider) {
		try {
			riderServ.updateForm(rider);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	/*
	 * Método para borrar un rider
	 * 
	 * */
	@PostMapping("/deleteRider")
	public void deleteRider(@RequestBody String riderNif) {
		try {
			riderServ.deleteRiderById(riderNif);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	/**
	 *Método para consultar todos los pedidos existentes 
	 */
	@PostMapping("/consultarPedidos")
	public List<PedidoComanda> consultarTodosLosPedidos(){
		try {
			return riderServ.consultarPedidos();
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	/*
	 * Método para consultar los pedidos únicamente por su estado de preparación (Por si es necesario filtrar)
	 * */
	@GetMapping("/consultarPedidosPorEstado/{estado}")
	public List<PedidoComanda> consultarTodosLosPedidosPorEstado(@PathVariable String estado){
		try {
			return riderServ.consultarPedidosPorEstado(estado);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	/*
     * Método para cambiar el estado de un pedido al haberlos consultado por estado de pedido
     * */
    @PostMapping("/cambiarEstadoReparto")
    public void cambiarEstadoPedido(@RequestBody PedidoComanda pedido) throws MalEstadoPedidoException {
        
        try {
        	riderServ.cambiarEstadoPedido(pedido);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
    }
    
    /*Método que llama para consultar los pedidos en reparto del rider*/
    @GetMapping("/misPedidosEnReparto/{idRider}")
    public List<PedidoComanda> consultarMisPedidosEnReparto(@PathVariable String idRider){
    	try {
			return riderServ.consultarMisPedidosEnReparto(idRider);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
    }
    /*Método que devuelve objeto rider buscando por NIF*/
    @GetMapping("/rider/{nif}")
    public Optional<Rider> findById(@PathVariable String nif) {
        try {
            return riderServ.findById(nif);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
