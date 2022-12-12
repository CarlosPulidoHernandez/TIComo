package com.example.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.Facturas;
import com.example.model.PedidoComanda;
import com.example.exceptions.ContraseniaIncorrectaException;
import com.example.exceptions.IlegalNumberException;
import com.example.exceptions.IncompleteFormException;
import com.example.exceptions.InvalidEmailException;
import com.example.exceptions.InvoiceGenException;
import com.example.exceptions.MalEstadoPedidoException;
import com.example.exceptions.PerfilBloqueadoException;
import com.example.exceptions.UnexistentOrderException;
import com.example.exceptions.UnexistentUser;
import com.example.exceptions.YaEnUsoException;
import com.example.model.Persona;
import com.example.model.Plato;
import com.example.model.Restaurante;
import com.example.model.Rider;
import com.example.model.Usuario;
import com.example.model.Valoracion;
import com.example.repository.FacturasRepository;
import com.example.repository.PedidosRepository;
import com.example.repository.RestauranteRepository;
import com.example.repository.RiderRepository;
import com.example.repository.UsuarioRepository;
import com.example.repository.ValoracionesRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.itextpdf.layout.Document;

@Service
@RequestMapping("/Usuarios")
/*
 * Clase representativa del servicio que aloja funcionalidades para los usuarios
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class UsuarioService {
		/* ========================================================== */
	// VARIABLES
	// usuarioRepo: variable solo conocida por esta clase que accede al repositorio
	// para hacer consultas en la BBDD
	// Anota Autowired para instanciarse automáticamente al iniciar Spring
	@Autowired
	private UsuarioRepository usuarioRepo;
	//restauranteRepo : repositorio para hacer consultas de los pedidos de los restaurantes
	@Autowired
	private RestauranteRepository restauranteRepo;
	//pedidosRepo : repositorio para hacer consultas de los pedidos del usuario
	@Autowired
	private PedidosRepository pedidosRepo;
	//valRepo : repositorio para hacer consultas de las valoraciones de los pedidos
	@Autowired
	private ValoracionesRepository valRepo;
	// checkSecurity : variable que accede a métodos que comprueban la seguridad del
	// sistema
	private SecurityMethods checkSecurity = new SecurityMethods();
	// restauranteService : variable que permite a un usuario acceder al servicio de restaurantes para realizar un pedido
	@Autowired
	private RestauranteService restaurantServ;
	// facturasRepo : repositorio de facturas para manejar la BBDD
	@Autowired
	private FacturasRepository facturasRepo;
	// riderService : variable que la vamos a usar al valorar riders
    @Autowired
    private RiderService riderServ;
    // valoracionService : variable que la vamos a usar para guardar las
    // valoraciones en la BBDD
    @Autowired
    private ValoracionService valService;
    //riderRepo : repositorio para pedir a los rider
    @Autowired
    private RiderRepository riderRepo;
    
    static final String UNUS = "No existe para valorar";

	/* ========================================================================== */
	// MÉTODOS
	/*
	 * 
	 * Este método sirve para registrar usuarios o actualizar su información.
	 * Controla excepciones de
	 * YaEnUsoException,contraseniaIncorrecta,IlegalNumberException e
	 * InvalidEmailException.
	 * 
	 * 
	 */
	public Usuario saveUseR(Usuario usuario)
			throws YaEnUsoException, ContraseniaIncorrectaException,
			IlegalNumberException, InvalidEmailException {

		Optional<Usuario> possibleUsuario = usuarioRepo.findByEmail(usuario.getEmail());

		if (possibleUsuario.isPresent())
			throw new YaEnUsoException("Error.Ya existe un usuario que utiliza este correo. Utilize otro");

		possibleUsuario = usuarioRepo.findById(usuario.getNif());

		if (possibleUsuario.isPresent())
			throw new YaEnUsoException("Error.Ya existe un usuario que utiliza este NIF. Intente otro");

		Persona aux = usuario;
		checkSecurity.restriccionesContrasenia(aux);
		checkSecurity.verificarNumero(usuario.getTelefono());
		if(Boolean.FALSE.equals(checkSecurity.validEmail(usuario.getEmail())))
			throw new InvalidEmailException("El email no corresponde con uno válido");
		
		if(Boolean.FALSE.equals(checkSecurity.validNif(usuario.getNif())))
			throw new IlegalNumberException("El NIF introducido no es un NIF válido. Tiene que contener 8 números y un caracter");

		checkSecurity.equalPass(usuario.getContrasenia(), usuario.getContraseniaDoble());
		usuario.setContrasenia(checkSecurity.cifradoContrasenia(aux.getContrasenia()));
		usuario.setContraseniaDoble(checkSecurity.cifradoContrasenia(usuario.getContraseniaDoble()));	
		
		return usuarioRepo.insert(usuario);

	}

	/*
	 * Retorna una consulta de todos los usuarios de la BBDD
	 */
	public List<Usuario> consultarUsuarios() {
		return usuarioRepo.findAll();
	}

	/*
	 * 
	 * Retorna un opcional que pueda contener un usuario buscado por su ID
	 */
	public Optional<Usuario> findById(String nif) {
		return usuarioRepo.findById(nif);
	}
	
	/*Método para guardar una factura si lo desea*/
	public void guardarFactura(Facturas factura) {
		facturasRepo.insert(factura);
	}

	/*
	 * Retorna una consulta de todas las facturas con ese idPedido de la BBDD
	 */
	public void consultarFacturasPorIdPedido(String idPedido) throws InvoiceGenException, IncompleteFormException, IOException {
		
		Optional<Facturas> facts= facturasRepo.findByidPedido(idPedido);
		Optional<PedidoComanda> pedido = pedidosRepo.findById(idPedido);
		
		if(!pedido.isPresent() || !facts.isPresent()) {
			throw new InvoiceGenException("Hubo un problema con la factura del pedido");
		}
		
		if(!pedido.get().getId().equals(facts.get().getId())) {
			throw new InvoiceGenException("Hubo un problema con la factura del pedido");
		}
		
		restaurantServ.restauranteGeneraFactura(pedido.get(),facts.get());

		
	}
	

	/*
	 * 
	 * Retorna un opcional que puede contener un usuario buscado por su email
	 * 
	 */
	public Optional<Usuario> findByEmail(String email) {
		return usuarioRepo.findByEmail(email);
	}
	

	/*
	 * 
	 * Retorna un opcional que puede contener un usuario buscado por su email
	 * 
	 */
	public Optional<Valoracion> consultarValoracion(String id) {
		return valRepo.findById(id);
	}
	
	/*
	 * 
	 * Los cambios los cuales hagamos cuando modifiquemos los datos
	 * del usuario se guardarán en la base de datos
	 * 
	 */
	public void updateForm(Usuario usuario) throws IncompleteFormException, IlegalNumberException,
	ContraseniaIncorrectaException {
		
		if (usuario.getNif().equals("") || usuario.getNombre().equals("")
				|| usuario.getDireccion().equals("") || usuario.getTelefono().equals("")
				|| usuario.getEmail().equals("") || usuario.getContraseniaDoble().equals("")) 
			throw new IncompleteFormException("Introduzca todos los datos");
		
		Persona aux = usuario;
		checkSecurity.restriccionesContrasenia(aux);
		checkSecurity.validEmail(usuario.getEmail());
		checkSecurity.verificarNumero(usuario.getTelefono());
		
		if(Boolean.FALSE.equals(checkSecurity.verificarNumero(usuario.getTelefono())))
			throw new IlegalNumberException("El teléfono introducido no es un teléfono válido. Tiene que contener 9 números");
		
		
		if(Boolean.FALSE.equals(checkSecurity.validNif(usuario.getNif())))
			throw new IlegalNumberException("El NIF introducido no es un NIF válido. Tiene que contener 8 números y un caracter");
		
		if(usuario.getContrasenia().length() != 60 && !usuario.getContraseniaDoble().isEmpty()) {
			checkSecurity.equalPass(usuario.getContrasenia(), usuario.getContraseniaDoble());
			usuario.setContrasenia(checkSecurity.cifradoContrasenia(usuario.getContrasenia()));
			usuario.setContraseniaDoble(checkSecurity.cifradoContrasenia(usuario.getContrasenia()));
		}
		
		
		
		usuarioRepo.save(usuario);
			
	}
	/*Método que borra un usuario por ID*/
	public void deleteById(String nif) {
		usuarioRepo.deleteById(nif);	
	}
	
	/*Este método es utilizado por los administradores para bloquear usuarios y riders*/
	public void lockPerson(Persona usuario) throws PerfilBloqueadoException 
	{
		if(usuario.getIntentos()==0) {
			throw new PerfilBloqueadoException("Usuario ya deshabilitado");
		}
		usuario.setIntentos(0);
		
		Usuario aux = (Usuario) usuario;
		
		usuarioRepo.save(aux);
	}
	
	/*Este método es utilizado por los administradores para desbloquear usuarios y riders*/
	public void unlockPerson(Persona usuario) throws PerfilBloqueadoException 
	{
		if(usuario.getIntentos()==5) {
			throw new PerfilBloqueadoException("Usuario ya habilitado");
		}
		usuario.setIntentos(5);
		
		Usuario aux = (Usuario) usuario;
		
		usuarioRepo.save(aux);
	}

	/*Método que actua de llamada a la preparación de un pedido del usuario*/
	public PedidoComanda realizarPedido(Map<String,String> comanda) throws IlegalNumberException {
		
		return restaurantServ.prepararPedido(comanda);
		
	}

	/*Método que llama al restaurante para que le genere la factura*/
	
	public Map<String, Document> pedirFactura(PedidoComanda comanda) throws IncompleteFormException, IOException {
		return restaurantServ.pedirFactura(comanda);
	}
	/*Método que consulta un pedido por id proporcionado por un usuario*/
	public Optional<PedidoComanda> consultarPedidoPorId(String idPedido) {
		return restaurantServ.consultarPedidoPorId(idPedido);
	}
	/* Método en el que el usuario cambia el estado de un pedido */
    public void cambiarEstadoPedido(PedidoComanda comanda) throws UnexistentUser, MalEstadoPedidoException {
    	Optional<PedidoComanda> pedido = pedidosRepo.findById(comanda.getId());
        Optional<Rider> r= riderServ.findById(comanda.getIdRider());
        if(!pedido.isPresent())
            throw new UnexistentUser("No existe el pedido");
        if(!r.isPresent())
            throw new MalEstadoPedidoException("No existe el rider para consultar ese pedido.");

        r.get().setNumeroPedidos(r.get().getNumeroPedidos()+1);
        riderRepo.save(r.get());
        pedido.get().setEstadoPedido("Entregado");
        pedidosRepo.save(pedido.get());
        }

	public void modificarPedido(PedidoComanda pedido) throws UnexistentUser {
		Optional<PedidoComanda> optPedido = pedidosRepo.findById(pedido.getId());	
		if(!optPedido.isPresent()) {
			throw new UnexistentUser("No existe el pedido");
		}
		pedidosRepo.save(pedido);
	}

	/*
     * Método que llama al repositorio de pedidos para encontrar los pedidos pendientes de un cliente
     * */
    public List<PedidoComanda> consultarMisPedidosEnReparto(String cliente){
        List<PedidoComanda> pedidosPendientes = new ArrayList<>();
        List<PedidoComanda> total=  pedidosRepo.findBynifCliente(cliente);
        for(int i = 0; i< total.size();i++) {
            if(total.get(i).getEstadoPedido().equalsIgnoreCase("en reparto")) {
                pedidosPendientes.add(total.get(i));
            }
        }
        return pedidosPendientes;
    }
    /*
     * Método que llama al repositorio de pedidos para encontrar los pedidos de un cliente
     * */
    public List<PedidoComanda> consultarMisPedidos(String cliente){
        return pedidosRepo.findBynifCliente(cliente);

    }
    /*Método que actualiza los intentos de un usuario. Se puede usar a mano*/
	public void updateUserIntentos(String email, int intentos) throws UnexistentUser {
		
		Optional<Usuario> user = usuarioRepo.findByEmail(email);
		
		if(!user.isPresent())
			throw new UnexistentUser("Imposible encontrar al usuario");
		
		user.get().setIntentos(intentos);
		
		usuarioRepo.save(user.get());
		
		
	}
	/*Método que consulta el nombre de un plato*/
	public Plato consultarIdPorNombrePlato(String nombre) throws IlegalNumberException {
		
		return restaurantServ.consultarIdPorNombrePlato(nombre);
	}
	
	/*
	 * Método que guarda la valoracion del restaurante comprobando si es su primera
	 */
	public Valoracion valorarPedido(Valoracion valoracion) throws UnexistentUser {
		Optional<Restaurante> optRestaurante = restaurantServ.findByIdRestaurante(valoracion.getIdValoradoRestaurante());
		Optional<Rider> optRider = riderServ.findById(valoracion.getIdValoradoRider());
		
		if(!optRestaurante.isPresent() || !optRider.isPresent())
			throw new UnexistentUser(UNUS);
		
		List<Valoracion> valoraciones = valRepo.findAll();
		List<Integer> notasRestaurante = new ArrayList<>();
		notasRestaurante.add(valoracion.getNotaRestaurante());
		for (Valoracion valoracionRepo : valoraciones) {
			notasRestaurante.add(valoracionRepo.getNotaRestaurante());
		}
		optRestaurante.get().setValoracionMedia(valoracion.calcularMedia(notasRestaurante));
		restauranteRepo.save(optRestaurante.get());
		
		List<Integer> notasRider = new ArrayList<>();
		notasRider.add(valoracion.getNotaRider());
		for (Valoracion valoracionRepo : valoraciones) {
			notasRider.add(valoracionRepo.getNotaRider());
		}
		optRider.get().setValoracionMedia(valoracion.calcularMedia(notasRider));
		riderRepo.save(optRider.get());
		
		return valService.saveValoracionPedido(valoracion);
	}
	
	/*Consulta una factura por id*/
	public Optional<Facturas> findByidPedido(String idPedido) throws UnexistentUser {
		
		Optional<Facturas> factura = facturasRepo.findById(idPedido);
		
		if(!factura.isPresent())
			throw new UnexistentUser("Imposible cargar la factura");

		return factura;
	}

	public void deleteOrder(String id) throws MalEstadoPedidoException, UnexistentOrderException {
		Optional<PedidoComanda> optPedidoComanda = pedidosRepo.findById(id);		
		if(optPedidoComanda.isPresent()) {
			PedidoComanda pedido = optPedidoComanda.get();
			if(pedido.getEstadoPedido().equals("Entregado") || pedido.getEstadoPedido().equals("EnReparto")){
				throw new MalEstadoPedidoException("No se puede cancelar un pedido en el estado " + pedido.getEstadoPedido().toUpperCase());
			}
			pedidosRepo.deleteById(id);
		} else {
			throw new UnexistentOrderException("No se encuentra el pedido con id " + id);
		}
	}
	
}
