package fr.concurrency.training.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.concurrency.training.model.Customer;
import fr.concurrency.training.repository.CustomerRepository;

import static org.instancio.Select.field;

/**
 * @author gfourny
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ExecutorService executorService;

    public CustomerService(
            CustomerRepository customerRepository,
            @Value("${spring.datasource.hikari.maximum-pool-size}") int poolSize) {
        this.customerRepository = customerRepository;
        this.executorService = Executors.newFixedThreadPool(poolSize); //On se base sur le nombre de pool de connexion maximum
    }

    /**
     * On doit définir un pool de thread qui a pour taille le nombre maximum de pool de connexion
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
                .map(customer -> CompletableFuture.supplyAsync(() -> customerRepository.save(customer), executorService))
                .toList()
                .forEach(CompletableFuture::join);

        return customerRepository.findAll();
    }
}
