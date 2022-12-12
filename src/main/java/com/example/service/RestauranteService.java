package com.example.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.exceptions.IlegalNumberException;
import com.example.exceptions.IncompleteFormException;
import com.example.exceptions.UnexistentUser;
import com.example.model.Facturas;
import com.example.model.PedidoComanda;
import com.example.model.Plato;
import com.example.model.Restaurante;
import com.example.repository.FacturasRepository;
import com.example.repository.PedidosRepository;
import com.example.repository.RestauranteRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

/**
 * Métodos invocados por el controlador que se encargan de revisar que los datos
 * introducidos son correctos y de llamar al repositorio para realizar las
 * operaciones CRUD
 *
 */
@Service
@RequestMapping("/Restaurantes")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RestauranteService {
	/* ================================================ */
	// VARIABLES
	// restauranteRepo : permite acceder al repositorio de Restaurantes y hacer
	// consultas a la BBDD. Solo lo conoce esta clase
	@Autowired
	private RestauranteRepository restauranteRepo;
	// pedidosRepo : repositorio que guarda los pedidos
	@Autowired
	private PedidosRepository pedidosRepo;
	// platoService : variable del tipo PlatoService que permite instanciar un
	// objeto para acceder al servicio de platos
	@Autowired
	private PlatoService platoService;
	// checkSecurity : objeto que comprobará aspectos de seguridad
	private SecurityMethods checkSecurity = new SecurityMethods();
	// facturasRepo : repositorio de facturas para que el restaurante guarde las
	// facturas.
	@Autowired
	private FacturasRepository facturasRepo;

	static final String RUTA = "./facturasPDF";
	static final String RUTA2 = "\\nuevaFacturaTicomoPedido";

	/*
	 * Este método se encarga de guardar restaurantes, ya sea para modificar sus
	 * datos o registrarlos. Controla excepciones de emails y datos ya en uso
	 * 
	 */

	public void saveRestaurante(Restaurante restaurante) throws IncompleteFormException {
		if (restaurante.getCif().equals("") || restaurante.getNombre().equals("")
				|| restaurante.getEmailContacto().equals("") || restaurante.getCategoria().equals("")
				|| restaurante.getDireccion().equals(""))
			throw new IncompleteFormException("Faltan datos");

		checkSecurity.validEmail(restaurante.getEmailContacto());
		if (restaurante.getId() != null) {
			restaurante.setId(null);
		}
		restauranteRepo.insert(restaurante);

	}

	/**
	 * Este metodo se encarga de encontrar al restaurante por su id
	 */
	public Optional<Restaurante> findByIdRestaurante(String id) {
		return restauranteRepo.findById(id);
	}

	/**
	 * Método que se encarga de listar en la página todos los restaurantes que se
	 * han registrado
	 */
	public List<Restaurante> findAllRestaurantes() {
		return restauranteRepo.findAll();
	}

	/**
	 * Método que se encarga de borrar restaurante por su id
	 */
	public void deleteByIdRestaurante(String id) {
		restauranteRepo.deleteById(id);
	}

	/**
	 * Método en el cual se van a realizar los cambios en la base cuando
	 * modifiquemos los datos de cualquier restaurante
	 */
	public void updateRestaurante(Restaurante restaurante) throws IncompleteFormException {

		if (restaurante.getCif().equals("") || restaurante.getNombre().equals("")
				|| restaurante.getDireccion().equals("") || restaurante.getEmailContacto().equals("")
				|| restaurante.getCategoria().equals(""))
			throw new IncompleteFormException("Introduce todos los datos.");

		checkSecurity.validEmail(restaurante.getEmailContacto());

		restauranteRepo.save(restaurante);

	}

	/**
	 * Método el cual llamará al Servicio de Platos para que se realice el save de
	 * un plato.
	 */
	public void savePlato(Plato plato) throws IncompleteFormException {
		platoService.save(plato);
	}

	/**
	 * Método el cual llamará al Servicio de Platos para obtener la lista de platos
	 * de un restaurante mediante su ID.
	 */
	public List<Plato> getPlatosByIdRestaurante(String idRestaurante) {
		return platoService.getPlatosByIdRestaurante(idRestaurante);
	}

	/**
	 * Método el cual llamará al Servicio de Platos para obtener un optional de
	 * Plato mediante su ID.
	 */
	public Optional<Plato> findByIdPlato(String idPlato) {
		return platoService.findById(idPlato);
	}

	/**
	 * Método el cual llamará al Servicio de Platos para eliminar un Plato mediante
	 * su ID.
	 */
	public void deleteByIdPlato(String idPlato) {
		platoService.deleteById(idPlato);
	}

	/**
	 * Método el cual llamará al Servicio de Platos para actualizar un Plato.
	 */
	public void updatePlato(Plato plato) throws IncompleteFormException {
		platoService.update(plato);
	}

	/* Método que prepara un nuevo pedido y calcula su precio */
	public PedidoComanda prepararPedido(Map<String, String> comanda) throws IlegalNumberException {

		Timestamp time = new Timestamp(System.currentTimeMillis());
		Date fecha = new Date(time.getTime());
		String nifCliente = comanda.get("nifCliente");
		comanda.remove("nifCliente");
		String idRestaurante = comanda.get("idRestaurante");
		comanda.remove("idRestaurante");
		double precioComanda = 0.0;
		String estadoPedido = "PedidoHecho";
		PedidoComanda nuevoPedido = new PedidoComanda(null, comanda, estadoPedido, precioComanda, nifCliente,
				idRestaurante, fecha, "");
		nuevoPedido.setPrecioComanda(ajustarPrecioDePedido(nuevoPedido.getComanda()));
		pedidosRepo.insert(nuevoPedido);
		return nuevoPedido;

	}

	// Método auxiliar que calculará el precio de la comanda realizada por el
	// cliente
	private double ajustarPrecioDePedido(Map<String, String> comanda) throws IlegalNumberException {

		double precio = 0.0;

		for (Map.Entry<String, String> entry : comanda.entrySet()) {

			String idPlato = entry.getKey();
			int numeroPlatos = Integer.parseInt(entry.getValue());

			Optional<Plato> platoPedido = platoService.findById(idPlato);
			if (Boolean.FALSE.equals(platoPedido.isPresent()))
				throw new IlegalNumberException("Error. El plato con id : " + idPlato
						+ " puede que no esté disponible para servir en este momento. Realize de nuevo el pedido");

			precio += platoPedido.get().getPrecio() * numeroPlatos;

		}
		return precio;
	}

	/* Método que lista todos los pedidos existentes */
	public List<PedidoComanda> listarPedidos() {
		return pedidosRepo.findAll();
	}

	/* Método que lista todos los pedidos según la etiqueta/estado que tengan */
	public List<PedidoComanda> listarPedidosPorEstado(String estado) {
		return pedidosRepo.findByestadoPedidoContaining(estado);
	}
	
	/* Método que genera la factura al pedido */

	public Map<String, Document> pedirFactura(PedidoComanda comanda) throws IncompleteFormException, IOException {
		Facturas facturas;
		facturas = new Facturas("Factura TIComo",comanda.getId(),comanda.getFecha(),comanda.getNifCliente());
		facturasRepo.save(facturas);
		return this.restauranteGeneraFactura(comanda, facturas);
	}

	/* Método core de la clase que genera la factura de una comanda */
	public Map<String, Document> restauranteGeneraFactura(PedidoComanda comanda, Facturas factura0)
			throws IncompleteFormException, IOException {

		PdfWriter teclado = new PdfWriter(RUTA + RUTA2 + comanda.getId() + ".pdf");
		PdfDocument pdf = new PdfDocument(teclado);
		pdf.setDefaultPageSize(PageSize.A4);
		try (Document factura = new Document(pdf)) {
			float size = 285f;
			float size150 = size + 150f;
			float[] anchoColumna = { size, size150 };
			// Cabezera
			Table tablaTitulo = new Table(anchoColumna);
			Table infoFactura = new Table(new float[] { size / 2, size / 2 });

			tablaTitulo.addCell(new Cell().add(new Paragraph(factura0.getTitulo())).setBorder(Border.NO_BORDER));
			infoFactura.addCell(new Cell().add(new Paragraph("factura Nº : ")).setBold().setBorder(Border.NO_BORDER));
			infoFactura.addCell(new Cell().add(new Paragraph(factura0.getId()).setBorder(Border.NO_BORDER)));
			infoFactura.addCell(new Cell().add(new Paragraph("fecha factura : ")).setBold().setBorder(Border.NO_BORDER));
			infoFactura.addCell(new Cell().add(new Paragraph(factura0.getFechaFactura().toString())).setBorder(Border.NO_BORDER));
			tablaTitulo.addCell(new Cell().add(infoFactura).setBorder(Border.NO_BORDER));

			factura.add(tablaTitulo);
			factura.add(new Paragraph("\n"));
			// Divisor
			Border divisor = new SolidBorder(1f / 2f);
			Table divisora = new Table(anchoColumna);
			divisora.setBorder(divisor);

			factura.add(divisora);
			factura.add(new Paragraph("\n"));

			// Contenido del pedido

			ArrayList<String> platos = new ArrayList<>(comanda.getComanda().size());
			ArrayList<String> numero = new ArrayList<>(comanda.getComanda().size());
			ArrayList<String> precios = new ArrayList<>(comanda.getComanda().size());

			// Lectura del pedido
			for (Map.Entry<String, String> entry : comanda.getComanda().entrySet()) {

				Optional<Plato> plato = platoService.findById(entry.getKey());

				if (!plato.isPresent())
					throw new IncompleteFormException("Error al generar la factura");

				precios.add(Double.toString(plato.get().getPrecio()));
				platos.add(plato.get().getNombre());
				numero.add(entry.getValue());

			}
			float[] anchuraTablaComanda = { size, size, size };
			Table order = new Table(anchuraTablaComanda);
			// Subtabla Pedido
			Float padding = 5f;  
			order.addCell(new Cell().add(new Paragraph("Tu pedido || ")).setBold().setBorder(Border.NO_BORDER));
			order.addCell(new Cell().add(new Paragraph("Cantidad || ")).setBold().setBorder(Border.NO_BORDER));
			order.addCell(new Cell().add(new Paragraph(" Precio/Unidad ")).setBold().setBorder(Border.NO_BORDER));

			for (int i = 0; i < platos.size(); i++) {
				order.addCell(new Cell().add(new Paragraph(platos.get(i)+"\n")).setBorder(Border.NO_BORDER).setPadding(padding));
				order.addCell(new Cell().add(new Paragraph(precios.get(i)+"\n ")).setBorder(Border.NO_BORDER).setPadding(padding));
				order.addCell(new Cell().add(new Paragraph(numero.get(i)+"\n")).setBorder(Border.NO_BORDER).setPadding(padding));
			}
			//Subtabla cantidad
			Table cantidad = new Table(new float[] { size });
			// Subtabla Precios
			Table preciosTabla = new Table(new float[] { size });

			order.addCell(new Cell().add(cantidad).setBorder(Border.NO_BORDER));
			order.addCell(new Cell().add(preciosTabla).setBorder(Border.NO_BORDER));

			factura.add(order);
			factura.add(divisora);
			factura.add(new Paragraph("\n"));

			// Precio total

			order.addCell(new Cell()
					.add(new Paragraph("\n\n\n\n\t\t\t\t\tPrecio total : "
							+ Double.toString(comanda.getPrecioComanda()) + "€"))
					.setBold().setBorder(Border.NO_BORDER));
			order.addCell(
					new Cell().add(new Paragraph("\n\n\t\t¡¡Gracias por usar nuestro servicio!!")
							.setBold().setBorder(Border.NO_BORDER)));
			factura.add(order);
			Map<String, Document> ret = new HashMap<>();
			ret.put("documento", factura);
			String url = RUTA + RUTA2 + comanda.getId() + ".pdf";
			ret.put(RUTA + RUTA2 + comanda.getId() + ".pdf", null);
			Runtime rt = Runtime.getRuntime();
			
			rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
		

		return ret;
		}
	}
	
	

	/* Método que consulta al repositorio de pedidos por un id de pedido */
	public Optional<PedidoComanda> consultarPedidoPorId(String idPedido) {
		return pedidosRepo.findById(idPedido);
	}

	/* Método que consulta un nombre de plato dado su id */
	public Plato consultarIdPorNombrePlato(String nombre) throws IlegalNumberException {
		return platoService.consultarIdPorNombrePlato(nombre);
	}

	/* Método que comprueba si el restaurante todavía no ha sido valorado */
	public boolean isPrimeraValoracion(String id) throws UnexistentUser {
		boolean first = false;
		Optional<Restaurante> restaurante = restauranteRepo.findById(id);
		
		if(!restaurante.isPresent())
			throw new UnexistentUser("No existe ese restaurante por valoración");
		
		if (restaurante.get().getValoracionMedia() == 0) {
			first = true;
		}
		return first;
	}

	public List<PedidoComanda> getPedidosByIdRestaurante(String idRestaurante, String estado) {
		List<PedidoComanda> totalPedidos = pedidosRepo.findAll();
		List<PedidoComanda> pedidosRestaurante = new ArrayList<>();
		List<PedidoComanda> pedidosEstado = new ArrayList<>();
		for(int i =0; i< totalPedidos.size();i++) {
			if (idRestaurante.equalsIgnoreCase(totalPedidos.get(i).getIdRestaurante())) {
				pedidosRestaurante.add(totalPedidos.get(i));
			}
		}
		for(int i =0; i< pedidosRestaurante.size();i++) {
			if (estado.equalsIgnoreCase(pedidosRestaurante.get(i).getEstadoPedido())) {
				pedidosEstado.add(pedidosRestaurante.get(i));
			}
		}
		return pedidosEstado;
	}

}
