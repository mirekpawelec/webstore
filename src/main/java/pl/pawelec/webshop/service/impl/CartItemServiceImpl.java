/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.CartItem;
import pl.pawelec.webshop.repository.CartItemRepository;
import pl.pawelec.webshop.service.CartItemService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemDao;

    @Transactional
    @Override
    public void create(CartItem cartItem) {
        cartItemDao.create(cartItem);
    }

    @Transactional
    @Override
    public void update(CartItem cartItem) {
        cartItemDao.update(cartItem);
    }

    @Transactional
    @Override
    public void delete(CartItem cartItem) {
        cartItemDao.delete(cartItem);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        cartItemDao.deleteAll();
    }

    @Override
    public CartItem getOneById(Long id) {
        return cartItemDao.getOneById(id);
    }

    @Override
    public List<CartItem> getAll() {
        return cartItemDao.getAll();
    }

    @Override
    public Long count() {
        return cartItemDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return cartItemDao.exists(id);
    }

}
