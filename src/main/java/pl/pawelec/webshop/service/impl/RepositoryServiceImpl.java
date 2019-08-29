/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.LoadUnit;
import pl.pawelec.webshop.repository.LoadUnitRepository;
import pl.pawelec.webshop.service.RepositoryService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private LoadUnitRepository repositoryDao;

    @Transactional
    @Override
    public void create(LoadUnit repository) {
        repositoryDao.create(repository);
    }

    @Transactional
    @Override
    public void update(LoadUnit repository) {
        repositoryDao.update(repository);
    }

    @Transactional
    @Override
    public void delete(LoadUnit repository) {
        repositoryDao.delete(repository);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        repositoryDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        repositoryDao.deleteAll();
    }

    @Override
    public LoadUnit getOneById(Long id) {
        return repositoryDao.getOneById(id);
    }

    @Override
    public List<LoadUnit> getAll() {
        return repositoryDao.getAll();
    }

    @Override
    public Long count() {
        return repositoryDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return repositoryDao.exists(id);
    }

    @Override
    public LoadUnit getByLoadunitNo(String loadunitNo) {
        return repositoryDao.getByLoadunitNo(loadunitNo);
    }

    @Override
    public List<LoadUnit> getByStatus(String status) {
        return repositoryDao.getByStatus(status);
    }

    @Override
    public List<LoadUnit> getByProductNo(String productNo) {
        return repositoryDao.getByProductNo(productNo);
    }

    @Override
    public List<LoadUnit> getByOwnCriteria(String sqlQuery, String modificationDate, String createDate) {
        return repositoryDao.getByOwnCriteria(sqlQuery, modificationDate, createDate);
    }

}
