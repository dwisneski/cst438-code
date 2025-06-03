package com.cst438.controller;


import com.cst438.domain.Customer;
import com.cst438.domain.CustomerRepository;
import com.cst438.domain.Order;
import com.cst438.domain.OrderRepository;
import com.cst438.dto.CustomerDTO;
import com.cst438.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Run a unit test environment using a Spring server running on a random port number
// Why a random port -- so that multiple unit tests can be run in parallel without conflicting
// for the same port number
// The unit test and the spring server run in a single process
// When using H2 in-memory database, the database is also in the same process
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerUnitTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TestRestTemplate template;


    @Test
    public void registerAndLoginGood()  {
        CustomerDTO cdto = new CustomerDTO(0, "testname", "testname@csumb.edu", "password123", null);
        ResponseEntity<String> result = template.postForEntity("/register", cdto, String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        // check that customer table row was inserted.
        Customer c = customerRepository.findCustomerByEmail("testname@csumb.edu");
        assertNotNull(c);

        // now try to login
        result = template.withBasicAuth("testname@csumb.edu", "password123")
                .getForEntity("/login", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void registerBadEmailFails() throws JsonProcessingException {
        // register user with bad email
        CustomerDTO cdto = new CustomerDTO(0, "testname", "testname.csumb.edu", "password123", null);
        ResponseEntity<String> result = template.postForEntity("/register", cdto, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

        // validation errors are not being returned when an unsecured URL is invoked.
        // we cannot check for the exception message.
    }


    @Test
    public void createGoodOrder() {

        OrderDTO orderDTO = new OrderDTO(0, null, "item123", 1, new BigDecimal("10.99"));
        ResponseEntity<String> result = template.withBasicAuth("tom@csumb.edu", "tom123")
                        .postForEntity("/orders", orderDTO, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        OrderDTO resultOrder = fromJsonString(result.getBody(), OrderDTO.class);

        // verify that order is saved to database
        Order order = orderRepository.findById(resultOrder.orderId()).orElse(null);
        assertNotNull(order);

    }

    @Test
    public void createOrderWithNullItemFails() throws JsonProcessingException {

        OrderDTO orderDTO = new OrderDTO(0, null, null, 10, new BigDecimal("10.99"));


        ResponseEntity<String> result = template.withBasicAuth("tom@csumb.edu", "tom123")
                        .postForEntity("/orders", orderDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        boolean msgFound = isValidationErrorFound(result.getBody(), "Item can not be null");
        assertTrue(msgFound, "Response did not contain validation error message 'Item can not be null'");
    }

    @Test
    public void createOrderWithZeroQuantityFails() throws JsonProcessingException {

        OrderDTO orderDTO = new OrderDTO(0, null, "itemtest", 0, new BigDecimal("10.99"));


        ResponseEntity<String> result = template.withBasicAuth("tom@csumb.edu", "tom123")
                .postForEntity("/orders", orderDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        boolean msgFound = isValidationErrorFound(result.getBody(), "Order quantity must be greater than 0");
        assertTrue(msgFound, "Response did not contain validation error message 'Item can not be null'");
    }

    // return true if a validation message is present in the response exception data.
    private boolean isValidationErrorFound(String body, String message) throws JsonProcessingException {
        // more info about jsonpath https://github.com/json-path/JsonPath
        // does an "errors" attribute exists has contains "defaultMessage" with the given value
        String path = String.format("$.errors[?(@.defaultMessage=='%s')].defaultMessage", message);
        List<String> df  =  JsonPath.read(body, path);
        return !df.isEmpty();

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T  fromJsonString(String str, Class<T> valueType ) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

