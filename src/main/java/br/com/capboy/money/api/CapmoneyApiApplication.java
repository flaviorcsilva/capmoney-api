package br.com.capboy.money.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.capboy.money.api.config.CapmoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(CapmoneyApiProperty.class)
public class CapmoneyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapmoneyApiApplication.class, args);
	}
}
