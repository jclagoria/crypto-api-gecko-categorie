package ar.com.api.categories.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api")
@Getter
public class ExternalServerConfig {

    private String urlCoinGecko;
    private String ping;

}
