/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.AppParameter;
import pl.pawelec.webshop.repository.AppParameterRepository;
import pl.pawelec.webshop.service.AppParameterService;
import pl.pawelec.webshop.service.utils.TimeUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AppParameterServiceImpl implements AppParameterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppParameterServiceImpl.class);

    @Autowired
    private AppParameterRepository appParameterRepository;

    @Transactional
    @Override
    public void create(AppParameter appParameter) {
        appParameterRepository.create(appParameter);
    }

    @Transactional
    @Override
    public void update(AppParameter appParameter) {
        appParameter.setLastModificationDate(TimeUtils.now());
        appParameterRepository.update(appParameter);
    }

    @Transactional
    @Override
    public void delete(AppParameter appParameter) {
        appParameterRepository.delete(appParameter);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        appParameterRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        appParameterRepository.deleteAll();
    }

    @Override
    public AppParameter getOneById(Long id) {
        return appParameterRepository.getOneById(id);
    }

    @Override
    public List<AppParameter> getAll() {
        return appParameterRepository.getAll();
    }

    @Override
    public Long count() {
        return appParameterRepository.count();
    }

    @Override
    public boolean exists(Long id) {
        return appParameterRepository.exists(id);
    }

    public AppParameter getByUniqueKey(String symbol, String name) {
        return appParameterRepository.getByUniqueKey(symbol, name);
    }

    public List<AppParameter> getBySymbol(String symbol) {
        return getAll().stream()
                .filter(sc -> sc.getSymbol().equals(symbol))
                .collect(Collectors.toList());
    }
}
