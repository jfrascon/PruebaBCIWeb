package modelo;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa un nodo del grafo mapa.
 * 
 * @author jfrascon
 * @version "%I%, %G%
 */

public class Ciudad {

	private String nombreCiudad;
	private float coordX;
	private float coordY;
	private Map<String, Float> ciudadesAdyacentes;

	/**
	 * Metodo constructor de la clase.
	 * 
	 * @param nombreCiudad
	 * El nombre de la ciudad
	 * @param coordX
	 * La coordenada x de la ciudad con respecto al sistema de referencia del mapa.
	 * @param coordY
	 * La coordenada y de la ciudad con respecto al sistema de referencia del mapa.
	 */
	public Ciudad(String nombreCiudad, float coordX, float coordY) {

		this.nombreCiudad = nombreCiudad;
		this.coordX = coordX;
		this.coordY = coordY;
		ciudadesAdyacentes = new HashMap<String, Float>();
	}

	public Ciudad() {

		this.nombreCiudad = null;
		this.coordX = 0;
		this.coordY = 0;
		ciudadesAdyacentes = new HashMap<String, Float>();
	}

	/**
	 * Metodo que devuelve el nombre de la ciudad.
	 * 
	 * @return El nombre de la ciudad.
	 */
	public String getNombreCiudad() {
		return nombreCiudad;
	}

	/**
	 * Metodo que establece el nombre de la ciudad.
	 * 
	 * @param nombreCiudad
	 * Nombre que desea dar a la ciudad.
	 */
	public void setNombreCiudad(String nombreCiudad) {
		this.nombreCiudad = nombreCiudad;
	}

	/**
	 * Metodo que devuelve la coordenada x de la ciudad en el sistema de referencia del mapa.
	 * 
	 * @return La coordenada x de la ciudad en el sistema de referencia del mapa.
	 */
	public float getCoordX() {
		return coordX;
	}

	/**
	 * Metodo que da valor a la coordenada x de la ciudad en el sistema de referencia del mapa.
	 * 
	 * @param coordX
	 * Valor para la coordenada x de la ciudad en el sistema de referencia del mapa.
	 */
	public void setCoordX(float coordX) {
		this.coordX = coordX;
	}

	/**
	 * Metodo que devuelve la coordenada x de la ciudad en el sistema de referencia del mapa.
	 * 
	 * @return La coordenada x de la ciudad en el sistema de referencia del mapa.
	 */
	public float getCoordY() {
		return coordY;
	}

	/**
	 * Metodo que da valor a la coordenada y de la ciudad en el sistema de referencia del mapa.
	 * 
	 * @param coordY
	 * Valor para la coordenada y de la ciudad en el sistema de referencia del mapa.
	 */
	public void setCoordY(float coordY) {
		this.coordY = coordY;
	}

	/**
	 * Metodo que aniade el nombre de una ciudad adyacente a la ciudad actual en un registro de ciudades adyacentes.
	 * 
	 * @param nombreCiudadAdyacente
	 * El nombre de la ciudad adyacente a la ciudad actual.
	 * @param distanciaKM
	 * La distancia en KM que separa la ciudad actual de la ciudad adyacente que se registra.
	 */
	public void aniadirCiudadAdyacente(String nombreCiudadAdyacente, float distanciaKM) {

		ciudadesAdyacentes.put(nombreCiudadAdyacente, new Float(distanciaKM));

	}

	/**
	 * Metodo que elimina una ciudad adyacente de la lista de ciudades adyacentes a esta ciudad.
	 * 
	 * @param nombreCiudadAdyacente
	 * El nombre de la ciudad adyacente que se desea eliminar de la lista de ciudades adyacentes.
	 */
	public void eliminarCiudadVecina(String nombreCiudadAdyacente) {

		ciudadesAdyacentes.remove(nombreCiudadAdyacente);

	}

	/**
	 * Metodo que devuelve el numero de ciudades adyacentes a la ciudad actual.
	 * 
	 * @return El numero de ciudades adyacenes a la ciudad actual. 0 si no hay ciudades adyacentes a la ciudad actual.
	 */
	public int numeroCiudadesAdyacentes() {

		return ciudadesAdyacentes.size();

	}

	/**
	 * Metodo que retorna el registro de ciudades adyacentes a la actual. En cada registro se puede obtener el nombre de
	 * la ciudad adyacente y la distancia en KM que separa la ciudad actual de la ciudad adyacente.
	 * 
	 * @return Registro con el nombre de las ciudades adyacentes y la distancia que separa la ciudad actual de cada
	 * ciudad adyacente. Si no hay ciudades adyacentes el registro no es null y no posee entradas.
	 * @see Map
	 */
	public Map<String, Float> obtenerCiudadesAdyacentes() {

		return ciudadesAdyacentes;

	}

}