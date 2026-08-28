package com.labanta.servidorlocal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = System.getProperty("user.dir") + "/uploads/imagens/";

        registry.addResourceHandler("/imagens/**")
                .addResourceLocations("file:" + uploadPath);
    }
}