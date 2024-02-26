package ar.com.api.categories.services;

import ar.com.api.categories.configuration.ExternalServerConfig;
import ar.com.api.categories.configuration.HttpServiceCall;
import ar.com.api.categories.dto.CategorieDTO;
import ar.com.api.categories.model.Categorie;
import ar.com.api.categories.model.CategoryMarket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class CategoriesApiService extends CoinGeckoServiceApi {

    @Value("${api.listCategoriesMarketData}")
    private String URL_GET_LIST_CATEGORIES_WITH_MARKET_DATA_API;

    private ExternalServerConfig externalServerConfig;

    private HttpServiceCall httpServiceCall;

    public CategoriesApiService(HttpServiceCall serviceCall, ExternalServerConfig serverConfig) {
        this.httpServiceCall = serviceCall;
        this.externalServerConfig = serverConfig;
    }

    public Flux<Categorie> getListOfCategories() {

        log.info("In getListOfCategories() to call api : {}",
                externalServerConfig.getListCategories());

        return httpServiceCall.getFluxObject(externalServerConfig.getListCategories(), Categorie.class);
    }

    public Flux<CategoryMarket> getListCategoriesByMarket(CategorieDTO filterDTO) {

        log.info("In getListCategoriesByMarket() to call api ",
                URL_GET_LIST_CATEGORIES_WITH_MARKET_DATA_API);

        return null;
    }

}