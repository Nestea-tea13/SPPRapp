package com.application.SPPRapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.application.SPPRapp.models.DataAVONA;
import com.application.SPPRapp.models.DataRiskTheory;

@SpringBootApplication
public class SppRappApplication {

	public static DataAVONA dataAVONA = new DataAVONA();
	public static DataRiskTheory dataRiskTheory = new DataRiskTheory();

	public static void main(String[] args) {
		SpringApplication.run(SppRappApplication.class, args);
	}

}
