package com.visara.transportationweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class TransportationwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransportationwebApplication.class, args);
	}

}
