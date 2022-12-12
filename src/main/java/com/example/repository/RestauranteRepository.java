package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Restaurante;
/*Esta interfaz es el repositorio de la BBDD de los restaurantes*/
@Repository
public interface RestauranteRepository extends MongoRepository<Restaurante, String> {

}
