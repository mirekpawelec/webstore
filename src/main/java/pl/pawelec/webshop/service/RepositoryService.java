/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service;

import pl.pawelec.webshop.model.Repository;

import java.util.List;

/**
 * @author mirek
 */
public interface RepositoryService extends CrudService<Repository> {

    Repository getByLoadunitNo(String loadunitNo);

    List<Repository> getByStatus(String status);

    List<Repository> getByProductNo(String productNo);

    List<Repository> getByOwnCriteria(String sqlQuery, String modificationDate, String createDate);
}
