package com.fooddelivery.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("Food Delivery System API")
                        .version("1.0.0")
                        .description("""
                                Complete Food Delivery System REST API Documentation.
                                
                                ## Authentication
                                Most endpoints require JWT authentication. 
                                1. Register or Login using the Auth endpoints
                                2. Copy the `token` value from the response (just the token, without "Bearer")
                                3. Click the "Authorize" button above and paste ONLY the token value
                                4. Swagger will automatically add "Bearer " prefix
                                
                                ⚠️ Important: Do NOT include "Bearer" in the token field - enter only the token value!
                                
                                ## Rate Limiting
                                - Public APIs: 20 requests/minute
                                - Authenticated APIs: 100 requests/minute
                                - Payment APIs: 10 requests/minute
                                
                                ## User Roles
                                - **CUSTOMER**: Can place orders, manage cart, view menu
                                - **RESTAURANT**: Can manage menu, view orders, analytics
                                - **DELIVERY**: Can view assigned orders, update status
                                - **ADMIN**: Full system access, analytics, user management
                                """)
                        .contact(new Contact()
                                .name("Food Delivery Support")
                                .email("support@fooddelivery.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.fooddelivery.com")
                                .description("Production Server")
                ))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token obtained from /api/auth/login endpoint. " +
                                                "IMPORTANT: Enter only the token value (without 'Bearer' prefix) - Swagger will add it automatically.")
                        ));
    }
}

