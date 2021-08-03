package com.scooterrental.scooter_rental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
public class CatalogDTO extends RepresentationModel<CatalogDTO> implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;


}
