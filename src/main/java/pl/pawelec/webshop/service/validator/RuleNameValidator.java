/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.pawelec.webshop.model.Rule;
import pl.pawelec.webshop.service.RuleService;

import java.util.Optional;

/**
 * @author mirek
 */
@Component
public class RuleNameValidator implements Validator {

    @Autowired
    private RuleService ruleService;

    @Override
    public boolean supports(Class<?> type) {
        return Rule.class.equals(type);
    }

    @Override
    public void validate(Object validationClass, Errors errors) {
        Rule rule = (Rule) validationClass;
        if (rule.isNew()) {
            if (ruleService.getAll().stream().anyMatch(r -> r.getName().equals(rule.getName()))) {
                errors.rejectValue("name", "pl.pawelec.webshop.validator.RuleNameValidator.message");
            }
        } else {
            Rule existRule = ruleService.getAll().stream().filter(r -> r.getName().equals(rule.getName())).findFirst().orElse(new Rule());
            if (Optional.ofNullable(existRule.getRuleId()).isPresent() && !existRule.getRuleId().equals(rule.getRuleId())) {
                errors.rejectValue("name", "pl.pawelec.webshop.validator.RuleNameValidator.message");
            }
        }
    }

}
