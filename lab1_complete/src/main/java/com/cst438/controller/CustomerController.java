package com.cst438.controller;

import com.cst438.dto.*;
import com.cst438.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.cst438.domain.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final TokenService tokenService;

    private final BCryptPasswordEncoder encoder;

    public CustomerController(
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            TokenService tokenService) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.tokenService = tokenService;
        encoder = new BCryptPasswordEncoder();
    }

    // basic authorization is used to invoke the login method which returns
    // a CustomerDTO containing the jwt security token
    @GetMapping("/login")
    public CustomerDTO login(Authentication authentication) {
        System.out.println("login authentication "+authentication.getName());
        Customer c = customerRepository.findCustomerByEmail(authentication.getName());
        if (c==null) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "email or password incorrect");
        }
        String token = tokenService.generateToken(authentication);
        return new CustomerDTO(c.getCustId(), c.getName(), c.getEmail(), null, token);
    }


    @PostMapping("/register")
    public CustomerDTO register(@Valid @RequestBody CustomerDTO dto) {
        System.out.println("register");
        Customer c = customerRepository.findCustomerByEmail(dto.email());
        if (c!=null) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already in use");
        }
        c = new Customer();
        c.setEmail(dto.email());
        c.setName(dto.name());
        c.setPassword(encoder.encode(dto.password())); // save encrypted password
        customerRepository.save(c);
        dto = new CustomerDTO(c.getCustId(), c.getName(), c.getEmail(), null, null);
        return dto;
    }

    @GetMapping("/customers/orders")
    public Stream<OrderDTO> getOrders(Principal principal ) {
        Customer c = customerRepository.findCustomerByEmail(principal.getName());
        if (c==null)
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "customer invalid");
        List<Order> orders = customerRepository.findOrdersByCustomerOrderByDateDesc(c.getCustId());
        return orders.stream().map(order -> new OrderDTO(
                order.getOrderId(),
                order.getOrderDate(),
                order.getItem(),
                order.getQuantity(),
                order.getPrice()));
    }

    @PostMapping("/orders")
    public OrderDTO placeOrder(@Valid @RequestBody OrderDTO dto, Principal principal) {
        Customer c = customerRepository.findCustomerByEmail(principal.getName());
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
    public OrderDTO updateOrder(@Valid @RequestBody OrderDTO dto, Principal principal) {
        Customer c = customerRepository.findCustomerByEmail(principal.getName());
        if (c == null)
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "customer invalid");
        Order order = orderRepository.findById(dto.orderId()).orElse(null);
        // order not found, does not belong to user --> error
        if (order == null || order.getCustomer().getCustId() != c.getCustId())
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
    public void deleteOrder(@PathVariable("orderId") int orderId, Principal principal) {
        Customer c = customerRepository.findCustomerByEmail(principal.getName());
        if (c == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customer invalid");
        Order order = orderRepository.findById(orderId).orElse(null);
        // order not found, does not belong to user --> error
        if (order == null || order.getCustomer().getCustId() != c.getCustId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "orderId invalid");
        orderRepository.delete(order);
    }

}
