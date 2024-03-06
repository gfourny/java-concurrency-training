package fr.concurrency.training.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import fr.concurrency.training.model.gdu.UtilisateurRefUtApp;
import fr.concurrency.training.model.gdu.UtilisateurRefUtAppWithFonction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GduService {

    private final Apis apis;

    //Solution la plus simple, le client attend une réponse donc on parallélise les appels de récupération des fonctions
    //On bloque le thread http request dans le cas de l'appel bloquant
    //On bloque un des thread du Common ForkJoinPool dans le cas de l'appel non-bloquant
    public List<UtilisateurRefUtAppWithFonction> retrieveUsersWithFonctions() {
        log.info("current thread: {}", Thread.currentThread());
        val futures = apis.fetchUsers().stream()
                .map(u -> CompletableFuture.supplyAsync(() -> getUtilisateurRefUtAppWithFonction(u)))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    //Solution plus avancée, dit "réactive", se rapproche de l'utilisation de Spring WebFlux (Reactor).
    //Toutes les méthodes sont wrappés dans une Monade, CompletableFuture<> dans notre cas, Mono<> ou Flux<> avec Reactor
    public CompletableFuture<List<UtilisateurRefUtAppWithFonction>> retrieveUsersWithFonctionsOtherSolution() {
        log.info("current thread: {}", Thread.currentThread());
        val futures = apis.fetchUsers().stream()
                .map(u -> CompletableFuture.supplyAsync(() -> getUtilisateurRefUtAppWithFonction(u)))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    private UtilisateurRefUtAppWithFonction getUtilisateurRefUtAppWithFonction(UtilisateurRefUtApp utilisateurRefUtApp) {
        log.info("récupération des fonctions pour l'utilisateur {}", utilisateurRefUtApp.nom());
        val fonctions = apis.fetchFonctionForUser(utilisateurRefUtApp.uid());
        return UtilisateurRefUtAppWithFonction.builder()
                .utilisateurRefUtApp(utilisateurRefUtApp)
                .fonctions(fonctions)
                .build();
    }
}
