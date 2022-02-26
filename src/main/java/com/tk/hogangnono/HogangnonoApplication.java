package com.tk.hogangnono;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class HogangnonoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HogangnonoApplication.class, args);
	}

}
