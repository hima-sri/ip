package com.quiz.assessment.CrossOriginConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigurationFile implements WebMvcConfigurer{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS for all endpoints
                .allowedOrigins("*") // Allow requests from any origin
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH") // Allowed HTTP methods
                .allowedHeaders("*"); // Allowed headers12.18.4.81
    }
}