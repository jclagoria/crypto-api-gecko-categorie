package ar.com.api.categories.services;

import ar.com.api.categories.configuration.ExternalServerConfig;
import ar.com.api.categories.configuration.HttpServiceCall;
import ar.com.api.categories.enums.ErrorTypeEnum;
import ar.com.api.categories.exception.ApiServerErrorException;
import ar.com.api.categories.model.Ping;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CoinGeckoServiceStatusTest {

    @Mock
    private HttpServiceCall httpServiceCallMock;
    @Mock
    private ExternalServerConfig externalServerConfigMock;
    @InjectMocks
    private CoinGeckoServiceStatus serviceStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(externalServerConfigMock.getPing()).thenReturn("pingUrlGeckoMock");
    }

    @AfterEach
    void tearDown() {
        reset(httpServiceCallMock, externalServerConfigMock);
    }

    @Test
    @DisplayName("Ensure successful retrieval of CoinGecko service status")
    void getStatusCoinGeckoServiceTest_successfullyReturnObject() {
        Ping expectedPingObject = Instancio.create(Ping.class);
        when(httpServiceCallMock.getMonoObject(eq("pingUrlGeckoMock"), eq(Ping.class)))
                .thenReturn(Mono.just(expectedPingObject));

        Mono<Ping> actualObject = serviceStatus.getStatusCoinGeckoService();

        StepVerifier.create(actualObject)
                .expectNext(expectedPingObject)
                .verifyComplete();

        verify(externalServerConfigMock, times(2)).getPing();
        verify(httpServiceCallMock).getMonoObject("pingUrlGeckoMock", Ping.class);
    }

    @Test
    @DisplayName("Handle 4xx errors when retrieving CoinGecko service status")
    void getStatusCoinGeckoServiceTest_handlesOnStatus4xx() {
        when(httpServiceCallMock.getMonoObject(eq("pingUrlGeckoMock"), eq(Ping.class)))
                .thenReturn(Mono.error(new ApiServerErrorException("Failed to retrieve info", "Failed Dependency",
                        HttpStatus.FAILED_DEPENDENCY, ErrorTypeEnum.GECKO_CLIENT_ERROR)));

        Mono<Ping> actualObject = serviceStatus.getStatusCoinGeckoService();
        StepVerifier.create(actualObject)
                .expectErrorMatches(throwable -> throwable instanceof ApiServerErrorException &&
                        throwable.getMessage().contains("Failed to retrieve info") &&
                        ((ApiServerErrorException) throwable).getHttpStatus().is4xxClientError() &&
                        ((ApiServerErrorException) throwable).getErrorType().equals(ErrorTypeEnum.GECKO_CLIENT_ERROR))
                .verify();

        verify(externalServerConfigMock, times(2)).getPing();
        verify(httpServiceCallMock).getMonoObject("pingUrlGeckoMock", Ping.class);
    }

    @Test
    @DisplayName("Handle 5xx errors when retrieving CoinGecko service status")
    void getStatusCoinGeckoServiceTest_handlesOnStatus5xx() {
        when(httpServiceCallMock.getMonoObject(eq("pingUrlGeckoMock"), eq(Ping.class)))
                .thenReturn(Mono.error(new ApiServerErrorException("Server Error", "un expected error on server occurred",
                        HttpStatus.INTERNAL_SERVER_ERROR, ErrorTypeEnum.GECKO_SERVER_ERROR)));

        Mono<Ping> actualObject = serviceStatus.getStatusCoinGeckoService();
        StepVerifier.create(actualObject)
                .expectErrorMatches(throwable -> throwable instanceof ApiServerErrorException &&
                        throwable.getMessage().contains("Server Error") &&
                        ((ApiServerErrorException) throwable).getHttpStatus().is5xxServerError() &&
                        ((ApiServerErrorException) throwable).getErrorType().equals(ErrorTypeEnum.GECKO_SERVER_ERROR))
                .verify();

        verify(externalServerConfigMock, times(2)).getPing();
        verify(httpServiceCallMock).getMonoObject("pingUrlGeckoMock", Ping.class);
    }
}