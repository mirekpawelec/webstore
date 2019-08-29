/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.Rule;
import pl.pawelec.webshop.repository.RuleRepository;
import pl.pawelec.webshop.service.RuleService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RuleServiceImpl implements RuleService {

    @Autowired
    private RuleRepository ruleDao;

    @Transactional
    @Override
    public void create(Rule rule) {
        ruleDao.create(rule);
    }

    @Transactional
    @Override
    public void update(Rule rule) {
        ruleDao.update(rule);
    }

    @Transactional
    @Override
    public void delete(Rule rule) {
        ruleDao.delete(rule);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        ruleDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        ruleDao.deleteAll();
    }

    @Override
    public Rule getOneById(Long id) {
        return ruleDao.getOneById(id);
    }

    @Override
    public List<Rule> getAll() {
        return ruleDao.getAll();
    }

    @Override
    public Long count() {
        return ruleDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return ruleDao.exists(id);
    }

    @Transactional
    @Override
    public Long createAndGetId(Rule rule) {
        return ruleDao.createAndGetId(rule);
    }

}
