/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.validator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.pawelec.webshop.model.UserInfo;

import java.util.Optional;

/**
 * @author mirek
 */
@Component
@Qualifier("userValidator")
public class UserPasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return UserInfo.class.equals(type);
    }

    @Override
    public void validate(Object validationClass, Errors errors) {
        UserInfo userInfo = (UserInfo) validationClass;
        if (userInfo.isNew()) {
            if (!Optional.ofNullable(userInfo.getPassword()).isPresent() || userInfo.getPassword().isEmpty()) {
                errors.rejectValue("password", "pl.pawelec.webshop.validator.UserPsswordValidator.NotNull.message");
            }
            if (!Optional.ofNullable(userInfo.getRepeatPassword()).isPresent() || userInfo.getRepeatPassword().isEmpty()) {
                errors.rejectValue("repeatPassword", "pl.pawelec.webshop.validator.UserPsswordValidator.NotNull.message");
            }
        }
        if ((Optional.ofNullable(userInfo.getPassword()).isPresent() && Optional.ofNullable(userInfo.getRepeatPassword()).isPresent())
                && !userInfo.getPassword().equals(userInfo.getRepeatPassword())) {
            errors.rejectValue("repeatPassword", "pl.pawelec.webshop.validator.UserPsswordValidator.message");
        }
    }

}
