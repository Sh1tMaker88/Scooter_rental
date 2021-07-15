package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.Catalog;

import java.util.List;

public interface CatalogService {

    List<Catalog> findAllParentIdIsNull();

    List<Catalog> findAllSecondLevelTree(String parentTitle);

    Integer countChildrenOfItem(Long itemId);
}
