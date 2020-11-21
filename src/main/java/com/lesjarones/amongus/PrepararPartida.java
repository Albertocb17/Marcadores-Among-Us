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
public class PrepararPartida {

	@Autowired
	private LanzarPartida lanzar;

	Scanner entrada = new Scanner(System.in);

	public void iniciar() {

		System.out.println("*******************************************************");
		System.out.println("********************* Iniciando bot *******************");
		System.out.println("*******************************************************");
		System.out.println("");
		System.out.println("¿Cuantos jugadores hay en la partida?");
		int numJugadores = entrada.nextInt();

		List<Jugador> listaJugadores = new ArrayList<>();
		entrada.nextLine();
		for (int i = 0; i < numJugadores; i++) {
			Jugador jugador = new Jugador();
			jugador.setId(i + 1);
			System.out.println(String.format("¿Cuál es el nombre del jugador %d?", i + 1));
			String nombre = entrada.nextLine();
			jugador.setNombre(nombre);
			jugador.setPartidasGanadasImpostor(0);
			jugador.setPartidasGanadasTripulante(0);
			jugador.setPartidasImpostor(0);
			jugador.setPartidasJugadas(0);
			jugador.setActivo(true);
			listaJugadores.add(jugador);
		}

		System.out.println("");
		boolean esCorrecta = verificarJugadores(listaJugadores);

		if (esCorrecta) {
			lanzarJuego(listaJugadores);
		} else {
			listaJugadores = corregirJugadores(listaJugadores);
			lanzarJuego(listaJugadores);
		}

		entrada.close();
	}

	private boolean verificarJugadores(List<Jugador> listaJugadores) {
		String esCorrecta;
		do {
			System.out.println("¿La lista de jugadores es correcta? (S/n)");
			mostrarJugadores(listaJugadores);
			esCorrecta = entrada.nextLine();
		} while (!esCorrecta.equalsIgnoreCase("s") && !esCorrecta.equalsIgnoreCase("n")
				&& !esCorrecta.equalsIgnoreCase(""));

		return (esCorrecta.isEmpty() || esCorrecta.equalsIgnoreCase("S"));
	}

	public List<Jugador> corregirJugadores(List<Jugador> listaJugadores) {
		String otraCorrecion;
		do {
			System.out.println("¿Qué quieres hacer? Seleccionar número");
			System.out.println("1 - Añadir jugador");
			System.out.println("2 - Modificar jugador");
			System.out.println("3 - Eliminar jugador");
			System.out.println("4 - Activar jugador");
			System.out.println("5 - Nada");
			int opcion = entrada.nextInt();

			switch (opcion) {
			case 1:
				listaJugadores = añadirJugador(listaJugadores);
				break;
			case 2:
				listaJugadores = modificarJugador(listaJugadores);
				break;
			case 3:
				listaJugadores = eliminarJugador(listaJugadores);
				break;
			case 4:
				listaJugadores = activarJugador(listaJugadores);
				break;

			default:
				entrada.nextLine();
				break;
			}

			do {
				System.out.println("¿Quieres hacer otra correción? (S/n)");
				otraCorrecion = entrada.nextLine();
			} while (!otraCorrecion.equalsIgnoreCase("s") && !otraCorrecion.equalsIgnoreCase("n")
					&& !otraCorrecion.equalsIgnoreCase(""));
		} while (otraCorrecion.isEmpty() || otraCorrecion.equalsIgnoreCase("S"));

		return listaJugadores;
	}

	private List<Jugador> añadirJugador(List<Jugador> listaJugadores) {
		entrada.nextLine();

		String otroJugador;
		do {
			Jugador jugador = new Jugador();
			jugador.setId(listaJugadores.size() + 1);
			System.out.println("¿Cuál es el nombre del nuevo jugador?");
			String nombre = entrada.nextLine();
			jugador.setNombre(nombre);
			jugador.setPartidasGanadasImpostor(0);
			jugador.setPartidasGanadasTripulante(0);
			jugador.setPartidasImpostor(0);
			jugador.setPartidasJugadas(0);
			jugador.setActivo(true);
			listaJugadores.add(jugador);

			do {
				System.out.println("¿Añadir otro jugador? (S/n)");
				mostrarJugadores(listaJugadores);
				otroJugador = entrada.nextLine();
			} while (!otroJugador.equalsIgnoreCase("s") && !otroJugador.equalsIgnoreCase("n")
					&& !otroJugador.equalsIgnoreCase(""));
		} while (otroJugador.isEmpty() || otroJugador.equalsIgnoreCase("S"));

		return listaJugadores;
	}

	private List<Jugador> modificarJugador(List<Jugador> listaJugadores) {
		entrada.nextLine();

		String otroJugador;
		do {
			System.out.println("¿Qué jugador quieres modificar? Seleccionar número");
			mostrarJugadores(listaJugadores);
			int numJugador = entrada.nextInt();
			Jugador jugadorModificar = listaJugadores.get(numJugador - 1);

			entrada.nextLine();
			System.out.println("¿Cuál es el nuevo nombre del jugador?");
			String nombre = entrada.nextLine();
			jugadorModificar.setNombre(nombre);
			listaJugadores.stream().filter(jugador -> jugador.getId() == jugadorModificar.getId()).findFirst()
					.ifPresent(jugador -> jugador = jugadorModificar);

			do {
				System.out.println("¿Modificar otro jugador? (S/n)");
				mostrarJugadores(listaJugadores);
				otroJugador = entrada.nextLine();
			} while (!otroJugador.equalsIgnoreCase("s") && !otroJugador.equalsIgnoreCase("n")
					&& !otroJugador.equalsIgnoreCase(""));
		} while (otroJugador.isEmpty() || otroJugador.equalsIgnoreCase("S"));

		return listaJugadores;
	}

	private List<Jugador> eliminarJugador(List<Jugador> listaJugadores) {
		entrada.nextLine();

		String otroJugador;
		do {
			System.out.println("¿Qué jugador quieres eliminar? Seleccionar número");
			mostrarJugadores(listaJugadores);
			int numJugador = entrada.nextInt();

			entrada.nextLine();
			String eliminarDefinitivo;
			do {
				System.out.println("¿Eliminar definitivamente al jugador? (S/n)");
				System.out.println("Si lo eliminas definitivamente se borrarán todos sus datos.");
				System.out.println("En caso contrario, se marcará como inactivo y se mantendrán sus datos");
				eliminarDefinitivo = entrada.nextLine();
			} while (!eliminarDefinitivo.equalsIgnoreCase("s") && !eliminarDefinitivo.equalsIgnoreCase("n")
					&& !eliminarDefinitivo.equalsIgnoreCase(""));

			// Si no se borra definitivamente, se marca como inactivo para que no le cuenten
			// las partidas jugadas
			if (eliminarDefinitivo.equalsIgnoreCase("s") || eliminarDefinitivo.equalsIgnoreCase(""))
				listaJugadores.removeIf(jugador -> jugador.getId() == numJugador);
			else
				listaJugadores.stream().filter(jugador -> jugador.getId() == numJugador)
						.forEach(jugador -> jugador.setActivo(false));

			do {
				System.out.println("¿Eliminar otro jugador? (S/n)");
				mostrarJugadores(listaJugadores);
				otroJugador = entrada.nextLine();
			} while (!otroJugador.equalsIgnoreCase("s") && !otroJugador.equalsIgnoreCase("n")
					&& !otroJugador.equalsIgnoreCase(""));
		} while (otroJugador.isEmpty() || otroJugador.equalsIgnoreCase("S"));
		return listaJugadores;
	}

	private List<Jugador> activarJugador(List<Jugador> listaJugadores) {
		entrada.nextLine();

		List<Jugador> jugadoresInactivos = listaJugadores.stream()
				.filter(jugador -> !jugador.isActivo())
				.collect(Collectors.toList());

		if (!jugadoresInactivos.isEmpty()) {
			String otroJugador;
			do {
				System.out.println("¿Qué jugador quieres activar? Seleccionar número");
				mostrarJugadores(jugadoresInactivos);
				int numJugador = entrada.nextInt();
				listaJugadores.stream().filter(jugador -> jugador.getId() == numJugador)
						.forEach(jugador -> jugador.setActivo(true));

				do {
					System.out.println("¿Activar otro jugador? (S/n)");
					mostrarJugadores(listaJugadores);
					otroJugador = entrada.nextLine();
				} while (!otroJugador.equalsIgnoreCase("s") && !otroJugador.equalsIgnoreCase("n")
						&& !otroJugador.equalsIgnoreCase(""));
			} while (otroJugador.isEmpty() || otroJugador.equalsIgnoreCase("S"));
		} else {
			System.out.println("No hay ningún jugador inactivo");
		}
		return listaJugadores;
	}

	private void mostrarJugadores(List<Jugador> listaJugadores) {
		for (int i = 0; i < listaJugadores.size(); i++) {
			System.out.println(String.format("%d - %s", listaJugadores.get(i).getId(), listaJugadores.get(i).getNombre()));
		}
	}

	private void lanzarJuego(List<Jugador> listaJugadores) {
		lanzar.lanzarPartida(listaJugadores);
	}
}
