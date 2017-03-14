package de.agdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class SpringBootMain {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMain.class, args);
	}
}
