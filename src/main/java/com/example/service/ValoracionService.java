package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Valoracion;
import com.example.repository.ValoracionesRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Service
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ValoracionService {

    @Autowired
    private ValoracionesRepository valRepo;
    
    public Valoracion saveValoracionPedido(Valoracion valoracion) {
        return valRepo.save(valoracion);
    }
}