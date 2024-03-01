package fr.concurrency.training.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    public List<UtilisateurRefUtAppWithFonction> retrieveUsersWithFonctions() {
        log.info("current thread: {}", Thread.currentThread());
        var futures = apis.fetchUsers().stream()
                .map(u -> CompletableFuture.supplyAsync(() -> getUtilisateurRefUtAppWithFonction(u)))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
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
