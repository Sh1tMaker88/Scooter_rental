package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.dto.CatalogDTO;
import com.scooterrental.scooter_rental.service.CatalogService;
import com.scooterrental.scooter_rental.service.RentalPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@RequestMapping("/rental_points")
public class CatalogController {

    private final CatalogService catalogService;
    private final RentalPointService rentalPointService;

    public CatalogController(CatalogService catalogService, RentalPointService rentalPointService) {
        this.catalogService = catalogService;
        this.rentalPointService = rentalPointService;
    }

    //country endpoints
    @GetMapping
    public ResponseEntity<List<CatalogDTO>> getLevelWithoutParent() {
        List<CatalogDTO> catalogList = catalogService.findAllParentIdIsNull();
        for (CatalogDTO catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points")
                    .slash(catalog.getTitle().toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            Link addCatalogItem = linkTo(CatalogController.class)
                    .withRel("add_new_country")
                    .withType("POST");
            catalog.add(catalogItem, addCatalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Catalog> addNewCountry(@RequestBody Catalog country) {
        return ResponseEntity.ok(catalogService.saveNewCountry(country));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{countryTitle}")
    public ResponseEntity<Catalog> updateCountry(@RequestBody Catalog country,
                                                 @PathVariable String countryTitle) {
        String countryToSearch = catalogService.makeFirstLetterUppercase(countryTitle);
        Long catalogId = catalogService.getCatalogByTitle(countryToSearch).getId();
        country.setId(catalogId);
        return ResponseEntity.ok(catalogService.updateCountry(country));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{countryTitle}")
    public ResponseEntity<Catalog> deleteCountry(@PathVariable String countryTitle) {
        String countryToSearch = catalogService.makeFirstLetterUppercase(countryTitle);
        catalogService.deleteCatalogItemByTitle(countryToSearch);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //region endpoints
    @GetMapping("/{country}")
    public ResponseEntity<CollectionModel<CatalogDTO>> getCountryChildren(@PathVariable String country) {
        String countryToSearch = catalogService.makeFirstLetterUppercase(country);
        List<CatalogDTO> catalogList = catalogService.findAllSecondLevelTree(countryToSearch);
        for (CatalogDTO catalog : catalogList) {
            Integer childrenOfItem = 0;
            if (catalog.getTitle().contains("Region")) {
                childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            } else {
                childrenOfItem = rentalPointService.countRentalPointByCityId(catalog.getId());
                catalog.add(linkTo(StarterController.class)
                        .slash("/rental_points/belarus/belarus")
                        .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                        .withRel("Elements in section: " + childrenOfItem));
                continue;
            }
            catalog.add(linkTo(StarterController.class)
                    .slash("/rental_points/belarus")
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem));
        }
        Link linkToCreate = linkTo(CatalogController.class)
                .slash("/" + country)
                .withRel("create_new_region_for_this_country")
                .withType("POST");
        Link linkToUpdate = linkTo(CatalogController.class)
                .slash("/" + country)
                .withRel("update_country")
                .withType("PUT");
        Link linkToDelete = linkTo(methodOn(CatalogController.class)
                .deleteCountry(country))
                .withRel("delete_this_country")
                .withType("DELETE");

        return ResponseEntity.ok(CollectionModel.of(catalogList, linkToCreate, linkToUpdate, linkToDelete));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{country}")
    public ResponseEntity<Catalog> addNewRegion(@PathVariable String country,
                                                @RequestBody Catalog catalog) {
        Catalog catalogItem = catalogService.saveNewRegionOrCity(catalog, country);
        return ResponseEntity.ok(catalogItem);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{country}/{region}")
    public ResponseEntity<Catalog> updateRegion(@PathVariable String country,
                                                @PathVariable String region,
                                                @RequestBody Catalog catalogItem) {
        catalogItem.setId(catalogService.getCatalogByTitle(region).getId());
        Catalog savedItem = catalogService.saveCatalogItem(catalogItem, country);
        return ResponseEntity.ok(savedItem);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{country}/{region}")
    public ResponseEntity<Catalog> deleteRegion(@PathVariable String country,
                                                @PathVariable String region) {
        catalogService.deleteCatalogItemByTitle(region);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //city endpoints
    @GetMapping("/{country}/{region}")
    public ResponseEntity<CollectionModel<CatalogDTO>> getRegionChildren(@PathVariable String country,
                                                                         @PathVariable String region) {
        String pathString = catalogService.makeEveryWordStartsUppercase(region);
        List<CatalogDTO> catalogList = catalogService.findAllSecondLevelTree(pathString);
        for (CatalogDTO catalog : catalogList) {
            Integer childrenOfItem = rentalPointService.countRentalPointByCityId(catalog.getId());
            Link catalogItem = linkTo(CatalogController.class)
                    .slash("/" + country + "/" + region)
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Rental points in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }
        Link linkToCreate = linkTo(CatalogController.class)
                .slash("/" + country + "/" + region)
                .withRel("add_new_city_to_this_region")
                .withType("POST");
        Link linkToUpdate = linkTo(CatalogController.class)
                .slash("/" + country + "/" + region)
                .withRel("edit_this_region")
                .withType("PUT");
        Link linkToDelete = linkTo(methodOn(CatalogController.class)
                .deleteRegion(country, region))
                .withRel("delete_this_region")
                .withType("DELETE");

        return ResponseEntity.ok(CollectionModel.of(catalogList, linkToCreate, linkToUpdate, linkToDelete));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{country}/{region}")
    public ResponseEntity<Catalog> addNewCatalogItem(@PathVariable String country,
                                                     @PathVariable String region,
                                                     @RequestBody Catalog city) {
        Catalog catalogItem = catalogService.saveNewRegionOrCity(city, region);
        return ResponseEntity.ok(catalogItem);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{country}/{region}/{city}")
    public ResponseEntity<Catalog> updateCity(@PathVariable String country,
                                              @PathVariable String region,
                                              @PathVariable String city,
                                              @RequestBody Catalog catalog) {
        catalog.setId(catalogService.getCatalogByTitle(city).getId());
        Catalog savedItem = catalogService.saveCatalogItem(catalog, region);
        return ResponseEntity.ok(savedItem);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{country}/{region}/{city}")
    public ResponseEntity<Catalog> deleteCity(@PathVariable String country,
                                              @PathVariable String region,
                                              @PathVariable String city) {
        catalogService.deleteCatalogItemByTitle(city);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
