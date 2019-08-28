/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.Repository;
import pl.pawelec.webshop.model.dao.RepositoryDao;
import pl.pawelec.webshop.service.RepositoryService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private RepositoryDao repositoryDao;

    @Transactional
    @Override
    public void create(Repository repository) {
        repositoryDao.create(repository);
    }

    @Transactional
    @Override
    public void update(Repository repository) {
        repositoryDao.update(repository);
    }

    @Transactional
    @Override
    public void delete(Repository repository) {
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
    public Repository getOneById(Long id) {
        return repositoryDao.getOneById(id);
    }

    @Override
    public List<Repository> getAll() {
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
    public Repository getByLoadunitNo(String loadunitNo) {
        return repositoryDao.getByLoadunitNo(loadunitNo);
    }

    @Override
    public List<Repository> getByStatus(String status) {
        return repositoryDao.getByStatus(status);
    }

    @Override
    public List<Repository> getByProductNo(String productNo) {
        return repositoryDao.getByProductNo(productNo);
    }

    @Override
    public List<Repository> getByOwnCriteria(String sqlQuery, String modificationDate, String createDate) {
        return repositoryDao.getByOwnCriteria(sqlQuery, modificationDate, createDate);
    }

}
