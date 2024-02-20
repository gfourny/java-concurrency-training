package fr.concurrency.training.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import fr.concurrency.training.model.Beer;
import fr.concurrency.training.model.Preferences;
import fr.concurrency.training.model.Vodka;

/**
 * @author gfourny
 */
@Service
public class Api {

    private final RestClient restClient;

    @Value("${wiremock.server.port}")
    int port;
    @Value("${baseUrl}")
    String baseUrl;

    public Api() {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl + port)
                .build();
    }

    public Preferences fetchPreferences() {
        return restClient.get().uri("/preferences").retrieve().body(Preferences.class);
    }

    public Beer fetchBeer(Preferences pref) {
        return restClient.get().uri("/beer/" + pref.favoriteBeerType()).retrieve().body(Beer.class);
    }

    public Vodka fetchVodka() {
        return restClient.get().uri("/vodka").retrieve().body(Vodka.class);
    }
}
