package com.mycompany.obitemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })

public class ObItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObItemServiceApplication.class, args);
	}

}
