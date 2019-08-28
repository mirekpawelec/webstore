/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.Faq;
import pl.pawelec.webshop.model.dao.FaqDao;
import pl.pawelec.webshop.service.FaqService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FaqServiceImpl implements FaqService {

    @Autowired
    private FaqDao faqDao;

    @Transactional
    @Override
    public void create(Faq faq) {
        faqDao.create(faq);
    }

    @Transactional
    @Override
    public void update(Faq faq) {
        faqDao.update(faq);
    }

    @Transactional
    @Override
    public void delete(Faq faq) {
        faqDao.delete(faq);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        faqDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        faqDao.deleteAll();
    }

    @Override
    public Faq getOneById(Long id) {
        return faqDao.getOneById(id);
    }

    @Override
    public List<Faq> getAll() {
        return faqDao.getAll();
    }

    @Override
    public Long count() {
        return faqDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return faqDao.exists(id);
    }

    @Transactional
    @Override
    public Long createAndGetId(Faq faq) {
        return faqDao.createAndGetId(faq);
    }
}
