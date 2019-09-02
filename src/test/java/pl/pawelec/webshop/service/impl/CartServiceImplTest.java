package pl.pawelec.webshop.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pawelec.webshop.config.WebstoreApplication;
import pl.pawelec.webshop.data.CartFactory;
import pl.pawelec.webshop.data.CartItemFactory;
import pl.pawelec.webshop.data.ProductFactory;
import pl.pawelec.webshop.model.Cart;
import pl.pawelec.webshop.model.CartItem;
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.model.UserInfo;
import pl.pawelec.webshop.repository.CartRepository;
import pl.pawelec.webshop.service.CartItemService;
import pl.pawelec.webshop.service.CartService;
import pl.pawelec.webshop.service.ProductService;
import pl.pawelec.webshop.service.UserInfoService;
import pl.pawelec.webshop.service.converter.CartNotFoundException;
import pl.pawelec.webshop.service.exception.NoProductIdFoundException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebstoreApplication.class})
public class CartServiceImplTest {

    @Autowired
    private CartService cartService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private UserInfoService userInfoService;

    @MockBean
    private ProductService productService;

    @MockBean
    private CartItemService cartItemService;

    @Test
    public void testGetActiveCartBySessionIdMethod_ShouldReturnNewCartObjectWithoutCartIdField_WhenAnyCartWasNotFoundInDatabase() {
        // given
        final String sessionId = "123";
        when(cartRepository.getBySessionId(sessionId)).thenReturn(Collections.emptyList());

        // when
        Cart cart = cartService.getActiveCartBySessionId(sessionId);

        // then
        assertNull(cart.getCartId());
        assertEquals(sessionId, cart.getSessionId());
    }

    @Test
    public void testGetActiveCartBySessionIdMethod_ShouldReturnExistingCartObjectWithCartIdField_WhenTheCartWasSavedInDatabaseBefore() {
        // given
        final Long cartId = 1l;
        final String sessionId = "123456789";
        Cart savedCart = CartFactory.create(cartId, sessionId);
        when(cartRepository.getBySessionId(sessionId)).thenReturn(Arrays.asList(savedCart));

        // when
        Cart cart = cartService.getActiveCartBySessionId(sessionId);

        // then
        assertEquals(cartId, cart.getCartId());
        assertEquals(sessionId, cart.getSessionId());
    }

    @Test
    public void testAddItemToCartMethod_ShouldNotThrowAnyErrors_CartItemObjectShouldBeUpdated() {
        // given
        final Long cartId = 1L;
        final Long productId = 2L;
        final Long cartItemId = 3L;
        final String sessionId = "123456789";
        final String userLogin = "test_user";
        Cart cart = CartFactory.create(cartId, sessionId);
        UserInfo userInfo = new UserInfo();
        userInfo.setLogin(userLogin);
        cart.setUser(userInfo);
        Product product = ProductFactory.create(productId);
        CartItem cartItem = CartItemFactory.create(cartItemId, product);
        cart.getCartItemSet().add(cartItem);

        when(cartRepository.getBySessionId(sessionId)).thenReturn(Arrays.asList(cart));
        when(userInfoService.getByLogin(userLogin)).thenReturn(userInfo);
        doNothing().when(cartRepository).create(cart);
        when(productService.getOneById(productId)).thenReturn(product);
        doNothing().when(cartItemService).create(cartItem);

        // when
        // then
        Assertions.assertThatCode(() -> cartService.addItemToCart(sessionId, userLogin, productId))
                .doesNotThrowAnyException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemToCartMethod_ShouldThrowTheIllegalArgumentException() {
        // given
        final Long cartId = 1L;
        final Long productId = 2L;
        final String sessionId = "123456789";
        Cart cart = CartFactory.create(cartId, sessionId);

        when(cartRepository.getBySessionId(sessionId)).thenReturn(Arrays.asList(cart));
        when(productService.getOneById(productId)).thenThrow(NoProductIdFoundException.class);

        // when
        // then
        cartService.addItemToCart(sessionId, null, productId);
    }

    @Test
    public void testAddItemToCartMethod_ShouldNotThrowAnyErrors_CartItemObjectShouldBeCreated() {
        // given
        final Long cartId = 1L;
        final Long productId = 2L;
        final String sessionId = "123456789";
        Cart cart = CartFactory.create(cartId, sessionId);
        Product product = ProductFactory.create(productId);
        CartItem cartItem = CartItemFactory.create(product);

        when(cartRepository.getBySessionId(sessionId)).thenReturn(Arrays.asList(cart));
        when(productService.getOneById(productId)).thenReturn(product);
        doNothing().when(cartItemService).create(cartItem);

        // when
        // then
        Assertions.assertThatCode(() -> cartService.addItemToCart(sessionId, null, productId))
                .doesNotThrowAnyException();
    }

    @Test(expected = CartNotFoundException.class)
    public void testDeleteItemFromCartMethod_ShouldThrowCartNotFoundException_WhenNotFoundCart() {
        // given
        final Long productId = 1L;
        final String sessionId = "123456789";

        when(cartRepository.getBySessionId(sessionId)).thenReturn(Collections.emptyList());

        // when
        // then
        cartService.deleteItemFromCart(sessionId, productId);
    }

    @Test
    public void testDeleteItemFromCartMethod_ShouldNotThrowAnyErrors_CartItemObjectShouldBeDeleted() {
        // given
        final Long cartId = 1L;
        final Long productId = 2L;
        final Long cartItemId = 3L;
        final String sessionId = "123456789";
        Cart cart = CartFactory.create(cartId, sessionId);
        Product product = ProductFactory.create(productId);
        CartItem cartItem = CartItemFactory.create(cartItemId, product);
        cart.getCartItemSet().add(cartItem);

        when(cartRepository.getBySessionId(sessionId)).thenReturn(Arrays.asList(cart));
        doNothing().when(cartItemService).delete(cartItem);

        // when
        // then
        Assertions.assertThatCode(() -> cartService.deleteItemFromCart(sessionId, productId))
                .doesNotThrowAnyException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteItemFromCartMethod_ShouldThrowIllegalArgumentException_WhenCartNotContainGivenProduct() {
        // given
        final Long cartId = 1L;
        final Long productId = 2L;
        final Long unknownProductId = 3L;
        final Long cartItemId = 4L;
        final String sessionId = "123456789";
        Cart cart = CartFactory.create(cartId, sessionId);
        Product product = ProductFactory.create(productId);
        CartItem cartItem = CartItemFactory.create(cartItemId, product);
        cart.getCartItemSet().add(cartItem);

        when(cartRepository.getBySessionId(sessionId)).thenReturn(Arrays.asList(cart));
        doNothing().when(cartItemService).delete(cartItem);

        // when
        // then
        cartService.deleteItemFromCart(sessionId, unknownProductId);
    }

    @Test
    public void testGetSizeOfCartMethod_ShouldReturnNumberOfCartItemElementsInCart() {
        // given
        final String sessionId = "123456789";
        final int numberOfProductsInCart = 2;
        Cart cart = CartFactory.create(sessionId, CartItemFactory.create(1L, ProductFactory.create(2L)),
                CartItemFactory.create(3L, ProductFactory.create(4L)));
        cart.setCartId(5L);

        when(cartRepository.getBySessionId(sessionId)).thenReturn(Arrays.asList(cart));

        // when
        // then
        assertEquals(numberOfProductsInCart, cartService.getSizeOfCart(sessionId));
    }
}
