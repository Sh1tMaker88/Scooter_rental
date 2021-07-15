package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Slf4j
@RequestMapping("/rental_points")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public ResponseEntity<List<Catalog>> getLevelWithoutParent() {
        List<Catalog> catalogList = catalogService.findAllParentIdIsNull();
        for (Catalog catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points")
                    .slash(catalog.getTitle().toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

    @GetMapping("belarus")
    public ResponseEntity<List<Catalog>> getBelarusChildren() {
        List<Catalog> catalogList = catalogService.findAllSecondLevelTree("Belarus");
        for (Catalog catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/belarus")
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }


    @GetMapping("belarus/brest_region")
    public ResponseEntity<List<Catalog>> getBrestRegionChildren() {
        List<Catalog> catalogList = catalogService.findAllSecondLevelTree("Brest Region");
        for (Catalog catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/belarus/brest_region")
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

    @GetMapping("belarus/gomel_region")
    public ResponseEntity<List<Catalog>> getGomelRegionChildren() {
        List<Catalog> catalogList = catalogService.findAllSecondLevelTree("Gomel Region");
        for (Catalog catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/belarus/gomel_region")
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

    @GetMapping("belarus/grodno_region")
    public ResponseEntity<List<Catalog>> getGrodnoRegionChildren() {
        List<Catalog> catalogList = catalogService.findAllSecondLevelTree("Grodno Region");
        for (Catalog catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/belarus/grodno_region")
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

    @GetMapping("belarus/mogilev_region")
    public ResponseEntity<List<Catalog>> getMogilevRegionChildren() {
        List<Catalog> catalogList = catalogService.findAllSecondLevelTree("Mogilev Region");
        for (Catalog catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/belarus/mogilev_region")
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

    @GetMapping("belarus/minsk_region")
    public ResponseEntity<List<Catalog>> getMinskRegionChildren() {
        List<Catalog> catalogList = catalogService.findAllSecondLevelTree("Minsk Region");
        for (Catalog catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/belarus/minsk_region")
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

    @GetMapping("belarus/vitebsk_region")
    public ResponseEntity<List<Catalog>> getVitebskRegionChildren() {
        List<Catalog> catalogList = catalogService.findAllSecondLevelTree("Vitebsk Region");
        for (Catalog catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/belarus/vitebsk_region")
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }
}
