package ar.edu.utn.frc.tup.lc.iii.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Value("${app.name}")
    private String appName;

    @Value("${app.desc}")
    private String appDesc;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.dev-name}")
    private String devName;

    @Value("${app.dev-email}")
    private String devEmail;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String tenantHeaderName = "X-Tenant-Id";

        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .description(appDesc)
                        .version(appVersion)
                        .contact(new Contact()
                                .name(devName)
                                .email(devEmail)))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName)
                        .addList(tenantHeaderName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addSecuritySchemes(tenantHeaderName, new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(tenantHeaderName)));
    }
}
