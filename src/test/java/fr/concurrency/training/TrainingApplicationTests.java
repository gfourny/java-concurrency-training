package fr.concurrency.training;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import fr.concurrency.training.config.WiremockInitializer;

@SpringBootTest
@ContextConfiguration(initializers = { WiremockInitializer.class })
@ActiveProfiles("test")
class TrainingApplicationTests {

    @Test
    void contextLoads() {
    }

}
