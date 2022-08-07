package com.bank.statement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.bank.statement"))
                .paths(PathSelectors.any()).build().securitySchemes(Collections.singletonList(basicAuth()))
                .securityContexts(Collections.singletonList(securityContext()));


    }
    private BasicAuth basicAuth()
    {
        BasicAuth ba=new BasicAuth("basic");
        return ba;
    }
    private SecurityContext securityContext()
    {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths((PathSelectors.ant("/admin/**"))).build();
    }
    private List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope=new AuthorizationScope("global","AccessEverything");
        AuthorizationScope[] authorizationScopes=new AuthorizationScope[1];
        authorizationScopes[0]=authorizationScope;
        return Collections.singletonList(new SecurityReference("basic",authorizationScopes));
    }
}
