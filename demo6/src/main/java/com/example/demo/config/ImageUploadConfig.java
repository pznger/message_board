package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageUploadConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:C:\\Users\\Administrator\\Desktop\\message_board\\demo6\\src\\main\\resources\\static\\uploads\\");
    }
}
//C:\Users\Administrator\Desktop\message_board\demo6\src\main\resources\static