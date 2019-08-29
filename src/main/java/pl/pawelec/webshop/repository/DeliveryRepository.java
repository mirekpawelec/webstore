/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository;

import pl.pawelec.webshop.model.Delivery;

import java.util.List;

public interface DeliveryRepository extends BaseCrudRepository<Delivery> {

    List<Delivery> getByDriver(String firstName, String lastName, String phoneNo);

    List<Delivery> getByTruck(String type, String truckNumber, String trailerOrCaravanNumber);

    Delivery createAndGetDelivery(Delivery entity);

    Delivery startProcessDelivery();
}
