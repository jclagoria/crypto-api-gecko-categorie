package ar.com.api.categories.router;

import ar.com.api.categories.handler.HealthApiHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HealtApiRouter {

    @Value("${coins.baseURL}")
    private String URL_SERVICE_API;

    @Value("${coins.healthAPI}")
    private String URL_HEALTH_GECKO_API;
    @Bean
    public RouterFunction<ServerResponse> routeHealthApi(HealthApiHandler handler) {
        return RouterFunctions
                .route()
                .GET(URL_SERVICE_API + URL_HEALTH_GECKO_API,
                        handler::getStatusServiceCoinGecko)
                .build();
    }

}