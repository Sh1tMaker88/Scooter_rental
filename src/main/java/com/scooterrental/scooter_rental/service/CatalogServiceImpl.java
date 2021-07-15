package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.repository.CatalogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<Catalog> findAllParentIdIsNull() {
        List<Catalog> catalog = catalogRepository.findByParentIdIsNull();
        if (catalog.isEmpty()) {
            throw new ServiceException("No such entity with parent_id=null");
        }
        return catalog;
    }

    public List<Catalog> findAllSecondLevelTree(String parentTitle) {
        Long parentId = catalogRepository.getByTitle(parentTitle).getId();
        if (parentId == null) {
            throw new ServiceException("No such entity with parentTitle=" + parentTitle);
        }
        return catalogRepository.findAllByParentId(parentId);
    }

    public Integer countChildrenOfItem(Long itemId) {
        return catalogRepository.countCatalogByParentId(itemId);
    }
}
