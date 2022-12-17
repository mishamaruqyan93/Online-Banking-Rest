package online.banking.rest.onlinebankingrest.service.impl;

import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.repository.CustomerRepository;
import online.banking.rest.onlinebankingrest.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> customer = customerRepository.findByEmail(email);
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new CurrentUser(customer.get());
    }
}
