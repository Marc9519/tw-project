package org.tw.data.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tw.data.service.db.DBTool;

@Configuration
public class BeanConfig {

	@Bean
	public DBTool dbTool() {
		return new DBTool();
	}

}
