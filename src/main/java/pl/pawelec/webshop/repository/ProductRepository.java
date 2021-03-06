/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository;

import pl.pawelec.webshop.model.Product;

import java.util.List;

public interface ProductRepository extends BaseCrudRepository<Product> {

    List<Product> getByUnitsPrice(Double minPrice, Double maxPrice);

    List<Product> getByManufacturer(String manufacturer);

    List<Product> getByStatus(String status);

    Product getOneByProductNo(String productNo);

    List<String> getAllManufacturers();

    List<String> getAllCategories();
}
