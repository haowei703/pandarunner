package com.shiroha.pandarunner;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
@SecurityScheme(type = SecuritySchemeType.HTTP)
public class PandarunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandarunnerApplication.class, args);
    }

}
