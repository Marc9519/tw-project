package org.tw.data.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for swagger related configurations.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * Gets the Swagger docket
	 * @return The docket
	 */
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).select().
				apis(RequestHandlerSelectors.basePackage("org.tw.data.service")).build();
	}

}