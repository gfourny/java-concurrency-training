package fr.concurrency.training.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.concurrency.training.model.gdu.UtilisateurRefUtApp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author gfourny
 */
class ApisTest extends AbastractITSpring {

    @Autowired
    private Apis apis;

    @Test
    void should_return_list_of_users() {
        var utilisateurRefUtApps = apis.fetchUsers();
        assertThat(utilisateurRefUtApps).isNotNull().isNotEmpty();
    }

    @Test
    void should_return_list_of_users_with_their_fonctions() {
        var utilisateurRefUtAppWithFonctions = apis.fetchUsers().stream()
                .map(UtilisateurRefUtApp::uid)
                .map(apis::fetchFonctionForUser)
                .toList();

        assertThat(utilisateurRefUtAppWithFonctions).isNotNull().isNotEmpty();
    }
}