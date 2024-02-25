package ar.com.api.categories.services;

import ar.com.api.categories.configuration.ExternalServerConfig;
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

    private final HttpServiceCall httpServiceCall;
    private final ExternalServerConfig externalServerConfig;

    public CoinGeckoServiceStatus(HttpServiceCall httpServiceCall, ExternalServerConfig serverConfig) {
        this.httpServiceCall = httpServiceCall;
        this.externalServerConfig = serverConfig;
    }

    public Mono<Ping> getStatusCoinGeckoService() {

        log.info("Calling method {} : ", externalServerConfig.getPing());

        return httpServiceCall.getMonoObject(externalServerConfig.getPing(), Ping.class);
    }

}
