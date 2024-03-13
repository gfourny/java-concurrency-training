package fr.concurrency.training.service;

import org.springframework.stereotype.Service;

import fr.concurrency.training.model.Dilly;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
public class DillyService {

    private final Apis apis;

    /**
     * Modifier le code suivant afin de faire passer le test
     *
     * @return {@link Dilly}
     */
    public Dilly command() {

        val preferences = apis.fetchPreferences();
        val beer = apis.fetchBeer(preferences);
        val vodka = apis.fetchVodka();

        return new Dilly(beer, vodka);
    }
}
