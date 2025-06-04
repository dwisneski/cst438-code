package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.Random;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerUnitTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    WebTestClient client;

    Random random = new Random();


    @Test
    public void testRegisterAndCreateOrder() throws Exception {
        String email = String.format("test%s@csumb.edu", random.nextInt(1,10000));
        // register and obtain customer id
        int customerId = registerCustomer("testname", email, "password123");

        // login and obtain jwt token
        String jwt = login(email, "password123");

        // check that customer table row was inserted.
        Customer c = customerRepository.findById(customerId).orElse(null);
        assertNotNull(c);

        OrderDTO orderDTO = new OrderDTO(0, null, "item123", 1, new BigDecimal("15.95"));

        // create an Order
        EntityExchangeResult<OrderDTO> order =  client.post().uri("/orders")
                .headers(headers -> headers.setBearerAuth(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDTO.class).returnResult();
        int orderId = order.getResponseBody().orderId();
        assertNotEquals(0, orderId);

        // verify that an order record exists in the database
        Order o = orderRepository.findById(orderId).orElse(null);
        assertNotNull(o);
        assertEquals("item123", o.getItem());
        assertEquals(1, o.getQuantity());
        assertEquals(new BigDecimal("15.95"), o.getPrice());

        // create a second Order
        orderDTO = new OrderDTO(0, null, "item456", 2, new BigDecimal("29.95"));
        order =  client.post().uri("/orders")
                .headers(headers -> headers.setBearerAuth(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDTO.class).returnResult();
        int orderId2 = order.getResponseBody().orderId();
        assertNotEquals(0, orderId2);

        // get orders in reverse sequence
        EntityExchangeResult<OrderDTO[]> orders =  client.get().uri("/orders")
                .headers(headers -> headers.setBearerAuth(jwt))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDTO[].class).returnResult();

        OrderDTO[] result = orders.getResponseBody();
        // check that 2 orders are returned with orderId2 first
        assertEquals(2, result.length);
        assertEquals(orderId2, result[0].orderId());
        assertEquals(orderId, result[1].orderId());
        
    }

    // example of test for validation error 
    @Test
    public void testOrderValidation() throws Exception {
        // register and login
        String email = String.format("test%s@csumb.edu", random.nextInt(1,10000));
        // register and obtain customer id
        int customerId = registerCustomer("testname", email, "password123");
        String jwt = login(email, "password123");

        // create an invalid Order with null item, negative price and price too large
        // check that the correct validation messages are returned in response

        OrderDTO orderDTO = new OrderDTO(0, null, null, -1, new BigDecimal("999999999"));
		
		// expect that the status code is 400 BAD_REQUEST
		// and 3 validation messages 
        EntityExchangeResult<byte[]> response = client.post().uri("/orders")
                .headers(headers -> headers.setBearerAuth(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                   // search the list of validation messages
                   // jsonPath is documented at https://github.com/json-path/JsonPath
                .jsonPath("$.errors[?(@.defaultMessage=='quantity must be positive')]").exists()
                .jsonPath("$.errors[?(@.defaultMessage=='item can not be null')]").exists()
                .jsonPath("$.errors[?(@.defaultMessage=='invalid price')]").exists()
                .returnResult();

    }

    private int registerCustomer(String name, String email, String password) {
        CustomerDTO cdto = new CustomerDTO(0, name, email, password, null);

        EntityExchangeResult<CustomerDTO> register = client.post().uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cdto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class).returnResult();

        int customerId = register.getResponseBody().customerId();
        assertNotEquals(0,customerId);
        return customerId;
    }

    private String login(String email, String password) {
        EntityExchangeResult<CustomerLoginDTO> login =  client.get().uri("/login")
                .headers(headers -> headers.setBasicAuth(email, password))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerLoginDTO.class).returnResult();

        String jwt = login.getResponseBody().token();
        assertNotNull(jwt);
        return jwt;
    }
}
