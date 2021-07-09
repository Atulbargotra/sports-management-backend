package com.atul.sportsmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Component
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket sportsManagementApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }
    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title("Sports Management API")
                .version("1.0")
                .description("API for Sports Management System for TIAA")
                .contact(new Contact("Atul Verma","http://atulverma.live","writetoatulverma@outlook.com"))
                .license("Apache Licence Version 2.0")
                .build();
    }
}
