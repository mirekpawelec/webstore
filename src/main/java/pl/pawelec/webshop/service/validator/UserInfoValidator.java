/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.pawelec.webshop.model.UserInfo;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mirek
 */
@Component
public class UserInfoValidator implements Validator {

    @Autowired
    private javax.validation.Validator beanValidator;
    private Set<Validator> springValidators;

    public UserInfoValidator() {
        springValidators = new HashSet<Validator>();
    }

    @Autowired
    @Qualifier("userValidator")
    public void setSpringValidators(Set<Validator> springValidators) {
        this.springValidators = springValidators;
    }

    @Override
    public boolean supports(Class<?> type) {
        return UserInfo.class.equals(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Set<ConstraintViolation<Object>> constraintViolations = beanValidator.validate(target);
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.rejectValue(propertyPath, "", message);
        }

        for (Validator validator : springValidators) {
            validator.validate(target, errors);
        }
    }

}
