/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.Address;
import pl.pawelec.webshop.model.dao.AddressDao;
import pl.pawelec.webshop.service.AddressService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Transactional
    @Override
    public void create(Address address) {
        addressDao.create(address);
    }

    @Transactional
    @Override
    public void update(Address address) {
        addressDao.update(address);
    }

    @Transactional
    @Override
    public void delete(Address address) {
        addressDao.delete(address);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        addressDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        addressDao.deleteAll();
    }

    @Override
    public Address getOneById(Long id) {
        return addressDao.getOneById(id);
    }

    @Override
    public List<Address> getAll() {
        return addressDao.getAll();
    }

    @Override
    public Long count() {
        return addressDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return addressDao.exists(id);
    }

    @Override
    public Address createAndReturn(Address address) {
        return addressDao.createAndReturn(address);
    }
}
