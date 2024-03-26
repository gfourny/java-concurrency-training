package fr.concurrency.training.service;

import java.util.List;

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
     * Récupère la liste des utilisateurs avec leurs fonctions associées.<br/>
     * Actuellement non optimisé car la liste des utilisateurs est traitée de manière <b>synchrone</b><br/>
     * <b>TODO Modifier l'implémentation afin de paralléliser les appels de récupération des fonctions cf: <br/>
     * {@link GduService#getUtilisateurRefUtAppWithFonction(UtilisateurRefUtApp)}</b><br/>
     *
     * @return {@link List} d'{@link UtilisateurRefUtAppWithFonction}
     */
    public List<UtilisateurRefUtAppWithFonction> retrieveUsersWithFonctions() {
        log.info("run on {}", Thread.currentThread());
        var utilisateurRefUtApps = apis.fetchUsers();

        return utilisateurRefUtApps.stream()
                .map(this::getUtilisateurRefUtAppWithFonction)
                .toList();
    }

    /**
     * Récupère les fonctions d'un utilisateur
     *
     * @param utilisateurRefUtApp {@link UtilisateurRefUtApp}
     * @return {@link UtilisateurRefUtAppWithFonction}
     */
    private UtilisateurRefUtAppWithFonction getUtilisateurRefUtAppWithFonction(UtilisateurRefUtApp utilisateurRefUtApp) {
        log.info("récupération des fonctions pour l'utilisateur {}", utilisateurRefUtApp.nom());
        var fonctions = apis.fetchFonctionForUser(utilisateurRefUtApp.uid());
        return UtilisateurRefUtAppWithFonction.builder()
                .utilisateurRefUtApp(utilisateurRefUtApp)
                .fonctions(fonctions)
                .build();
    }
}
