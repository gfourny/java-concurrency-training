package fr.concurrency.training.service;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import fr.concurrency.training.config.WiremockInitializer;
import fr.concurrency.training.model.Dilly;

import static java.util.Objects.isNull;
import static org.awaitility.Awaitility.await;

/**
 * @author gfourny
 */
@SpringBootTest
@ContextConfiguration(initializers = { WiremockInitializer.class })
@ActiveProfiles("test")
class DillyServiceTest {

    @Autowired
    private DillyService dillyService;

    @Test
    @DisplayName("Doit récupérer une commande en moins de 2 secondes")
    void should_return_dilly_less_than_two_seconds() {
        await().atMost(Duration.ofMillis(1700)).until(() ->
                {
                    Dilly dilly = dillyService.command();
                    return !isNull(dilly) && !isNull(dilly.beer()) && !isNull(dilly.vodka());
                }
        );
    }
}