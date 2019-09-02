package pl.pawelec.webshop.data;

import pl.pawelec.webshop.model.Cart;
import pl.pawelec.webshop.model.CartItem;

import java.util.stream.Stream;

public class CartFactory {

    private static final String SESSION_ID = "e7525b65cd754c6a9c752731afb0a836";

    public static Cart create() {
        return new Cart(SESSION_ID);
    }

    public static Cart create(String sessionId, CartItem ...cartItems) {
        Cart cart = create();
        cart.setSessionId(sessionId);
        Stream.of(cartItems).forEach(cartItem -> {
            cart.getCartItemSet().add(cartItem);
            cartItem.setCart(cart);
        });
        return cart;
    }
}
