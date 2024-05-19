package com.eazybytes.config;

import com.eazybytes.model.Authority;
import com.eazybytes.model.Customer;
import com.eazybytes.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
public class EazyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /*
         * 1. Get the username(in this case email) and password from the authentication object
         * 2. Check if the user exists in the database
         * 3. Check if the password is correct
         * 4. Return the user details as a token
         */
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<Customer> customerList = customerRepository.findByEmail(username);
        if(customerList.size() > 0){
            if(passwordEncoder.matches(password, customerList.get(0).getPwd())){
//                List<GrantedAuthority> authorities = new ArrayList<>();
//                authorities.add(new SimpleGrantedAuthority(customerList.get(0).getRole()));
                return new UsernamePasswordAuthenticationToken(username, password, getGrantedAuthorities(customerList.get(0).getAuthorities()));
            }else{
                throw new BadCredentialsException("Incorrect Password");
            }

        }else{
            throw new BadCredentialsException("No User registered with this details");
        }
//        return null;
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(Authority authority : authorities){
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
