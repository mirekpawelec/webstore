package pl.pawelec.webshop.data;

import pl.pawelec.webshop.model.CartItem;
import pl.pawelec.webshop.model.Product;

public class CartItemFactory {
    public static CartItem create(Product product) {
        return new CartItem(product);
    }
}
