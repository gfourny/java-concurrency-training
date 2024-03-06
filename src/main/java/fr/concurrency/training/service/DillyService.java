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

    // Implémentation classique. On effectue les appels les uns après les autres.
    // L'utilisation du temps CPU n'est pas optimisé --> Chute de performance sur du GKE
    // Le temps d'exécution = somme du temps de réponse des appels
    public Dilly command() {
        val preferences = apis.fetchPreferences();
        val beer = apis.fetchBeer(preferences);
        val vodka = apis.fetchVodka();

        return new Dilly(beer, vodka);
    }

    // Implémentation optimisée, accessible depuis Java 8
    // Les appels bloquants qui n'ont pas d'adhérence entre eux sont parallélisés
    // Le temps d'exécution = temps de réponse d'un appel le plus long
    public Dilly commandWithCF() {
        return CompletableFuture.supplyAsync(apis::fetchPreferences)
                .thenApply(apis::fetchBeer)
                .thenCombine(CompletableFuture.supplyAsync(apis::fetchVodka), Dilly::new)
                .join();
    }

    // Idem que précédent, mais en utilisant les VirtualThread
    // L'executorService peut être injecté dans le constructeur (fortement recommandé actuellement lorsque l'on se définit un pool de thread)
    public Dilly commandWithCFAnExecutorService() {
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            return CompletableFuture.supplyAsync(apis::fetchPreferences, executorService)
                    .thenApply(apis::fetchBeer)
                    .thenCombine(CompletableFuture.supplyAsync(apis::fetchVodka, executorService), Dilly::new)
                    .join();
        }
    }

    // Java 21 !
    // Effectue exactement le même traitement mais dans un style impératif
    // API Future mise à jour pour cette occasion (Java 7)
    // Beaucoup moins de méthode à connaître que l'API CompletableFuture
    // Ne répondra pas à tous les besoins, mais répond à la majeure partie des cas 
    public Dilly commandWithVirtualThreadExecutor() {
        try (val executors = Executors.newVirtualThreadPerTaskExecutor()) {

            val preferencesFuture = executors.submit(apis::fetchPreferences);
            val beerFuture = executors.submit(() -> apis.fetchBeer(preferencesFuture.get()));
            val vodkaFuture = executors.submit(apis::fetchVodka);

            return new Dilly(beerFuture.get(), vodkaFuture.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Preview Java 21 et 22
    // Permet de répondre à des besoins impossibles à traiter avec l'API Future et compliqué avec l'API CompletableFuture
    // Facilité d'implémentation pour paralléliser et définir des règles de gestion
    // Possibilité d'étendre facilement StructuredTaskScope
    public Dilly commandWithStructuredConcurrency() {
        //En preview Java 21 et 22

        Future<Preferences> preferencesFuture;
        try (val executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            preferencesFuture = executorService.submit(apis::fetchPreferences);
        }

        try (val scope = new StructuredTaskScope.ShutdownOnFailure()) {

            val beerTask = scope.fork(() -> apis.fetchBeer(preferencesFuture.get()));
            val vodkaTask = scope.fork(apis::fetchVodka);

            scope.join().throwIfFailed();

            return new Dilly(beerTask.get(), vodkaTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    //Utilise des VirtualThread lors d'appel bloquant mais ne permet pas de paralléliser (ça n'est pas magique non plus)
    //On continue de bloquer le main thread
    public Dilly commandWithVirtualThreadInProperty() {
        val preferences = apis.fetchPreferences();
        val beer = apis.fetchBeer(preferences);
        val vodka = apis.fetchVodka();

        return new Dilly(beer, vodka);
    }
}
