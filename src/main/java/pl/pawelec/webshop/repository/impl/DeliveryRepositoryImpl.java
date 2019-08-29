/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.Delivery;
import pl.pawelec.webshop.model.Storageplace;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.DeliveryRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DeliveryRepositoryImpl extends AbstrRepository<Delivery> implements DeliveryRepository {

    private static String DEFAULT_PLACE_NO = "select place";
    private static String DEFAULT_PLACE_NAME = "default place";
    private List<Delivery> deliverysList;

    public List<Delivery> getByDriver(String firstName, String lastName, String phoneNo) {
        return deliverysList = this.getAll().parallelStream()
                .filter((delivery) -> {
                    if (firstName != null && !firstName.isEmpty()) {
                        return delivery.getDriverFirstName().equals(firstName);
                    } else {
                        return true;
                    }
                })
                .filter((delivery) -> {
                    if (lastName != null && !lastName.isEmpty()) {
                        return delivery.getDriverLastName().equals(lastName);
                    } else {
                        return true;
                    }
                })
                .filter((delivery) -> {
                    if (phoneNo != null && !phoneNo.isEmpty()) {
                        return delivery.getDriverPhoneNo().equals(phoneNo);
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<Delivery> getByTruck(String type, String truckNumber, String trailerOrCaravanNumber) {
        return deliverysList = this.getAll().parallelStream()
                .filter((delivery) -> {
                    if (type != null && !type.isEmpty()) {
                        return delivery.getTruckType().equals(type);
                    } else {
                        return true;
                    }
                })
                .filter((delivery) -> {
                    if (truckNumber != null && !truckNumber.isEmpty()) {
                        return delivery.getTruckNumber().equals(truckNumber);
                    } else {
                        return true;
                    }
                })
                .filter((delivery) -> {
                    if (trailerOrCaravanNumber != null && !trailerOrCaravanNumber.isEmpty()) {
                        return delivery.getTrailerOrCaravanNumber().equals(trailerOrCaravanNumber);
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }

    private Long getIdDefaultPlace() {
        Storageplace defaultPlace = (Storageplace) getEntityManager().createQuery("from Storageplace WHERE place_no = :placeNo AND name = :name")
                .setParameter("placeNo", DEFAULT_PLACE_NO).setParameter("name", DEFAULT_PLACE_NAME).getSingleResult();
        return defaultPlace.getPlaceId();
    }

    public Delivery createAndGetDelivery(Delivery entity) {
        EntityManager em = getEntityManager();
        em.persist(entity);
        return entity;
    }

    public Delivery startProcessDelivery() {
        return createAndGetDelivery(new Delivery(new Storageplace(getIdDefaultPlace())));
    }
}
