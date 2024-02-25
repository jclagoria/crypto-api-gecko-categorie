package ar.com.api.categories.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class CategorieDTO implements IFilterDTO {

    private Optional<String> order;

    @Override
    public String getUrlService() {

        StringBuilder urlService = new StringBuilder();
        urlService.append("?order=").append(order.orElse("market_cap_desc"));

        return urlService.toString();
    }

}
