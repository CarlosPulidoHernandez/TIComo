package com.example.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exceptions.IlegalNumberException;
import com.example.exceptions.IncompleteFormException;
import com.example.model.Plato;

import com.example.repository.PlatosRepository;

/*
 * Clase que representa el servicio de los platos de los restaurantes
 * */
@Service
@RequestMapping("/Platos")
public class PlatoService {
	/*================================================*/
	//VARIABLES
	//platoRepo : permite acceder al repositorio de Platos y hacer consultas a la BBDD. Solo lo conoce esta clase
	@Autowired
	private PlatosRepository platoRepo;
	
		
	/*
	 * Mediante éste método vamos a obtener la lista de los platos correspondientes
	 * a un determinado restaurante mediante su id
	 * 
	 * */
	public List<Plato> getPlatosByIdRestaurante(String idRestaurante){
		List<Plato> totalPlatos = platoRepo.findAll();
		List<Plato> platosRestaurante = new ArrayList<>();
		for(int i =0; i< totalPlatos.size();i++) {
			if (idRestaurante.equalsIgnoreCase(totalPlatos.get(i).getIdRestaurante())) {
				platosRestaurante.add(totalPlatos.get(i));
			}
		}
		return platosRestaurante;
	}
	
	
	/**
	 * Guardamos un plato el cual va a pertenecer a un restaurante mediante su id
	 */
	public void save(Plato plato) throws IncompleteFormException{
		if(plato.getNombre().equals("") || plato.getDescripcion().equals("")
			|| plato.getIdRestaurante().equals("") || plato.getPrecio()<=0)
				throw new IncompleteFormException("Faltan datos");
	
		if(plato.getId()!=null) {
			plato.setId(null);
		}
		platoRepo.insert(plato);
	}
	
	/**
	 * Nos devuelve un plato por su id
	 */
	public Optional<Plato> findById(String idPlato) {
		return platoRepo.findById(idPlato);
	}
	
	/**
	 * Vamos a borrar un plato de la bbdd y de un restaurante mediante
	 * su id
	 */
	public void deleteById(String idPlato) {
		platoRepo.deleteById(idPlato);	
	}
	
	/**
	 * Los cambios los cuales se realicen al modificar los datos de un
	 * determinado plato mediante su id se guardan en la bbdd
	 */
	public void update(Plato plato) throws IncompleteFormException {
		if(plato.getNombre().equals("") || plato.getDescripcion().equals("")
				||  plato.getPrecio()<=0)
			throw new IncompleteFormException("Introduce todos los datos.");
			
		platoRepo.save(plato);
	}

	/*Método que consulta el nombre de un plato dado su ID*/
	public Plato consultarIdPorNombrePlato(String nombre) throws IlegalNumberException {
		
		Optional<Plato> plato = platoRepo.findIdBynombre(nombre);
		
		if(!plato.isPresent())
			throw new IlegalNumberException("El id del plato no coincide con uno existente");
		
		return plato.get();
		
	}

	
}