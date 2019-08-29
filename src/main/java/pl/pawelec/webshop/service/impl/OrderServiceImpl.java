/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.execution.RequestContext;
import pl.pawelec.webshop.model.*;
import pl.pawelec.webshop.model.status.CartStatus;
import pl.pawelec.webshop.model.status.OrderStatus;
import pl.pawelec.webshop.repository.*;
import pl.pawelec.webshop.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final static String DELIVERY_METHOD_SYMBOL_CLASS = "delivery_method";
    private final static String PAYMENT_METHOD_SYMBOL_CLASS = "payment_method";

    @Autowired
    private OrderRepository orderDao;

    @Autowired
    private CartRepository cartDao;

    @Autowired
    private CartItemRepository cartItemDao;

    @Autowired
    private AddressRepository addressDao;

    @Autowired
    private CustomerRepository customerDao;

    @Autowired
    private ShippingAddressRepository shippingAddressDao;

    @Autowired
    private ShippingDetailsRepository shippingDetailsDao;

    @Autowired
    private AppParameterRepository appParameterDao;

    @Autowired
    private UserInfoRepository userInfoDao;

    @Transactional
    @Override
    public void create(Order order) {
        orderDao.create(order);
    }

    @Transactional
    @Override
    public void update(Order order) {
        orderDao.update(order);
    }

    @Transactional
    @Override
    public void delete(Order order) {
        orderDao.delete(order);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        orderDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        orderDao.deleteAll();
    }

    @Override
    public Order getOneById(Long id) {
        return orderDao.getOneById(id);
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public Long count() {
        return orderDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return orderDao.exists(id);
    }

    @Transactional
    @Override
    public Order createAndReturn(Order order) {
        return orderDao.createAndReturn(order);
    }

    @Override
    public void fillInCustomerAndShippingAddressInOrder(Order order, Customer customer) {
        order.setCustomer(customer);
        if (!Optional.ofNullable(order.getShippingAddress().getName()).isPresent()) {
            order.getShippingAddress().setName(customer.getFirstName() + " " + customer.getLastName());
            order.getShippingAddress().setPhoneNumber(customer.getPhoneNumber());
            order.getShippingAddress().getAddress().setDoorNo(customer.getAddress().getDoorNo());
            order.getShippingAddress().getAddress().setStreetName(customer.getAddress().getStreetName());
            order.getShippingAddress().getAddress().setZipCode(customer.getAddress().getZipCode());
            order.getShippingAddress().getAddress().setAreaName(customer.getAddress().getAreaName());
            order.getShippingAddress().getAddress().setState(customer.getAddress().getState());
            order.getShippingAddress().getAddress().setCountry(customer.getAddress().getCountry());
        }
    }

    @Override
    public void fillInShippingDetailsInOrder(Order order, ShippingDetails shippingDetails) {
        order.getShippingDetails().setDeliveryMethod(shippingDetails.getDeliveryMethod());
        order.getShippingDetails().setDeliveryCost(new BigDecimal(appParameterDao.getByUniqueKey(DELIVERY_METHOD_SYMBOL_CLASS, shippingDetails.getDeliveryMethod()).getValue())
        );
        order.getShippingDetails().setPaymentMethod(shippingDetails.getPaymentMethod());
        order.getShippingDetails().setPaymentCost(new BigDecimal(appParameterDao.getByUniqueKey(PAYMENT_METHOD_SYMBOL_CLASS, shippingDetails.getPaymentMethod()).getValue())
        );
        order.getShippingDetails().updateTotalCost();
    }

    @Override
    public void fillInShippingAddressInOrder(Order order, ShippingAddress shippingAddress) {
        order.getShippingAddress().setName(shippingAddress.getName());
        order.getShippingAddress().setPhoneNumber(shippingAddress.getPhoneNumber());
        order.getShippingAddress().getAddress().setDoorNo(shippingAddress.getAddress().getDoorNo());
        order.getShippingAddress().getAddress().setStreetName(shippingAddress.getAddress().getStreetName());
        order.getShippingAddress().getAddress().setZipCode(shippingAddress.getAddress().getZipCode());
        order.getShippingAddress().getAddress().setAreaName(shippingAddress.getAddress().getAreaName());
        order.getShippingAddress().getAddress().setState(shippingAddress.getAddress().getState());
        order.getShippingAddress().getAddress().setCountry(shippingAddress.getAddress().getCountry());
    }

    @Transactional
    @Override
    public Order saveCustomerOrder(Order order) {
        Cart cart = cartDao.getOneById(order.getCart().getCartId());
        cart.setLastModificationDate(LocalDateTime.now());
        cart.setStatus(CartStatus.FI.name());
        cartDao.update(cart);

        cartItemDao.getByCartId(order.getCart().getCartId()).stream().forEach(ci -> {
            ci.setLastModificationDate(LocalDateTime.now());
            ci.setStatus(CartStatus.FI.name());
            cartItemDao.update(ci);
        });

        Address address = addressDao.createAndReturn(order.getCustomer().getAddress());
        Customer customer = order.getCustomer();
        customer.setAddress(address);
        customer = customerDao.createAndReturn(customer);

        ShippingAddress shippingAddress = order.getShippingAddress();
        if (order.getCustomer().getAddress().equals(order.getShippingAddress().getAddress())) {
            shippingAddress.setAddress(address);
        } else {
            address = addressDao.createAndReturn(order.getShippingAddress().getAddress());
            shippingAddress.setAddress(address);
        }
        shippingAddress = shippingAddressDao.createAndReturn(shippingAddress);

        ShippingDetails shippingDetails = order.getShippingDetails();
        shippingDetails = shippingDetailsDao.createAndReturn(shippingDetails);

        Order orderToSave = createAndReturn(new Order(order.getCart(), customer, shippingAddress, shippingDetails));
        orderToSave.setStatus(OrderStatus.WT.name());
        return orderToSave;
    }

    @Override
    public void setFlowModelAttribute(RequestContext context) {
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
        String url = context.getFlowExecutionUrl();
        url = url.substring(url.indexOf("/", 1), url.length()) + "&";
        req.getSession().setAttribute("lastRequestUrl", url);
    }

    @Override
    public void checkUserAndFillInCustomer(Order order, Customer customer) {
        UserInfo user = Optional.ofNullable(order.getCart().getUser()).orElse(new UserInfo());
        if (Optional.ofNullable(user.getLogin()).isPresent()) {
            if (Optional.ofNullable(user.getCustomer()).isPresent()) {
                customer.setFirstName(user.getCustomer().getFirstName());
                customer.setLastName(user.getCustomer().getLastName());
                customer.setEmail(user.getCustomer().getEmail());
                customer.setPhoneNumber(user.getCustomer().getPhoneNumber());
                customer.getAddress().setDoorNo(user.getCustomer().getAddress().getDoorNo());
                customer.getAddress().setStreetName(user.getCustomer().getAddress().getStreetName());
                customer.getAddress().setZipCode(user.getCustomer().getAddress().getZipCode());
                customer.getAddress().setAreaName(user.getCustomer().getAddress().getAreaName());
                customer.getAddress().setState(user.getCustomer().getAddress().getState());
                customer.getAddress().setCountry(user.getCustomer().getAddress().getCountry());
            } else {
                customer.setFirstName(user.getFirstName());
                customer.setLastName(user.getLastName());
                customer.setEmail(user.getEmail());
            }
        }
    }

    @Transactional
    @Override
    public void associateUserWithCustomer(Order order) {
        UserInfo user = Optional.ofNullable(order.getCart().getUser()).orElse(new UserInfo());
        if (Optional.ofNullable(user.getLogin()).isPresent()) {
            user.setCustomer(order.getCustomer());
            userInfoDao.update(user);
        }
    }

    @Override
    public List<Order> getByUserLogin(String login) {
        return orderDao.getByUserLogin(login);
    }
}
