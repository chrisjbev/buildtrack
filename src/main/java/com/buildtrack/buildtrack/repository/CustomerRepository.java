package com.buildtrack.buildtrack.repository;

import com.buildtrack.buildtrack.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
