/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.DeliveryItem;
import pl.pawelec.webshop.model.enum_.ProductState;
import pl.pawelec.webshop.model.enum_.ProductStatus;
import pl.pawelec.webshop.model.Repository;
import pl.pawelec.webshop.model.dao.DeliveryItemDao;
import pl.pawelec.webshop.model.enum_.QualityStatus;
import pl.pawelec.webshop.service.DeliveryItemService;
import pl.pawelec.webshop.service.RepositoryService;

/**
 *
 * @author mirek
 */
@Service
@Transactional
public class DeliveryItemServiceImpl implements DeliveryItemService, Serializable{

    @Autowired
    private DeliveryItemDao deliveryItemDao;
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Override
    public void create(DeliveryItem deliveryItem) {
        System.out.println("### DeliveryItemServiceImpl; deliveryItem=" + deliveryItem);
        if(deliveryItem.getStatus()==null){
            deliveryItem.setStatus( ProductStatus.OK.getProductStatusType() );
        }
        if(deliveryItem.getItemId()==null){
            deliveryItemDao.create(deliveryItem);
        } else {
            deliveryItemDao.update(deliveryItem);
        }
    }

    @Override
    public void update(DeliveryItem deliveryItem) {
        deliveryItemDao.update(deliveryItem);
    }

    @Override
    public void delete(DeliveryItem deliveryItem) {
        deliveryItemDao.delete(deliveryItem);
    }

    @Override
    public void deleteById(Long id) {
        deliveryItemDao.deleteById(id);
    }

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

    @Override
    public String moveItemsToRepository(Long placeId, List<DeliveryItem> deliveryItems) {
        Repository repository = null;
        try{
            for(DeliveryItem item : deliveryItems){
                repository = new Repository();
                repository.setLoadunitNo( item.getLoadunitNo() );
                repository.getProduct().setProductId( item.getProduct().getProductId() );
                repository.setQuantity( item.getQuantity() );
                repository.getPlace().setPlaceId( placeId );
                repository.setState( ProductState.NEW.name() );
                repository.setQualityStatus( QualityStatus._0.getNumer() );
                repository.setStatus( ProductStatus.OK.getProductStatusType() );
                repository.setLastModifikationDate( LocalDateTime.now() );
                repository.setCreateDate( item.getCreateDate() );
                repositoryService.create(repository);
                item.setStatus( ProductStatus.FI.getProductStatusType() );
                this.update(item); 
            };
        } catch(Exception e){
           return "false"; 
        }
        return "true";
    }

    @Override
    public List<Object> getSummaryDelivery(Long id) {
        return deliveryItemDao.getSummaryDelivery(id);
    }
    

}