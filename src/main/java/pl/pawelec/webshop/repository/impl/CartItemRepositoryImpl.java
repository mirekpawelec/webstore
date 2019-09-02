/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.CartItem;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.CartItemRepository;

import java.util.List;

@Repository
public class CartItemRepositoryImpl extends AbstrRepository<CartItem> implements CartItemRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartItemRepositoryImpl.class);

    @Override
    public void delete(CartItem entity) {
        try {
            if (super.exists(entity.getId())) {
                super.delete(entity);
            } else {
                LOGGER.info("The object does not exists!");
            }
        } catch (NullPointerException ne) {
            LOGGER.info("The argument passed is empty!");
        }
    }

    public List<CartItem> getByCartId(Long cartId) {
        return getEntityManager().createQuery("from CartItem where cart_id = :cartId").setParameter("cartId", cartId).getResultList();
    }
}
