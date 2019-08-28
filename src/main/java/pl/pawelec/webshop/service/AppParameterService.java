/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service;

import pl.pawelec.webshop.model.AppParameter;

import java.util.List;

/**
 * @author mirek
 */
public interface AppParameterService extends CrudService<AppParameter> {

    AppParameter getByUniqueKey(String symbol, String name);

    List<AppParameter> getBySymbol(String symbol);
}
