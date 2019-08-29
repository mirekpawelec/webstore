/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.pawelec.webshop.model.LoadUnit;
import pl.pawelec.webshop.service.RepositoryService;

/**
 * @author mirek
 */
public class RepositoryConverter implements Converter<Object, LoadUnit> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryConverter.class);

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public LoadUnit convert(Object element) {
        Long id = Long.valueOf((String) element);
        LoadUnit repository = repositoryService.getOneById(id);
        LOGGER.info("### RepositoryConverter: element=" + id + " , repository=" + repository);
        return repository;
    }

}
