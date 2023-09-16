package ar.com.api.categories.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.categories.dto.CategorieDTO;
import ar.com.api.categories.model.Categorie;
import ar.com.api.categories.model.CategorieMarket;
import ar.com.api.categories.services.CategoriesApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class CategoriesApiHandler {

 private CategoriesApiService serviceCategories;

 public Mono<ServerResponse> getListOfCategories(ServerRequest sRequest) {

     log.info("In getListOfCategories");

     return ServerResponse
                    .ok()
                    .body(
                         serviceCategories.getListOfCategories(),
                         Categorie.class
                    );
 }

 public Mono<ServerResponse> getListCategoriesWithMarketData(ServerRequest sRequest) {

     log.info("In getListCategoriesWithMarketData");

     CategorieDTO filterDto = CategorieDTO
                    .builder()
                    .order(sRequest.queryParam("order"))
                    .build();

     return ServerResponse
                    .ok()
                    .body(
                         serviceCategories.getListCategoriesByMarket(filterDto),
                         CategorieMarket.class
                         );

 }

}
