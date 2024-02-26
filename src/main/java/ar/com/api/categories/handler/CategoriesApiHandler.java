package ar.com.api.categories.handler;

import ar.com.api.categories.dto.CategorieDTO;
import ar.com.api.categories.exception.ApiCustomException;
import ar.com.api.categories.model.CategoryMarket;
import ar.com.api.categories.services.CategoriesApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class CategoriesApiHandler {

    private CategoriesApiService serviceCategories;

    public Mono<ServerResponse> getListOfCategories(ServerRequest sRequest) {

        log.info("In getListOfCategories");

        return serviceCategories.getListOfCategories()
                .collectList()
                .flatMap(categories -> ServerResponse.ok().bodyValue(categories))
                .doOnSubscribe(subscription -> log.info("Retrieving list of categories"))
                .onErrorResume(error -> Mono
                        .error(new ApiCustomException("An expected error occurred in getStatusServiceCoinGecko",
                                HttpStatus.INTERNAL_SERVER_ERROR))
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
                        CategoryMarket.class
                );

    }

}
