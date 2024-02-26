package ar.com.api.categories.services;

import ar.com.api.categories.configuration.ExternalServerConfig;
import ar.com.api.categories.configuration.HttpServiceCall;
import ar.com.api.categories.dto.CategorieDTO;
import ar.com.api.categories.enums.ErrorTypeEnum;
import ar.com.api.categories.exception.ApiServerErrorException;
import ar.com.api.categories.model.Categorie;
import ar.com.api.categories.model.CategoryMarket;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class CategoriesApiServiceTest {

    @Mock
    private ExternalServerConfig externalServerConfigMock;

    @Mock
    private HttpServiceCall httpServiceCallMock;

    @InjectMocks
    private CategoriesApiService categoriesApiServiceMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        given(externalServerConfigMock.getListCategories())
                .willReturn("listCategoriesUrlMock");
        given(externalServerConfigMock.getListCategoriesMarketData())
                .willReturn("listCategoriesWithMarketUrlMock");
    }

    @AfterEach
    void tearDown() {
        reset(httpServiceCallMock, externalServerConfigMock);
    }

    @Test
    @DisplayName("Ensure successfully retrieval of CoinGecko service List all Categories")
    void getListOfCategoriesServiceTest_successfullyReturnListOfCategories(){
        Categorie expectedCategorie = Instancio.create(Categorie.class);
        given(httpServiceCallMock.getFluxObject(eq("listCategoriesUrlMock"), eq(Categorie.class)))
                .willReturn(Flux.just(expectedCategorie));

        Flux<Categorie> actualObject = categoriesApiServiceMock.getListOfCategories();

        StepVerifier
                .create(actualObject)
                .expectNext(expectedCategorie)
                .expectNextCount(0)
                .verifyComplete();

        verify(externalServerConfigMock, times(2)).getListCategories();
        verify(httpServiceCallMock).getFluxObject("listCategoriesUrlMock", Categorie.class);
    }

    @Test
    @DisplayName("Handle 4xx errors when retrieving CoinGecko List of Categories")
    void getListOfCategoriesServiceTest_HandleOnStatus4xx() {
        given(httpServiceCallMock.getFluxObject(eq("listCategoriesUrlMock"), eq(Categorie.class)))
                .willReturn(Flux.error(new ApiServerErrorException("Failed to retrieve info", "Upgrade Required",
                        HttpStatus.UPGRADE_REQUIRED, ErrorTypeEnum.GECKO_CLIENT_ERROR)));

        Flux<Categorie> actualObject = categoriesApiServiceMock.getListOfCategories();

        StepVerifier
                .create(actualObject)
                .expectErrorMatches(throwable ->
                    throwable instanceof ApiServerErrorException &&
                    throwable.getMessage().contains("Failed to retrieve info") &&
                            ((ApiServerErrorException) throwable).getHttpStatus().is4xxClientError() &&
                            ((ApiServerErrorException) throwable).getErrorType()
                                    .equals(ErrorTypeEnum.GECKO_CLIENT_ERROR)
                ).verify();

        verify(externalServerConfigMock, times(2)).getListCategories();
        verify(httpServiceCallMock).getFluxObject("listCategoriesUrlMock", Categorie.class);
    }

    @Test
    @DisplayName("Handle 5xx errors when retrieving CoinGecko List of Categories")
    void getListOfCategoriesServiceTest_HandleOnStatus5xx() {
        given(httpServiceCallMock.getFluxObject(eq("listCategoriesUrlMock"), eq(Categorie.class)))
                .willReturn(Flux.error(new ApiServerErrorException("Server Error", "Service Unavailable",
                        HttpStatus.SERVICE_UNAVAILABLE, ErrorTypeEnum.GECKO_SERVER_ERROR)));

        Flux<Categorie> actualObject = categoriesApiServiceMock.getListOfCategories();

        StepVerifier.create(actualObject)
                .expectErrorMatches(throwable -> throwable instanceof ApiServerErrorException &&
                        throwable.getMessage().contains("Server Error") &&
                        ((ApiServerErrorException) throwable).getHttpStatus().is5xxServerError() &&
                        ((ApiServerErrorException) throwable).getErrorType().equals(ErrorTypeEnum.GECKO_SERVER_ERROR))
                .verify();

        verify(externalServerConfigMock, times(2)).getListCategories();
        verify(httpServiceCallMock).getFluxObject("listCategoriesUrlMock", Categorie.class);
    }

    @Test
    @DisplayName("Ensure successfully retrieval of CoinGecko service List all Categories With Market Data")
    void getListCategoriesByMarketServiceTest_SuccessfullyReturnListOfCategoriesWithMarketData() {
        CategorieDTO filterDTO = CategorieDTO.builder().order(Optional.empty()).build();
        Collection<CategoryMarket> expectedListOfCategoriesMarket = Instancio
                .ofList(CategoryMarket.class).size(3).create();
        given(httpServiceCallMock.getFluxObject(
                eq("listCategoriesWithMarketUrlMock?order=market_cap_desc"),
                eq(CategoryMarket.class)))
                .willReturn(Flux.fromIterable(expectedListOfCategoriesMarket));

        Flux<CategoryMarket> resultFlux = categoriesApiServiceMock.getListCategoriesByMarket(filterDTO);

        StepVerifier.create(resultFlux)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(item -> true)
                .consumeRecordedWith(actualListOfCategoriesMarket -> {
                    Assertions.assertEquals(3, actualListOfCategoriesMarket.size());
                    Assertions.assertTrue(actualListOfCategoriesMarket.containsAll(expectedListOfCategoriesMarket));
                })
                .verifyComplete();

        verify(externalServerConfigMock, times(2)).getListCategoriesMarketData();
        verify(httpServiceCallMock).getFluxObject("listCategoriesWithMarketUrlMock?order=market_cap_desc",
                CategoryMarket.class);
    }

    @Test
    @DisplayName("Handle 4xx errors when CoinGecko service List all Categories With Market Data")
    void getListCategoriesByMarketServiceTest_HandleOnStatus4xx(){
        CategorieDTO filterDTO = CategorieDTO.builder().order(Optional.empty()).build();
        given(httpServiceCallMock.getFluxObject(eq("listCategoriesWithMarketUrlMock?order=market_cap_desc"),
                eq(CategoryMarket.class)))
                .willReturn(Flux.error(new ApiServerErrorException("Failed to retrieve info", "Unprocessable Entity",
                        HttpStatus.UNPROCESSABLE_ENTITY, ErrorTypeEnum.GECKO_CLIENT_ERROR)));


        Flux<CategoryMarket> actualError = categoriesApiServiceMock.getListCategoriesByMarket(filterDTO);

        StepVerifier
                .create(actualError)
                .expectErrorMatches(throwable ->
                        throwable instanceof ApiServerErrorException &&
                                throwable.getMessage().contains("Failed to retrieve info") &&
                                ((ApiServerErrorException) throwable).getHttpStatus().is4xxClientError() &&
                                ((ApiServerErrorException) throwable).getErrorType()
                                        .equals(ErrorTypeEnum.GECKO_CLIENT_ERROR))
                .verify();

        verify(externalServerConfigMock, times(2)).getListCategoriesMarketData();
        verify(httpServiceCallMock).getFluxObject("listCategoriesWithMarketUrlMock?order=market_cap_desc",
                CategoryMarket.class);
    }

    @Test
    @DisplayName("Handle 5xx errors when CoinGecko service List all Categories With Market Data")
    void getListCategoriesByMarketServiceTest_HandleOnStatus5xx(){
        CategorieDTO filterDTO = CategorieDTO.builder().order(Optional.empty()).build();
        given(httpServiceCallMock.getFluxObject(eq("listCategoriesWithMarketUrlMock?order=market_cap_desc"),
                eq(CategoryMarket.class)))
                .willReturn(Flux.error(new ApiServerErrorException("Server Error", "Insufficient Storage",
                        HttpStatus.INSUFFICIENT_STORAGE, ErrorTypeEnum.GECKO_SERVER_ERROR)));


        Flux<CategoryMarket> actualError = categoriesApiServiceMock.getListCategoriesByMarket(filterDTO);

        StepVerifier
                .create(actualError)
                .expectErrorMatches(throwable ->
                        throwable instanceof ApiServerErrorException &&
                                throwable.getMessage().contains("Server Error") &&
                                ((ApiServerErrorException) throwable).getHttpStatus().is5xxServerError() &&
                                ((ApiServerErrorException) throwable).getErrorType()
                                        .equals(ErrorTypeEnum.GECKO_SERVER_ERROR))
                .verify();

        verify(externalServerConfigMock, times(2)).getListCategoriesMarketData();
        verify(httpServiceCallMock).getFluxObject("listCategoriesWithMarketUrlMock?order=market_cap_desc",
                CategoryMarket.class);
    }
}