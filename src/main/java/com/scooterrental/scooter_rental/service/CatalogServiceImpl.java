package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.dto.CatalogDTO;
import com.scooterrental.scooter_rental.repository.CatalogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<CatalogDTO> findAllParentIdIsNull() {
        List<Catalog> catalog = catalogRepository.findByParentIdIsNull();
        if (catalog.isEmpty()) {
            throw new ServiceException("No such entity with parent_id=null");
        }
        return catalog
                .stream()
                .map(this::convertCatalogToCatalogDTO)
                .collect(Collectors.toList());
    }

    public List<CatalogDTO> findAllSecondLevelTree(String parentTitle) {
        Long parentId = catalogRepository.getByTitle(parentTitle).getId();
        if (parentId == null) {
            throw new ServiceException("No such entity with parentTitle=" + parentTitle);
        }
        List<Catalog> catalogList = catalogRepository.findAllByParentId(parentId);
        return catalogList
                .stream()
                .map(this::convertCatalogToCatalogDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Catalog getCatalogByTitle(String title) {
        return catalogRepository.getCatalogByTitle(title);
    }

    public Integer countChildrenOfItem(Long itemId) {
        return catalogRepository.countCatalogByParentId(itemId);
    }

    public String getPathString(String region) {
        StringBuilder regionForSearch = new StringBuilder(region.substring(0, 1).toUpperCase());
        String str = region.replace("_", " ");
        for (int i = 1; i < str.length(); i++) {
            if (str.substring(i - 1, i).equals(" ")) {
                regionForSearch.append(str.substring(i, i + 1).toUpperCase());
            } else {
                regionForSearch.append(str.substring(i, i + 1));
            }
        }
        return regionForSearch.toString();
    }

    private CatalogDTO convertCatalogToCatalogDTO(Catalog catalog) {
        CatalogDTO catalogDTO = new CatalogDTO();
        catalogDTO.setId(catalog.getId());
        catalogDTO.setTitle(catalog.getTitle());
        return catalogDTO;
    }
}
