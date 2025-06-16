package com.sls.product_management.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Shibin L S",
                        email = "shibinls1998@gmail.com",
                        url = "https://www.linkedin.com/in/shibin-l-s-6a441512b/"
                ),
                version = "1.0",
                description = "Documentation for Product Management",
                title = "Product Management Backend Documentation",
                license = @License(
                        name = "Shibin L S",
                        url = "https://www.linkedin.com/in/shibin-l-s-6a441512b/"
                ),
                summary = "API Documentation for Product Management App"
        ),
        servers = {
                @Server(
                        description = "Local Dev Environment",
                        url = "http://localhost:8081"
                )
        },
        security = {
                @SecurityRequirement(name = "basicAuth")
        }
)
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
