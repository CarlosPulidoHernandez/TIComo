package com.example.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exceptions.*;
import com.example.model.Administrator;
import com.example.model.PedidoComanda;
import com.example.model.Persona;
import com.example.repository.AdminRepository;
import com.example.repository.PedidosRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
@Service
@RequestMapping("/administrators")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
/*Clase que representa al servicio que da las funcionalidades a los administradores*/
public class AdminService {
	/*==========================================*/
	//VARIABLES
	//adminRepo : variable que solo conoce el servicio de administradores y que realiza consultas a la BBDD con respecto a Admins
	//Anota Autowired para instanciarse automáticamente al iniciar Spring
	@Autowired
	private AdminRepository adminRepo;
	//pedidosRepo : permite hacer consultas al repositorio de pedidos
	@Autowired 
	private PedidosRepository pedidosRepo;
	//checkSecurity : objeto que se encarga de llamar a métodos que se usan para revisar la seguridad del sistema u otras funcionalidades
	private SecurityMethods checkSecurity = new SecurityMethods();
	
	private String tokenStr = "admin";
	private String errorToken = "Invalid admin token.";
	/*======================================================================*/
	//MÉTODOS
	
	/*
	 * 
	 * Este método se encarga de revisar que un administrador es válido para guardar datos referentes a su perfil en el repositorio,
	 * así como para crear administradores.
	 * 
	 * Controla excepciones de YaEnUsoException, contraseniaIncorrecta y InvalidEmailException
	 * 
	 * */
	public void saveAdmin(Administrator admin) 
			throws YaEnUsoException, ContraseniaIncorrectaException, InvalidEmailException {
		
		Optional<Administrator> possibleAdmin = adminRepo.findByEmail(admin.getEmail());
		
		if (possibleAdmin.isPresent())
			throw new YaEnUsoException("El email que has introducido ya existe");

		if(Boolean.FALSE.equals(checkSecurity.validEmail(admin.getEmail()))) {
			throw new InvalidEmailException("El email no corresponde con uno válido");
		}
		
		checkSecurity.restriccionesContraseniaAdmin(admin);
		checkSecurity.equalPass(admin.getContrasenia(), admin.getContraseniaDoble());
		admin.setContrasenia(checkSecurity.cifradoContrasenia(admin.getContrasenia()));	
		admin.setContraseniaDoble(checkSecurity.cifradoContrasenia(admin.getContrasenia()));	
		admin.setIntentos(5);
		adminRepo.insert(admin);	
	}
	
	/*Este método es utilizado por los administradores para desbloquear usuarios y riders*/
	public void unlockPerson(Persona usuario, String token) throws NonAdminValidationException 
	{
		if(!token.toLowerCase().contains(this.tokenStr))
			throw new NonAdminValidationException(this.errorToken);
		usuario.setIntentos(5);
		
	}
	
	/*Este método es utilizado por los administradores para desbloquear otros administradores*/
	public void unlockAdmin(Administrator admin, String token) throws NonAdminValidationException 
	{
		if(!token.toLowerCase().contains(this.tokenStr))
			throw new NonAdminValidationException(this.errorToken);
		admin.setIntentos(5);
		adminRepo.save(admin);
	}
	
	/*Este método es utilizado por los administradores para bloquear otros administradores*/
	public void lockAdmin(Administrator admin, String token) throws NonAdminValidationException 
	{
		if(!token.toLowerCase().contains(this.tokenStr))
			throw new NonAdminValidationException(this.errorToken);
		admin.setIntentos(0);
		adminRepo.save(admin);

	}
	
	/*
	 * 
	 * Método que retorna un opcional que puede contener un Administrador buscado por email,
	 *  así como no contener nada
	 * 
	 * */
	public Optional<Administrator> findByEmail(String email){
		return adminRepo.findByEmail(email);
	}

	/*Método que actualiza a un admin en el repositorio*/
	public void updateAdmnForm(Administrator admn) throws ContraseniaIncorrectaException, IncompleteFormException {
		
		if (admn.getNombre().equals("") || admn.getApellidos().equals("")
				|| admn.getContrasenia().equals("") || admn.getEmail().equals("")
				|| admn.getZonaGeografica().equals("")) 
			throw new IncompleteFormException("Introduzca todos los datos");
		
		if (!admn.getContraseniaDoble().isEmpty()) {
			checkSecurity.equalPass(admn.getContrasenia(), admn.getContraseniaDoble());
			admn.setContrasenia(checkSecurity.cifradoContrasenia(admn.getContrasenia()));
			admn.setContraseniaDoble(checkSecurity.cifradoContrasenia(admn.getContrasenia()));
		}
		
		checkSecurity.validEmail(admn.getEmail());
		checkSecurity.restriccionesContraseniaAdmin(admn);
		adminRepo.save(admn);
		
	}
	/*
	 * Método que llama al repositorio de pedidos para listar los pedidos de un restaurante
	 * */
	public List<PedidoComanda> findByidRestaurante(String idRestaurante) {
		return pedidosRepo.findByidRestaurante(idRestaurante);
	}
	
	/*Método que actualiza los intentos del admin. Se suele llamar desde login pero se puede llamar a mano*/
	public void updateAdmnIntentos(String email, int intentos) throws NonAdminValidationException {
		
		Optional<Administrator> admin = adminRepo.findByEmail(email);
		
		if(!admin.isPresent())
			throw new NonAdminValidationException("Imposible encontrar al admin");
		
		admin.get().setIntentos(intentos);
		
		adminRepo.save(admin.get());
		
	}

	public List<PedidoComanda> consultarPedidosPorEstado(String estado) {
		return pedidosRepo.findByestadoPedidoContaining(estado);
	}
	

}
