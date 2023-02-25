package ar.com.api.categories.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.com.api.categories.model.Categorie;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class CategoriesApiService {

 @Value("${api.listCategories}")
 private String URL_GET_LIST_CATAGORIES_API;

 private WebClient webClient;

 public CategoriesApiService(WebClient wClient) {
  this.webClient = wClient;
 }

 public Flux<Categorie> getListOfCategories() {

  log.info("In getlistOfCategories() to call api ", URL_GET_LIST_CATAGORIES_API);

  return webClient
              .get()
              .uri(URL_GET_LIST_CATAGORIES_API)
              .retrieve()
              .bodyToFlux(Categorie.class)
              .doOnError(throwable -> log.error("The service is unavailable!", throwable))
              .onErrorComplete();
 }
 
}