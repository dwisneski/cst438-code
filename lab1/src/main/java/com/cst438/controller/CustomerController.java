package com.cst438.controller;

import com.cst438.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.cst438.domain.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class CustomerController {
 
 // define variables for CustomerRepository, OrderRepository
 
 // use constructor injection to set values


    @GetMapping("/login")
    public CustomerDTO login(@RequestBody CustomerDTO dto) {
		// TBD login method will be updated later for security 
        return null;
    }


    @PostMapping("/register")
    public CustomerDTO register(@RequestBody CustomerDTO dto) {
		// check that customer email does not already exist
		// if so throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already in use");
       
	   // create customer entity, set fields and save to database
	   
	   // return CustomerDTO with id, name, email
      return null;
    }

    @GetMapping("/customers/{id}/orders")
    public Stream<OrderDTO> getOrders(@PathVariable("id") int customerId ) {
		// return the orders for a customer
		
        return null;
    }

    @PostMapping("/customers/{id}/orders")
    public OrderDTO placeOrder(@RequestBody OrderDTO dto, @PathVariable("id") int customerId) {
        // verify customerId
		// create Order entity and save
		return null;
    }

    @PutMapping("/orders")
    public OrderDTO updateOrder(@RequestBody OrderDTO dto) {
        // find and update Order entity
		return null;
    }

    @DeleteMapping("/orders/{orderId}")
    public void deleteOrder(@PathVariable("orderId") int orderId) {
        // delete Order entity
	}

}
