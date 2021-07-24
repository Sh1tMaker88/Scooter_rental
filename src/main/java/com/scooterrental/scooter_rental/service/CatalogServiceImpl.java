package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.dto.CatalogDTO;
import com.scooterrental.scooter_rental.repository.CatalogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<CatalogDTO> findAllParentIdIsNull() {
        List<Catalog> catalog = catalogRepository.findByParentIdIsNull();
        if (catalog.isEmpty()) {
            log.warn("IN findAllParentIdIsNull - no such entity with parentID=null");
            throw new ServiceException("No such entity with parent_id=null");
        }
        return catalog
                .stream()
                .map(this::convertCatalogToCatalogDTO)
                .collect(Collectors.toList());
    }

    public List<CatalogDTO> findAllSecondLevelTree(String parentTitle) {
        if (catalogRepository.getByTitle(parentTitle).isEmpty()) {
            log.warn("IN findAllSecondLevelTree - no such entity with parent title = {}", parentTitle);
            throw new ServiceException("No such entity with parentTitle=" + parentTitle);
        }
        Long parentId = catalogRepository.getByTitle(parentTitle).get().getId();
        List<Catalog> catalogList = catalogRepository.findAllByParentId(parentId);
        log.info("IN findAllSecondLevelTree - found {} entity with parentID={}", catalogList.size(), parentId);
        return catalogList
                .stream()
                .map(this::convertCatalogToCatalogDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Catalog getCatalogByTitle(String title) {
        String strToSearch = makeEveryWordStartsUppercase(title);
        if (catalogRepository.getByTitle(strToSearch).isEmpty()) {
            log.warn("IN getCatalogByTitle - no such catalog item with title={}", strToSearch);
            throw new ServiceException("No such catalog item with title=" + strToSearch);
        }
        Catalog catalogByTitle = catalogRepository.getByTitle(strToSearch).get();
        log.info("IN getCatalogByTitle - successfully found catalog item with title={}", strToSearch);
        return catalogByTitle;
    }

    @Override
    public Integer countChildrenOfItem(Long itemId) {
        Integer childrenOfItem = catalogRepository.countCatalogByParentId(itemId);
        log.info("IN countChildrenOfItem - catalog item with ID={} have {} children", itemId, childrenOfItem);
        return childrenOfItem;
    }

    @Override
    public Catalog saveNewCountry(Catalog country) {
        country.setTitle(makeFirstLetterUppercase(country.getTitle()));
        Catalog savedCountry = catalogRepository.save(country);
        log.info("IN saveNewCountry - country with title={} successfully saved", country.getTitle());
        return savedCountry;
    }

    @Override
    public Catalog updateCountry(Catalog country) {
        country.setParentId(null);
        country.setTitle(makeFirstLetterUppercase(country.getTitle()));
        Catalog updatedCountry = catalogRepository.save(country);
        log.info("IN updateCountry - country with title={} was successfully updated", country.getTitle());
        return updatedCountry;
    }

    @Override
    public void deleteCatalogItemByTitle(String title) {
        String strToSearch = makeEveryWordStartsUppercase(title);
        if (catalogRepository.getByTitle(strToSearch).isEmpty()) {
            log.warn("IN deleteCatalogItemByTitle - no such catalog item with title={}", strToSearch);
            throw new ServiceException("No such catalog item with title=" + strToSearch);
        }
        Catalog catalogItem = catalogRepository.getByTitle(strToSearch).get();
        if (!catalogRepository.findAllByParentId(catalogItem.getId()).isEmpty()) {
            log.warn("IN deleteCatalogItemByTitle - you try to delete catalog item={} with children " +
                    "what is not allowed", catalogItem.getTitle());
            throw new ServiceException("you try to delete catalog item=" + catalogItem.getTitle() +
                    " with children what is not allowed");
        }
        catalogRepository.delete(catalogItem);
        log.info("IN deleteCatalogItemByTitle - catalog item with title={} was successfully deleted", strToSearch);
    }

    @Override
    public Catalog saveCatalogItem(Catalog catalogItem, String parent) {
        String parentInDB = makeEveryWordStartsUppercase(parent);
        if (catalogRepository.getByTitle(parentInDB).isEmpty()) {
            log.warn("IN saveCatalogItem - no such catalog item with title={}", parentInDB);
            throw new ServiceException("No such catalog item with title=" + parentInDB);
        }
        Catalog catalogItemFromDB = catalogRepository.getByTitle(parentInDB).get();
        log.info("IN saveCatalogItem - found catalog item with title={}", catalogItemFromDB.getTitle());
        catalogItem.setParentId(catalogItemFromDB.getId());
        catalogItem.setTitle(makeEveryWordStartsUppercase(catalogItem.getTitle()));
        Catalog savedCatalog = catalogRepository.save(catalogItem);
        log.info("IN saveCatalogItem - catalog item with title={} was saved", savedCatalog.getTitle());
        return savedCatalog;
    }

    @Override
    public String makeFirstLetterUppercase(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    @Override
    public String makeEveryWordStartsUppercase(String region) {
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
