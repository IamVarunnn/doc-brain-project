package com.docbrain.docbrain_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("DocBrain - AI Document backend")
                                .description("REST API For Multi-format document")
                                .version("1.0.0")
                                .contact(new Contact()
                                        .name("Que Tech")
                                        .email("qtech@gmail.com")
                                        .url("https://quetech.com")
                                )
                );
    }
}
