package fr.concurrency.training.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

import org.springframework.stereotype.Service;

import fr.concurrency.training.model.gdu.UtilisateurRefUtApp;
import fr.concurrency.training.model.gdu.UtilisateurRefUtAppWithFonction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GduService {

    private final Apis apis;

    /**
     * Solution la plus simple, le client attend une réponse donc on parallélise les appels de récupération des fonctions.<br/>
     * <b>On bloque le thread http request dans le cas de l'appel bloquant</b>.<br/>
     * <b>On bloque un des threads du Common ForkJoinPool dans le cas de l'appel non-bloquant</b>.
     *
     * @return {@link List} d'{@link UtilisateurRefUtAppWithFonction}
     */
    public List<UtilisateurRefUtAppWithFonction> retrieveUsersWithFonctions() {
        log.info("current thread: {}", Thread.currentThread());
        var futures = apis.fetchUsers().stream()
                .map(u -> CompletableFuture.supplyAsync(() -> getUtilisateurRefUtAppWithFonction(u)))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    /**
     * Solution plus avancée, dit "réactive", se rapproche de l'utilisation de Spring WebFlux (Reactor).<br/>
     * Toutes les méthodes sont wrappés dans une Monade, CompletableFuture<> dans notre cas, Mono<> ou Flux<> avec Reactor.<br/>
     * <b>OverEngineering dans notre cas</b>
     *
     * @return {@link CompletableFuture}
     */
    public CompletableFuture<List<UtilisateurRefUtAppWithFonction>> retrieveUsersWithFonctionsOtherSolution() {
        log.info("current thread: {}", Thread.currentThread());
        var futures = apis.fetchUsers().stream()
                .map(u -> CompletableFuture.supplyAsync(() -> getUtilisateurRefUtAppWithFonction(u)))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    /**
     * Solution Java 23+ en utilisant la <b>Sructured Concurrency</b>.<br/>
     *
     * @return {@link List} d'{@link UtilisateurRefUtAppWithFonction}
     */
    public List<UtilisateurRefUtAppWithFonction> retrieveUsersWithFonctionsSC() {

        var utilisateurRefUtApps = apis.fetchUsers();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            var utilisateurRefUtAppsWithFonctions = utilisateurRefUtApps.stream()
                    .map(utilisateurRefUtApp -> scope.fork(() -> getUtilisateurRefUtAppWithFonction(utilisateurRefUtApp)))
                    .toList();

            scope.join().throwIfFailed();

            return utilisateurRefUtAppsWithFonctions.stream()
                    .map(StructuredTaskScope.Subtask::get)
                    .toList();

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private UtilisateurRefUtAppWithFonction getUtilisateurRefUtAppWithFonction(UtilisateurRefUtApp utilisateurRefUtApp) {
        log.info("récupération des fonctions pour l'utilisateur {}", utilisateurRefUtApp.nom());
        var fonctions = apis.fetchFonctionForUser(utilisateurRefUtApp.uid());
        return UtilisateurRefUtAppWithFonction.builder()
                .utilisateurRefUtApp(utilisateurRefUtApp)
                .fonctions(fonctions)
                .build();
    }
}
