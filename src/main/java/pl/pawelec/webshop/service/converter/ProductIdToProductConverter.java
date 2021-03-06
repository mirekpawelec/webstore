/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.service.ProductService;

/**
 * @author mirek
 */
public class ProductIdToProductConverter implements Converter<Object, Product> {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryIdToDeliveryConverter.class);

    @Autowired
    private ProductService productService;

    @Override
    public Product convert(Object element) {
        logger.info("Konwertuje...");
        Long id = (Long) element;
        Product product = productService.getOneById(id);
        logger.info("Zakończono konwersję elementu nr " + element.toString() + " na " + product);
        return product;
    }

}
