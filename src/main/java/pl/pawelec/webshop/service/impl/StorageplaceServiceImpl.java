/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.Storageplace;
import pl.pawelec.webshop.repository.StorageplaceRepository;
import pl.pawelec.webshop.service.StorageplaceService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StorageplaceServiceImpl implements StorageplaceService {

    @Autowired
    private StorageplaceRepository storageplaceDao;

    @Transactional
    @Override
    public void create(Storageplace storageplace) {
        storageplaceDao.create(storageplace);
    }

    @Transactional
    @Override
    public void update(Storageplace storageplace) {
        storageplaceDao.update(storageplace);
    }

    @Transactional
    @Override
    public void delete(Storageplace storageplace) {
        storageplaceDao.delete(storageplace);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        storageplaceDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        storageplaceDao.deleteAll();
    }

    @Override
    public Storageplace getOneById(Long id) {
        return storageplaceDao.getOneById(id);
    }

    @Override
    public List<Storageplace> getAll() {
        return storageplaceDao.getAll();
    }

    @Override
    public Long count() {
        return storageplaceDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return storageplaceDao.exists(id);
    }

    @Override
    public Storageplace getByPlaceNo(String placeNo) {
        return storageplaceDao.getByPlaceNo(placeNo);
    }

    @Override
    public List<Storageplace> getByType(String type) {
        return storageplaceDao.getByType(type);
    }
}
