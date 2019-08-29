/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author mirek
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductNoValidator.class)
public @interface ProductNo {

    String message() default "{pl.pawelec.webshop.service.validator.ProductNo.message}";

    Class<?>[] groups() default {};

    public abstract Class<? extends Payload>[] payload() default {};

}
