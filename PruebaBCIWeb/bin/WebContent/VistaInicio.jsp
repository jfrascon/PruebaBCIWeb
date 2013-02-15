<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="javax.sql.DataSource, javax.naming.*, java.net.*, modelo.BaseDatos"%>

<%!String rutaCiudades = "";%>

<%
	//Necesitamos una conexion del 'pool' para poder consultar cierta informacion de la base de datos.
	Context initCtx = new InitialContext();
	//Buscar el pool de conexiones en JNDI.
	Context envCtx = (Context) initCtx.lookup("java:comp/env");
	DataSource servicioConexiones = (DataSource) envCtx.lookup("jdbc/pruebabiicode");
	BaseDatos bd = new BaseDatos(servicioConexiones);
	String[] nombreCiudades = bd.obtenerNombreCiudades();
	bd.cerrarConexionConBD();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Prueba BCI Web</title>
<style>
body {
	background-image: url("figuras/textured.jpg");
}

h1 {
	text-align: center;
	color: rgba(0, 0, 0, 0.6);
	text-shadow: 2px 4px 3px rgba(0, 0, 0, 0.3);
}

div.entradas {
	margin-left: auto;
	margin-right: auto;
	width: 40em;
}

div.input {
	margin-left: auto;
	margin-right: auto;
	width: 14em;
}

div.ruta {
	margin-left: auto;
	margin-right: auto;
	width: 40em;
}

div.espania {
	margin-left: auto;
	margin-right: auto;
	width: 50em;
}
</style>
</head>
<body>
	<h1>CALCULA LA RUTA MÁS CORTA ENTRE DOS CIUDADES DE ESPAÑA</h1>
	<br />
	<br />
	<br />
	<br />
	<br />
	<%
		if (nombreCiudades.length != 0) {
	%>
	<form name="CalcularRuta" action="/PruebaBCIWeb/CalcularRuta"
		method="post">

		<div class="entradas">
			<label for="etiquetaCiudadOrigen">Ciudad Origen</label> <select
				name="nombreCiudadOrigen">
				<%
					for (int i = 0; i < nombreCiudades.length; i++) {
				%>
				<option value="<%=nombreCiudades[i]%>"><%=nombreCiudades[i]%></option>
				<%
					}
				%>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label for="etiquetaCiudadDestino">Ciudad Destino</label> <select
				name="nombreCiudadDestino">
				<%
					for (int i = 0; i < nombreCiudades.length; i++) {
				%>
				<option value="<%=nombreCiudades[i]%>"><%=nombreCiudades[i]%></option>
				<%
					}
				%>
			</select>
		</div>
		<br /> <br />
		<div class="input">
			<input type="submit" value="Submit" />
		</div>
	</form>
	<%
		rutaCiudades = (String) request.getAttribute("ruta");
			if (rutaCiudades != null) {
	%>
	<br />
	<br />
	<br />
	<div class="ruta">
		<label>Ruta: <%=rutaCiudades%></label>
	</div>

	<%
		}
		} else {
	%>
	<label for="NO HAY CIUDADES EN EL MAPA (X_X)"></label>

	<%
		}
	%>

</body>
</html>