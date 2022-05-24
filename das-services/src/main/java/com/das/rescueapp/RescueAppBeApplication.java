package com.das.rescueapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@OpenAPIDefinition(info = @Info(title = "RESCUE APP API", version = "1"))
@SpringBootApplication
@EnableSwagger2
public class RescueAppBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RescueAppBeApplication.class, args);
    }
}
