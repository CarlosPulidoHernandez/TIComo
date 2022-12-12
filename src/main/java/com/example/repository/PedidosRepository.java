package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.PedidoComanda;
/*Clase repositorio que hace consultas a la BBDD con respecto a pedidos realizados por los usuarios*/
@Repository
public interface PedidosRepository extends MongoRepository<PedidoComanda, String>{
		
	Optional<PedidoComanda> findById(String idPedido);
	List<PedidoComanda> findAll();
	List<PedidoComanda> findByestadoPedidoContaining(String estado);
	List<PedidoComanda> findBynifCliente(String cliente);
	List<PedidoComanda> findByidRestaurante(String idRestaurante);
	
	
}
