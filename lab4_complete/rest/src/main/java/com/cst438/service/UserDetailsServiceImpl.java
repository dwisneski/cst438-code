package com.cst438.service;

import com.cst438.domain.Customer;
import com.cst438.domain.CustomerRepository;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService  {

    private final CustomerRepository repository;

    public UserDetailsServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer currentUser = repository.findCustomerByEmail(username);
        UserBuilder builder = null;
        if (currentUser!=null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(currentUser.getPassword());
            // setting role to XXXX is equivalent to the authority ROLE_XXXX
            builder.roles("customer");
        } else {
            System.out.println("Customer not found. " + username);
            throw new UsernameNotFoundException("Customer not found.");
        }

        return builder.build();
    }
}
