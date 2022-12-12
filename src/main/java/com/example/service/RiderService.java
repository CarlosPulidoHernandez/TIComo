package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exceptions.ContraseniaIncorrectaException;
import com.example.exceptions.IlegalNumberException;
import com.example.exceptions.IncompleteFormException;
import com.example.exceptions.InvalidEmailException;
import com.example.exceptions.MalEstadoPedidoException;
import com.example.exceptions.UnexistentUser;
import com.example.exceptions.YaEnUsoException;
import com.example.model.PedidoComanda;
import com.example.model.Persona;
import com.example.model.Rider;
import com.example.repository.PedidosRepository;
import com.example.repository.RiderRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;



@Service
@RequestMapping("/Riders")
/*
 * Clase que representa el servicio de los riders
 * */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RiderService {
	/*================================================*/
	//VARIABLES
	//riderRepo : permite acceder al repositorio de Riders y hacer consultas a la BBDD. Solo lo conoce esta clase
	@Autowired
	private RiderRepository riderRepo;
	//restaurantServ : permite acceder al servicio de restaurantes que les proporcionará los pedidos disponibles a través del pedidosRepo
	@Autowired
	private RestauranteService restaurantServ;
	//checkSecurity : variable que accede a métodos auxiliares para controlar la seguridad del sistema
	private SecurityMethods checkSecurity = new SecurityMethods();
	//pedidosRepo : accede al repositorio de pedidos
	@Autowired
	private PedidosRepository pedidosRepo;
	
	/*
	 * Este método se encarga de guardar riders, ya sea para modificar sus datos o registrarlos.
	 * Controla excepciones de contraseñas, emails y datos ya en uso
	 * 
	 * */
	public Rider saveRider(Rider rider) throws YaEnUsoException, ContraseniaIncorrectaException, 
	InvalidEmailException, IlegalNumberException {
		
		Optional<Rider> possibleRider = riderRepo.findByEmail(rider.getEmail());
		
		if(possibleRider.isPresent())
			throw new YaEnUsoException("Error.Ya existe un rider que utiliza este correo. Utilize otro");
		
		possibleRider = riderRepo.findById(rider.getNif());
		
		if(possibleRider.isPresent())
			throw new YaEnUsoException("Error.Ya existe un rider que utiliza este NIF. Intente otro");

		Persona aux = rider;
		checkSecurity.restriccionesContrasenia(aux);
		checkSecurity.verificarNumero(rider.getTelefono());
		
		if(Boolean.FALSE.equals(checkSecurity.validNif(rider.getNif())))
			throw new IlegalNumberException("El NIF introducido no es un NIF válido. Tiene que contener 8 números y un caracter");

		if(Boolean.FALSE.equals(checkSecurity.validEmail(rider.getEmail())))
			throw new InvalidEmailException("El campo email no corresponde con uno verdadero");
		
		checkSecurity.equalPass(rider.getContrasenia(), rider.getContraseniaDoble());
		rider.setContrasenia(checkSecurity.cifradoContrasenia(rider.getContrasenia()));
		rider.setContraseniaDoble(checkSecurity.cifradoContrasenia(rider.getContraseniaDoble()));
		
		return riderRepo.insert(rider);

		}
	/*
	 * Método que hace una consulta por email al repositorio para sacar un rider con dicho email
	 * */
	public Optional<Rider> findByEmail(String email){
		return riderRepo.findByEmail(email);
	}
	/*
	 * Retorna un rider consultado en la BBDD por ese NIF que se le pasa como Input
	 * */
	public Optional<Rider> findById(String nif){
		return riderRepo.findById(nif);
	}
	
	/*Método que permite eliminar un rider de la BBDD*/
	public void deleteRiderById(String id) {
		riderRepo.deleteById(id);
	}
	
	/*Método para actualizar un rider*/
	public void updateForm(Rider rider) throws IlegalNumberException, IncompleteFormException,
	ContraseniaIncorrectaException {
		
		if (rider.getNif().equals("") || rider.getNombre().equals("")
				|| rider.getCarnet().equals("") || rider.getMatricula().equals("") 
				|| rider.getTipoVehiculo().equals("") || rider.getTelefono().equals("")
				|| rider.getEmail().equals("")  || rider.getContrasenia().equals(""))
			throw new IncompleteFormException("Introduzca todos los datos");
		
		Persona aux = rider;
		
		checkSecurity.restriccionesContrasenia(aux);
		checkSecurity.validEmail(rider.getEmail());
		checkSecurity.verificarNumero(rider.getTelefono());
		
		if(Boolean.FALSE.equals(checkSecurity.validNif(rider.getNif())))
			throw new IlegalNumberException("El NIF introducido no es un NIF válido. Tiene que contener 8 números y un caracter");
		
		if(rider.getContrasenia().length() != 60 && !rider.getContraseniaDoble().isEmpty()) {
			checkSecurity.equalPass(rider.getContrasenia(), rider.getContraseniaDoble());
			rider.setContrasenia(checkSecurity.cifradoContrasenia(rider.getContrasenia()));
			rider.setContraseniaDoble(checkSecurity.cifradoContrasenia(rider.getContraseniaDoble()));
        }
		
		riderRepo.save(rider);
	}
	/*Método que retorna a los riders la lista de todos los pedidos existentes*/
	public List<PedidoComanda> consultarPedidos() {
		return restaurantServ.listarPedidos();
	}
	/*Método que retorna a los riders la lista de todos los pedidos existentes según una etiqueta*/
	public List<PedidoComanda> consultarPedidosPorEstado(String estado) {
		return restaurantServ.listarPedidosPorEstado(estado);
	}
	
	/* Método en el que el rider cambia el estado de un pedido */
    public void cambiarEstadoPedido(PedidoComanda comanda) throws MalEstadoPedidoException {
    	
    	Optional<Rider> r = riderRepo.findById(comanda.getIdRider());
    	
    	if(!r.isPresent())
    		 throw new MalEstadoPedidoException("No existe el rider para consultar ese pedido.");
    	
    	if(r.get().getNumeroPedidos() == 0)
    		 throw new MalEstadoPedidoException("El rider no puede realizar más entregas.");
    	
    	r.get().setNumeroPedidos(r.get().getNumeroPedidos()-1);
    	
    	riderRepo.save(r.get());
    	
    	comanda.setEstadoPedido("EnReparto");
    	
    	pedidosRepo.save(comanda);
    	
    	
    	
    }
    /*Método que retorna todos los rider tras consultar en la BBDD*/
	public List<Rider> consultarRiders() {
		return riderRepo.findAll();
	}
	/*Método que actualiza los intentos de un rider. Se suele llamar en login pero se puede usar a mano*/
	public void updateRiderIntentos(String email, int intentos) throws UnexistentUser {
		
		Optional<Rider> rider = riderRepo.findByEmail(email);
		
		if(!rider.isPresent())
			throw new UnexistentUser("Imposible encontrar al rider");
		
		rider.get().setIntentos(intentos);
		
		riderRepo.save(rider.get());
		
		
	}
	/*Método que consulta los pedidos en reparto y retorna una lista*/
	public List<PedidoComanda> consultarMisPedidosEnReparto(String idRider) {

		List<PedidoComanda> lista = pedidosRepo.findByestadoPedidoContaining("EnReparto");
		List<PedidoComanda> listaEnRepartoRider = new ArrayList<>();
		for (PedidoComanda p : lista) {
			if (p.getIdRider().equals(idRider)) {
				listaEnRepartoRider.add(p);
			}
		}
		return listaEnRepartoRider;
	}
	/* Método que comprueba si el rider todavía no ha sido valorado */
	public boolean isPrimeraValoracion(String id) throws UnexistentUser {
		boolean first = false;
		Optional<Rider> restaurante = riderRepo.findById(id);
		
		if(!restaurante.isPresent())
			throw new UnexistentUser("No existe ese rider por valoración");
		
		if (restaurante.get().getValoracionMedia() == 0) {
			first = true;
		}
		return first;
	}
}
