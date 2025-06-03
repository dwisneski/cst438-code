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
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerController(
            CustomerRepository customerRepository,
            OrderRepository orderRepository ) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }


    @GetMapping("/login")
    public CustomerDTO login(@RequestBody CustomerDTO dto) {
        System.out.println("login authentication "+dto.email());
		// TBD login method will be updated later for security 
        Customer c = customerRepository.findCustomerByEmail(dto.email());
        if (c==null || !c.getPassword().equals(dto.password())) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "email or password incorrect");
        }
        return new CustomerDTO(c.getCustId(), c.getName(), c.getEmail(), null, null);
    }


    @PostMapping("/register")
    public CustomerDTO register(@RequestBody CustomerDTO dto) {
        Customer c = customerRepository.findCustomerByEmail(dto.email());
        if (c!=null) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already in use");
        }
        c = new Customer();
        c.setEmail(dto.email());
        c.setName(dto.name());
        c.setPassword(dto.password()); // TBD encrypt password for security
        customerRepository.save(c);
        dto = new CustomerDTO(c.getCustId(), c.getName(), c.getEmail(), null, null);
        return dto;
    }

    @GetMapping("/customers/{id}/orders")
    public Stream<OrderDTO> getOrders(@PathVariable("id") int custId ) {
        List<Order> orders = customerRepository.findOrdersByCustomerOrderByDateDesc(custId);
        return orders.stream().map(order -> new OrderDTO(
                order.getOrderId(),
                order.getOrderDate(),
                order.getItem(),
                order.getQuantity(),
                order.getPrice()));
    }

    @PostMapping("/customers/{id}/orders")
    public OrderDTO placeOrder(@RequestBody OrderDTO dto, @PathVariable("id") int custId) {
        Customer c = customerRepository.findById(custId).orElse(null);
        if (c==null)
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "customer invalid");
        Order order = new Order();
        order.setCustomer(c);
        order.setOrderDate(Date.valueOf(LocalDate.now()) );
        order.setItem(dto.item());
        order.setQuantity(dto.quantity());
        order.setPrice(dto.price());
        orderRepository.save(order);
        return new OrderDTO(
                order.getOrderId(),
                order.getOrderDate(),
                order.getItem(),
                order.getQuantity(),
                order.getPrice());
    }

    @PutMapping("/orders")
    public OrderDTO updateOrder(@RequestBody OrderDTO dto) {
        Order order = orderRepository.findById(dto.orderId()).orElse(null);
        // order not found, does not belong to user --> error
        if (order == null )
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "orderId invalid");
        // sanitize item
        order.setItem(dto.item());
        order.setQuantity(dto.quantity());
        order.setPrice(dto.price());
        orderRepository.save(order);
        return new OrderDTO(
                order.getOrderId(),
                order.getOrderDate(),
                order.getItem(),
                order.getQuantity(),
                order.getPrice());
    }

    @DeleteMapping("/orders/{orderId}")
    public void deleteOrder(@PathVariable("orderId") int orderId) {
        orderRepository.deleteById(orderId);
	}

}
