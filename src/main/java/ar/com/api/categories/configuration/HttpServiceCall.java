package ar.com.api.categories.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .bodyToMono(responseType)
                .onErrorResume(Exception.class, e ->
                        Mono.error(e.getCause()) );
    }

    public <T> Flux<T> getFluxObject(String urlEndpoint, Class<T> responseType) {
        return webClient.get()
                .uri(urlEndpoint)
                .retrieve()
                .bodyToFlux(responseType)
                .onErrorResume(Exception.class, e ->
                        Mono.error(e.getCause()) );
    }

}
