package org.tw.data.service.config;

import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Application related configurations.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer{

	/**
	 * Adds and configures the Cors Mappings
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE");
	}

	/**
	 * Gets the ApplicationPidFileWriter as bean.
	 * @return the ApplicationPidFileWriter
	 */
	@Bean
	public ApplicationPidFileWriter pidWriter() {
		ApplicationPidFileWriter applicationPidFileWriter = new ApplicationPidFileWriter("./bin/process.pid");
		applicationPidFileWriter.setTriggerEventType(ApplicationStartedEvent.class);
		return applicationPidFileWriter;
	}

}