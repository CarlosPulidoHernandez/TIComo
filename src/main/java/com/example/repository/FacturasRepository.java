package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Facturas;
/*Clase repositorio que hace consultas a la BBDD con respecto a facturas de pedidos realizados por los usuarios*/
@Repository
public interface FacturasRepository extends MongoRepository<Facturas, String>{

		List<Facturas> findAll();
		Optional<Facturas> findById(String id);
		Optional<Facturas> findByidPedido(String idPedido);
	
	
}
