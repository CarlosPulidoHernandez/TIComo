package com.example.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Rider;
/*Clase repositorio que hace consultas a la BBDD con respecto a Riders*/
@Repository
public interface RiderRepository extends MongoRepository<Rider, String>{

	Optional<Rider> findById(String nif);
	Optional<Rider> findByEmail(String email);
}
