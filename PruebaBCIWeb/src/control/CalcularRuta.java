package control;

import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import modelo.BaseDatos;
import modelo.Mapa;

/**
 * Servlet que recibe la peticion http del jsp VistaInicio, obiente la ruta entre dos ciuades, cuyos nombres obtiene de
 * la peticion, y finalmente devuelve la peticion al jsp VistaInicio para que muestre los resultados.
 */
@WebServlet("/CalcularRuta")
public class CalcularRuta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Mapa mapa;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CalcularRuta() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Este metodo se ejecuta solo una vez, durante la inicializacion del servlet. En el se accede a la base de datos y
	 * construye el mapa de ciudades. De ese modo se tiene disponible para las futuras peticiones de calculo de camino
	 * entre dos ciudades cualesquiera.
	 * 
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// String baseDatos = null;
		String nombreRecurso = null;
		// Recuperamos el contexto inicial
		try {
			// baseDatos = getInitParameter("baseDatos");
			nombreRecurso = "jdbc/pruebabiicode";
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			// Conexiones para la base de datos buscada.
			DataSource servicioConexiones = (DataSource) envCtx.lookup(nombreRecurso);
			BaseDatos bd = new BaseDatos(servicioConexiones);
			mapa = bd.obtenerMapaBD();
			bd.cerrarConexionConBD();
		} catch (Exception e) {
			throw new ServletException("Imposible recuperar java:comp/env/" + nombreRecurso, e);
		}
	}

	/**
	 * Metodo para procesr las peticiones get. En la aplicacion no se usa metodo get para enviar datos del cliente al
	 * servidor pero tampoco esta de mas implementar el metodo doGet como abajo.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * Metodo para procesar las peticiones post. Con el metodo de envio post el cliente envia al servidor la informacion
	 * en en el cuerpo de la peticion. El servlet extrae la informacion de interes y calcula la ruta entre dos ciudades.
	 * A continuacion redirige la peticion al jsp VistaInicio para que la muestre en el navegador.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nombreCiudadOrigen = request.getParameter("nombreCiudadOrigen");
		String nombreCiudadDestino = request.getParameter("nombreCiudadDestino");
		String ruta = mapa.obtenerCamino(mapa.dijkstra(nombreCiudadOrigen), nombreCiudadDestino);
		// System.out.println(nombreCiudadOrigen + " " + nombreCiudadDestino);
		// System.out.println(ruta);
		request.setAttribute("ruta", ruta/* URLEncoder.encode(ruta, "UTF-8") */);
		RequestDispatcher rd = request.getRequestDispatcher("VistaInicio.jsp");
		rd.forward(request, response);
	}
}
