/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.ShippingAddress;
import pl.pawelec.webshop.model.dao.ShippingAddressDao;
import pl.pawelec.webshop.service.ShippingAddressService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShippingAddressServiceImpl implements ShippingAddressService {

    @Autowired
    private ShippingAddressDao shippingDetailDao;

    @Transactional
    @Override
    public void create(ShippingAddress shippingAddress) {
        shippingDetailDao.create(shippingAddress);
    }

    @Transactional
    @Override
    public void update(ShippingAddress shippingAddress) {
        shippingDetailDao.update(shippingAddress);
    }

    @Transactional
    @Override
    public void delete(ShippingAddress shippingAddress) {
        shippingDetailDao.delete(shippingAddress);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        shippingDetailDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        shippingDetailDao.deleteAll();
    }

    @Override
    public ShippingAddress getOneById(Long id) {
        return shippingDetailDao.getOneById(id);
    }

    @Override
    public List<ShippingAddress> getAll() {
        return shippingDetailDao.getAll();
    }

    @Override
    public Long count() {
        return shippingDetailDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return shippingDetailDao.exists(id);
    }

    @Transactional
    @Override
    public ShippingAddress createAndReturn(ShippingAddress shippingAddress) {
        return shippingDetailDao.createAndReturn(shippingAddress);
    }

}
