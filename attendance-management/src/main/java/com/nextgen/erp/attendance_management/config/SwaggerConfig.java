package com.nextgen.erp.attendance_management.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI academiaXOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("AcademiaX User Management API")
                        .description("REST APIs for User Management Service")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("NextGen ERP")
                                .email("support@academiax.com"))
                        .license(new License()
                                .name("Apache 2.0")))
                .externalDocs(
                        new ExternalDocumentation()
                                .description("AcademiaX Documentation")
                                .url("https://academiax.com"));
    }
}