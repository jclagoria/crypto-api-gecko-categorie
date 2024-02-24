package ar.com.api.categories.configuration;

import ar.com.api.categories.enums.ErrorTypeEnum;
import ar.com.api.categories.exception.ApiServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class HttpServiceCall {

    private final WebClient webClient;

    public HttpServiceCall(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> Mono<T> getMonoObject(String urlEndPoint, Class<T> responseType) {

        return webClient.get()
                .uri(urlEndPoint)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response
                                .bodyToMono(Map.class)
                                .flatMap(errorMessage -> {
                                    String errorBody = errorMessage
                                            .getOrDefault("error", "Unknown error").toString();
                                    HttpStatus status = (HttpStatus) response.statusCode();

                                    return Mono.error(new ApiServerErrorException("Error occurred", errorBody,
                                            status, ErrorTypeEnum.GECKO_CLIENT_ERROR));
                                })
                ).onStatus(
                        status -> status.is5xxServerError(),
                        response -> response
                                .bodyToMono(Map.class)
                                .flatMap(errorMessage -> {
                                    String errorBody = errorMessage
                                            .getOrDefault("error", "Unknown error").toString();
                                    HttpStatus status = (HttpStatus) response.statusCode();

                                    return Mono.error(new ApiServerErrorException("Error occurred", errorBody,
                                            status, ErrorTypeEnum.GECKO_SERVER_ERROR));
                                }
                ))
                .bodyToMono(responseType)
                .onErrorResume(Exception.class, e ->
                        Mono.error(e.getCause()) );
    }

    public <T> Flux<T> getFluxObject(String urlEndpoint, Class<T> responseType) {
        return webClient.get()
                .uri(urlEndpoint)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response
                                .bodyToMono(Map.class)
                                .flatMap(errorMessage -> {
                                    String errorBody = errorMessage
                                            .getOrDefault("error", "Unknown error").toString();
                                    HttpStatus status = (HttpStatus) response.statusCode();

                                    return Mono.error(new ApiServerErrorException("Error occurred", errorBody,
                                            status, ErrorTypeEnum.GECKO_CLIENT_ERROR));
                                })
                ).onStatus(
                        status -> status.is5xxServerError(),
                        response -> response
                                .bodyToMono(Map.class)
                                .flatMap(errorMessage -> {
                                            String errorBody = errorMessage
                                                    .getOrDefault("error", "Unknown error").toString();
                                            HttpStatus status = (HttpStatus) response.statusCode();

                                            return Mono.error(new ApiServerErrorException("Error occurred", errorBody,
                                                    status, ErrorTypeEnum.GECKO_SERVER_ERROR));
                                        }
                                ))
                .bodyToFlux(responseType)
                .onErrorResume(Exception.class, e ->
                        Mono.error(e.getCause()) );
    }

}
