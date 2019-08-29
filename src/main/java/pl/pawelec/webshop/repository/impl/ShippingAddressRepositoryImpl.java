/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.ShippingAddress;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.ShippingAddressRepository;

@Repository
public class ShippingAddressRepositoryImpl extends AbstrRepository<ShippingAddress> implements ShippingAddressRepository {

    public ShippingAddress createAndReturn(ShippingAddress shippingAddress) {
        getEntityManager().persist(shippingAddress);
        return shippingAddress;
    }
}
