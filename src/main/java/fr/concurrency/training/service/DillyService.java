package fr.concurrency.training.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;

import org.springframework.stereotype.Service;

import fr.concurrency.training.model.Dilly;
import fr.concurrency.training.model.Preferences;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DillyService {

    private final Apis apis;

    public Dilly command() {
        var preferences = apis.fetchPreferences();
        var beer = apis.fetchBeer(preferences);
        var vodka = apis.fetchVodka();

        return new Dilly(beer, vodka);
    }

    public Dilly commandWithCF() {
        //TODO A faire en démonstration
        return CompletableFuture.supplyAsync(apis::fetchPreferences)
                .thenApply(apis::fetchBeer)
                .thenCombine(CompletableFuture.supplyAsync(apis::fetchVodka), Dilly::new)
                .join();
    }

    public Dilly commandWithCFAnExecutorService() {
        //TODO A faire en démonstration
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            return CompletableFuture.supplyAsync(apis::fetchPreferences, executorService)
                    .thenApply(apis::fetchBeer)
                    .thenCombine(CompletableFuture.supplyAsync(apis::fetchVodka, executorService), Dilly::new)
                    .join();
        }
    }

    public Dilly commandWithVirtualThreadExecutor() {
        //TODO A faire en démonstration
        try (var executors = Executors.newVirtualThreadPerTaskExecutor()) {

            var preferencesFuture = executors.submit(apis::fetchPreferences);
            var beerFuture = executors.submit(() -> apis.fetchBeer(preferencesFuture.get()));
            var vodkaFuture = executors.submit(apis::fetchVodka);

            return new Dilly(beerFuture.get(), vodkaFuture.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Dilly commandWithStructuredConcurrency() {
        //TODO A faire en démonstration
        //En preview Java 21 et 22

        Future<Preferences> preferencesFuture;
        try (val executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            preferencesFuture = executorService.submit(apis::fetchPreferences);
        }

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            var beerTask = scope.fork(() -> apis.fetchBeer(preferencesFuture.get()));
            var vodkaTask = scope.fork(apis::fetchVodka);

            scope.join().throwIfFailed();

            return new Dilly(beerTask.get(), vodkaTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Dilly commandWithVirtualThreadInProperty() {
        //TODO A faire en démonstration
        var preferences = apis.fetchPreferences();
        var beer = apis.fetchBeer(preferences);
        var vodka = apis.fetchVodka();

        return new Dilly(beer, vodka);
    }
}
