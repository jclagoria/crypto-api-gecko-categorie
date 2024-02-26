package ar.com.api.categories.handler;

import ar.com.api.categories.exception.ApiCustomException;
import ar.com.api.categories.model.Categorie;
import ar.com.api.categories.services.CategoriesApiService;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;

class CategoriesApiHandlerTest {

    @Mock
    private CategoriesApiService categoriesApiServiceMock;
    @Mock
    private ServerRequest serverRequestMock;
    @InjectMocks
    private CategoriesApiHandler apiHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        reset(categoriesApiServiceMock, serverRequestMock);
    }

    @Test
    @DisplayName("Ensure successful retrieval of CoinGecko List of Categories")
    void getListOfCategories_SuccessfullyReturnListOfCategories() {
        Categorie expectedCategorie = Instancio.create(Categorie.class);
        given(categoriesApiServiceMock.getListOfCategories())
                .willReturn(Flux.just(expectedCategorie));

        Mono<ServerResponse> actualResponse = apiHandler.getListOfCategories(serverRequestMock);

        StepVerifier
                .create(actualResponse)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Handle error during retrieval of categories")
    void getListOfCategories_WithError() {
        String expectedErrorMessage = "An expected error occurred in getStatusServiceCoinGecko";
        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        given(categoriesApiServiceMock.getListOfCategories())
                .willReturn(Flux.error(new ApiCustomException(expectedErrorMessage, expectedStatus)));

        Mono<ServerResponse> actualResponse = apiHandler.getListOfCategories(serverRequestMock);

        StepVerifier
                .create(actualResponse)
                .expectErrorMatches(throwable -> throwable instanceof ApiCustomException &&
                        expectedErrorMessage.equals(throwable.getMessage()) &&
                        expectedStatus.equals(((ApiCustomException) throwable).getHStatus()))
                .verify();
    }

}