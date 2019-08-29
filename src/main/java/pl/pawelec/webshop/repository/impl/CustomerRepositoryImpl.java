/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.Customer;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.CustomerRepository;

@Repository
public class CustomerRepositoryImpl extends AbstrRepository<Customer> implements CustomerRepository {

    public Customer createAndReturn(Customer customer) {
        getEntityManager().persist(customer);
        return customer;
    }
}