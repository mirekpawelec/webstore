/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pawelec.webshop.config.WebstoreApplication;
import pl.pawelec.webshop.data.CartFactory;
import pl.pawelec.webshop.data.CartItemFactory;
import pl.pawelec.webshop.data.ProductFactory;
import pl.pawelec.webshop.model.Cart;
import pl.pawelec.webshop.model.CartItem;
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.model.status.CartStatus;
import pl.pawelec.webshop.service.CartItemService;
import pl.pawelec.webshop.service.CartService;
import pl.pawelec.webshop.service.ProductService;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebstoreApplication.class})
@TestPropertySource(locations = "classpath:application.yml")
@DataJpaTest
public class CartRestControllerTest {

    Product product1, product2;
    Cart cartWithMultiProducts;
    private MockMvc mockMvc;

    @Autowired
    private CartRestController cartRestController;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    MockHttpSession mockHttpSession;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartRestController).build();
    }

    @After
    public void clean() {
        cartItemService.deleteAll();
        cartService.deleteAll();
        productService.deleteAll();
    }

    @Test
    public void create_method_a_new_cart_Json_object_should_be_added_and_returned() throws Exception {
        // given
        // when
        // then
        this.mockMvc.perform(post("/rest/cart/" + mockHttpSession.getId()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.cartId").isNotEmpty())
                .andExpect(jsonPath("$.sessionId").value(mockHttpSession.getId()))
                .andExpect(jsonPath("$.status").value(CartStatus.RE.name()))
                .andExpect(jsonPath("$.costOfAllItems").value(0))
                .andExpect(jsonPath("$.cartItemSet").isEmpty());
//        Cart cart = cartService.getActiveCartBySessionId(mockHttpSession.getId());
//        assertNotNull(cart.getCartId());
//        assertEquals(mockHttpSession.getId(), cart.getSessionId());
    }

    @Test
    public void read_method_a_cart_Json_object_should_be_returned() throws Exception {
        // given
        Cart cart = CartFactory.create();
        cartService.create(cart);

        // when
        // then
        this.mockMvc.perform(get("/rest/cart/" + cart.getSessionId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.cartId").isNotEmpty())
                .andExpect(jsonPath("$.sessionId").value(cart.getSessionId()))
                .andExpect(jsonPath("$.status").value(CartStatus.RE.name()))
                .andExpect(jsonPath("$.costOfAllItems").value(0))
                .andExpect(jsonPath("$.cartItemSet").isEmpty());
    }

    @Test
    public void delete_method_a_given_cart_should_be_removed() throws Exception {
        // given
        Cart deleteCart = CartFactory.create();
        cartService.create(deleteCart);

        // when
        this.mockMvc.perform(delete("/rest/cart/" + deleteCart.getCartId()))
                .andExpect(status().isNoContent());

        // then
        assertFalse(cartService.exists(deleteCart.getCartId()));
    }

    @Test
    public void add_cartItem_to_cart_should_be_added() throws Exception {
        // given
        Product product = ProductFactory.create();
        productService.create(product);

        // when
        this.mockMvc.perform(put("/rest/cart/add/" + product.getProductId())
                .session(mockHttpSession))
                .andExpect(status().isNoContent());
        Cart cart = cartService.getActiveCartBySessionId(mockHttpSession.getId());

        // then
        assertEquals(1, cart.getCartItemSet().size());
        assertEquals(product, cart.getCartItemSet().iterator().next().getProduct());
    }

    @Test
    public void add_second_the_same_product_to_cart_should_be_increment_its_quantity() throws Exception {
        // given
        prepareCartWithMultiProducts();

        // when
        this.mockMvc.perform(put("/rest/cart/add/" + product1.getProductId())
                .session(mockHttpSession))
                .andExpect(status().isNoContent());
        Cart cartUpdated = cartService.getActiveCartBySessionId(mockHttpSession.getId());
        CartItem cartItemUpdated = cartUpdated.getCartItemSet().stream()
                .filter(cartItem -> cartItem.getProduct().getProductId() == product1.getProductId())
                .findFirst()
                .orElse(new CartItem());

        // then
        assertEquals(2, cartUpdated.getCartItemSet().size());
        assertEquals(2, cartItemUpdated.getQuantity());
        assertEquals(product1, cartItemUpdated.getProduct());
    }

    @Test
    public void delete_cartItem_from_cart_should_be_removed() throws Exception {
        // given
        prepareCartWithMultiProducts();

        // when
        this.mockMvc.perform(put("/rest/cart/delete/" + product1.getProductId())
                .session(mockHttpSession))
                .andExpect(status().isNoContent());
        Cart cartWithoutCartItem = cartService.getActiveCartBySessionId(mockHttpSession.getId());

        // then
        assertEquals(1, cartWithoutCartItem.getCartItemSet().size());
        assertEquals(product2.getUnitPrice(), cartWithMultiProducts.getCostOfAllItems());
    }

    @Test
    public void checkout_size_of_cart_should_return_2() throws Exception {
        // given
        prepareCartWithMultiProducts();

        // when
        // then
        this.mockMvc.perform(get("/rest/cart/items/" + mockHttpSession.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));
    }

    private void prepareCartWithMultiProducts() {
        product1 = ProductFactory.create();
        product1.setProductNo("111.111.11");
        product2 = ProductFactory.create();
        product2.setProductNo("222.222.22");
        productService.create(product1);
        productService.create(product2);
        CartItem cartItem1 = CartItemFactory.create(product1);
        CartItem cartItem2 = CartItemFactory.create(product2);
        cartWithMultiProducts = CartFactory.create(mockHttpSession.getId(), cartItem1, cartItem2);
        cartService.create(cartWithMultiProducts);
        cartItemService.create(cartItem1);
        cartItemService.create(cartItem2);
    }
}
