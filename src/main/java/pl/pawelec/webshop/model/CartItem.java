/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.pawelec.webshop.model.status.CartStatus;
import pl.pawelec.webshop.service.converter.TimestampToLocalDateTimeConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cart_items")
public class CartItem implements Serializable {

    private static final long serialVersionUID = -1347553385512546482L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "cart_id", referencedColumnName = "cart_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Cart cart;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(length = 2)
    private String status;

    @Convert(converter = TimestampToLocalDateTimeConverter.class)
    @Column(name = "lm_date")
    private LocalDateTime lastModificationDate;

    @Convert(converter = TimestampToLocalDateTimeConverter.class)
    @Column(name = "c_date")
    private LocalDateTime createDate;

    public CartItem() {
        cart = new Cart();
        product = new Product();
    }

    public CartItem(Product product) {
        this();
        this.product = product;
        this.quantity = 1;
        this.totalPrice = product.getUnitPrice();
        this.status = CartStatus.RE.name();
        this.lastModificationDate = LocalDateTime.now();
        this.createDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonIgnore
    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(LocalDateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    @JsonIgnore
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void updateTotalPrice() {
        this.totalPrice = product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity &&
                Objects.equals(id, cartItem.id) &&
                Objects.equals(cart, cartItem.cart) &&
                Objects.equals(product, cartItem.product) &&
                Objects.equals(totalPrice, cartItem.totalPrice) &&
                Objects.equals(status, cartItem.status) &&
                Objects.equals(lastModificationDate, cartItem.lastModificationDate) &&
                Objects.equals(createDate, cartItem.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cart, product, quantity, totalPrice, status, lastModificationDate, createDate);
    }

    @Override
    public String toString() {
        return "CartItem{" + "id=" + id + ", cartId=" + cart.getCartId() + ", product=" + product + ", quantity=" + quantity + ", totalPrice=" + totalPrice
                + ", status=" + status + ", lastModificationDate=" + lastModificationDate + ", createDate=" + createDate + '}';
    }

}
