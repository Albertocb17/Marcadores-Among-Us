package com.lesjarones.amongus.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Jugador {
	
	@Id @GeneratedValue
	long id;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	Date fechaCreacion;
	
	String nombre;
	int partidasJugadas;
	int partidasImpostor;
	int partidasGanadasImpostor;
	int partidasGanadasTripulante;
	
	@Column(columnDefinition = "integer default 0")
	int partidasJugadasHoy;
	@Column(columnDefinition = "integer default 0")
	int partidasImpostorHoy = 0;
	@Column(columnDefinition = "integer default 0")
	int partidasGanadasImpostorHoy = 0;
	@Column(columnDefinition = "integer default 0")
	int partidasGanadasTripulanteHoy = 0;
	
	boolean activo;
}
