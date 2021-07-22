package com.scooterrental.scooter_rental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceTitleDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;
}
