package fr.concurrency.training.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.concurrency.training.repository.CustomerRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author gfourny
 */
class CustomerServiceTest extends AbastractITSpring {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Doit retourner la liste des utilisateurs")
    void testCreateCustomers() {
        var customers = customerService.createCustomers();
        assertThat(customers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1_000);
    }

}