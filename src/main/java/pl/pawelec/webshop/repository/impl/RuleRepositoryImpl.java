/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.Rule;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.RuleRepository;

@Repository
public class RuleRepositoryImpl extends AbstrRepository<Rule> implements RuleRepository {

    public Long createAndGetId(Rule rule) {
        getEntityManager().persist(rule);
        return rule.getRuleId();
    }
}
