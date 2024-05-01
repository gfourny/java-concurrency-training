package fr.concurrency.training.service;

import fr.concurrency.training.model.Customer;
import fr.concurrency.training.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static org.instancio.Select.field;

/**
 * @author gfourny
 */
@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ExecutorService executorService;
    private final Semaphore semaphore; //On injecte un Semaphore

    public CustomerService(
            CustomerRepository customerRepository,
            @Value("${spring.datasource.hikari.maximum-pool-size}") int poolSize) {
        this.customerRepository = customerRepository;
        this.semaphore = new Semaphore(poolSize); //On se base sur le nombre de pool de connexion maximum
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * On doit définir un pool de permit qui a pour taille le nombre maximum de pool de connexion
     *
     * @return {@link List} de {@link Customer}
     */
    public List<Customer> createCustomers() {
        /*
         * Ne pas modifier la création d'utilisateur
         * */
        var customers = Instancio.ofList(Customer.class)
                .size(1_000)
                .set(field(Customer::getId), null)
                .generate(field(Customer::getEmail), gen -> gen.net().email())
                .create();

        /*
         * La structure doit rester la même, par démonstration, on n'utilise pas la méthode {@link CustomerRepository#saveAll(Iterable)}
         * */
        customers.stream()
                .map(this::saveCustomer)
                .toList()
                .forEach(CompletableFuture::join);

        return customerRepository.findAll();
    }

    private CompletableFuture<Customer> saveCustomer(Customer customer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                //On réserve un permit
                semaphore.acquire();
                log.info("thread {}", Thread.currentThread());
                return customerRepository.save(customer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                //On libère le permit pour un autre VirtualThread
                semaphore.release();
            }

        }, executorService);
    }
}
