package de.hsb.softw2muebase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EnableJpaRepositories
public class Softw2muebaseApplication implements CommandLineRunner {

	@Autowired
	private FilmRepository filmRepository;
	public static void main(String[] args) {

		SpringApplication.run(Softw2muebaseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
