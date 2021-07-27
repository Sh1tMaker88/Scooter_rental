package com.scooterrental.scooter_rental.repository;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.dto.CatalogDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    List<Catalog> findByParentIdIsNull();

    List<Catalog> findAllByParentId(Long id);

    Optional<Catalog> getByTitle(String title);

    Integer countCatalogByParentId(Long itemId);

}
