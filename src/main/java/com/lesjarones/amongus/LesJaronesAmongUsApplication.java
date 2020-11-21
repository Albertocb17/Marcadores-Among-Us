package com.lesjarones.amongus;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LesJaronesAmongUsApplication {
	
	@Autowired
	private PrepararPartida launcher;

	public static void main(String[] args) {
		SpringApplication.run(LesJaronesAmongUsApplication.class, args);
	}
	
	@PostConstruct
	private void init() {
		launcher.iniciar();
	}

}
