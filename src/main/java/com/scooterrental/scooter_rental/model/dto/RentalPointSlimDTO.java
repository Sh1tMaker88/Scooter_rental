package com.scooterrental.scooter_rental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class RentalPointSlimDTO extends RepresentationModel<RentalPointSlimDTO> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;
}
