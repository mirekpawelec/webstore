/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.Cart;
import pl.pawelec.webshop.model.dao.CartDao;
import pl.pawelec.webshop.service.CartService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartDao;

    @Transactional
    @Override
    public void create(Cart cart) {
        cartDao.create(cart);
    }

    @Transactional
    @Override
    public void update(Cart cart) {
        cartDao.update(cart);
    }

    @Transactional
    @Override
    public void delete(Cart cart) {
        cartDao.delete(cart);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        cartDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        cartDao.deleteAll();
    }

    @Override
    public Cart getOneById(Long id) {
        Cart cart = cartDao.getOneById(id);
        cart.updateCostOfAllItems();
        return cart;
    }

    @Override
    public List<Cart> getAll() {
        return cartDao.getAll();
    }

    @Override
    public Long count() {
        return cartDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return cartDao.exists(id);
    }

    @Transactional
    @Override
    public Cart createAndGetCart(Cart cart) {
        return cartDao.createAndGetCart(cart);
    }

    public List<Cart> getBySessionId(String sessionId) {
        return cartDao.getBySessionId(sessionId);
    }

    public boolean existsBySessionId(String sessionId, String status) {
        return cartDao.existsBySessionId(sessionId, status);
    }


}
