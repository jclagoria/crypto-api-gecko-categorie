package ar.com.api.categories.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.categories.model.Categorie;
import ar.com.api.categories.model.Ping;
import ar.com.api.categories.services.CategoriesApiService;
import ar.com.api.categories.services.CoinGeckoServiceStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class CategoriesApiHandler {
 
 private CoinGeckoServiceStatus serviceStatus;

 private CategoriesApiService serviceCategorie;

 public Mono<ServerResponse> getStatusServiceCoinGecko(ServerRequest serverRequest) {

  log.info("In getStatusServiceCoinGecko");

  return ServerResponse
                .ok()
                .body(
                     serviceStatus.getStatusCoinGeckoService(), 
                     Ping.class);
 }

 public Mono<ServerResponse> getListOfCategories(ServerRequest sRequest) {
     
     log.info("In getListOfCategories");

     return ServerResponse
                    .ok()
                    .body(
                         serviceCategorie.getListOfCategories(),
                         Categorie.class
                    );
 }

}
