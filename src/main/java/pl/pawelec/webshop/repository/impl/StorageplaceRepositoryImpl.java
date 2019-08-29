/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.Storageplace;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.StorageplaceRepository;

import java.util.List;

@Repository
public class StorageplaceRepositoryImpl extends AbstrRepository<Storageplace> implements StorageplaceRepository {

    @Override
    public Storageplace getByPlaceNo(String placeNo) {
        return (Storageplace) getEntityManager().createQuery("from Storageplace WHERE place_no = :placeNo").setParameter("placeNo", placeNo).getSingleResult();
    }

    @Override
    public List<Storageplace> getByType(String type) {
        return getEntityManager().createQuery("from Storageplace WHERE type = :type").setParameter("type", type).getResultList();
    }

}
