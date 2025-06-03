package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerWebClientMockUnitTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    WebTestClient client;

    static String jwt;

    @LocalServerPort
    private  int port;

    @BeforeEach
    public  void eachTime() {
        if (jwt!=null) {
            System.out.println("no login needed");
        } else {
            System.out.println("performing login");
            assertNotEquals(0, port);
            WebTestClient webClient = WebTestClient.bindToServer()
                    .baseUrl("http://localhost:" + port)
                    .build();

            EntityExchangeResult<CustomerDTO> s = webClient.get().uri("/login")
                    .headers(headers -> headers.setBasicAuth("tom@csumb.edu", "tom123"))
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(CustomerDTO.class).returnResult();

            jwt = s.getResponseBody().jwtToken();
            assertNotNull(jwt);
        }
    }

//    @BeforeEach
//    public  void login() {
//        if (jwt!=null) {
//            System.out.println("no login required");
//        } else {
//            System.out.println("doing login");
//
//        EntityExchangeResult<CustomerDTO> s =  client.get().uri("/login")
//                .headers(headers -> headers.setBasicAuth("tom@csumb.edu", "tom123"))
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(CustomerDTO.class).returnResult();
//
//        jwt = s.getResponseBody().jwtToken();
//        assertNotNull(jwt);
//        }
//    }

    @Test
    public void registerAndLoginGood() throws Exception {
//        CustomerDTO cdto = new CustomerDTO(0, "testname", "testname@csumb.edu", "password123", null);
//
//        EntityExchangeResult<CustomerDTO> s = client.post().uri("/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(cdto)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(CustomerDTO.class).returnResult();
//        System.out.println(s.getResponseBody());
//        assertNotEquals(0, s.getResponseBody().customerId());
//
//        s =  client.get().uri("/login")
//                .headers(headers -> headers.setBasicAuth("testname@csumb.edu", "password123"))
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(CustomerDTO.class).returnResult();
//
//        jwt = s.getResponseBody().jwtToken();
//        assertNotNull(jwt);
//
//
//        // check that customer table row was inserted.
//        Customer c = customerRepository.findById(s.getResponseBody().customerId()).orElse(null);
//        assertNotNull(c);

        OrderDTO  orderDTO = new OrderDTO(0, null, "test-item", 1, new BigDecimal("5.99"));
        // create order
        EntityExchangeResult<OrderDTO> actualOrder = client.post().uri("/orders")
                .headers(headers -> headers.setBearerAuth(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDTO.class).returnResult();

        assertNotEquals(0, actualOrder.getResponseBody().orderId() );
        System.out.println(actualOrder.getResponseBody());
    }

    @Test
    public void testGetOrderHistory() {
        EntityExchangeResult<OrderDTO[]> s =  client.get().uri("/customers/orders")
                .headers(headers -> headers.setBearerAuth(jwt))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDTO[].class).returnResult();
        OrderDTO[] actual = s.getResponseBody();
        System.out.println(actual.length);
        for (OrderDTO orderDTO : actual) System.out.println(orderDTO);
    }

//    @Test
//    public void registerBadEmailFails() throws Exception {
//        // register user a malformed email
//        CustomerDTO cdto = new CustomerDTO(0, "testname", "testname.csumb.edu", "password123", null);
//        MockHttpServletResponse response = mvc.perform(
//                        post("/register")
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(asJsonString(cdto)))
//                .andReturn()
//                .getResponse();
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//
//        // validation errors are not being returned when an unsecured URL is invoked.
//        // we cannot check for the exception message.
//    }
//
//    @Test
//    public void createGoodOrder() throws Exception {
//
//        OrderDTO orderDTO = new OrderDTO(0, null, "item123", 1, new BigDecimal("10.99"));
//        // customer with email tom@csumb.edu must exist in data.sql
//
//        MockHttpServletResponse response = mvc.perform(
//                        post("/orders")
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(asJsonString(orderDTO)))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse();
//
//        OrderDTO actualOrder = fromJsonString(response.getContentAsString(), OrderDTO.class);
//        assertEquals("item123", actualOrder.item());
//        assertEquals(1, actualOrder.quantity());
//
//        // check that Order Entity was saved to database and is related to the correct customer.
//        Order order = orderRepository.findById(actualOrder.orderId()).orElse(null);
//        assertNotNull(order);
//        assertEquals("tom@csumb.edu", order.getCustomer().getEmail());
//    }
//
//    @Test
//    public void createOrderWithNullItemFails() throws Exception {
//
//        OrderDTO orderDTO = new OrderDTO(0, null, null, 10, new BigDecimal("10.99"));
//        mvc.perform(post("/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(orderDTO)))
//                .andExpect(status().isBadRequest());
//
//        OrderDTO orderDTO = new OrderDTO(0, null, "itemtest", 0, new BigDecimal("10.99"));
//        mvc.perform(post("/orders")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(orderDTO)))
//                .andExpect(status().isBadRequest());
//
//    }
//
//
//    private static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static <T> T  fromJsonString(String str, Class<T> valueType ) {
//        try {
//            return new ObjectMapper().readValue(str, valueType);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}

