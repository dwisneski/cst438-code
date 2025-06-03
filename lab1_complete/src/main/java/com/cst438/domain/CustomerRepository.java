package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Query("select o from Order o where o.customer.custId=:custId order by o.orderDate desc")
    List<Order> findOrdersByCustomerOrderByDateDesc(int custId);

    @Query("select c from Customer c where c.email=:email")
    Customer findCustomerByEmail(String email);
}
