/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.Order;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.OrderRepository;

import java.util.List;

@Repository
public class OrderRepositoryImpl extends AbstrRepository<Order> implements OrderRepository {

    public Order createAndReturn(Order order) {
        getEntityManager().persist(order);
        return order;
    }

    public List<Order> getByUserLogin(String login) {
        return getEntityManager().createQuery("from Order WHERE cart.user.login = :login").setParameter("login", login).getResultList();
    }
}
