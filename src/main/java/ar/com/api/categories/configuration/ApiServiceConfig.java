package ar.com.api.categories.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "coins")
public class ApiServiceConfig {

    private String baseUrl;
    private String healthApi;

}
