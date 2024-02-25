package ar.com.api.categories.handler;

import ar.com.api.categories.model.Ping;
import ar.com.api.categories.services.CoinGeckoServiceStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class HealthApiHandler {

    private final CoinGeckoServiceStatus serviceStatus;

    public Mono<ServerResponse> getStatusServiceCoinGecko(ServerRequest serverRequest) {

        log.info("In getStatusServiceCoinGecko, handling request: {}", serverRequest);

        return serviceStatus.getStatusCoinGeckoService()
                .flatMap(ping -> ServerResponse.ok().bodyValue(ping))
                .onErrorResume( e -> {
                    log.error("Error fetching CoinGecko service status", e);
                    return ServerResponse.status(500).bodyValue(e.getMessage());
                });
    }

}
