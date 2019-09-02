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
import pl.pawelec.webshop.model.CartItem;
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.model.status.CartStatus;
import pl.pawelec.webshop.repository.CartRepository;
import pl.pawelec.webshop.service.CartItemService;
import pl.pawelec.webshop.service.CartService;
import pl.pawelec.webshop.service.ProductService;
import pl.pawelec.webshop.service.UserInfoService;
import pl.pawelec.webshop.service.converter.CartNotFoundException;
import pl.pawelec.webshop.service.exception.NoProductIdFoundException;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    private static final int DEFAULT_QUANTITY = 1;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserInfoService userInfoService;

    @Transactional
    @Override
    public void create(Cart cart) {
        cartRepository.create(cart);
    }

    @Transactional
    @Override
    public Cart create(String sessionId) {
        Cart cart = this.getActiveCartBySessionId(sessionId);
        if (cart.isNew()) {
            cartRepository.create(cart);
        }
        return cart;
    }

    @Transactional
    @Override
    public void update(Cart cart) {
        cartRepository.update(cart);
    }

    @Transactional
    @Override
    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        cartRepository.deleteAll();
    }

    @Override
    public Cart getOneById(Long id) {
        Cart cart = cartRepository.getOneById(id);
        cart.updateCostOfAllItems();
        return cart;
    }

    @Override
    public List<Cart> getAll() {
        return cartRepository.getAll();
    }

    @Override
    public Long count() {
        return cartRepository.count();
    }

    @Override
    public boolean exists(Long id) {
        return cartRepository.exists(id);
    }

    @Override
    public Cart getActiveCartBySessionId(String sessionId) {
        Cart cart = cartRepository.getBySessionId(sessionId).stream()
                .filter(ci -> ci.getStatus().equals(CartStatus.RE.name()))
                .findFirst()
                .orElse(new Cart(sessionId));
        cart.updateCostOfAllItems();
        return cart;
    }

    @Transactional
    @Override
    public void addItemToCart(String sessionId, String user, long productId) {
        Cart cart = this.getActiveCartBySessionId(sessionId);
        if (cart.isNew()) {
            if (Objects.nonNull(user)) {
                cart.setUser(userInfoService.getByLogin(user));
            }
            cartRepository.create(cart);
        }

        Product addingProduct;
        try {
            addingProduct = productService.getOneById(productId);
        } catch (NoProductIdFoundException npi) {
            throw new IllegalArgumentException(String.format("The product about ID %d is not exists!",
                    npi.getProductId()));
        }

        CartItem cartItem = cart.getCartItemSet().stream()
                .filter(ci -> ci.getProduct().getProductId() == productId)
                .findFirst()
                .orElse(new CartItem(addingProduct));
        if (Objects.isNull(cartItem.getId())) {
            cart.getCartItemSet().add(cartItem);
            cartItem.setCart(cart);
            cartItemService.create(cartItem);
        } else {
            cartItem.setLastModificationDate(LocalDateTime.now());
            cartItem.setQuantity(cartItem.getQuantity() + DEFAULT_QUANTITY);
            cartItem.updateTotalPrice();
            cartItemService.update(cartItem);
        }
    }

    @Transactional
    @Override
    public void deleteItemFromCart(String sessionId, long productId) {
        Cart cart = getActiveCartBySessionId(sessionId);
        if (Objects.isNull(cart.getCartId())) {
            throw new CartNotFoundException("", sessionId);
        }

        CartItem cartItemToBeDeleted = null;
        for (Iterator<CartItem> carItem = cart.getCartItemSet().iterator(); carItem.hasNext(); ) {
            cartItemToBeDeleted = carItem.next();
            if (cartItemToBeDeleted.getProduct().getProductId() == productId) {
                carItem.remove();
                break;
            } else {
                cartItemToBeDeleted = null;
            }
        }

        if (Objects.nonNull(cartItemToBeDeleted)) {
            cartItemService.delete(cartItemToBeDeleted);
        } else {
            throw new IllegalArgumentException(
                    String.format("The cart does not containt the product about ID %s !", productId));
        }
    }

    @Override
    public int getSizeOfCart(String sessionId) {
        return getActiveCartBySessionId(sessionId)
                .getCartItemSet()
                .size();
    }
}
