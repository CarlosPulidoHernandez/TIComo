package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.exceptions.ContraseniaIncorrectaException;
import com.example.exceptions.InvalidEmailException;
import com.example.exceptions.NonAdminValidationException;
import com.example.exceptions.PerfilBloqueadoException;
import com.example.exceptions.UnexistentUser;
import com.example.model.Administrator;
import com.example.model.Persona;
import com.example.model.Rider;
import com.example.model.Telefonista;
import com.example.model.TokenSession;
import com.example.model.Usuario;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Service
@RequestMapping("/Login")
/*
 *
 * Clase referente al servicio de Loggeo que contiene las funcionalidades para
 * loggearse
 *
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class LoginService {
	/* ================================================================= */

	// VARIABLES
	// adminServ : Servicio que conoce para buscar administradores que quieren
	// loggearse
	// Anota Autowired para instanciarse automáticamente al iniciar Spring
	@Autowired
	private AdminService adminServ;
	// riderServ : Servicio que conoce para buscar riders que quieren loggearse
	@Autowired
	private RiderService riderServ;
	// userServ : Servicio que conoce para buscar usuarios que quieren loggearse
	@Autowired
	private UsuarioService userServ;
	// userServ : Servicio que conoce para buscar telefonistas que quieren loggearse
	@Autowired
	private TelefonistaService telServ;

	// checkSecurity : variable que contiene métodos auxiliares para comprobar la
	// seguridad del sistema
	private SecurityMethods checkSecurity = new SecurityMethods();

	/*
	 * Este método representa la funcionalidad del LogIn. Obtiene por entrada un
	 * JSON que contiene la información (email y contraseña) de la persona que
	 * quiere entrar a la aplicación Retorna un JSON con información util para el
	 * cliente en caso de autorizar la entrada. En caso de denegarla, lanza
	 * excepciones de UnexistentUser, contraseniaIncorrecta o InvalidEmailException
	 */
	public TokenSession signIn(Usuario info) throws UnexistentUser, ContraseniaIncorrectaException,
			InvalidEmailException, PerfilBloqueadoException, NonAdminValidationException {

		TokenSession retornoLogin = new TokenSession("", "", "", "");

		Optional<Administrator> possibleAdmin;
		Optional<Usuario> possibleUsuario;
		Optional<Rider> possibleRider;
		Optional<Telefonista> possibleTelefonista;

		Persona possibleLogin = null;

		String errMsg = "Las contraseñas no son correctas";
		String token = "";
		String email = info.getEmail();

		String remplazar = "loquesea";
		String remplazo = "Este loquesea está bloqueado debido a múltiples inicios fallidos de sesión o decisión de un administrador. "
				+ "Si necesita ayuda consulte con un administrador de la aplicación de TIComo";

		if (Boolean.FALSE.equals(checkSecurity.validEmail(email)))
			throw new InvalidEmailException("El email no corresponde con uno válido");

		possibleAdmin = adminServ.findByEmail(email);
		possibleUsuario = userServ.findByEmail(email);
		possibleRider = riderServ.findByEmail(email);
		possibleTelefonista = telServ.findByEmail(email);

		if (!possibleAdmin.isPresent() && !possibleUsuario.isPresent() && !possibleRider.isPresent()
				&& !possibleTelefonista.isPresent())
			throw new UnexistentUser("No se ha podido encontrar usuario con ese mail");

		if (possibleAdmin.isPresent()) {

			boolean contraseniaAdmin = checkSecurity.decoder(info.getContrasenia(),
					possibleAdmin.get().getContrasenia());

			if (possibleAdmin.get().getIntentos() <= 0)
				throw new PerfilBloqueadoException(remplazo.replace(remplazar, "administrador"));

			token += "Admin";
			if (!contraseniaAdmin) {
				possibleAdmin.get().setIntentos(possibleAdmin.get().getIntentos() - 1);
				adminServ.updateAdmnIntentos(possibleAdmin.get().getEmail(), possibleAdmin.get().getIntentos());
				throw new ContraseniaIncorrectaException(errMsg);
			}
			retornoLogin.setNif("None");
			retornoLogin.setUsuario(possibleAdmin.get().getNombre());
			retornoLogin.setTokenType(token);
			retornoLogin.setEmail(possibleAdmin.get().getEmail());
			possibleAdmin.get().setIntentos(5);
			adminServ.updateAdmnIntentos(possibleAdmin.get().getEmail(), possibleAdmin.get().getIntentos());
			return retornoLogin;
		}

		if (possibleUsuario.isPresent()) {

			boolean contraseniaUser = checkSecurity.decoder(info.getContrasenia(),
					possibleUsuario.get().getContrasenia());

			if (possibleUsuario.get().getIntentos() <= 0)
				throw new PerfilBloqueadoException(remplazo.replace(remplazar, "usuario"));

			token += "Usuario";
			if (!contraseniaUser) {
				possibleUsuario.get().setIntentos(possibleUsuario.get().getIntentos() - 1);
				userServ.updateUserIntentos(possibleUsuario.get().getEmail(), possibleUsuario.get().getIntentos());
				throw new ContraseniaIncorrectaException(errMsg);
			}
			possibleUsuario.get().setIntentos(5);
			userServ.updateUserIntentos(possibleUsuario.get().getEmail(), possibleUsuario.get().getIntentos());
			possibleLogin = possibleUsuario.get();
		}

		if (possibleRider.isPresent()) {

			boolean contraseniaRider = checkSecurity.decoder(info.getContrasenia(),
					possibleRider.get().getContrasenia());

			if (possibleRider.get().getIntentos() <= 0)
				throw new PerfilBloqueadoException(remplazo.replace(remplazar, "rider"));

			token += "Rider";
			if (!contraseniaRider) {
				possibleRider.get().setIntentos(possibleRider.get().getIntentos() - 1);
				riderServ.updateRiderIntentos(possibleRider.get().getEmail(), possibleRider.get().getIntentos());
				throw new ContraseniaIncorrectaException(errMsg);
			}
			possibleRider.get().setIntentos(5);
			riderServ.updateRiderIntentos(possibleRider.get().getEmail(), possibleRider.get().getIntentos());
			possibleLogin = possibleRider.get();
		}

		if (possibleTelefonista.isPresent()) {

			boolean contraseniaTelefonista = checkSecurity.decoder(info.getContrasenia(),
					possibleTelefonista.get().getContrasenia());

			token += "Telefonista";
			if (!contraseniaTelefonista)
				throw new ContraseniaIncorrectaException(errMsg);

			possibleLogin = possibleTelefonista.get();
		}

		retornoLogin.setNif(possibleLogin.getNif());
		retornoLogin.setUsuario(possibleLogin.getNombre());
		retornoLogin.setTokenType(token);
		retornoLogin.setEmail(possibleLogin.getEmail());

		return retornoLogin;

	}

	public Usuario buscarUsuarioComoTelefonista(Usuario info) throws InvalidEmailException {
		Optional<Usuario> possibleUsuario;

		String email = info.getEmail();
		
		possibleUsuario = userServ.findByEmail(email);
		
		if (Boolean.FALSE.equals(checkSecurity.validEmail(email)))
			throw new InvalidEmailException("El email no corresponde con uno válido");
		
		if(possibleUsuario.isPresent()) {
			return possibleUsuario.get();
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se encuentra el usuario");
		}
	}

}
