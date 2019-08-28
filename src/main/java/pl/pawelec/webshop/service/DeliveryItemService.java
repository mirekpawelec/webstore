/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service;

import pl.pawelec.webshop.model.DeliveryItem;

import java.util.List;


/**
 * @author mirek
 */
public interface DeliveryItemService extends CrudService<DeliveryItem> {

    DeliveryItem getByLoadunitNo(String loadunitNo);

    List<DeliveryItem> getByDeliveryId(Long deliveryId);

    DeliveryItem newDeliveryItem();

    String moveItemsToRepository(Long placeId, List<DeliveryItem> deliveryItems);

    List<Object> getSummaryDelivery(Long id);
}
