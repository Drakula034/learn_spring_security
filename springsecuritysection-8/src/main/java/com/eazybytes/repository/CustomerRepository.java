package com.eazybytes.repository;

import com.eazybytes.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByEmail(String email);

}
