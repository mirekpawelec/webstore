/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webstore.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author mirek
 */
@Entity
@Table(name = "product" )
public class Product implements Serializable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "product_id")
    private Long productId;
    @Column(name = "product_no", nullable = false, length = 20, unique = true)
    private String productNo;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String manufacturer;
    @Column(nullable = false, length = 25)
    private String category;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal unitPrice;
    @Column(nullable = false, precision = 4, scale = 0)
    private Integer quantityInBox;
    @Column(nullable = true, length = 2)
    private String status;
    @Column(nullable = true)
    private LocalDateTime createDate;

    public Product() {
    }

    public Product(Builder builder) {
        this.productId = builder.productId;
        this.productNo = builder.productNo;
        this.name = builder.name;
        this.manufacturer = builder.manufacturer;
        this.category = builder.category;
        this.description = builder.description;
        this.unitPrice = builder.unitPrice;
        this.quantityInBox = builder.quantityInBox;
        this.status = builder.status;
        this.createDate = builder.createDate;
    }
    
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    @NotNull
    public String getProductNo() {
        return productNo;
    }
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }
    //@NotNull
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    //@NotNull
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    //@NotNull
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    //@NotNull
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    //@NotNull
    //@Min(value = 0)
    //@Digits(integer = 7, fraction = 2)
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    //@NotNull
    //@Min(value = 0)
    //@Digits(integer = 4, fraction = 0)
    public Integer getQuantityInBox() {
        return quantityInBox;
    }
    public void setQuantityInBox(Integer quantityInBox) {
        this.quantityInBox = quantityInBox;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.productId);
        hash = 29 * hash + Objects.hashCode(this.productNo);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.productNo, other.productNo)) {
            return false;
        }
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", productNo=" + productNo + ", name=" + name + ", manufacturer=" + manufacturer + ", category=" + category + ", description=" + description + ", unitPrice=" + unitPrice + ", quantityInBox=" + quantityInBox + ", status=" + status + ", createDate=" + createDate + '}';
    }
    
        public static class Builder{
            private Long productId;
            private String productNo;
            private String name;
            private String manufacturer;
            private String category;
            private String description;
            private BigDecimal unitPrice;
            private Integer quantityInBox;
            private String status;
            private LocalDateTime createDate;

            public Builder withProductId(Long productId){
                this.productId=productId;
                return this;
            }
            public Builder withProductNo(String productNo){
                this.productNo=productNo;
                return this;
            }
            public Builder withName(String name){
                this.name=name;
                return this;
            }
            public Builder withManufacturer(String manufacturer){
                this.manufacturer=manufacturer;
                return this;
            }
            public Builder withCategory(String category){
                this.category=category;
                return this;
            }
            public Builder withDescription(String description){
                this.description=description;
                return this;
            }
            public Builder withUnitPrice(BigDecimal unitPrice){
                this.unitPrice=unitPrice;
                return this;
            }
            public Builder withQuantityInBox(Integer quantityInBox){
                this.quantityInBox=quantityInBox;
                return this;
            }
            public Builder withStatus(String status){
                this.status=status;
                return this;
            }
            public Builder withCreateDate(LocalDateTime createDate){
                this.createDate=createDate;
                return this;
            }
            public Product build(){
                return new Product(this);
            }
        }
    
}
