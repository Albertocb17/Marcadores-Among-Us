package com.lesjarones.amongus;

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
public class PrepararPartida {

	@Autowired
	private LanzarPartida lanzar;

	@Autowired
	private JugadorService service;

	Scanner entrada = new Scanner(System.in);
	
	int numJugadores;

	public void iniciar() {

		System.out.println("*******************************************************");
		System.out.println("********************* Iniciando bot *******************");
		System.out.println("*******************************************************");
		System.out.println("");
		
		Util.mostrarMarcadoresPc(service.jugadoresGuardados(), Mostrar.Global);
		Util.estadisticas(service.jugadoresGuardados(), service.masDe100(), service.masDe500(), Mostrar.Global);
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("¿Cuantos jugadores hay en la partida?");
		numJugadores = entrada.nextInt();
		entrada.nextLine();
		
		System.out.println();
		
		if (Util.preguntaBoolean("¿Hay jugadores nuevos?"))
			añadirJugador();

		System.out.println();
		
		Util.mostrarJugadores(service.jugadoresGuardados());
		String idJugador;
		System.out.println("Selecciona el número del jugador que va a jugar y pulsa intro");
		do {
			idJugador = entrada.nextLine();
			service.activarJugador(Long.parseLong(idJugador));
		} while(service.jugadoresActivos().size() < numJugadores);
		
		

		System.out.println("");
		Util.mostrarJugadores(service.jugadoresActivos());
		if (Util.preguntaBoolean("¿La lista de jugadores es correcta?")) {
			lanzar.lanzarPartida();
		} else {
			corregirJugadores();
			lanzar.lanzarPartida();
		}

		entrada.close();
	}

	public void corregirJugadores() {
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
				añadirJugador();
				break;
			case 2:
				modificarJugador();
				break;
			case 3:
				eliminarJugador();
				break;
			case 4:
				activarJugador();
				break;
			case 5:
				desactivarJugador();
				break;

			default:
				entrada.nextLine();
				break;
			}

			System.out.println();
		} while (Util.preguntaBoolean("¿Quieres hacer otra correción?"));

	}

	private void añadirJugador() {
		entrada.nextLine();
		do {
			service.nuevoJugador(numJugadores);
			Util.mostrarJugadores(service.jugadoresActivos());
			System.out.println();
		} while (Util.preguntaBoolean("¿Añadir otro jugador?"));
	}

	private void modificarJugador() {
		entrada.nextLine();

		do {
			System.out.println("¿Qué jugador quieres modificar? Seleccionar número");
			Util.mostrarJugadores(service.jugadoresActivos());
			long numJugador = entrada.nextLong();

			entrada.nextLine();
			System.out.println("¿Cuál es el nuevo nombre del jugador?");
			String nombre = entrada.nextLine();
			Jugador jugador = service.buscarPorId(numJugador);
			jugador.setNombre(nombre);
			service.modificarJugador(jugador);

			Util.mostrarJugadores(service.jugadoresActivos());
			System.out.println();
		} while (Util.preguntaBoolean("¿Modificar otro jugador?"));

	}

	private void eliminarJugador() {
		entrada.nextLine();

		do {
			System.out.println("¿Qué jugador quieres eliminar? Seleccionar número");
			Util.mostrarJugadores(service.jugadoresActivos());
			long numJugador = entrada.nextLong();

			entrada.nextLine();

			// Si no se borra definitivamente, se marca como inactivo para que no le cuenten
			// las partidas jugadas
			System.out.println("Si eliminas definitivamente al jugador se borrarán todos sus datos.");
			System.out.println("En caso contrario, se marcará como inactivo y se mantendrán sus datos");
			if (Util.preguntaBoolean("¿Eliminar definitivamente al jugador?"))
				service.borrarJugador(numJugador);
			else
				service.desactivarJugador(numJugador);

			Util.mostrarJugadores(service.jugadoresActivos());
			System.out.println();
		} while (Util.preguntaBoolean("¿Eliminar otro jugador?"));
		
	}

	private void activarJugador() {
		entrada.nextLine();

		if (!service.jugadoresInactivos().isEmpty()) {
			do {
				System.out.println("¿Qué jugador quieres activar? Seleccionar número");
				Util.mostrarJugadores(service.jugadoresInactivos());
				long numJugador = entrada.nextLong();
				service.activarJugador(numJugador);

				Util.mostrarJugadores(service.jugadoresActivos());
				System.out.println();
			} while (Util.preguntaBoolean("¿Activar otro jugador?"));
		} else {
			System.out.println("No hay ningún jugador inactivo");
		}
	}
	
	private void desactivarJugador() {
		entrada.nextLine();

		do {
			System.out.println("¿Qué jugador quieres desactivar? Seleccionar número");
			Util.mostrarJugadores(service.jugadoresActivos());
			long numJugador = entrada.nextLong();

			entrada.nextLine();

			service.desactivarJugador(numJugador);

			Util.mostrarJugadores(service.jugadoresActivos());
			System.out.println();
		} while (Util.preguntaBoolean("¿Desactivar otro jugador?"));
		
	}

}
