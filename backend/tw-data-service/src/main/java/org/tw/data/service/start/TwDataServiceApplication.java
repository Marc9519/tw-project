package org.tw.data.service.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The application Launcher
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.tw.data")
@EnableJpaRepositories("org.tw.data")
@EntityScan("org.tw.data")
public class TwDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwDataServiceApplication.class, args);
	}

}