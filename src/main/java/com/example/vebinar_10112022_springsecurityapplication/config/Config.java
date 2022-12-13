package com.example.vebinar_10112022_springsecurityapplication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Веб.24.11.22 2часть 1:21:23 – созд. Конфиг, где пропишем раздачу пути img:
@Configuration
    public class Config implements WebMvcConfigurer
    {
        @Value("${upload.path}")
        private String uploadPath;

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/img/**").addResourceLocations("file:///" + uploadPath + "/");
        }
    }

