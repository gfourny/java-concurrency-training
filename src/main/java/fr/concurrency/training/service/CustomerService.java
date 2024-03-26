package fr.concurrency.training.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.instancio.Instancio;
import org.springframework.stereotype.Service;

import fr.concurrency.training.model.Customer;
import fr.concurrency.training.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

import static org.instancio.Select.field;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * FIXME: corriger l'implémentation suivante pour ne plus avoir des erreurs lors de la création des utilisateurs en base
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
