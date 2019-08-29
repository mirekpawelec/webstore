/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.pawelec.webshop.model.Product;

/**
 * @author mirek
 */
@Component
public class ProductImageValidator implements Validator {

    private Long sizeImage = 2097152l;

    public void setSizeImage(Long sizeImage) {
        this.sizeImage = sizeImage;
    }

    @Override
    public boolean supports(Class<?> type) {
        return Product.class.equals(type);
    }

    @Override
    public void validate(Object validationClass, Errors errors) {
        try {
            Product product = (Product) validationClass;
            if (!product.getProductImage().isEmpty() && product.getProductImage().getSize() > sizeImage) {
                errors.rejectValue("productImage", "pl.pawelec.webshop.validator.ProductImageValidator.message");
            }
        } catch (NullPointerException n) {
        }
    }

}
