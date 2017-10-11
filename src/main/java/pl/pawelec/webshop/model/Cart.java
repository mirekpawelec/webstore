/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import pl.pawelec.webshop.converter.CartItemConvertToJson;
import pl.pawelec.webshop.converter.TimestampToLocalDateTimeConverter;
import pl.pawelec.webshop.model.enum_.CartStatus;

/**
 *
 * @author mirek
 */
@Entity
@Table(name = "cart")
public class Cart implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="cart_id", nullable = false)
    private Long cartId;
    
    @Column(name = "session_id", nullable = false, length = 100)
    private String sessiontId;
    
    @Column(name = "user_id")
    private Long userId;
    
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
    private Set<CartItem> cartItemSet;
    
//    @OneToOne(mappedBy = "cart", fetch = FetchType.EAGER)
//    private Order order;
    
    
    
    public Cart() {
        this.costOfAllItems = new BigDecimal(0);
        this.cartItemSet = new HashSet<CartItem>();
//        this.order = new Order(); 
    }

    public Cart(String sessiontId) {
        this();
        this.sessiontId = sessiontId;
        this.status = CartStatus.RE.name();
        this.lastModificationDate = LocalDateTime.now();
        this.createDate = LocalDateTime.now();
    }   

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }
    
    public String getSessiontId() {
        return sessiontId;
    }

    public void setSessiontId(String sessiontId) {
        this.sessiontId = sessiontId;
    }
    
    @JsonIgnore
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public BigDecimal getCostOfAllItems() {
        return costOfAllItems;
    }   

//    @JsonIgnore
//    public Order getOrder() {
//        return order;
//    }
//
//    public void setOrder(Order order) {
//        this.order = order;
//    }
    
    
    
    
    public void updateCostOfAllItems() {
        costOfAllItems = new BigDecimal(0);
        // map przekazuje dalej tylko cene, a reduce ją sumuje
        costOfAllItems = cartItemSet.stream().map( c -> c.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.cartId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cart other = (Cart) obj;
        if (!Objects.equals(this.cartId, other.cartId)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        cartItemSet.forEach(v -> sb.append("[productNo=" + v.getProduct().getProductNo() + ", quantity=" + v.getQuantity() + ", totalPrice=" + v.getTotalPrice() + "] "));
        return "Cart{ cartId=" + cartId + ", sessiontId=" + sessiontId + ", userId=" + userId + ", status=" + status + ", lastModificationDate=" + lastModificationDate 
             + ", createDate=" + createDate + ", costOfAllItems=" + costOfAllItems + ", cartItemSet=" + sb.toString() /* + ", order=" + order */ + '}';
    }

}