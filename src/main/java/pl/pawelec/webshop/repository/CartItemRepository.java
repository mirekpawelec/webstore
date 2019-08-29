/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository;

import pl.pawelec.webshop.model.CartItem;

import java.util.List;

public interface CartItemRepository extends BaseCrudRepository<CartItem> {

    List<CartItem> getByCartId(Long cartId);
}
