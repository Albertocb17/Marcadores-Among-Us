package com.lesjarones.amongus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lesjarones.amongus.model.Jugador;
import com.lesjarones.amongus.model.Mostrar;
import com.lesjarones.amongus.service.JugadorService;
import com.lesjarones.amongus.util.Util;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class LanzarPartida {

	@Autowired
	private PrepararPartida modificarPartida;

	@Autowired
	private JugadorService service;

	Scanner entrada = new Scanner(System.in);

	int numImpostores;

	public void lanzarPartida() {

		numeroImpostores(service.jugadoresActivos());

		boolean otraPartida;
		do {
			System.out.println("*******************************************************");
			System.out.println("****************** Comienza la partida ****************");
			System.out.println("*******************************************************");

			System.out.println("Pulsa intro al finalizar la partida");
			entrada.nextLine();

			// Se suma 1 al numero de partidas jugadas
			service.jugadoresActivos().forEach(jugador -> {
				jugador.setPartidasJugadasHoy(jugador.getPartidasJugadasHoy() + 1);
				service.modificarJugador(jugador);
			});

			System.out.println("¿Quienes eran los impostores? Seleccionar un número y pulsar intro");
			Util.mostrarJugadores(service.jugadoresActivos());
			List<Long> listaImpostores = new ArrayList<Long>();
			for (int i = 0; i < numImpostores; i++) {
				long numJugador = entrada.nextLong();
				listaImpostores.add(numJugador);
			}
			entrada.nextLine();

			// Se suma 1 al numero de partidas jugadas como impostor
			service.jugadoresActivos().stream().filter(jugador -> listaImpostores.contains(jugador.getId()))
					.forEach(jugador -> {
						jugador.setPartidasImpostorHoy(jugador.getPartidasImpostorHoy() + 1);
						service.modificarJugador(jugador);
					});

			// Si los impostores ganan se suma 1 al numero de partidas ganadas como impostor
			if (Util.preguntaBoolean("¿Han ganado los impostores?")) {
				service.jugadoresActivos().stream().filter(jugador -> listaImpostores.contains(jugador.getId()))
						.forEach(jugador -> {
							jugador.setPartidasGanadasImpostorHoy(jugador.getPartidasGanadasImpostorHoy() + 1);
							service.modificarJugador(jugador);
						});
			} else {
				service.jugadoresActivos().stream().filter(jugador -> !listaImpostores.contains(jugador.getId()))
						.forEach(jugador -> {
							jugador.setPartidasGanadasTripulanteHoy(jugador.getPartidasGanadasTripulanteHoy() + 1);
							service.modificarJugador(jugador);
						});
			}

			mostrarMarcadores(service.jugadoresActivos(), Mostrar.Hoy);
			
			Util.estadisticas(service.jugadoresActivos(), service.masDe100(), service.masDe500(), Mostrar.Hoy);

			System.out.println();
			otraPartida = Util.preguntaBoolean("¿Se juega otra partida?");
			if (otraPartida) {
				// Comprobar si hay cambios antes de empezar la siguiente partida
				if (Util.preguntaBoolean("¿Hay cambios antes de comenzar la partida?")) {
					modificarPartida.corregirJugadores();
				}

				// Cambiar el numero de impostores
				if (Util.preguntaBoolean("¿Cambiar el numero de impostores?")) {
					numeroImpostores(service.jugadoresActivos());
				}
			}
			
			System.out.println();

		} while (otraPartida);
		
		// Marcar todos como inactivos
		service.jugadoresActivos().forEach(jugador -> service.desactivarJugador(jugador.getId()));
		
		// Sumar info de hoy a la global y poner lo de hoy a cero
		service.jugadoresGuardados().forEach(jugador -> {
			jugador.setPartidasJugadas(jugador.getPartidasJugadas() + jugador.getPartidasJugadasHoy());
			jugador.setPartidasImpostor(jugador.getPartidasImpostor() + jugador.getPartidasImpostorHoy());
			jugador.setPartidasGanadasImpostor(jugador.getPartidasGanadasImpostor() + jugador.getPartidasGanadasImpostorHoy());
			jugador.setPartidasGanadasTripulante(jugador.getPartidasGanadasTripulante() + jugador.getPartidasGanadasTripulanteHoy());
			jugador.setPartidasJugadasHoy(0);
			jugador.setPartidasImpostor(0);
			jugador.setPartidasGanadasImpostorHoy(0);
			jugador.setPartidasGanadasTripulanteHoy(0);
			service.modificarJugador(jugador);
		});
		
		mostrarMarcadores(service.jugadoresActivos(), Mostrar.Global);

		Util.mostrarMarcadoresPc(service.jugadoresActivos(), Mostrar.Global);
		
		Util.estadisticas(service.jugadoresActivos(), service.masDe100(), service.masDe500(), Mostrar.Global);

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
		boolean numeroIncorrectoImpostores;
		
		do {
			System.out.println("¿Cuantos impostores hay en la partida? Seleccionar número");
			numImpostores = entrada.nextInt();
			entrada.nextLine();

			numeroIncorrectoImpostores = numImpostores > 3 || numImpostores < 1
					|| numImpostores >= listaJugadores.size();

			if (numeroIncorrectoImpostores) {
				System.out.println("No puede haber menos de 1 impostor, ni mas de 3");
				System.out.println("El número de impostores debe ser menor que el número de jugadores");
			}

		} while (!Util.preguntaBoolean("¿Estas seguro de que hay %d impostores?", numImpostores) || numeroIncorrectoImpostores);
	}

	private void mostrarMarcadores(List<Jugador> listaJugadores, Mostrar mostrar) {
		System.out.println();
		System.out.println("********************************");
		System.out.println("********* MARCADORES ***********");
		System.out.println("********************************");
		System.out.println("            J | I | GI | GT");
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
			System.out.println(pintarNombreJugador(jugador) + Util.pintarBlancos("", 2) + partidasJugadas
					+ Util.pintarBlancos("", 3) + partidasImpostor + Util.pintarBlancos("", 3) + partidasGanadasImpostor
					+ Util.pintarBlancos("", 4) + partidasGanadasTripulante);
		});
		System.out.println("********************************");
		System.out.println("********************************");
		System.out.println("* J -> Patidas jugadas");
		System.out.println("* I -> Patidas impostor");
		System.out.println("* GI -> Ganadas impostor");
		System.out.println("* GT -> Ganadas tripulante");
		System.out.println();
	}

	private String pintarNombreJugador(Jugador jugador) {
		return Util.pintarBlancos(
				jugador.getNombre().substring(0,
						(jugador.getNombre().length() > 10 ? 10 : jugador.getNombre().length())),
				10 - jugador.getNombre()
						.substring(0, (jugador.getNombre().length() > 10 ? 10 : jugador.getNombre().length()))
						.length());
	}

}
