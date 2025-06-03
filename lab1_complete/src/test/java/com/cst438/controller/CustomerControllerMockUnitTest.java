package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc @SpringBootTest
public class CustomerControllerMockUnitTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @WithMockUser(username="testname@csumb.edu")
    @Test
    public void registerAndLoginGood() throws Exception {
        CustomerDTO cdto = new CustomerDTO(0, "testname", "testname@csumb.edu", "password123", null);
        MockHttpServletResponse response = mvc.perform(
                        post("/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(cdto)))
                .andReturn()
                .getResponse();
        assertEquals(200, response.getStatus());
        CustomerDTO actual = fromJsonString(response.getContentAsString(), CustomerDTO.class);
        assertNotEquals(0, actual.customerId());

        // check that customer table row was inserted.
        Customer c = customerRepository.findById(actual.customerId()).orElse(null);
        assertNotNull(c);

        // now do login
        response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/login")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void registerBadEmailFails() throws Exception {
        // register user a malformed email
        CustomerDTO cdto = new CustomerDTO(0, "testname", "testname.csumb.edu", "password123", null);
        MockHttpServletResponse response = mvc.perform(
                        post("/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(cdto)))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        // validation errors are not being returned when an unsecured URL is invoked.
        // we cannot check for the exception message.
    }

    @WithMockUser(username="tom@csumb.edu")
    @Test
    public void createGoodOrder() throws Exception {

        OrderDTO orderDTO = new OrderDTO(0, null, "item123", 1, new BigDecimal("10.99"));
        // customer with email tom@csumb.edu must exist in data.sql

        MockHttpServletResponse response = mvc.perform(
                        post("/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        OrderDTO actualOrder = fromJsonString(response.getContentAsString(), OrderDTO.class);
        assertEquals("item123", actualOrder.item());
        assertEquals(1, actualOrder.quantity());

        // check that Order Entity was saved to database and is related to the correct customer.
        Order order = orderRepository.findById(actualOrder.orderId()).orElse(null);
        assertNotNull(order);
        assertEquals("tom@csumb.edu", order.getCustomer().getEmail());
    }

    @WithMockUser(username="tom@csumb.edu")
    @Test
    public void createOrderWithNullItemFails() throws Exception {

        OrderDTO orderDTO = new OrderDTO(0, null, null, 10, new BigDecimal("10.99"));
        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(orderDTO)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username="tom@csumb.edu")
    @Test
    public void createOrderWithZeroQuantityFails() throws Exception {

        OrderDTO orderDTO = new OrderDTO(0, null, "itemtest", 0, new BigDecimal("10.99"));
        mvc.perform(post("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(orderDTO)))
                .andExpect(status().isBadRequest());

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
