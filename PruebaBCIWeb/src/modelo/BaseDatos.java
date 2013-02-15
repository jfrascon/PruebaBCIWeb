package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

/**
 * Clase que representa la base de datos que contiene la informacion con la que se contruye el grafo de la aplicacion.
 * La base de datos puede ser un fichero de texto o bien una base de datos relacional como MySQL, etc. El usuario puede
 * especificar la fuente de donde se extrae la informacion.
 * 
 * @author jfrascon
 * @version "%I%, %G%
 * 
 */
public class BaseDatos {

	private Connection conexion;

	/**
	 * Constructor de la clase.
	 * 
	 * @param servicioConexiones
	 * Pool de conexiones disponibles con la base de datos. De ahi se obtiene una conexion cuando sea necesario para
	 * consultar datos. Tras su uso se deja la conexion de nuevo en el pool para que otro recurso pueda hacer uso de
	 * ella.
	 */
	public BaseDatos(DataSource servicioConexiones) throws SQLException {

		// El fragmento de codigo en donde se obtiene una conexion esta definido como seccion critica.
		// La conexion se recupera desde el servicio de conexiones pasado como argumento
		// al constructor de la clase. Es mas eficiente recuperar una conexion
		// existente que crear una conexion nueva.
		synchronized (servicioConexiones) {
			conexion = servicioConexiones.getConnection();
		}

	}

	/**
	 * Este metodo cierra la conexion para devolverla al pool de conexiones disponibles de la aplicacion web.
	 * 
	 * @throws SQLException
	 */
	public void cerrarConexionConBD() throws SQLException {
		if (conexion != null) {
			conexion.close();
		}
	}

	/**
	 * Este metodo construye un mapa a partir de la informacion almacenada en una base de datos relacional. Los datos
	 * necesarios para acceder a la base de datos estan disponibles en el objeto conexion, gracias a que ha sido creado
	 * a partir de un pool de conexiones en el que ya se especifica la url de la base de datos, el nombre del usuario y
	 * su clave.
	 * 
	 * @return Mapa que representa el grafo con el que trabaja la aplicacion.
	 * @see Mapa
	 */
	public Mapa obtenerMapaBD() {

		Statement stmt = null;
		ResultSet rs = null;
		Mapa mapa = new Mapa();

		try {
			// Crear la estructura que permite acceder a la base de datos.
			stmt = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// Recuperar las ciudades de la base de datos.
			rs = stmt.executeQuery("SELECT nombreCiudad, coordX, coordY FROM Ciudades");
			// Recorrer todos los registros obtenidos de la base de datos extrayendo la informacion util.
			while (rs.next()) {
				mapa.aniadirCiudad(new Ciudad(rs.getString("nombreCiudad"), rs.getFloat("coordX"), rs.getFloat("coordY")));
			}
			// Recuperar todas las carreteras de la base de datos.
			rs = stmt.executeQuery("SELECT nombreCiudadA, nombreCiudadb FROM Carreteras");
			// Recorrer todos los registros obtenidos de la base de datos extrayendo la informacion util.
			while (rs.next()) {
				mapa.aniadirCarretera(rs.getString("nombreCiudadA"), rs.getString("nombreCiudadB"));
			}
			rs.close();
		} catch (SQLException sqle) {
			// sqle.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException sqle) {
				// sqle.printStackTrace();
			}
		}
		return mapa;
	}

	/**
	 * Metodo que obtiene el nombre de todas las ciudades que existen en la base de datos. Este metodo ha sido
	 * implementado para que el jsp VistaInicio durante su primera carga pueda consultar dinamicamente que ciudades
	 * existen en la aplicacion, recuperar sus nombres, y generar etiquetas html para su visualizacion en el navegador.
	 * Asi no hay que que escribir 'a pincho' sus nombres en el jsp.
	 * 
	 * @return El nombre de todas las ciudades que constituyen el mapa de trabajo de la aplicacion.
	 */
	public String[] obtenerNombreCiudades() {

		Statement stmt = null;
		ResultSet rs = null;
		String nombreCiudades = "";

		try {
			// Crear la estructura que permite acceder a la base de datos.
			stmt = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// Recuperar las ciudades de la base de datos.
			rs = stmt.executeQuery("SELECT nombreCiudad FROM Ciudades");
			// Recorrer todos los registros obtenidos de la base de datos extrayendo la informacion util.
			// Para evitar que el primer registro sea un espacio en blanco.
			rs.next();
			nombreCiudades = rs.getString("nombreCiudad");
			while (rs.next()) {

				nombreCiudades += " " + rs.getString("nombreCiudad");
			}
			rs.close();
		} catch (SQLException sqle) {
			// sqle.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException sqle) {
				// sqle.printStackTrace();
			}
		}
		return nombreCiudades.split(" ");

	}
}