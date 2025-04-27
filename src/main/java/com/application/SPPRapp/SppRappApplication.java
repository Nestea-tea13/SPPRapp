package com.application.SPPRapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.application.SPPRapp.models.DataAVONA;

@SpringBootApplication
public class SppRappApplication {

	public static DataAVONA dataAVONA = new DataAVONA();

	public static void main(String[] args) {
		SpringApplication.run(SppRappApplication.class, args);
	}

}
