package fr.concurrency.training.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import fr.concurrency.training.model.Beer;
import fr.concurrency.training.model.Preferences;
import fr.concurrency.training.model.Vodka;
import lombok.RequiredArgsConstructor;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
public class Apis {

    private final RestClient restClient;

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
