package com.scooterrental.scooter_rental.model.dto.mapper;

import com.scooterrental.scooter_rental.model.*;
import com.scooterrental.scooter_rental.model.dto.*;
import com.scooterrental.scooter_rental.security.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
        )
public interface MapStructMapper {

    RentalPointDTO toRentalPointDTO(RentalPoint rentalPoint);

    @InheritInverseConfiguration
    @Mapping(target = "scooterList", ignore = true)
    RentalPoint toRentalPoint(RentalPointDTO rentalPointDTO);

    CityDTO toCityDTO(Catalog catalog);

    @InheritInverseConfiguration
    Catalog toCatalogFromCityDTO(CityDTO cityDTO);

    UserSlimDTO toUserSlimDTO(User user);

    ScooterSlimDTO toScooterSlimDTO(Scooter scooter);

    RentalPointSlimDTO toRentalPointSlimDTO(RentalPoint rentalPoint);

    RentHistoryDTO toRentHistoryDTO(RentHistory rentHistory);

    List<RentHistoryDTO> toRentHistoryListDTO(List<RentHistory> rentHistory);

    ScooterDTO toScooterDTO(Scooter scooter);

    CatalogDTO toCatalogDTO(Catalog catalog);

    List<RentalPointDTO> toRentalPointsDTOList(List<RentalPoint> rentalPoints);

    List<ScooterDTO> toScooterDTOList(List<Scooter> scooterList);

    PriceTitleDTO toPriceTitleDTO(RentalPrice rentalPrice);

    StarterDTO toStarterDTO(Catalog catalog);
}
