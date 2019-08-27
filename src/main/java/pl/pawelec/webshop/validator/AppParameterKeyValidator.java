/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.pawelec.webshop.exception.NoParametersKeyFoundException;
import pl.pawelec.webshop.model.AppParameter;
import pl.pawelec.webshop.service.AppParameterService;

/**
 * @author mirek
 */
@Component
public class AppParameterKeyValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppParameterKeyValidator.class);

    @Autowired
    private AppParameterService appParameterService;

    @Override
    public boolean supports(Class<?> type) {
        return AppParameter.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object validationClass, Errors errors) {
        AppParameter appParameter, parameterExist = new AppParameter();
        boolean errorValidation = false;

        appParameter = (AppParameter) validationClass;
        try {
            parameterExist = appParameterService.getByUniqueKey(appParameter.getSymbol(), appParameter.getName());
            if (appParameter.isNew()) {
                errorValidation = true;
            } else {
                if (!parameterExist.getParameterId().equals(appParameter.getParameterId())) {
                    errorValidation = true;
                }
            }
            if (errorValidation) {
                errors.rejectValue("symbol", "pl.pawelec.webshop.validator.AppParameterKeyValidator.message");
            }
        } catch (NoParametersKeyFoundException sce) {
            LOGGER.info("The given key do not exist");
        }
    }

}
