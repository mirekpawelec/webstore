package pl.pawelec.webshop.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.repository.ProductRepository;
import pl.pawelec.webshop.service.ProductService;

import java.io.Serializable;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService, Serializable {

    @Autowired
    private ProductRepository productDao;

    @Transactional
    @Override
    public void create(Product product) {
        productDao.create(product);
    }

    @Transactional
    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    @Transactional
    @Override
    public void delete(Product product) {
        productDao.delete(product);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        productDao.deleteAll();
    }

    @Override
    public Product getOneById(Long id) {
        return productDao.getOneById(id);
    }

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public Long count() {
        return productDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return productDao.exists(id);
    }

    @Override
    public List<Product> getByUnitsPrice(Double minPrice, Double maxPrice) {
        return productDao.getByUnitsPrice(minPrice, maxPrice);
    }

    @Override
    public Product getOneByProductNo(String productNo) {
        return productDao.getOneByProductNo(productNo);
    }

    @Override
    public List<String> getAllCategories() {
        return productDao.getAllCategories();
    }

    @Override
    public List<String> getAllManufacturers() {
        return productDao.getAllManufacturers();
    }

    @Override
    public List<Product> getByManufacturer(String manufacturer) {
        return productDao.getByManufacturer(manufacturer);
    }

    @Override
    public List<Product> getByStatus(String status) {
        return productDao.getByStatus(status);
    }
}
