/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.ShippingDetails;
import pl.pawelec.webshop.model.dao.ShippingDetailsDao;
import pl.pawelec.webshop.service.ShippingDetailsService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShippingDetailsServiceImpl implements ShippingDetailsService {

    @Autowired
    private ShippingDetailsDao shippingDetailsDao;

    @Transactional
    @Override
    public void create(ShippingDetails shippingDetails) {
        shippingDetailsDao.create(shippingDetails);
    }

    @Transactional
    @Override
    public void update(ShippingDetails shippingDetails) {
        shippingDetailsDao.update(shippingDetails);
    }

    @Transactional
    @Override
    public void delete(ShippingDetails shippingDetails) {
        shippingDetailsDao.delete(shippingDetails);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        shippingDetailsDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        shippingDetailsDao.deleteAll();
    }

    @Override
    public ShippingDetails getOneById(Long id) {
        return shippingDetailsDao.getOneById(id);
    }

    @Override
    public List<ShippingDetails> getAll() {
        return shippingDetailsDao.getAll();
    }

    @Override
    public Long count() {
        return shippingDetailsDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return shippingDetailsDao.exists(id);
    }

    @Transactional
    @Override
    public ShippingDetails createAndReturn(ShippingDetails shippingDetails) {
        return shippingDetailsDao.createAndReturn(shippingDetails);
    }

}
