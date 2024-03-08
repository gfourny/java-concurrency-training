package fr.concurrency.training.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import fr.concurrency.training.config.WiremockInitializer;
import fr.concurrency.training.model.Customer;
import fr.concurrency.training.repository.CustomerRepository;
import lombok.val;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

/**
 * @author gfourny
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = { WiremockInitializer.class })
@ActiveProfiles("test")
class DBTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    @Autowired
    CustomerRepository customerRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldGetAllCustomers() {

        val customers = Instancio.ofList(Customer.class)
                .size(10000)
                .set(field(Customer::getId), null)
                .generate(field(Customer::getEmail), gen -> gen.net().email())
                .create();

        //FIXME nombre de thread trop élevé, plus de connexion bdd dispo
        customers.stream()
                .map(customer -> CompletableFuture.supplyAsync(() -> customerRepository.save(customer), executorService))
                .toList()
                .forEach(CompletableFuture::join);

        //FIXME nombre de thread trop élevé, plus de connexion bdd dispo
        //customers.parallelStream().forEach(customer -> customerRepository.save(customer));

        val customerList = customerRepository.findAll();

        assertThat(customerList).isNotNull().isNotEmpty().hasSize(10000);
    }
}
