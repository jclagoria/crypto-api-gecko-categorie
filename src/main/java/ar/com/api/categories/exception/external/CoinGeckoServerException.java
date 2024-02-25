package ar.com.api.categories.exception.external;

public class CoinGeckoServerException extends RuntimeException {

    public CoinGeckoServerException(String message) {
        super(message);
    }

}
