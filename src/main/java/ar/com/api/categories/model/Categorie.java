package ar.com.api.categories.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Categorie implements Serializable {
 
 @JsonProperty("category_id")
 private String categoryId;

 @JsonProperty("name")
 private String name;

}
