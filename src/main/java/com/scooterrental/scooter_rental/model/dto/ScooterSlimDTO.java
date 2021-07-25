package com.scooterrental.scooter_rental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScooterSlimDTO extends RepresentationModel<ScooterSlimDTO> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("model")
    private String model;
}
