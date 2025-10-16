package ar.edu.utn.frc.tup.lc.iii.config;


// import io.swagger.v3.oas.annotations.info.Info;
// import lombok.Value;
// import org.springdoc.webmvc.api.OpenApiActuatorResource;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import java.util.zip.InflaterOutputStream;

// @Configuration
// public class SpringDocConfig {
//     @Value("${app.url}") private String url;
//     @Value("${app.dev-name}") private String devName;
//     @Value("${app.dev-email}") private String devEmail;
//
//     @Bean
//     public OpenApi openApi(
//             @Value("${app.name}") String appName,
//             @Value("${app.desc}") String appDescription,
//             @Value("${app.version}") String appVersion) {
//         Info info = new Info()
//                 .title(appName)
//                 .description(appDescription)
//                 .version(appVersion)
//                 .contact(new Info.Contact()
//                         .name(devName)
//                         .email(devEmail)
//                         .url(url));
//     }
// }
