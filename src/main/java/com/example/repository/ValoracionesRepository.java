package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Valoracion;
/*Repositorio que guarda las valoraciones de rider*/
@Repository
public interface ValoracionesRepository extends MongoRepository<Valoracion, String>{

}