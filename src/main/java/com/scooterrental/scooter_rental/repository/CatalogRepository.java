package com.scooterrental.scooter_rental.repository;

import com.scooterrental.scooter_rental.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    List<Catalog> findByParentIdIsNull();

    List<Catalog> findAllByParentId(Long id);

    Catalog getByTitle(String title);

    Integer countCatalogByParentId(Long itemId);
}
