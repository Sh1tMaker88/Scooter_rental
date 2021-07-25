package com.scooterrental.scooter_rental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class UserSlimDTO extends RepresentationModel<UserSlimDTO> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;
}
