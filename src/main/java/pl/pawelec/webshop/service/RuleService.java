/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service;

import pl.pawelec.webshop.model.Rule;

import java.util.List;

/**
 * @author mirek
 */
public interface RuleService extends CrudService<Rule> {

    Long createAndGetId(Rule rule);
}
