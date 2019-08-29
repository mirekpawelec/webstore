/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.Address;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.AddressRepository;

@Repository
public class AddressRepositoryImpl extends AbstrRepository<Address> implements AddressRepository {

    public Address createAndReturn(Address address) {
        getEntityManager().persist(address);
        return address;
    }
}
