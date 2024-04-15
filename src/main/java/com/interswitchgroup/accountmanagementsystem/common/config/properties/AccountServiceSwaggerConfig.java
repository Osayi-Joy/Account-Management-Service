package com.interswitchgroup.accountmanagementsystem.common.config.properties;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Primary;
import org.thymeleaf.spring5.SpringTemplateEngine;

import static com.interswitchgroup.accountmanagementsystem.common.constants.Constants.API_V1;


/**
 * @author Joy Osayi
 * @createdOn April-12(Fri)-2024
 */
@Configuration
@RequiredArgsConstructor
public class AccountServiceSwaggerConfig {


  @Value("${springdoc.swagger-ui.url}")
  private String swaggerUiUrl;

  @Bean
  public OpenAPI springShopOpenAPI() {
    final String securitySchemeName = "bearerAuth";
    return new OpenAPI()
            .info(
                    new Info()
                            .title("Account Service APIs")
                            .description(
                                    "This documentation contains all the APIs exposed for the Account Management console. Aside the authentication and reset password APIs, all other APIs requires a valid authenticated user jwt access token before they can be invoked")
                            .version("v1.0.0")
                            .license(
                                    new License()
                                            .name("Proprietary License")
                                            .url("http://localhost:2760/download-license")))
            .addServersItem(new Server().description("Local Server").url("http://localhost:8080"))
            .addServersItem(
                    new Server()
                            .description("Group 2 Interswitch Job Shadowing")
                            .url(swaggerUiUrl))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(
                    new Components()
                            .addSecuritySchemes(
                                    securitySchemeName,
                                    new SecurityScheme()
                                            .name(securitySchemeName)
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")));
  }

  @Bean
  public GroupedOpenApi accountServiceApi() {
    return GroupedOpenApi.builder().group("accountService").pathsToMatch(API_V1.concat("**")).build();
  }



  @Bean
  @Primary
  @Description("Thymeleaf template engine with Spring integration")
  public SpringTemplateEngine springTemplateEngine() {
    return new SpringTemplateEngine();
  }
}
