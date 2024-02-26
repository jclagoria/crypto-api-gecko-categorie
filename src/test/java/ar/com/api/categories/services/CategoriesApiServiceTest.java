package ar.com.api.categories.services;

import ar.com.api.categories.configuration.ExternalServerConfig;
import ar.com.api.categories.configuration.HttpServiceCall;
import ar.com.api.categories.enums.ErrorTypeEnum;
import ar.com.api.categories.exception.ApiServerErrorException;
import ar.com.api.categories.model.Categorie;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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
        when(externalServerConfigMock.getListCategories())
                .thenReturn("listCategoriesUrlMock");
    }

    @AfterEach
    void tearDown() {
        reset(httpServiceCallMock, externalServerConfigMock);
    }

    @Test
    @DisplayName("Ensure successfully retrieval of CoinGecko service List all Categories")
    void getListOfCategoriesServiceTest_successfullyReturnListOfCategories(){
        Categorie expectedCategorie = Instancio.create(Categorie.class);
        when(httpServiceCallMock.getFluxObject(eq("listCategoriesUrlMock"), eq(Categorie.class)))
                .thenReturn(Flux.just(expectedCategorie));

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
        when(httpServiceCallMock.getFluxObject(eq("listCategoriesUrlMock"), eq(Categorie.class)))
                .thenReturn(Flux.error(new ApiServerErrorException("Failed to retrieve info", "Upgrade Required",
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
        when(httpServiceCallMock.getFluxObject(eq("listCategoriesUrlMock"), eq(Categorie.class)))
                .thenReturn(Flux.error(new ApiServerErrorException("Server Error", "Service Unavailable",
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
}