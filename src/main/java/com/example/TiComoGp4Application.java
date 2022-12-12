package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ServletComponentScan
/*Clase que inicia la aplicación Spring*/
public class TiComoGp4Application {

	public static void main(String[] args) {
		SpringApplication.run(TiComoGp4Application.class, args);
		
	}
	

}
