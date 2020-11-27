package com.lesjarones.amongus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lesjarones.amongus.model.Jugador;

@Repository
public interface JugadorRepository extends CrudRepository<Jugador, Long> {

	Optional<Jugador> findByNombre(String nombre);

	List<Jugador> findByActivoTrue();

	List<Jugador> findByActivoFalse();

	List<Jugador> findByPartidasJugadasBetween(int jugadas1, int jugadas2);

	List<Jugador> findByPartidasJugadasGreaterThan(int jugadas);

}
