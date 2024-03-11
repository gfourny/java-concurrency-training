package fr.concurrency.training.service;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.awaitility.Awaitility.await;

/**
 * @author gfourny
 */
class GduServiceTest extends AbastractITSpring {

    @Autowired
    private GduService gduService;

    @Test
    @DisplayName("Doit récupérer la liste des utilisateurs avec leur fonctions de manière concurrente")
    void should_retrieve_users_with_fonction_asynchronously() {
        await().atMost(Duration.ofSeconds(3)).until(() -> gduService.retrieveUsersWithFonctionsSC().size() == 3);
    }
}