package fr.concurrency.training.service;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.lifecycle.Startables;

import eu.rekawek.toxiproxy.ToxiproxyClient;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import fr.concurrency.training.config.WiremockInitializer;

/**
 * @author gfourny
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = { WiremockInitializer.class })
@ActiveProfiles("test")
public abstract class AbastractITSpring {

    static Network network = Network.newNetwork();

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withNetwork(network)
            .withNetworkAliases("postgres");

    static ToxiproxyContainer toxi = new ToxiproxyContainer("ghcr.io/shopify/toxiproxy:2.8.0")
            .withNetwork(network);

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) throws IOException {

        Startables.deepStart(postgres, toxi).join();

        var toxiproxyClient = new ToxiproxyClient(toxi.getHost(), toxi.getControlPort());
        var proxy = toxiproxyClient.createProxy("postgres", "0.0.0.0:8666", "postgres:5432");
        var jdbcUrl = "jdbc:postgresql://%s:%d/%s".formatted(toxi.getHost(), toxi.getMappedPort(8666), postgres.getDatabaseName());

        proxy.toxics()
                .latency("latency", ToxicDirection.DOWNSTREAM, 45) // Latence de 60 ms sur les requêtes
                .setJitter(0); // Variation de latence en ms

        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
