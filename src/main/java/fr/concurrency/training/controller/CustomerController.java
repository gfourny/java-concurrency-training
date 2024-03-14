package fr.concurrency.training.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.concurrency.training.model.Customer;
import fr.concurrency.training.service.CustomerService;
import lombok.RequiredArgsConstructor;

/**
 * @author gfourny
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    
    @PostMapping("/customers")
    public List<Customer> createCustomers() {
        return customerService.createCustomers();
    }
}
