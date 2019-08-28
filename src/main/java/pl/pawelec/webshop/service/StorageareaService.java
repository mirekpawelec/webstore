/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service;

import pl.pawelec.webshop.model.Storagearea;

import java.util.List;

/**
 * @author mirek
 */
public interface StorageareaService extends CrudService<Storagearea> {

    List<Storagearea> getByDescription(String wholeDescriptionOrPart);
}
