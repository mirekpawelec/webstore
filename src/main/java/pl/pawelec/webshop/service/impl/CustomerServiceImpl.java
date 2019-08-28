/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.Customer;
import pl.pawelec.webshop.model.dao.CustomerDao;
import pl.pawelec.webshop.service.CustomerService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Transactional
    @Override
    public void create(Customer customer) {
        customerDao.create(customer);
    }

    @Transactional
    @Override
    public void update(Customer customer) {
        customerDao.update(customer);
    }

    @Transactional
    @Override
    public void delete(Customer customer) {
        customerDao.delete(customer);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        customerDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        customerDao.deleteAll();
    }

    @Override
    public Customer getOneById(Long id) {
        return customerDao.getOneById(id);
    }

    @Override
    public List<Customer> getAll() {
        return customerDao.getAll();
    }

    @Override
    public Long count() {
        return customerDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return customerDao.exists(id);
    }

    @Transactional
    @Override
    public Customer createAndReturn(Customer customer) {
        return customerDao.createAndReturn(customer);
    }
}
