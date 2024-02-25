package ar.com.api.categories.services;

import ar.com.api.categories.dto.CategorieDTO;
import ar.com.api.categories.exception.ManageExceptionCoinGeckoServiceApi;
import ar.com.api.categories.model.Categorie;
import ar.com.api.categories.model.CategoryMarket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class CategoriesApiService extends CoinGeckoServiceApi {

    @Value("${api.listCategories}")
    private String URL_GET_LIST_CATEGORIES_API;

    @Value("${api.listCategoriesMarketData}")
    private String URL_GET_LIST_CATEGORIES_WITH_MARKET_DATA_API;

    private WebClient webClient;

    public CategoriesApiService(WebClient wClient) {
        this.webClient = wClient;
    }

    public Flux<Categorie> getListOfCategories() {

        log.info("In getlistOfCategories() to call api ",
                URL_GET_LIST_CATEGORIES_API);

        return webClient
                .get()
                .uri(URL_GET_LIST_CATEGORIES_API)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        getClientResponseMonoServerException()
                )
                .bodyToFlux(Categorie.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

    public Flux<CategoryMarket> getListCategoriesByMarket(CategorieDTO filterDTO) {

        log.info("In getListCategoriesByMarket() to call api ",
                URL_GET_LIST_CATEGORIES_WITH_MARKET_DATA_API);

        return webClient
                .get()
                .uri(URL_GET_LIST_CATEGORIES_WITH_MARKET_DATA_API + filterDTO.getUrlService())
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        getClientResponseMonoServerException()
                )
                .bodyToFlux(CategoryMarket.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

}