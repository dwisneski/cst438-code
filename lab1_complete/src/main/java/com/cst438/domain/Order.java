package com.cst438.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name="ordertab")
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="order_id")
    private int orderId;
    private Date orderDate;
    private String item;
    private int quantity;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name="cust_id", nullable=false)
    private Customer customer;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
