package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.controller.RiderController;
import com.example.controller.ServiceController;
import com.example.controller.UsuarioController;
import com.example.model.PedidoComanda;
import com.example.model.Usuario;
import org.junit.runners.MethodSorters;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-tests.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TelefonistaTests {

	
	private UsuarioController uController;
	
	private ServiceController lController;

	@Before
	public void setUp() {
		uController = new UsuarioController();

		lController = new ServiceController();
	}
    
    //LOGIN TEST

    @Test
	public void test01_loginCorrecto() {
        try {
            Usuario info = new Usuario();
            info.setEmail("telefonista@telefonista.com");
            info.setContrasenia("8Caracteres");
            info.setIntentos(5);
            
            lController.signIn(info);

		} catch (Exception expected) {
			Assert.fail();
		}
	}

	@Test
	public void test02_loginIncorrecto() {
	    try {
	        Usuario info = new Usuario();
	        info.setEmail("telefonista@telefonista.com");
	        info.setContrasenia("8caracteres");
	        info.setIntentos(5);
	        
	        lController.signIn(info);
	        
	        Assert.fail();
	    } catch (Exception expected) {}
	    
	}
	
	@Test
	public void test03_loginIncorrecto() {
	    try {
	        Usuario info = new Usuario();
	        info.setEmail("telefonista.telefonista.com");
	        info.setContrasenia("8Caracteres");
	        info.setIntentos(0);
	        
	        lController.signIn(info);
	        
	        Assert.fail();
	    } catch (Exception expected) {}
	    
	}
	
	@Test
	public void test04_loginIncorrecto() {
	    try {
	        Usuario info = new Usuario();
	        info.setEmail("tel@telefonista.com");
	        info.setContrasenia("8Caracteres");
	        info.setIntentos(3);
	        
	        lController.signIn(info);
	        
	        Assert.fail();
	    } catch (Exception expected) {}
	    
	}
	
	//AÑADIR PEDIDO TEST
	@Test
	public void test05_anadirPedidoCorrecto() {
        try {
        	Map<String,String> info = new HashMap<String,String>();
        	info.put("nifCliente", "50505050Q");
        	info.put("idRestaurante", "1111");
        	
        	info.put("1", "1");
        	info.put("2", "1");
        	
        	uController.realizarPedido(info);

		} catch (Exception expected) {
			Assert.fail();
		}
	}
	
	@Test
	public void test06_anadirPedidoIncorrecto() {
        try {
        	Map<String,String> info = new HashMap<String,String>();
        	info.put("nifCliente", "50505050Q");
        	info.put("idRestaurante", "2");
        	
        	info.put("1000", "1");
        	info.put("2", "1");
        	
        	uController.realizarPedido(info);

			Assert.fail();
		} catch (Exception expected) {}
	}
	
	@Test
	public void test07_anadirPedidoIncorrecto() {
        try {
        	Map<String,String> info = new HashMap<String,String>();
        	info.put("nifCliente", "50505050Q");
        	info.put("idRestaurante", "3");
        	
        	info.put("1", "1");
        	info.put("2.7", "1");
        	
			uController.realizarPedido(info);

			Assert.fail();
		} catch (Exception expected) {}
	}
	
	//MODIFICAR PEDIDO TEST
	@Test
	public void test08_modificarPedidoCorrecto() {
        try {
        	RiderController rController = new RiderController();
        	List<PedidoComanda> pedidos = rController.consultarTodosLosPedidos();
        	
        	String id = pedidos.get(0).getId();
        	
        	PedidoComanda info = new PedidoComanda();
        	
        	info.setId(id);
        	info.setEstadoPedido("PedidoHecho");
        	
        	uController.modificarPedido(info);

		} catch (Exception expected) {
			Assert.fail();
		}	
	}
	
	@Test
	public void test09_modificarPedidoInorrecto() {
		try {
        	PedidoComanda info = new PedidoComanda();
            
        	info.setId("54");
        	
        	uController.modificarPedido(info);

        	Assert.fail();
		} catch (Exception expected) {}
	}
	
	@Test
	public void test10_modificarPedidoInorrecto() {
		try {
        	PedidoComanda info = new PedidoComanda();
            
        	info.setId("346.4");
        	
        	uController.modificarPedido(info);

        	Assert.fail();
		} catch (Exception expected) {}
	}
	
	//ELIMINAR PEDIDO TEST
	@Test
	public void test11_eliminarPedidoCorrecto() {
		try {
        	RiderController rController = new RiderController();
        	List<PedidoComanda> pedidos = rController.consultarTodosLosPedidos();
        	
        	String id = pedidos.get(0).getId();
			
			uController.deletePedidoByid(id);
			
		} catch (Exception expected) {
			Assert.fail();
		}
	}
	
	@Test
	public void test12_eliminarPedidoIncorrecto() {
		try {
			String id = "12";
			
			uController.deletePedidoByid(id);
			
			Assert.fail();
		} catch (Exception expected) {}
	}
	
	@Test
	public void test13_eliminarPedidoIncorrecto() {
		try {
			String id = "1.98";
			
			uController.deletePedidoByid(id);
			
			Assert.fail();
		} catch (Exception expected) {}
	}
}
