/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository;

import pl.pawelec.webshop.model.ShippingAddress;

public interface ShippingAddressRepository extends BaseCrudRepository<ShippingAddress> {

    ShippingAddress createAndReturn(ShippingAddress shippingAddress);
}
