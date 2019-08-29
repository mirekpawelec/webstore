/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.ShippingDetails;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.ShippingDetailsRepository;

@Repository
public class ShippingDetailsRepositoryImpl extends AbstrRepository<ShippingDetails> implements ShippingDetailsRepository {

    @Override
    public ShippingDetails createAndReturn(ShippingDetails shippingDetails) {
        getEntityManager().persist(shippingDetails);
        return shippingDetails;
    }
}
