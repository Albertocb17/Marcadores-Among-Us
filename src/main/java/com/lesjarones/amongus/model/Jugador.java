package com.lesjarones.amongus.model;

import lombok.Data;

@Data
public class Jugador {
	int id;
	String nombre;
	int partidasJugadas;
	int partidasImpostor;
	int partidasGanadasImpostor;
	int partidasGanadasTripulante;
	boolean activo;
}
