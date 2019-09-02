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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.pawelec.webshop.model.Cart;
import pl.pawelec.webshop.service.CartService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/rest/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartRestController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Cart create(@PathVariable("sessionId") String sessionId) {
        return cartService.create(sessionId);
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET)
    public Cart read(@PathVariable String sessionId) {
        return cartService.getActiveCartBySessionId(sessionId);
    }

    @RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String cartId) {
        cartService.deleteById(Long.valueOf(cartId));
    }

    @RequestMapping(value = "/add/{productId}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addItemToCart(@PathVariable String productId, HttpServletRequest request) {
        cartService.addItemToCart(request.getSession(true).getId(), request.getRemoteUser(), Long.valueOf(productId));
    }

    @RequestMapping(value = "/delete/{productId}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteItemFromCart(@PathVariable String productId, HttpServletRequest request) {
        cartService.deleteItemFromCart(request.getSession(true).getId(), Long.valueOf(productId));
    }

    @RequestMapping(value = "/items/{sessionId}", method = RequestMethod.GET)
    public int getSizeOfCart(@PathVariable String sessionId) {
        return cartService.getSizeOfCart(sessionId);
    }
}
