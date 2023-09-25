package com.example.admin.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //corsConfiguration.addAllowedOrigin("http://localhost:3000"); // 允许的域
        corsConfiguration.addAllowedOrigin("*"); // 允许的域
        corsConfiguration.addAllowedMethod("*"); // 允许所有 HTTP 方法
        corsConfiguration.addAllowedHeader("*"); // 允许所有 HTTP 头

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

}