package com.labanta.servidorlocal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry; import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class ServidorlocalApplication implements WebMvcConfigurer {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override public void addResourceHandlers(ResourceHandlerRegistry registry) { registry.addResourceHandler("/imagens/**") .addResourceLocations("file:uploads/imagens/"); }

	public static void main(String[] args) {
		SpringApplication.run(ServidorlocalApplication.class, args);
	}

}
