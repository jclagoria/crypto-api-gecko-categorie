package ar.com.api.categories.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryMarket {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("market_cap")
    private double marketCap;

    @JsonProperty("market_cap_change_24h")
    private long marketCapChange24h;

    @JsonProperty("content")
    private String content;

    @JsonProperty("top_3_coins")
    private List<String> top3Coins;

    @JsonProperty("volume_24h")
    private long volume24h;

    @JsonProperty("updated_at")
    private String updatedAt;

}
