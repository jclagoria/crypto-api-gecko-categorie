package ar.com.api.categories.router;

import ar.com.api.categories.configuration.ApiServiceConfig;
import ar.com.api.categories.handler.CategoriesApiHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CategoriesApiRouter {

    private ApiServiceConfig apiServiceConfig;

    public CategoriesApiRouter(ApiServiceConfig serviceConfig) {
        this.apiServiceConfig = serviceConfig;
    }

    @Bean
    public RouterFunction<ServerResponse> routeCategoriesApi(CategoriesApiHandler handler) {

        return RouterFunctions
                .route()
                .GET(apiServiceConfig.getBaseURL() +
                                apiServiceConfig.getListCategories(),
                        handler::getListOfCategories)
                .GET(apiServiceConfig.getBaseURL() + apiServiceConfig.getListCategoriesMarketData(),
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        handler::getListCategoriesWithMarketData)
                .build();

    }

}
