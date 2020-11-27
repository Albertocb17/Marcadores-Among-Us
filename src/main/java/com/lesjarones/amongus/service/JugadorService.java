package com.lesjarones.amongus.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lesjarones.amongus.model.Jugador;
import com.lesjarones.amongus.repository.JugadorRepository;
import com.lesjarones.amongus.util.Util;

@Service
public class JugadorService {

	@Autowired
	private JugadorRepository repo;

	Scanner entrada = new Scanner(System.in);

	public List<Jugador> jugadoresGuardados() {
		return (List<Jugador>) repo.findAll();
	}

	public List<Jugador> jugadoresActivos() {
		return (List<Jugador>) repo.findByActivoTrue();
	}
	
	public List<Jugador> jugadoresInactivos() {
		return (List<Jugador>) repo.findByActivoFalse();
	}

	public void activarJugador(long idJugador) {
		Optional<Jugador> jugador = repo.findById(idJugador);
		if (jugador.isPresent()) {
			jugador.get().setActivo(true);
			repo.save(jugador.get());
		}
	}
	
	public void desactivarJugador(long idJugador) {
		Optional<Jugador> jugador = repo.findById(idJugador);
		if (jugador.isPresent()) {
			jugador.get().setActivo(false);
			repo.save(jugador.get());
		}
	}

	public Jugador nuevoJugador(int numJugadores) {
		entrada.nextLine();
		Jugador jugador = new Jugador();
		jugador.setFechaCreacion(new Date());
		System.out.println("¿Cuál es el nombre del nuevo jugador?");
		String nombre = entrada.nextLine();
		jugador.setNombre(nombre);
		jugador.setPartidasGanadasImpostor(0);
		jugador.setPartidasGanadasTripulante(0);
		jugador.setPartidasImpostor(0);
		jugador.setPartidasJugadas(0);
		
		boolean activar = false;
		if (numJugadores >= jugadoresActivos().size()) {
			activar = Util.preguntaBoolean("¿Va a jugar esta partida?");
		}
		jugador.setActivo(activar);

		return repo.save(jugador);
	}
	
	public Jugador modificarJugador(Jugador jugador) {
		return repo.save(jugador);
	}

	public Jugador buscarPorId(long idJugador) {
		return repo.findById(idJugador).get();
	}

	public void borrarJugador(long idJugador) {
		repo.delete(buscarPorId(idJugador));
	}

	public List<Jugador> masDe100() {
		return repo.findByPartidasJugadasBetween(100, 500);
	}

	public List<Jugador> masDe500() {
		return repo.findByPartidasJugadasGreaterThan(500);
	}

}
