package com.example.repository;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.model.Valoracion;
/*Repositorio que guarda las valoraciones de restaurantes*/
@Document(value = "ValoracionesRest")
public interface ValoracionesRestauranteRepo extends MongoRepository<Valoracion, String> {

}