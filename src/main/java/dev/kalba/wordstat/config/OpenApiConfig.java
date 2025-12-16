package dev.kalba.wordstat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI wordStatOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WordStat API")
                        .description("""
                                Word frequency analyzer API.
                                
                                Originally created in 2021 for language learning.
                                Refactored in 2025 as REST API.
                                
                                Dictionary source: gwicks.net/dictionaries.htm
                                """)
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Kalba Lab")
                                .url("https://kalba.dev"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
