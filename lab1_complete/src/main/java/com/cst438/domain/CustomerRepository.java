package com.cst438.domain;

import org.springframework.data.repository.CrudRepository;


public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    Customer findCustomerByEmail(String email);

}
