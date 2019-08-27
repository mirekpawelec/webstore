/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.pawelec.webshop.converter.CartNotFoundException;
import pl.pawelec.webshop.exception.NoProductIdFoundException;
import pl.pawelec.webshop.model.Cart;
import pl.pawelec.webshop.model.CartItem;
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.model.statuses.CartStatus;
import pl.pawelec.webshop.service.CartItemService;
import pl.pawelec.webshop.service.CartService;
import pl.pawelec.webshop.service.ProductService;
import pl.pawelec.webshop.service.UserInfoService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 *
 * @author mirek
 */
@Controller
@RequestMapping("/rest/cart")
public class CartRestController {
    private static final int DEFAULT_QUANTITY = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(CartRestController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired    
    private CartItemService cartItemService;
    @Autowired
    private UserInfoService userInfoService;
    
    
    
    @RequestMapping(value = "/{sessionId}", method = RequestMethod.POST)
    public @ResponseBody Cart create(@PathVariable String sessionId){
        return cartService.createAndGetCart(new Cart(sessionId));
    }
    
    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET)
    public @ResponseBody Cart read(@PathVariable String sessionId){
        Cart cart = null; 
        try{
            cart = cartService.getBySessionId(sessionId).stream().filter(ci->ci.getStatus().equals(CartStatus.RE.name()))
                .sorted((o1, o2) -> o1.getCreateDate().compareTo(o2.getCreateDate())).findFirst().orElse(new Cart(sessionId));
            cart.updateCostOfAllItems();
        }catch(CartNotFoundException cnfe){
            LOGGER.info("No found cart for sessionId=" + sessionId);
        }
        return cart==null ? new Cart(sessionId) : cart;
    }
    
    @RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String cartId){
        cartService.deleteById(Long.valueOf(cartId));
    }
    
    @RequestMapping(value = "/add/{productId}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addItemToCart(@PathVariable String productId, HttpServletRequest request){
        Cart cart = null;
        Product addingProduct = null;
        CartItem cartItem = null;
        String sessionId = request.getSession(true).getId();
        if(cartService.existsBySessionId(sessionId, CartStatus.RE.name())){
            cart = getCurrentCart(sessionId, CartStatus.RE.name());
        } else {
            if(request.getRemoteUser()!=null){
                cart = cartService.createAndGetCart(new Cart(sessionId, userInfoService.getByLogin(request.getRemoteUser())));
            }else{
                cart = cartService.createAndGetCart(new Cart(sessionId));
            }
        }
        try{
            addingProduct = productService.getOneById(Long.valueOf(productId));
        }catch(NoProductIdFoundException npi){
            throw new IllegalArgumentException(String.format("The product about ID %d is not exists!", npi.getProductId()));
        }
        cartItem = cart.getCartItemSet().stream().filter(ci->ci.getProduct().getProductId().equals(Long.valueOf(productId))).findFirst().orElse(new CartItem(addingProduct));
        if(cartItem.getId()==null){
            cartItem.getCart().setCartId(cart.getCartId());
            cartItemService.create(cartItem);
        }else{
            cartItem.setLastModificationDate(LocalDateTime.now());
            cartItem.setQuantity( cartItem.getQuantity() + DEFAULT_QUANTITY);
            cartItem.updateTotalPrice();
            cartItemService.update(cartItem);
        }
    }
    
    @RequestMapping(value = "/delete/{productId}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteItemFromCart(@PathVariable String productId, HttpServletRequest request){
        Cart cart = null;
        String sessionId = request.getSession(true).getId();
        if(cartService.existsBySessionId(sessionId, CartStatus.RE.name())){
            cart = getCurrentCart(sessionId, CartStatus.RE.name());
        } else {
            throw new CartNotFoundException("", sessionId);
        }
        CartItem deletingCartItem = cart.getCartItemSet().stream().filter(ci->ci.getProduct().getProductId().equals(Long.valueOf(productId))).findFirst().orElse(null);
        if(deletingCartItem!=null){
            cartItemService.delete(deletingCartItem);
        } else {
            throw new IllegalArgumentException(String.format("The cart does not containt the product about ID %s !", productId));
        }
    }
    
    @RequestMapping(value = "/items/{sessionId}", method = RequestMethod.GET)
    public @ResponseBody String getNumberOfItemsFromCart(@PathVariable String sessionId){
        Cart cart = null;
        try{
            cart = cartService.getBySessionId(sessionId).stream().filter(c->c.getStatus().equals(CartStatus.RE.name())).findFirst().orElse(new Cart(sessionId));
        } catch (CartNotFoundException cnfe){
            LOGGER.info("No found cart for sessionId=" + sessionId);
        }
        return cart==null ? "0" : String.valueOf(cart.getCartItemSet().size());
    }
    
    private Cart getCurrentCart(String sessionId, String status){
        return cartService.getBySessionId(sessionId).stream().filter(c->c.getStatus().equals(status)).findFirst().get();
    }
}
