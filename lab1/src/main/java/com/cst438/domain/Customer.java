package com.cst438.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="cust_id")
    private int custId;
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy="customer")
    private List<Order> orders;

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
