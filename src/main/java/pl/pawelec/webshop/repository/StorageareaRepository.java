/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository;

import pl.pawelec.webshop.model.Storagearea;

import java.util.List;

public interface StorageareaRepository extends BaseCrudRepository<Storagearea> {

    List<Storagearea> getByDescription(String wholeDescriptionOrPart);
}
