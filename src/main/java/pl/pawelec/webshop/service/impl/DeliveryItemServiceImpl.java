/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.DeliveryItem;
import pl.pawelec.webshop.model.LoadUnit;
import pl.pawelec.webshop.repository.DeliveryItemDao;
import pl.pawelec.webshop.model.status.ProductState;
import pl.pawelec.webshop.model.status.ProductStatus;
import pl.pawelec.webshop.model.status.QualityStatus;
import pl.pawelec.webshop.service.DeliveryItemService;
import pl.pawelec.webshop.service.RepositoryService;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DeliveryItemServiceImpl implements DeliveryItemService, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryItemServiceImpl.class);

    @Autowired
    private DeliveryItemDao deliveryItemDao;

    @Autowired
    private RepositoryService repositoryService;

    @Transactional
    @Override
    public void create(DeliveryItem deliveryItem) {
        if (!Optional.ofNullable(deliveryItem.getItemId()).isPresent()) {
            LOGGER.info("Create item");
            deliveryItemDao.create(deliveryItem);
        } else {
            LOGGER.info("Update item");
            deliveryItemDao.update(deliveryItem);
        }
    }

    @Transactional
    @Override
    public void update(DeliveryItem deliveryItem) {
        deliveryItemDao.update(deliveryItem);
    }

    @Transactional
    @Override
    public void delete(DeliveryItem deliveryItem) {
        deliveryItemDao.delete(deliveryItem);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        deliveryItemDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        deliveryItemDao.deleteAll();
    }

    @Override
    public DeliveryItem getOneById(Long id) {
        return deliveryItemDao.getOneById(id);
    }

    @Override
    public List<DeliveryItem> getAll() {
        return deliveryItemDao.getAll();
    }

    @Override
    public Long count() {
        return deliveryItemDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return deliveryItemDao.exists(id);
    }

    @Override
    public DeliveryItem getByLoadunitNo(String loadunitNo) {
        return deliveryItemDao.getByLoadunitNo(loadunitNo);
    }

    @Override
    public List<DeliveryItem> getByDeliveryId(Long deliveryId) {
        return deliveryItemDao.getByDeliveryId(deliveryId);
    }

    @Override
    public DeliveryItem newDeliveryItem() {
        return new DeliveryItem();
    }

    @Transactional
    @Override
    public String moveItemsToRepository(Long placeId, List<DeliveryItem> deliveryItems) {
        LoadUnit repository = null;
        try {
            for (DeliveryItem item : deliveryItems) {
                repository = new LoadUnit();
                repository.setLoadunitNo(item.getLoadunitNo());
                repository.getProduct().setProductId(item.getProduct().getProductId());
                repository.setQuantity(item.getQuantity());
                repository.getPlace().setPlaceId(placeId);
                repository.setState(ProductState.NEW.name());
                repository.setQualityStatus(QualityStatus._0.getNumer());
                repository.setStatus(ProductStatus.OK.name());
                repository.setLastModificationDate(LocalDateTime.now());
                repository.setCreateDate(item.getCreateDate());
                repositoryService.create(repository);
                item.setStatus(ProductStatus.OK.name());
                this.update(item);
            }
            ;
        } catch (Exception e) {
            return "false";
        }
        return "true";
    }

    @Override
    public List<Object> getSummaryDelivery(Long id) {
        return deliveryItemDao.getSummaryDelivery(id);
    }


}
