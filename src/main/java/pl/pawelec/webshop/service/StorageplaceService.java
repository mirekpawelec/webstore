/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service;

import pl.pawelec.webshop.model.Storageplace;

import java.util.List;

/**
 * @author mirek
 */
public interface StorageplaceService extends CrudService<Storageplace> {

    Storageplace getByPlaceNo(String placeNo);

    List<Storageplace> getByType(String type);
}
