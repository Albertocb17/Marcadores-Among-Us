package com.lesjarones.amongus.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.lesjarones.amongus.model.Jugador;
import com.lesjarones.amongus.model.Mostrar;

public class Util {
	
	static Scanner entrada = new Scanner(System.in);
	
	public static boolean preguntaBoolean(String pregunta, Object... args) {
		String respuesta;
		do {
			System.out.println(String.format(pregunta + " (S/n)", args));
			respuesta = entrada.nextLine();
		} while (!respuesta.equalsIgnoreCase("s") && !respuesta.equalsIgnoreCase("n")
				&& !respuesta.equalsIgnoreCase(""));
		return respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("");
	}
	
	public static void mostrarJugadores(List<Jugador> listaJugadores) {
		if (listaJugadores.isEmpty())
			System.out.println("No hay ningún jugador en la base de datos.");
		for (int i = 0; i < listaJugadores.size(); i++) {
			System.out.println(
					String.format("%d - %s", listaJugadores.get(i).getId(), listaJugadores.get(i).getNombre()));
		}
	}
	
	public static String pintarBlancos(String cadena, int numBlancos) {
		int tamTotal = cadena.length() + numBlancos;
		while (cadena.length() < tamTotal)
			cadena = cadena + " ";
		return cadena;
	}
	
	public static void estadisticas(List<Jugador> listaJugadores, List<Jugador> masDe100, List<Jugador> masDe500, Mostrar mostrar) {
		
		comoImpostor(listaJugadores, mostrar);
		
		System.out.println("");
		
		ganadasComoImpostor(listaJugadores, mostrar);
		
		System.out.println("");
		
		ganadasComoTripulante(listaJugadores, mostrar);
		
		if (mostrar.equals(Mostrar.Global)) {
			
			System.out.println("");
			
			if (!masDe100.isEmpty()) {
				System.out.println("Los siguientes jugadores dejan de ser noobs:");
				masDe100.forEach(jugador -> {
					System.out.println(String.format("¡Increible, %s! Más de 100 partidas, concretamente %d partidas! Dejas de ser un noob.",
							jugador.getNombre(), jugador.getPartidasJugadas()));
				});
			}
			
			System.out.println("");
			
			if (!masDe500.isEmpty()) {
				System.out.println("Los siguientes jugadores pasan a ser pros:");
				masDe500.forEach(jugador -> {
					System.out.println(String.format("Eres un puto friki, %s. Más de 500 partidas, concretamente %d partidas! Te paaaassassss!!.",
							jugador.getNombre(), jugador.getPartidasJugadas()));
				});
			}
		}

	}
	
	public static void mostrarMarcadoresPc(List<Jugador> listaJugadores, Mostrar mostrar) {
		System.out.println();
		System.out.println("***********************************************************************************");
		System.out.println("********************************* MARCADORES **************************************");
		System.out.println("***********************************************************************************");
		System.out.println("                Jugadas |    Impostor  | Ganadas Impostor |      Ganadas Tripulante");
		listaJugadores.forEach(jugador -> {
			int partidasJugadas;
			int partidasImpostor;
			int partidasGanadasImpostor;
			int partidasGanadasTripulante;
			if (mostrar.equals(Mostrar.Global)) {
				partidasJugadas = jugador.getPartidasJugadas();
				partidasImpostor = jugador.getPartidasImpostor();
				partidasGanadasImpostor = jugador.getPartidasGanadasImpostor();
				partidasGanadasTripulante = jugador.getPartidasGanadasTripulante();
			} else {
				partidasJugadas = jugador.getPartidasJugadasHoy();
				partidasImpostor = jugador.getPartidasImpostorHoy();
				partidasGanadasImpostor = jugador.getPartidasGanadasImpostorHoy();
				partidasGanadasTripulante = jugador.getPartidasGanadasTripulanteHoy();
			}
			System.out.println(pintarNombreJugadorPc(jugador) + pintarBlancos("", 3) + partidasJugadas
					+ "\t\t"  + partidasImpostor + "\t\t" + partidasGanadasImpostor
					+ "\t\t\t" + partidasGanadasTripulante);
		});
		System.out.println("***********************************************************************************");
		System.out.println("***********************************************************************************");
		System.out.println();
	}

	private static String pintarNombreJugadorPc(Jugador jugador) {
		return pintarBlancos(
				jugador.getNombre().substring(0,
						(jugador.getNombre().length() > 16 ? 16 : jugador.getNombre().length())),
				16 - jugador.getNombre()
						.substring(0, (jugador.getNombre().length() > 16 ? 16 : jugador.getNombre().length()))
						.length());
	}
	
	
	private static void comoImpostor(List<Jugador> listaJugadores, Mostrar mostrar) {
		System.out.println("Porcentaje de partidas como impostor:");
		Map<Jugador, Float> listaGanadasComoImpostor = new HashMap<>();
		listaJugadores.forEach(jugador -> {
			int partidasJugadas;
			int partidasImpostor;
			if (mostrar.equals(Mostrar.Global)) {
				partidasJugadas = jugador.getPartidasJugadas();
				partidasImpostor = jugador.getPartidasImpostor();
			} else {
				partidasJugadas = jugador.getPartidasJugadasHoy();
				partidasImpostor = jugador.getPartidasImpostorHoy();
			}
			float porcentaje = partidasImpostor * 100f / partidasJugadas;
			listaGanadasComoImpostor.put(jugador, porcentaje);
		});
		
		LinkedHashMap<Jugador, Float> listaOrdenadaGanadasComoImpostor = new LinkedHashMap<>();
		
		listaGanadasComoImpostor.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
			    .forEachOrdered(x -> listaOrdenadaGanadasComoImpostor.put(x.getKey(), x.getValue()));
		
		listaOrdenadaGanadasComoImpostor.forEach((jugador, porcentaje) -> 
			System.out.println(String.format("%s: %f", jugador.getNombre(), porcentaje))
		);
	}

	private static void ganadasComoImpostor(List<Jugador> listaJugadores, Mostrar mostrar) {
		System.out.println("Porcentaje de partidas ganadas como impostor:");
		Map<Jugador, Float> listaGanadasComoImpostor = new HashMap<>();
		listaJugadores.forEach(jugador -> {
			int partidasGanadasImpostor;
			int partidasImpostor;
			if (mostrar.equals(Mostrar.Global)) {
				partidasGanadasImpostor = jugador.getPartidasGanadasImpostor();
				partidasImpostor = jugador.getPartidasImpostor();
			} else {
				partidasGanadasImpostor = jugador.getPartidasGanadasImpostorHoy();
				partidasImpostor = jugador.getPartidasImpostorHoy();
			}
			float porcentaje = partidasGanadasImpostor * 100f / partidasImpostor ;
			listaGanadasComoImpostor.put(jugador, porcentaje);
		});
		
		LinkedHashMap<Jugador, Float> listaOrdenadaGanadasComoImpostor = new LinkedHashMap<>();
		
		listaGanadasComoImpostor.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
			    .forEachOrdered(x -> listaOrdenadaGanadasComoImpostor.put(x.getKey(), x.getValue()));
		
		listaOrdenadaGanadasComoImpostor.forEach((jugador, porcentaje) -> 
			System.out.println(String.format("%s: %f", jugador.getNombre(), porcentaje))
		);
	}
	
	private static void ganadasComoTripulante(List<Jugador> listaJugadores, Mostrar mostrar) {
		System.out.println("Porcentaje de partidas ganadas como tripulante:");
		Map<Jugador, Float> listaGanadasComoImpostor = new HashMap<>();
		listaJugadores.forEach(jugador -> {
			int partidasGanadasTripulante;
			int partidasJugadas;
			int partidasImpostor;
			if (mostrar.equals(Mostrar.Global)) {
				partidasGanadasTripulante = jugador.getPartidasGanadasTripulante();
				partidasJugadas = jugador.getPartidasJugadas();
				partidasImpostor = jugador.getPartidasImpostor();
			} else {
				partidasGanadasTripulante = jugador.getPartidasGanadasTripulanteHoy();
				partidasJugadas = jugador.getPartidasJugadasHoy();
				partidasImpostor = jugador.getPartidasImpostorHoy();
			}
			float porcentaje = partidasGanadasTripulante * 100f / (partidasJugadas - partidasImpostor);
			listaGanadasComoImpostor.put(jugador, porcentaje);
		});
		
		LinkedHashMap<Jugador, Float> listaOrdenadaGanadasComoImpostor = new LinkedHashMap<>();
		
		listaGanadasComoImpostor.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
			    .forEachOrdered(x -> listaOrdenadaGanadasComoImpostor.put(x.getKey(), x.getValue()));
		
		listaOrdenadaGanadasComoImpostor.forEach((jugador, porcentaje) -> 
			System.out.println(String.format("%s: %f", jugador.getNombre(), porcentaje))
		);
	}

}
