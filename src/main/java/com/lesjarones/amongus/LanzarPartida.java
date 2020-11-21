package com.lesjarones.amongus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lesjarones.amongus.model.Jugador;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class LanzarPartida {

	@Autowired
	private PrepararPartida modificarPartida;

	Scanner entrada = new Scanner(System.in);

	int numImpostores;

	public void lanzarPartida(List<Jugador> listaJugadores) {

		numeroImpostores(listaJugadores);

		String otraPartida;
		do {
			System.out.println("*******************************************************");
			System.out.println("****************** Comienza la partida ****************");
			System.out.println("*******************************************************");

			System.out.println("Pulsa intro al finalizar la partida");
			entrada.nextLine();

			// Se suma 1 al numero de partidas jugadas
			listaJugadores.stream().filter(Jugador::isActivo)
					.forEach(jugador -> jugador.setPartidasJugadas(jugador.getPartidasJugadas() + 1));

			System.out.println("¿Quienes eran los impostores? Seleccionar un número y pulsar intro");
			mostrarJugadores(listaJugadores.stream().filter(Jugador::isActivo).collect(Collectors.toList()));
			List<Integer> listaImpostores = new ArrayList<Integer>();
			for (int i = 0; i < numImpostores; i++) {
				int numJugador = entrada.nextInt();
				listaImpostores.add(numJugador);
			}
			entrada.nextLine();

			// Se suma 1 al numero de partidas jugadas como impostor
			listaJugadores.stream().filter(jugador -> listaImpostores.contains(jugador.getId())).forEach(jugador -> {
				jugador.setPartidasImpostor(jugador.getPartidasImpostor() + 1);
			});

			String ganadoImpostores;
			do {
				System.out.println("¿Han ganado los impostores? (S/n)");
				ganadoImpostores = entrada.nextLine();
			} while (!ganadoImpostores.equalsIgnoreCase("s") && !ganadoImpostores.equalsIgnoreCase("n")
					&& !ganadoImpostores.equalsIgnoreCase(""));

			// Si los impostores ganan se suma 1 al numero de partidas ganadas como impostor
			if (ganadoImpostores.equalsIgnoreCase("s") || ganadoImpostores.equalsIgnoreCase("")) {
				listaJugadores.stream().filter(jugador -> listaImpostores.contains(jugador.getId())).forEach(
						jugador -> jugador.setPartidasGanadasImpostor(jugador.getPartidasGanadasImpostor() + 1));
			} else {
				listaJugadores.stream().filter(Jugador::isActivo)
						.filter(jugador -> !listaImpostores.contains(jugador.getId())).forEach(jugador -> jugador
								.setPartidasGanadasTripulante(jugador.getPartidasGanadasTripulante() + 1));
			}

			mostrarMarcadores(listaJugadores);

			do {
				System.out.println("¿Se juega otra partida? (S/n)");
				otraPartida = entrada.nextLine();
			} while (!otraPartida.equalsIgnoreCase("s") && !otraPartida.equalsIgnoreCase("n")
					&& !otraPartida.equalsIgnoreCase(""));

			if (otraPartida.equalsIgnoreCase("s") || otraPartida.equalsIgnoreCase("")) {
				// Comprobar si hay cambios antes de empezar la siguiente partida
				String cambios;
				do {
					System.out.println("¿Hay cambios antes de comenzar la partida? (S/n)");
					cambios = entrada.nextLine();
				} while (!cambios.equalsIgnoreCase("s") && !cambios.equalsIgnoreCase("n")
						&& !cambios.equalsIgnoreCase(""));
				if (cambios.equalsIgnoreCase("s") || cambios.equalsIgnoreCase("")) {
					listaJugadores = modificarPartida.corregirJugadores(listaJugadores);
				}

				// Cambiar el numero de impostores
				String cambioNumImpostores;
				do {
					System.out.println("¿Cambiar el numero de impostores? (S/n)");
					cambioNumImpostores = entrada.nextLine();
				} while (!cambioNumImpostores.equalsIgnoreCase("s") && !cambioNumImpostores.equalsIgnoreCase("n")
						&& !cambioNumImpostores.equalsIgnoreCase(""));
				if (cambioNumImpostores.equalsIgnoreCase("s") || cambioNumImpostores.equalsIgnoreCase("")) {
					numeroImpostores(listaJugadores);
				}
			}

		} while (otraPartida.isEmpty() || otraPartida.equalsIgnoreCase("S"));

		mostrarMarcadoresPc(listaJugadores);
		
		agradecimientosPorUtilizarBot();

	}

	private void agradecimientosPorUtilizarBot() {
		System.out.println();
		System.out.println();
		System.out.println("MUSHA GRASIA POR UTILISÁ ER BOT PAR AMONGA");
		System.out.println("HASIDO POR ER TOFU PA LES JARONES");
		System.out.println();
		System.out.println();
	}

	private void numeroImpostores(List<Jugador> listaJugadores) {
		String confirmar;
		boolean numeroIncorrectoImpostores;;
		do {
			System.out.println("¿Cuantos impostores hay en la partida? Seleccionar número");
			numImpostores = entrada.nextInt();
			entrada.nextLine();
			do {
				System.out.println(String.format("¿Estas seguro de que hay %d impostores? (S/n)", numImpostores));
				confirmar = entrada.nextLine();
			} while (!confirmar.equalsIgnoreCase("s") && !confirmar.equalsIgnoreCase("n")
					&& !confirmar.equalsIgnoreCase(""));
			
			numeroIncorrectoImpostores = numImpostores > 3 || numImpostores < 1 
					|| numImpostores >= listaJugadores.size();
			
			if(numeroIncorrectoImpostores) {
				System.out.println("No puede haber menos de 1 impostor, ni mas de 3");
				System.out.println("El número de impostores debe ser menor que el número de jugadores");
			}
			
		} while (confirmar.equalsIgnoreCase("n") || numeroIncorrectoImpostores);
	}

	private void mostrarJugadores(List<Jugador> listaJugadores) {
		for (int i = 0; i < listaJugadores.size(); i++) {
			System.out.println(
					String.format("%d - %s", listaJugadores.get(i).getId(), listaJugadores.get(i).getNombre()));
		}
	}
	
	private void mostrarMarcadores(List<Jugador> listaJugadores) {
		System.out.println();
		System.out.println("********************************");
		System.out.println("********* MARCADORES ***********");
		System.out.println("********************************");
		System.out.println("            J | I | GI | GT");
		listaJugadores.forEach(jugador -> System.out.println(pintarNombreJugador(jugador) + pintarBlancos("", 2)
				+ jugador.getPartidasJugadas() + pintarBlancos("", 3) + jugador.getPartidasImpostor()
				+ pintarBlancos("", 3) + jugador.getPartidasGanadasImpostor() + pintarBlancos("", 4)
				+ jugador.getPartidasGanadasTripulante()));
		System.out.println("********************************");
		System.out.println("********************************");
		System.out.println("* J -> Patidas jugadas");
		System.out.println("* I -> Patidas impostor");
		System.out.println("* GI -> Ganadas impostor");
		System.out.println("* GT -> Ganadas tripulante");
		System.out.println();
	}
	
	private String pintarNombreJugador(Jugador jugador) {
		return pintarBlancos(
				jugador.getNombre().substring(0,
						(jugador.getNombre().length() > 10 ? 10 : jugador.getNombre().length())),
				10 - jugador.getNombre()
						.substring(0, (jugador.getNombre().length() > 10 ? 10 : jugador.getNombre().length()))
						.length());
	}

	private void mostrarMarcadoresPc(List<Jugador> listaJugadores) {
		System.out.println();
		System.out.println("***************************************************************************");
		System.out.println("********************************* MARCADORES ******************************");
		System.out.println("***************************************************************************");
		System.out.println("                Jugadas | Impostor | Ganadas Impostor | Ganadas Tripulante");
		listaJugadores.forEach(jugador -> System.out.println(pintarNombreJugadorPc(jugador) + pintarBlancos("", 3)
				+ jugador.getPartidasJugadas() + pintarBlancos("", 10) + jugador.getPartidasImpostor()
				+ pintarBlancos("", 14) + jugador.getPartidasGanadasImpostor() + pintarBlancos("", 18)
				+ jugador.getPartidasGanadasTripulante()));
		System.out.println("***************************************************************************");
		System.out.println("***************************************************************************");
		System.out.println();
	}

	private String pintarNombreJugadorPc(Jugador jugador) {
		return pintarBlancos(
				jugador.getNombre().substring(0,
						(jugador.getNombre().length() > 16 ? 16 : jugador.getNombre().length())),
				16 - jugador.getNombre()
						.substring(0, (jugador.getNombre().length() > 16 ? 16 : jugador.getNombre().length()))
						.length());
	}

	private String pintarBlancos(String cadena, int numBlancos) {
		int tamTotal = cadena.length() + numBlancos;
		while (cadena.length() < tamTotal)
			cadena = cadena + " ";
		return cadena;
	}

}
