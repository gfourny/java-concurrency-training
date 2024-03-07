package fr.concurrency.training.service;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import fr.concurrency.training.config.WiremockInitializer;

import static org.awaitility.Awaitility.await;

/**
 * @author gfourny
 */
@SpringBootTest
@ContextConfiguration(initializers = { WiremockInitializer.class })
@ActiveProfiles("test")
class GduServiceTest {

    @Autowired
    private GduService gduService;

    @Test
    @DisplayName("Doit récupérer la liste des utilisateurs avec leur fonctions de manière concurrente")
    void should_retrieve_users_with_fonction_asynchronously() {
        await().atMost(Duration.ofSeconds(3)).until(() -> gduService.retrieveUsersWithFonctionsMoreReadable().size() == 3);
    }
}