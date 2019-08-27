/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.model.dao.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.exception.NoProductFoundUnderProductNoException;
import pl.pawelec.webshop.exception.NoProductIdFoundException;
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.model.dao.AbstrDao;
import pl.pawelec.webshop.model.dao.ProductDao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mirek
 */
@Repository
public class ProductDaoImpl extends AbstrDao<Product> implements ProductDao, Serializable {

    public List<Product> getByUnitsPrice(Double minPrice, Double maxPrice) {
        return getEntityManager().createQuery("from Product where unitPrice between :minPrice and :maxPrice")
                .setParameter("minPrice", minPrice)
                .setParameter("maxPrice", maxPrice)
                .getResultList();
    }

    public void delete(Product entity) {
        EntityManager em = getEntityManager();
        Product product = em.find(Product.class, entity.getProductId());
        if (product == null)
            throw new RuntimeException("The process has been finished unsuccessfull. No data found!");
        em.remove(product);
    }

    public Product getOneByProductNo(String productNo) {
        Product product = null;
        try {
            product = (Product) getEntityManager()
                    .createQuery("from Product where product_no = :product_no")
                    .setParameter("product_no", productNo)
                    .getSingleResult();
        } catch (NoResultException re) {
            throw new NoProductFoundUnderProductNoException(productNo);
        }
        return product;
    }

    public List<String> getAllManufacturers() {
        List<String> manufacturers = new ArrayList<String>();
        try{
            manufacturers = getEntityManager()
                    .createQuery("SELECT p.manufacturer FROM Product p GROUP BY p.manufacturer ORDER BY p.manufacturer")
                    .getResultList();
        } catch (NoResultException nr){}
        return manufacturers;
    }

    public List<String> getAllCategories() {
        try {
            return getEntityManager()
                    .createQuery("SELECT p.category FROM Product p GROUP BY p.category ORDER BY 1")
                    .getResultList();
        } catch (NoResultException nr) {
        }
        return null;
    }

    @Override
    public List<Product> getByManufacturer(String manufacturer) {
        try {
            return getEntityManager()
                    .createQuery("from Product WHERE manufacturer = :manufacturer")
                    .setParameter("manufacturer", manufacturer)
                    .getResultList();
        } catch (NoResultException nr) {
        }
        return null;
    }

    @Override
    public List<Product> getByStatus(String status) {
        return getEntityManager()
                .createQuery("from Product WHERE status = :status")
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public Product getOneById(Serializable id) {
        Product product = null;
        try {
            product = (Product) getEntityManager().createQuery("from Product WHERE product_id = :productId").setParameter("productId", id).getSingleResult();
        } catch (NoResultException e) {
            throw new NoProductIdFoundException((Long) id);
        }
        return product;
    }

}
