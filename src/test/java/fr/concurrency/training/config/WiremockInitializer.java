package fr.concurrency.training.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * @author gfourny
 */
@TestConfiguration(proxyBeanMethods = false)
public class WiremockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        configureFor(wireMockServer.port());
        System.setProperty("wiremock.server.port", String.valueOf(wireMockServer.port()));
        applicationContext.getBeanFactory().registerResolvableDependency(WireMockServer.class, wireMockServer);
    }
}
