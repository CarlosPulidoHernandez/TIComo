package com.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.exceptions.IncompleteFormException;
import com.example.exceptions.InvoiceGenException;
import com.example.exceptions.PerfilBloqueadoException;
import com.example.exceptions.UnexistentUser;
import com.example.model.Facturas;
import com.example.model.PedidoComanda;
import com.example.model.Persona;
import com.example.model.Plato;
import com.example.model.Usuario;
import com.example.model.Valoracion;
import com.example.service.UsuarioService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.itextpdf.layout.Document;

@RestController
@RequestMapping("/ticomo")
@CrossOrigin(origins = {"https://ticomo01man.web.app", "http://localhost:4200"})
/*
 * Esta clase actúa como un patrón de 
 * diseño encargado de llamar al servicio de los usuarios
 * para ejecutar sus funcionalidades
 * */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class UsuarioController {
	/*=============================================================================================*/
	//VARIABLES
	
	//usuarioServ : variable que solo conoce el controlador y 
	//que sirve para llamar al servicio de usuario
	private static UsuarioService usuarioServ;
    
	@Autowired
	public void setUsuarioService(UsuarioService usuarioServ) {
		UsuarioController.usuarioServ = usuarioServ;
	}
    
	/*
	 * Este método es llamado por los usuarios que desean modificar sus datos a través
	 * de la vista correspondiente
	 * 
	 * Controla excepciones y retorna un FORBIDDEN en caso de error.	  
	 * */
	@PostMapping("/guardarUsuarios")
	public void saveUser(@RequestBody Usuario usuario) {
		try {
			usuarioServ.saveUseR(usuario);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	
	/*
	 * Este método retorna un opcional que, puede o no, contener un usuario el cual ha sido buscado en el 
	 * repositorio mediante su NIF de usuario.
	 * 
	 * Controla excepciones y retorna un BAD_REQUEST en caso de error.	  
	 * */
	@GetMapping("/usuarios/{nif}")
	public Optional<Usuario> findById(@PathVariable String nif) {
		try {
			return usuarioServ.findById(nif);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
	}
	
	/*Método que actualiza el usuario desde un form*/
	@PostMapping("/updateUser")
	public void update(@RequestBody Usuario usuario) throws IncompleteFormException {
		try {
			usuarioServ.updateForm(usuario);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		
	}
	
	/*Método que actualiza los intentos del usuario. Se puede llamar a mano aunque este caso nunca se contemple*/
	@PostMapping("/updateUserIntentos")
	public void updateUserIntentos(@RequestBody String email,int intentos) {
		try {
			usuarioServ.updateUserIntentos(email,intentos);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	
	/*Método que borra un usuario por su nif*/
	@DeleteMapping("/deleteUsuarios/{nif}")
	public void deleteById(@PathVariable String nif) {
		usuarioServ.deleteById(nif);
	}
	
	/*Método que consulta un usuario por su email*/
	@GetMapping("/consultaPorEmail/{email}")
	public Optional<Usuario> consultaPorEmail(@PathVariable String email) {
		try {
			return usuarioServ.findByEmail(email);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
	}
	
	/*Método para bloquear usuarios*/
	@PostMapping("/lockUser")
	public void lockUser(@RequestBody Usuario user) throws PerfilBloqueadoException
	{
		try {
			Persona aux = user;
			usuarioServ.lockPerson(aux);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/*Método para bloquear usuarios*/
	@PostMapping("/unlockUser") 
	public void unlockUser(@RequestBody Usuario user) throws PerfilBloqueadoException
	{
		try {
			Persona aux = user;
			usuarioServ.unlockPerson(aux);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/*Método que permite a un usuario realizar un pedido*/
	@SuppressWarnings("unchecked")
	@PostMapping("/realizarPedido")
	public JSONObject realizarPedido(@RequestBody Object map) {
		try {
			
			Map<String, String> pedido = (Map<String, String>) map;
			if(pedido.isEmpty())
				throw new IncompleteFormException("El pedido no puede estar vacío.");
			
			for (Map.Entry<String, String> entry : pedido.entrySet() ) {
				
			    String key = entry.getKey();
			    String value = entry.getValue();
			    
			    if(key.isEmpty() || value.isEmpty())
			    	throw new IncompleteFormException("Puede que el pedido contenga información, pero faltan datos en el pedido");
			    
			}
			PedidoComanda aux = usuarioServ.realizarPedido(pedido); 
			if(Boolean.TRUE.equals(aux.getComanda().isEmpty()))
		    	throw new IncompleteFormException("Puede que el pedido no contenga información, faltan datos en el pedido");

			Map<String,Document> docAux = usuarioServ.pedirFactura(aux);
			
			docAux.remove("documento");
			String url = "";
			for ( Map.Entry<String, Document> entry : docAux.entrySet() ) {
				
				url = entry.getKey();
				
			}
			JSONObject obj = new JSONObject();
			obj.put("url", url);
			return obj;
			
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/*
     * Método para cambiar el estado de un pedido al haberlos consultado por si están en reparto.
     * */
    @PostMapping("/consultarPedidosPendientes")
    public void cambiarEstadoPedido(@RequestBody PedidoComanda pedido) {
    	  try {
    		  usuarioServ.cambiarEstadoPedido(pedido); 
    		  }catch(Exception e) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
          }
         
    }
    
    @PostMapping("/modificarPedido")
    public void modificarPedido(@RequestBody PedidoComanda pedido) {
    	  try {
    		  usuarioServ.modificarPedido(pedido); 
    		  }catch(Exception e) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
          }
    }
    
    /*
     * Método que permite al cliente consultar sus pedidos en reparto 
     * */
    @PostMapping("/consultarMisPedidosPendientes")
    public List<PedidoComanda> consultarMisPedidosPendientes(@RequestBody String cliente){
        try {
            return usuarioServ.consultarMisPedidosEnReparto(cliente);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /*
     * Método que permite al cliente consultar sus pedidos en reparto 
     * */
    @PostMapping("/consultarMisPedidos")
    public List<PedidoComanda> consultarMisPedidos(@RequestBody String cliente){
        try {
        	List<PedidoComanda> l = usuarioServ.consultarMisPedidos(cliente);
        	
        	if(l.isEmpty())
        		throw new UnexistentUser("No tienes pedidos en este apartado");
        	
            return l ;
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

	/*Método que consulta las facturas del usuario dando el id del pedido*/
	@PostMapping("/consultarFacturasPorId")
	public void consultarFacturaPorId(@RequestBody String idPedido){
		try {
			
			 Optional<Facturas> factura = usuarioServ.findByidPedido(idPedido);
			 Optional<PedidoComanda> pedido = usuarioServ.consultarPedidoPorId(idPedido);
			 
			 if(!factura.isPresent() || !pedido.isPresent())
				 throw new InvoiceGenException("No se pudo generar las facturas. No hay facturas o no hay ese pedido");
			 
			usuarioServ.consultarFacturasPorIdPedido(idPedido);
			
			 
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
 	}
	
	/*Método para que el usuario consulte el nombre de un plato*/
	@PostMapping("/consultarIdPorNombrePlato")
	public Plato consultarPlato(@RequestBody String nombre) {
		try {
			return usuarioServ.consultarIdPorNombrePlato(nombre);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	
	/* Método para que el usuario valore al restaurante y rider */
	@PostMapping("/valoracionPedido")
	public Valoracion valorarRestaurante(@RequestBody Valoracion valoracion) {
		try {
			return usuarioServ.valorarPedido(valoracion);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}
	
	/* Método para que el usuario consulte la valoracion del restaurante y rider */
	@GetMapping("/valoracionPedido/{idPedido}")
	public Optional<Valoracion> consultarValoracion(@PathVariable String idPedido) {
		try {
			return usuarioServ.consultarValoracion(idPedido);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/* Método para que el usuario anule el pedido */
	@DeleteMapping("/anularPedido/{id}")
	public void deletePedidoByid(@PathVariable String id) {
		try {
			usuarioServ.deleteOrder(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}	
	}


}
