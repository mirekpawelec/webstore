/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository;

import pl.pawelec.webshop.model.LoadUnit;

import java.util.List;

public interface LoadUnitRepository extends BaseCrudRepository<LoadUnit> {

    LoadUnit getByLoadunitNo(String loadunitNo);

    List<LoadUnit> getByStatus(String status);

    List<LoadUnit> getByProductNo(String productNo);

    List<LoadUnit> getByOwnCriteria(String sqlQuery, String modificationDate, String createDate);
}
