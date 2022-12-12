package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.model.Telefonista;
import com.example.repository.TelefonistaRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Service
@JsonAutoDetect(fieldVisibility = Visibility.ANY)

public class TelefonistaService {
	
	@Autowired
	private TelefonistaRepository telRepo;

	public void saveTelefonista(Telefonista telefonista) {
		telRepo.insert(telefonista);	
	}
	
	public Optional<Telefonista> findByEmail(String email){
		return telRepo.findByEmail(email);
	}
	
}
