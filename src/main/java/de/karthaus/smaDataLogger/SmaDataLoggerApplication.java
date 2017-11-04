package de.karthaus.smaDataLogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SmaDataLoggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmaDataLoggerApplication.class, args);
	}
}
