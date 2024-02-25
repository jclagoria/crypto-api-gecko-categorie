package ar.com.api.categories.handler;

import ar.com.api.categories.model.Ping;
import ar.com.api.categories.services.CoinGeckoServiceStatus;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

class HealthApiHandlerTest {

    @Mock
    private CoinGeckoServiceStatus serviceStatusMock;
    @Mock
    private ServerRequest sererRequestMock;

    private HealthApiHandler apiHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        apiHandler = new HealthApiHandler(serviceStatusMock);
    }

    @AfterEach
    void tearDown() {
        reset(sererRequestMock, serviceStatusMock);
    }

    @Test
    void getStatusServiceCoinGecko_Success() {
        Ping expectedPing = Instancio.create(Ping.class);
        given(serviceStatusMock.getStatusCoinGeckoService()).willReturn(Mono.just(expectedPing));

        Mono<ServerResponse> actualResponseMono = apiHandler.getStatusServiceCoinGecko(sererRequestMock);

        StepVerifier
                .create(actualResponseMono)
                .expectNextMatches(response ->
                        response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void getStatusServiceCOinGecko_Error() {
        given(serviceStatusMock.getStatusCoinGeckoService()).willReturn(Mono.error(new RuntimeException("Error occurred")));

        Mono<ServerResponse> responseActualMono = apiHandler.getStatusServiceCoinGecko(sererRequestMock);

        StepVerifier
                .create(responseActualMono)
                .expectNextMatches(response -> response.statusCode()
                        .equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }
}