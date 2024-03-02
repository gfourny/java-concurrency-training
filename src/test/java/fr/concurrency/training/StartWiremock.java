package fr.concurrency.training;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * @author gfourny
 */
public class StartWiremock {

    public static void main(String[] args) {
        WireMockServer wm = new WireMockServer(options()
                .port(9999)
                .asynchronousResponseEnabled(true)
                .asynchronousResponseThreads(200)
        );
        wm.start();
    }
}
