package fr.concurrency.training.service;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.concurrency.training.model.Dilly;

import static java.util.Objects.isNull;
import static org.awaitility.Awaitility.await;

/**
 * @author gfourny
 */
class DillyServiceTest extends AbastractITSpring {

    @Autowired
    private DillyService dillyService;

    @Test
    @DisplayName("Doit récupérer une commande en moins de 2 secondes")
    void should_return_dilly_less_than_two_seconds() {
        await().atMost(Duration.ofMillis(1700)).until(() ->
                {
                    Dilly dilly = dillyService.commandWithVirtualThreadInProperty();
                    return !isNull(dilly) && !isNull(dilly.beer()) && !isNull(dilly.vodka());
                }
        );
    }
}