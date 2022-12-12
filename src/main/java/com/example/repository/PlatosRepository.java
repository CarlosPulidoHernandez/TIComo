package com.example.repository;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.model.Plato;
/*Esta interfaz es el repositorio de la BBDD de los restaurantes*/
@Repository
public interface PlatosRepository extends MongoRepository<Plato, String>{
		
	Optional<Plato> findById(String idPlato);


	Optional<Plato> findIdBynombre(String nombre);
	
}