package modelo;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Clase que representa un grafo de nodos del tipo ciudad.
 * 
 * @author jfrascon
 * @version "%I%, %G%
 */

public class Mapa {

	private Map<String, Ciudad> ciudades = null;

	/**
	 * Constructor de la clase.
	 */
	public Mapa() {

		ciudades = new HashMap<String, Ciudad>();
	}

	/**
	 * Metodo que aniade un ciudad (nodo) al mapa (grafo).
	 * 
	 * @param ciudad
	 * Ciudad que forma parte del mapa.
	 * @see Ciudad
	 */
	public void aniadirCiudad(Ciudad ciudad) {
		// Si la clave 'ciudad.getNombreCiudad()' existe en el 'Map ciudades'
		// antes de la llamada a este metodo su valor asociado es machacado al
		// ejecutar el metodo put.
		ciudades.put(ciudad.getNombreCiudad(), ciudad);

	}

	/**
	 * Metodo que aniade una via de comunicacion (arista) entre dos ciudades adyacentes.
	 * 
	 * @param nombreCiudadA
	 * Nombre de la ciudad que se haya en un extremo de la carretera.
	 * @param nombreCiudadB
	 * Nombre de la ciudad que se haya en el otro extremo de la carretera.
	 */
	public void aniadirCarretera(String nombreCiudadA, String nombreCiudadB) {

		// Una carretera comunica dos ciudades que existen en el mapa.
		// Si una de las dos ciudades no existe finaliza el metodo.
		if (nombreCiudadA == null || nombreCiudadB == null) {
			return;
		}
		Ciudad ciudadA = ciudades.get(nombreCiudadA);
		Ciudad ciudadB = ciudades.get(nombreCiudadB);
		if (ciudadA == null || ciudadB == null) {
			return;
		}
		// Distancia entre dos ciudades. Teorema de Pitagoras.
		float deltaX = ciudadA.getCoordX() - ciudadB.getCoordX();
		float deltaY = ciudadA.getCoordY() - ciudadB.getCoordY();
		float distanciaKM = (float) Math.hypot(deltaX, deltaY);

		// El grafo es bidireccional.
		// La ciudad A esta unida con la ciudad B (B es adyacente a A).
		// la ciudad B esta unida con la ciudad A (A es adyacente a B).
		ciudadA.aniadirCiudadAdyacente(nombreCiudadB, distanciaKM);
		ciudadB.aniadirCiudadAdyacente(nombreCiudadA, distanciaKM);

		// Aniadir al mapa las ciudades con los valores modificados.
		ciudades.put(nombreCiudadA, ciudadA);
		ciudades.put(nombreCiudadB, ciudadB);

	}

	/**
	 * Metodo que eliminar una ciudad del mapa.
	 * 
	 * @param nombreCiudad
	 * Nombre de la ciudad que se desea eliminar del mapa.
	 */

	public void eliminarCiudad(String nombreCiudad) {

		ciudades.remove(nombreCiudad);

	}

	/**
	 * Metodo que devulve el numero de ciudades que constituyen el mapa.
	 * 
	 * @return Numero de ciudades constitutivas del mapa. 0 en caso de que no existan ciudades que constituyan el mapa.
	 */
	public int numeroCiudadesMapa() {

		return ciudades.size();
	}

	/**
	 * Metodo que devuelve un registro con las ciudades constitutivas del mapa. Cada registro consta de un nombre de
	 * ciudad y un objeto ciudad con informacion adicional.
	 * 
	 * @return Registro con todas las ciudades que forman el mapa. Si no hay ciudades que constituyen el mapa el
	 * registro devuelto tiene 0 entradas (no es null).
	 */
	public Map<String, Ciudad> obtenerCiudades() {

		return ciudades;
	}

	/**
	 * Metodo que permite obtener una ciudad del mapa a partir de su nombre.
	 * 
	 * @param nombreCiudad
	 * Nombre de la ciudad que se quiere obtener del mapa.
	 * @return Ciudad del mapa cuyo nombre coincide con el parametro pasado a la funcion. Null en caso de que la ciudad
	 * no exista en el mapa.
	 */
	public Ciudad obtenerCiudad(String nombreCiudad) {

		return ciudades.get(nombreCiudad);
	}

	/**
	 * Metodo que retorna una cadena de texto que contiene las ciudades por las que se debe pasar para ir desde una
	 * ciudad origen hasta una ciudad destino recorriendo el camino con el nœmero m’nimo de kil—metros.
	 * 
	 * @param ciudadesDijkstra
	 * Registro con tantas entradas como ciudades hay en el mapa. Cada entrada contiene el nombre de dos ciudades del
	 * mapa y un numero real. La primera cadena de texto representa el nombre de la ciudad que debe ser alcanzada desde
	 * la ciudad cuyo nombre esta representado en la segunda cadena de texto, siguiendo un criterio de camino minimo. El
	 * numero real representa la distancia minima existente entre la ciudad origen y la ciudad cuyo nombre aparece en el
	 * primer campo del registro.
	 * @param nombreCiudadDestino
	 * Nombre de la ciudad a la que se quiere llegar.
	 * 
	 * @return Cadena de texto que contiene la secuencia de ciudades que hay que atravesar para ir desde la ciudad
	 * origen hasta la ciudad destinto usando un criterio de distancia minima recorrida.
	 */
	public String obtenerCamino(Map<String, String> ciudadesDijkstra, String nombreCiudadDestino) {

		// La ciudad origen y la ciudad destino deben existir.
		if (ciudadesDijkstra == null || nombreCiudadDestino == null || !ciudadesDijkstra.containsKey(nombreCiudadDestino)) {

			return "No existe un camino hasta la ciudad " + nombreCiudadDestino
					+ ". Asegurese de que la ciudad origen y la ciudad destinto existen en el mapa";

		}
		// Guardar el nombre de la ciudad destino y recorrer el registro 'ciudadesDijkstra' en orde inverso, i.e desde
		// la ciudad destino hasta la ciudad origen para reconstruir la secuencia de ciudades intermedias.
		String camino = nombreCiudadDestino;
		String nombreCiudadPadreDistanciaDesdeCiudadOrigen[] = ciudadesDijkstra.get(nombreCiudadDestino).split(" ");
		String nombreCiudadPadre = nombreCiudadPadreDistanciaDesdeCiudadOrigen[0];
		float distanciaDesdeCiudadOrigen = Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]);
		// Aquella ciudad cuya ciudad padre se llame '?' constituye el origen del camino.
		while (nombreCiudadPadre.compareTo("?") != 0) {
			camino = nombreCiudadPadre + " > " + camino;
			nombreCiudadPadre = (String) ciudadesDijkstra.get(nombreCiudadPadre).split(" ")[0];
		}
		return camino + " ( " + distanciaDesdeCiudadOrigen + " km ) ";
	}

	/**
	 * Metodo que aplica el algoritmo Dijkstra en el grafo de ciudades para encontrar el camino de tamanio minimo entre
	 * una ciudad origen y cada ciudad restante del mapa.
	 * 
	 * @param nombreCiudadOrigen
	 * Nombre de la ciudad origen.
	 * @return Registro con tantas entradas como ciudades hay en el mapa. Cada entrada contiene el nombre de dos
	 * ciudades del mapa y un numero real. La primera cadena de texto representa el nombre de la ciudad que debe ser
	 * alcanzada desde la ciudad cuyo nombre esta representado en la segunda cadena de texto, siguiendo un criterio de
	 * camino minimo. El numero real representa la distancia minima existente entre la ciudad origen y la ciudad cuyo
	 * nombre aparece en el primer campo del registro.
	 */
	public Map<String, String> dijkstra(String nombreCiudadOrigen) {

		// Gestion de errores.
		// Para trazar un camino entre dos ciudades es necesario que la ciudad origen exista en el mapa.
		if (nombreCiudadOrigen == null || !ciudades.containsKey(nombreCiudadOrigen)) {

			return null;
		}

		int numeroCiudadesVisitadas = 0;
		String nombreCiudadPadreDistanciaDesdeCiudadOrigen[] = null;
		String nombreCiudadSeleccionada = "";
		float distanciaDesdeCiudadOrigen = 0.0f;
		float distanciaDesdeCiudadOrigenAnterior = 0.0f;
		float distanciaDesdeCiudadOrigenMinima = 0.0f;

		// boolean flagDepuracion = false;

		Map<String, String> ciudadesDijkstra = new HashMap<String, String>();
		Map<String, String> ciudadesVisitadas = new HashMap<String, String>();
		// Codigo nemotecnico SS -> String String
		Iterator<Entry<String, String>> itSS = null;
		Entry<String, String> entrySS = null;

		// Codigo nemotecnico SC -> String Ciudad
		Iterator<Entry<String, Ciudad>> itSC = ciudades.entrySet().iterator();
		Entry<String, Ciudad> entrySC = null;

		Map<String, Float> ciudadesAdyacentes = null;
		// Codigo nemotecnico SFCA -> String Float Ciudades Adyacentes.
		Iterator<Entry<String, Float>> itSFCA = null;
		Entry<String, Float> entrySFCA = null;

		// Marcar todos las ciudades del mapa como no procesadas al principio del
		// algoritmo.
		// Ciudad no procesada implica: nombreCiudadPadre: '?'. distanciaMinimaDesdeOrigen = -1.
		while (itSC.hasNext()) {

			entrySC = itSC.next();
			ciudadesDijkstra.put(entrySC.getKey(), "? -1");
		}

		// ----- ----- ----- ----- ----- CODIGO DEPURACION ----- ----- ----- ----- -----
		// System.out.println("\n");
		// itSS = ciudadesDijkstra.entrySet().iterator();
		// while (itSS.hasNext()) {
		// entrySS = itSS.next();
		// nombreCiudadPadreDistanciaDesdeCiudadOrigen = entrySS.getValue().split(" ");
		// flagDepuracion = ciudadesVisitadas.containsKey(entrySS.getKey());
		// if (flagDepuracion) {
		// System.out.println("* " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
		// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
		// } else {
		// System.out.println("  " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
		// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
		// }
		// }
		// ----- ----- ----- ----- ----- ----- ----- ----- ----- -----

		// La ciudad origen no tiene ciudad padre ('?') y dista 0 km de ella misma.
		ciudadesDijkstra.put(nombreCiudadOrigen, "? " + new Float(0).toString());
		// Registrar la ciudad origen como visitada.
		ciudadesVisitadas.put(nombreCiudadOrigen, "");
		numeroCiudadesVisitadas = 1;

		// ----- ----- ----- ----- ----- CODIGO DEPURACION ----- ----- ----- ----- -----
		// System.out.println("\n");
		// itSS = ciudadesDijkstra.entrySet().iterator();
		// while (itSS.hasNext()) {
		// entrySS = itSS.next();
		// nombreCiudadPadreDistanciaDesdeCiudadOrigen = entrySS.getValue().split(" ");
		// flagDepuracion = ciudadesVisitadas.containsKey(entrySS.getKey());
		// if (flagDepuracion) {
		// System.out.println("* " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
		// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
		// } else {
		// System.out.println("  " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
		// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
		// }
		// }
		// ----- ----- ----- ----- ----- ----- ----- ----- ----- -----

		// Obtener las ciudades adyacentes a la ciudad origen. Cada ciudad es asociada con la ciudad padre, en este caso
		// la ciudad origen, y con la distancia minima desde la ciudad origen hasta ella.
		ciudadesAdyacentes = ciudades.get(nombreCiudadOrigen).obtenerCiudadesAdyacentes();
		itSFCA = ciudadesAdyacentes.entrySet().iterator();
		while (itSFCA.hasNext()) {
			entrySFCA = itSFCA.next();
			ciudadesDijkstra.put(entrySFCA.getKey(), nombreCiudadOrigen + " " + entrySFCA.getValue().toString());
		}

		// ----- ----- ----- ----- ----- CODIGO DEPURACION ----- ----- ----- ----- -----
		// System.out.println("\n");
		// itSS = ciudadesDijkstra.entrySet().iterator();
		// while (itSS.hasNext()) {
		// entrySS = itSS.next();
		// nombreCiudadPadreDistanciaDesdeCiudadOrigen = entrySS.getValue().split(" ");
		// flagDepuracion = ciudadesVisitadas.containsKey(entrySS.getKey());
		// if (flagDepuracion) {
		// System.out.println("* " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
		// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
		// } else {
		// System.out.println("  " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
		// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
		// }
		// }
		// ----- ----- ----- ----- ----- ----- ----- ----- ----- -----

		// Bucle principal para procesar todos las ciudades del mapa.
		while (numeroCiudadesVisitadas < ciudades.size() - 1) {
			// Seleccionar en 'ciudadesDijkstra' la ciudad descubierta no visitada mas cercana a la ciuadad origen.
			distanciaDesdeCiudadOrigenMinima = Float.POSITIVE_INFINITY;
			itSS = ciudadesDijkstra.entrySet().iterator();
			while (itSS.hasNext()) {
				entrySS = itSS.next();
				nombreCiudadPadreDistanciaDesdeCiudadOrigen = entrySS.getValue().split(" ");
				distanciaDesdeCiudadOrigen = Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]);

				// distanciaDesdeCiudadOrigen != -1 significa ciudad descubierta.
				// distanciaDesdeCiudadOrigen = -1 significa ciudad NO descubierta aun.
				if (distanciaDesdeCiudadOrigen != -1 && !ciudadesVisitadas.containsKey(entrySS.getKey())
						&& distanciaDesdeCiudadOrigen < distanciaDesdeCiudadOrigenMinima) {

					// Anotar cual de las ciudades descubiertas esta a la menor distancia de ciudad origen.
					distanciaDesdeCiudadOrigenMinima = distanciaDesdeCiudadOrigen;
					nombreCiudadSeleccionada = entrySS.getKey();
				}

			}

			// System.out.println("\n----- ----- ----- ----- ----- ");
			// System.out.println("\n" + nombreCiudadSeleccionada + " " + distanciaDesdeCiudadOrigenMinima);

			// Marcar la ciudad descubierta como visitada.
			ciudadesVisitadas.put(nombreCiudadSeleccionada, "");
			numeroCiudadesVisitadas += 1;

			// ----- ----- ----- ----- ----- CODIGO DEPURACION ----- ----- ----- ----- -----
			// System.out.println("\n");
			// itSS = ciudadesDijkstra.entrySet().iterator();
			// while (itSS.hasNext()) {
			// entrySS = itSS.next();
			// nombreCiudadPadreDistanciaDesdeCiudadOrigen = entrySS.getValue().split(" ");
			// flagDepuracion = ciudadesVisitadas.containsKey(entrySS.getKey());
			// if (flagDepuracion) {
			// System.out.println("* " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
			// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
			// } else {
			// System.out.println("  " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
			// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
			// }
			// }
			// ----- ----- ----- ----- ----- ----- ----- ----- ----- -----

			// Para cada ciudad adyacente no visitada de la ciudad seleccionada decidir si resulta mejor el camino
			// calculado antes o si es mejor usar el camino que lleva a la ciudad actual seleccionada y a continuacion
			// usar la carretera que las une.

			// System.out.println("\nObtener las ciudades adyacentes a " + nombreCiudadSeleccionada);

			ciudadesAdyacentes = ciudades.get(nombreCiudadSeleccionada).obtenerCiudadesAdyacentes();
			itSFCA = ciudadesAdyacentes.entrySet().iterator();
			while (itSFCA.hasNext()) {
				entrySFCA = itSFCA.next();
				// Reseteo del valor de la variable 'distanciaDesdeCiudadOrigen' que debe almacenar la distancia desde
				// la ciudad origen hasta la ciudad seleccionada, antes de averiguar cuanto camino existe entre esta
				// ultima y
				// sus ciudades adyacentes. El valor que debe guardarse en la variable 'distanciaDesdeCiudadOrigen' se
				// hallo en el bucle anterior y reside en la variable 'distanciaDesdeCiudadOrigenMinima'.
				distanciaDesdeCiudadOrigen = distanciaDesdeCiudadOrigenMinima;

				// System.out.print(entrySFCA.getKey() + ": ");

				// Ciudad adyacente no visitada
				if (!ciudadesVisitadas.containsKey(entrySFCA.getKey())) {

					// System.out.print("No visitada antes, ");

					distanciaDesdeCiudadOrigen += entrySFCA.getValue().floatValue();
					distanciaDesdeCiudadOrigenAnterior = Float.parseFloat(ciudadesDijkstra.get(entrySFCA.getKey()).split(" ")[1]);

					// System.out.println(distanciaDesdeCiudadOrigenAnterior + " " + distanciaDesdeCiudadOrigen);

					// Si distanciaDesdeCiudadOrigenAnterior != -1 entonces la ciudad adyacente que se esta procesando
					// ya fue considerada como ciudad adyacente en un paso anterior del algoritmo y tendra una distancia
					// asignada.
					if (distanciaDesdeCiudadOrigenAnterior != -1) {
						// Reemplazar en caso necesario.
						if (distanciaDesdeCiudadOrigen < distanciaDesdeCiudadOrigenAnterior) {
							ciudadesDijkstra.put(entrySFCA.getKey(), nombreCiudadSeleccionada + " " + new Float(distanciaDesdeCiudadOrigen).toString());
						}
					}
					// Asignar primer valor en caso necesario.
					else {
						ciudadesDijkstra.put(entrySFCA.getKey(), nombreCiudadSeleccionada + " " + new Float(distanciaDesdeCiudadOrigen).toString());
					}
				}
				// else {
				// System.out.println("Si visitada antes.");
				// }
			}

			// ----- ----- ----- ----- ----- CODIGO DEPURACION ----- ----- ----- ----- -----
			// System.out.println("\n");
			// itSS = ciudadesDijkstra.entrySet().iterator();
			// while (itSS.hasNext()) {
			// entrySS = itSS.next();
			// nombreCiudadPadreDistanciaDesdeCiudadOrigen = entrySS.getValue().split(" ");
			// flagDepuracion = ciudadesVisitadas.containsKey(entrySS.getKey());
			// if (flagDepuracion) {
			// System.out.println("* " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
			// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
			// } else {
			// System.out.println("  " + entrySS.getKey() + " " + nombreCiudadPadreDistanciaDesdeCiudadOrigen[0] + " "
			// + Float.parseFloat(nombreCiudadPadreDistanciaDesdeCiudadOrigen[1]));
			// }
			// }
			// ----- ----- ----- ----- ----- ----- ----- ----- ----- -----

		}

		return ciudadesDijkstra;
	}

	// NO FUNCIONO EN VARIAS OCASIONES QUE LO PROBE Y LO DEJE SIN TERMINAR.
	public String algoritmoAEstrella(String nombreCiudadOrigen, String nombreCiudadDestinto) {

		if (nombreCiudadOrigen == null || nombreCiudadDestinto == null || !ciudades.containsKey(nombreCiudadOrigen)
				|| !ciudades.containsKey(nombreCiudadDestinto)) {

			return "El origen y/o el destino no se encuentran en el mapa";
		}

		if (nombreCiudadOrigen.compareTo(nombreCiudadDestinto) == 0) {

			return "Se encuentra en su destinto.";

		}

		// Lista abierta.
		Map<String, Float> ciudadesPendientes = new HashMap<String, Float>();

		// Lista cerrada.
		Map<String, Float> ciudadesVisitadas = new HashMap<String, Float>();

		Map<String, Float> ciudadesVecinas = new HashMap<String, Float>();

		Iterator<Entry<String, Float>> it = null;
		Entry<String, Float> ciudadApuntada = null;

		// g
		float g_distanciaAcumuladaTotal = 0.0f;
		// h
		float distanciaAcumulada = 0.0f;

		float costeCiudad = 0.0f;

		float costeMinimo = 0.0f;

		float deltaX = 0.0f;

		float deltaY = 0.0f;

		float coordXCiudadDestino = ciudades.get(nombreCiudadDestinto).getCoordX();

		float coordYCiudadDestino = ciudades.get(nombreCiudadDestinto).getCoordY();

		String nombreCiudadEscogida = "";

		// Aniadir la ciudad origen a la lista abierta.
		ciudadesPendientes.put(nombreCiudadOrigen, g_distanciaAcumuladaTotal);

		// Recorrer la lista abierta mientras posea ciudades en busca de aquella
		// que posea el menor coste.
		while (!ciudadesPendientes.isEmpty()) {

			it = ciudadesPendientes.entrySet().iterator();

			costeMinimo = Float.MAX_VALUE;

			// Buscar en la lista abierta la ciudad de menor corte.
			while (it.hasNext()) {

				ciudadApuntada = it.next();

				deltaX = ciudades.get(ciudadApuntada.getKey()).getCoordX() - coordXCiudadDestino;

				deltaY = ciudades.get(ciudadApuntada.getKey()).getCoordY() - coordYCiudadDestino;

				costeCiudad = ciudadApuntada.getValue() + (float) Math.hypot(deltaX, deltaY);

				if (costeCiudad < costeMinimo) {

					costeMinimo = costeCiudad;

					nombreCiudadEscogida = ciudadApuntada.getKey();
				}
			}

			if (nombreCiudadEscogida.compareTo(nombreCiudadDestinto) == 0) {

				// AQUI ACABA LA FUNCION.
				return "";
			} else {

				g_distanciaAcumuladaTotal = ciudadApuntada.getValue();

				// Eliminar la ciudad apuntada de la lista abierta.
				ciudadesPendientes.remove(nombreCiudadEscogida);

				// Aniadir la ciudad apuntada de la lista cerrada, junto con la
				// distancia
				// que existe entre la ciudad de origen y ella,
				ciudadesVisitadas.put(nombreCiudadEscogida, g_distanciaAcumuladaTotal);

				// Las ciudades vecinas de la ciudad escogida se deben aniadir a
				// la lista abierta excepto aquellas que se hayan en la lista
				// cerrada.
				ciudadesVecinas = ciudades.get(nombreCiudadEscogida).obtenerCiudadesAdyacentes();

				it = ciudadesVecinas.entrySet().iterator();

				while (it.hasNext()) {

					ciudadApuntada = it.next();

					// Comprobar si la ciudad vecina apuntada se haya en la
					// lista cerrada.
					// En caso afirmativo no hacer nada.
					// En caso negativo continuar con las comprobaciones.
					if (!ciudadesVisitadas.containsKey(ciudadApuntada.getKey())) {

						// Distancia desde la ciudad origen hasta la ciudad
						// vecina apuntada.
						distanciaAcumulada = g_distanciaAcumuladaTotal + ciudadApuntada.getValue();

						// Comprobar si la lista abierta cotiene la ciudad
						// vecina apuntada.
						// En caso afirmativo continuar con las comprobaciones.
						// En caso negativo, aniadir la ciudad vecina apuntada a
						// la lista abierta.
						if (ciudadesPendientes.containsKey(ciudadApuntada.getKey())) {

							// La ciudad vecina apuntada se haya en la lista
							// abierta.
							// Comprobar si la distancia desde la ciudad origen
							// hasta la ciudad vecina apuntada ahora es menor
							// que la distancia que se tiene apuntada en lista
							// abierta.
							// En caso afirmativo registrar la nueva distancia.
							// En caso negativo ignorar el resultado.
							if (distanciaAcumulada < ciudadesPendientes.get(ciudadApuntada.getKey())) {

								ciudadesPendientes.put(ciudadApuntada.getKey(), distanciaAcumulada);

							}

						} else {

							ciudadesPendientes.put(ciudadApuntada.getKey(), distanciaAcumulada);

						}

					}

				}

			}

		}

		return "No existe ruta hasta el destino";

	}

}
