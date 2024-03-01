package fr.concurrency.training.service;

import java.util.List;

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

    public List<UtilisateurRefUtAppWithFonction> retrieveUsersWithFonctions() {
        val utilisateurRefUtApps = apis.fetchUsers();

        return utilisateurRefUtApps.stream()
                .map(this::getUtilisateurRefUtAppWithFonction)
                .toList();
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
