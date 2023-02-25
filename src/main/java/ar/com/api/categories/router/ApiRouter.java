package ar.com.api.categories.router;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.categories.handler.CategoriesApiHandler;

@Configuration
public class ApiRouter {
 
 @Value("${coins.baseURL}")
 private String URL_SERVICE_API;

 @Value("${coins.healthAPI}")
 private String URL_HEALTH_GECKO_API;

 @Value("${coins.listCategories}")
 private String URL_CATEGORIE_API;

 @Value("${coins.listCategoriesMarketData}")
 private String URL_CATEGORIE_WITH_MARKET_DATA_API;
 
 @Bean
 public RouterFunction<ServerResponse> route(CategoriesApiHandler handler) {

  return RouterFunctions
            .route()
            .GET(URL_SERVICE_API + URL_HEALTH_GECKO_API, 
                        handler::getStatusServiceCoinGecko)
            .GET(URL_SERVICE_API + URL_CATEGORIE_API, 
                        handler::getListOfCategories)
            .GET(URL_SERVICE_API + URL_CATEGORIE_WITH_MARKET_DATA_API, 
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        handler::getListCategoriesWithMarketData)
            .build();

 }

}
