package com.das.entities;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@OpenAPIDefinition(info = @Info(title = "DAS ENTITY API", version = "1"))
@SpringBootApplication
@EnableSwagger2
public class EntitiesBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntitiesBeApplication.class, args);
    }
}
