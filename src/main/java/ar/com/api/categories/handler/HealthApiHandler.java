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

    private CoinGeckoServiceStatus serviceStatus;

    public Mono<ServerResponse> getStatusServiceCoinGecko(ServerRequest serverRequest) {

        log.info("In getStatusServiceCoinGecko");

        return ServerResponse
                .ok()
                .body(
                        serviceStatus.getStatusCoinGeckoService(),
                        Ping.class);
    }

}