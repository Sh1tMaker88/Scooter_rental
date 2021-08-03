package com.scooterrental.scooter_rental.model.dto.mapper;

import com.scooterrental.scooter_rental.model.*;
import com.scooterrental.scooter_rental.model.dto.*;
import com.scooterrental.scooter_rental.security.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        componentModel = "spring"
        )
public interface MapStructMapper {

    RentalPointDTO toRentalPointDTO(RentalPoint rentalPoint);

//    @InheritInverseConfiguration
    @Mapping(target = "scooterList", ignore = true)
    @Mapping(target = "rentHistoryList", ignore = true)
    RentalPoint toRentalPoint(RentalPointDTO rentalPointDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scooterList", ignore = true)
    @Mapping(target = "rentHistoryList", ignore = true)
    RentalPoint updateRentalPointFromRentalPointDTO(RentalPointDTO rentalPointDTO,
                                                    @MappingTarget RentalPoint rentalPoint);

    CityDTO toCityDTO(Catalog catalog);

    @Mapping(target = "rentalPoints", ignore = true)
    Catalog toCityFromCityDTO(CityDTO cityDTO);

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
