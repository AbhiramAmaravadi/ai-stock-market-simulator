package com.abhiram.stocktrader.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI configuration.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI stockTraderOpenAPI() {

        return new OpenAPI()

                .info(new Info()

                        .title("AI Stock Trader API")

                        .description("""
                                Backend API for an AI-powered
                                stock portfolio management platform.

                                Features include:

                                • JWT Authentication

                                • Portfolio Management

                                • Live Market Data (Finnhub)

                                • AI Investment Advisor (Ollama)

                                • Portfolio Analytics

                                • AI Chat Assistant
                                """)

                        .version("1.0.0")

                        .contact(new Contact()
                                .name("Abhiram Amaravadi")
                                .email("your-email@example.com"))

                        .license(new License()
                                .name("MIT License")))

                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation"));
    }
}