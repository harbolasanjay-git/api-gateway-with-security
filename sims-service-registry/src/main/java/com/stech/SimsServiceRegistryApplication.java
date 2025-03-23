package com.stech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SimsServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimsServiceRegistryApplication.class, args);
	}

}
