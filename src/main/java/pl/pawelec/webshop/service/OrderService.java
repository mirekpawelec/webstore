/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service;

import org.springframework.webflow.execution.RequestContext;
import pl.pawelec.webshop.model.Customer;
import pl.pawelec.webshop.model.Order;
import pl.pawelec.webshop.model.ShippingAddress;
import pl.pawelec.webshop.model.ShippingDetails;

import java.util.List;

/**
 * @author mirek
 */
public interface OrderService extends CrudService<Order> {

    Order createAndReturn(Order order);

    void fillInCustomerAndShippingAddressInOrder(Order order, Customer customer);

    void fillInShippingDetailsInOrder(Order order, ShippingDetails shippingDetails);

    void fillInShippingAddressInOrder(Order order, ShippingAddress shippingAddress);

    Order saveCustomerOrder(Order order);

    void setFlowModelAttribute(RequestContext context);

    void checkUserAndFillInCustomer(Order order, Customer customer);

    void associateUserWithCustomer(Order order);

    List<Order> getByUserLogin(String login);
}
