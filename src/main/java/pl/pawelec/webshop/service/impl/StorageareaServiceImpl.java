/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.Storagearea;
import pl.pawelec.webshop.repository.StorageareaRepository;
import pl.pawelec.webshop.service.StorageareaService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StorageareaServiceImpl implements StorageareaService {

    @Autowired
    private StorageareaRepository storageareaDao;

    @Transactional
    @Override
    public void create(Storagearea storagearea) {
        storageareaDao.create(storagearea);
    }

    @Transactional
    @Override
    public void update(Storagearea storagearea) {
        storageareaDao.update(storagearea);
    }

    @Transactional
    @Override
    public void delete(Storagearea storagearea) {
        storageareaDao.delete(storagearea);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        storageareaDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        storageareaDao.deleteAll();
    }

    @Override
    public Storagearea getOneById(Long id) {
        return storageareaDao.getOneById(id);
    }

    @Override
    public List<Storagearea> getAll() {
        return storageareaDao.getAll();
    }

    @Override
    public Long count() {
        return storageareaDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return storageareaDao.exists(id);
    }

    @Override
    public List<Storagearea> getByDescription(String wholeDescriptionOrPart) {
        return storageareaDao.getByDescription(wholeDescriptionOrPart);
    }
}
