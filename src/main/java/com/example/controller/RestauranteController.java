package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.exceptions.IncompleteFormException;
import com.example.model.PedidoComanda;
import com.example.model.Plato;
import com.example.model.Restaurante;

import com.example.service.RestauranteService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;


@RequestMapping("/ticomo")
@RestController
@CrossOrigin(origins = {"https://ticomo01man.web.app", "http://localhost:4200"})
/*
 * Esta clase actúa como un patrón de 
 * diseño encargado de llamar al servicio de los restaurantes
 * para ejecutar sus funcionalidades.
 * */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RestauranteController {
	
	/*================================================*/
	//VARIABLES
	//restauranteServ : Conoce al Service de restaurantes
	@Autowired
	private RestauranteService restauranteServ;
	
	
	
	/*=============================================================================================*/

	// MÉTODOS
	
	/*
	 * Este método es llamado desde la vista por tipo PostMapping y toma
	 * como entrada un restaurante que se le pasa al servicio para que lo registre y lo guarde.
	 * 
	 * Controla excepciones y retorna un FORBIDDEN en caso de error.
	 * 
	 * */
	@PostMapping("/restaurantes")
	public void saveRestaurante(@RequestBody Restaurante restaurante) throws IncompleteFormException {
		try {
			restauranteServ.saveRestaurante(restaurante);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		
	}
	
	
	/*
	 * Este método, llamado desde la vista, retorna un opcional que, puede o no, contener 
	 * un restaurante el cual ha sido buscado en el repositorio mediante su ID.
	 * 
	 * */
	@GetMapping("/restaurantes/{id}")
	public Optional<Restaurante> findByIdRestaurante(@PathVariable String id) {
		return restauranteServ.findByIdRestaurante(id);
	}
	
	/*
	 * Este método, llamado desde la vista, retorna la lista de todos los restaurantes.  
	 * 
	 * */
	@GetMapping(value= "/restaurantes")
	public List	<Restaurante> findAllRestaurantes(){
		return restauranteServ.findAllRestaurantes();
	}
	
	
	/*
	 * Este método, llamado desde la vista, sirve para eliminar un restaurante mediante su ID. 
	 * 
	 * */
	@DeleteMapping(value= "/restaurantes/{id}")
	public void deleteByIdRestaurante(@PathVariable String id) {
		restauranteServ.deleteByIdRestaurante(id);
	}
	
	/*
	 * Este método, llamado desde la vista, sirve para actualizar un restaurante mediante su ID. 
	 * 
	 * */
	@PutMapping(value= "/restaurantes")
	public void updateRestaurante(@RequestBody Restaurante restaurante) throws IncompleteFormException {
		try {
			restauranteServ.updateRestaurante(restaurante);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		
	}
	
	/*
	 * Este método, llamado desde la vista, sirve para guardar un plato. 
	 * 
	 * */
	@PostMapping("/restaurantes/platos")
	public void savePlato(@RequestBody Plato plato) throws IncompleteFormException {
		try {
			restauranteServ.savePlato(plato);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		
	}
	/*
	 * Este método, llamado desde la vista, sirve para buscar un plato mediante su id. 
	 * Retorna un optional.
	 * */
	@GetMapping("/restaurantes/plato/{id}")
	public Optional<Plato> findByIdPlato(@PathVariable String id) {
		return restauranteServ.findByIdPlato(id);
	}
	
	/*
     *  Este método, llamado desde la vista, sirve para obtener la lista de pedidos que pertenecen 
     * a un restaurante mediante su ID.
     */
    @GetMapping("/restaurantes/pedidos/{id}/{estado}")
    public List<PedidoComanda> getPedidosByIdRestaurante(@PathVariable String id, @PathVariable String estado){
        return restauranteServ.getPedidosByIdRestaurante(id, estado);
    }
    /*
     * Este método, llamado desde la vista, sirve para obtener la lista de platos que pertenecen 
     * a un restaurante mediante su ID. 
     * */
    @GetMapping("/restaurantes/platos/{id}")
    public List<Plato> getPlatosByIdRestaurante(@PathVariable String id){
        return restauranteServ.getPlatosByIdRestaurante(id);
    }
	/*
	 * Este método, llamado desde la vista, sirve para eliminar un plato mediante su ID.
	 * */
	
	@DeleteMapping(value= "/restaurantes/platosDelete/{id}")
	public void deleteByIdPlato(@PathVariable String id) {
		restauranteServ.deleteByIdPlato(id);
	}
	/*
	 * Este método, llamado desde la vista, sirve para actualizar un plato.
	 * */
	@PutMapping(value= "/restaurantes/platos")
	public void updatePlato(@RequestBody Plato plato) throws IncompleteFormException {
		restauranteServ.updatePlato(plato);
	}
}
	

