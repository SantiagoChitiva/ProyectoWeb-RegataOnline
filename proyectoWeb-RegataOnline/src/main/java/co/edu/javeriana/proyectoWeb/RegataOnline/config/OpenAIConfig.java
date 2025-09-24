package co.edu.javeriana.proyectoWeb.RegataOnline.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAIConfig {
    @Bean
    OpenAPI customOpenAI() {
            return new OpenAPI().info(new Info().title("Regata Online API").version("1.0.0").description("API para la gestion de una regata online"));
    }
}
