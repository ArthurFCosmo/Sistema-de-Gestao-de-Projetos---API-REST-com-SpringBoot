package br.com.cosmodev.sgp_api_dto_exceptions.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sgpApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SGP API")
                        .description("API REST para gestão de projetos, tarefas e usuários.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Arthur Cosmo")
                                .email("arthurcosmoservices@gmail.com"))
                        .license(new License()
                                .name("Uso pessoal - Arthur Cosmo")));
    }
}
