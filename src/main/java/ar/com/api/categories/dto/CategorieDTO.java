package ar.com.api.categories.dto;

import java.util.Optional;

import lombok.Builder;
import lombok.Getter;

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
