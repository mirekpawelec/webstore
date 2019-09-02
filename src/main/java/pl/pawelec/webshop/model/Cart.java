/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.pawelec.webshop.model.status.CartStatus;
import pl.pawelec.webshop.service.converter.CartItemConvertToJson;
import pl.pawelec.webshop.service.converter.TimestampToLocalDateTimeConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1348780155642091863L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @Column(name = "session_id", nullable = false, length = 100)
    private String sessionId;

    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private UserInfo user;

    @Column(length = 2)
    private String status;

    @Convert(converter = TimestampToLocalDateTimeConverter.class)
    @Column(name = "lm_date")
    private LocalDateTime lastModificationDate;

    @Convert(converter = TimestampToLocalDateTimeConverter.class)
    @Column(name = "c_date")
    private LocalDateTime createDate;

    @Transient
    private BigDecimal costOfAllItems;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cart", fetch = FetchType.EAGER)
    private Set<CartItem> cartItemSet = new HashSet<CartItem>();

    @OneToOne(mappedBy = "cart", fetch = FetchType.EAGER)
    private Order order;

    public Cart() {
        this.status = CartStatus.RE.name();
        this.costOfAllItems = cartItemSet.stream()
                .map(c -> c.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.lastModificationDate = LocalDateTime.now();
        this.createDate = LocalDateTime.now();
    }

    public Cart(String sessiontId) {
        this();
        this.sessionId = sessiontId;
    }

    public Cart(String sessionId, UserInfo user) {
        this();
        this.sessionId = sessionId;
        this.user = user;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessiontId) {
        this.sessionId = sessiontId;
    }

    @JsonIgnore
    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
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

    @JsonSerialize(converter = CartItemConvertToJson.class)
    public Set<CartItem> getCartItemSet() {
        return cartItemSet;
    }

    public void setCartItemSet(Set<CartItem> cartItemSet) {
        this.cartItemSet = cartItemSet;
    }

    public BigDecimal getCostOfAllItems() {
        return costOfAllItems;
    }

    @JsonIgnore
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isNew() {
        return Objects.isNull(this.cartId);
    }

    public void updateCostOfAllItems() {
        costOfAllItems = new BigDecimal(0);
        costOfAllItems = cartItemSet.stream()
                .map(c -> c.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(cartId, cart.cartId) &&
                Objects.equals(sessionId, cart.sessionId) &&
                Objects.equals(user, cart.user) &&
                Objects.equals(status, cart.status) &&
                Objects.equals(lastModificationDate, cart.lastModificationDate) &&
                Objects.equals(createDate, cart.createDate) &&
                Objects.equals(costOfAllItems, cart.costOfAllItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, sessionId, user, status, lastModificationDate, createDate, costOfAllItems);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        cartItemSet.forEach(v -> sb.append("[productNo=" + v.getProduct().getProductNo()
                + ", quantity=" + v.getQuantity()
                + ", totalPrice=" + v.getTotalPrice() + "] ")
        );
        return "Cart{"
                + " cartId=" + cartId
                + ", sessionId=" + sessionId
                + ", user=" + user
                + ", status=" + status
                + ", lastModificationDate=" + lastModificationDate
                + ", createDate=" + createDate
                + ", costOfAllItems=" + costOfAllItems
                + ", cartItemSet=" + sb.toString()
                + '}';
    }

}
