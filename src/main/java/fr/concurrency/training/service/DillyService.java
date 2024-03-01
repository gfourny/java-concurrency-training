package fr.concurrency.training.service;

import org.springframework.stereotype.Service;

import fr.concurrency.training.model.Dilly;
import lombok.RequiredArgsConstructor;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
public class DillyService {

    private final Apis apis;

    public Dilly command() {

        var preferences = apis.fetchPreferences();
        var beer = apis.fetchBeer(preferences);
        var vodka = apis.fetchVodka();

        return new Dilly(beer, vodka);
    }

    public Dilly commandWithCF() {
        //TODO A faire en démonstration
        return null;
    }

    public Dilly commandWithCFAnExecutorService() {
        //TODO A faire en démonstration
        return null;
    }

    public Dilly commandWithVirtualThreadExecutor() {
        //TODO A faire en démonstration
        return null;
    }

    public Dilly commandWithStructuredConcurrency() {
        //TODO A faire en démonstration
        //En preview Java 21 et 22
        return null;
    }

    public Dilly commandWithVirtualThreadInProperty() {
        //TODO A faire en démonstration
        return null;
    }
}
