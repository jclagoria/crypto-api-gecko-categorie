package ar.com.api.categories.services;

import ar.com.api.categories.configuration.HttpServiceCall;
import ar.com.api.categories.model.Ping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceStatus {

    @Value("${api.ping}")
    private String URL_PING_SERVICE;

    private final HttpServiceCall httpServiceCall;

    public CoinGeckoServiceStatus(HttpServiceCall httpServiceCall) {
        this.httpServiceCall = httpServiceCall;
    }

    public Mono<Ping> getStatusCoinGeckoService() {

        log.info("Calling method: ", URL_PING_SERVICE);

        return httpServiceCall.getMonoObject(URL_PING_SERVICE, Ping.class);
    }

}
