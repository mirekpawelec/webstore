/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pl.pawelec.webshop.model.Cart;
import pl.pawelec.webshop.model.status.CartStatus;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.CartRepository;
import pl.pawelec.webshop.service.converter.CartNotFoundException;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class CartRepositoryImpl extends AbstrRepository<Cart> implements CartRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartRepositoryImpl.class);

    @Override
    public void delete(Cart entity) {
        try {
            if (super.exists(entity.getCartId())) {
                super.delete(entity);
            } else {
                LOGGER.info("The object does not exists!");
            }
        } catch (NullPointerException ne) {
            LOGGER.info("The argument passed is empty!");
        }
    }

    @Override
    public List<Cart> getBySessionId(String sessionId) {
        List<Cart> listCart = getEntityManager()
                .createQuery("from Cart WHERE session_id = :sessionId")
                .setParameter("sessionId", sessionId)
                .getResultList();
        return listCart;
    }
}
