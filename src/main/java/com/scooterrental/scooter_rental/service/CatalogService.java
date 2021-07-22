package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.dto.CatalogDTO;

import java.util.List;

public interface CatalogService {

    List<CatalogDTO> findAllParentIdIsNull();

    List<CatalogDTO> findAllSecondLevelTree(String parentTitle);

    Catalog getCatalogByTitle(String title);

    Integer countChildrenOfItem(Long itemId);

    String getPathString(String region);
}
