package com.scooterrental.scooter_rental.model.dto.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.RentalPoint;
import com.scooterrental.scooter_rental.model.RentalPrice;
import com.scooterrental.scooter_rental.model.Scooter;
import com.scooterrental.scooter_rental.model.dto.*;
import org.locationtech.jts.geom.Point;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
        componentModel = "spring"
        )
public interface MapStructMapper {

    RentalPointDTO toRentalPointDTO(RentalPoint rentalPoint);

    @InheritInverseConfiguration
    @Mapping(target = "scooterList", ignore = true)
    RentalPoint toRentalPoint(RentalPointDTO rentalPointDTO);

    @Mapping(target = "x_coordinate", expression = "java(location.getX())")
    @Mapping(target = "y_coordinate", expression = "java(location.getY())")
    LocationDTO toLocationDTO(Point location);

    CityDTO toCityDTO(Catalog catalog);

    @InheritInverseConfiguration
    Catalog toCatalogFromCityDTO(CityDTO cityDTO);

//    @Mapping(ignore = true, target = "cityDTO.title")
//    Catalog toCatalogFromCityDTO(CityDTO cityDTO);

    ScooterDTO toScooterDTO(Scooter scooter);

    CatalogDTO toCatalogDTO(Catalog catalog);

    List<RentalPointDTO> toRentalPointsDTOList(List<RentalPoint> rentalPoints);

    List<ScooterDTO> toScooterDTOList(List<Scooter> scooterList);

    PriceTitleDTO toPriceTitleDTO(RentalPrice rentalPrice);

    RentalPointWithScooterDTO toRentalPointWithScooterDTO(RentalPoint rentalPoint);
}