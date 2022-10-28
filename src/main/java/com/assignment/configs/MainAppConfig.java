package com.assignment.configs;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class MainAppConfig {

	@Bean
	public CloseableHttpClient closeableHttpClient() {
		return HttpClients.createDefault();
	}
	
	/**
	 * Deserialize the json response using faster jackson
	 * @return
	 */
	@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }


}