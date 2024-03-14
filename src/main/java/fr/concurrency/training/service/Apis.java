package fr.concurrency.training.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import fr.concurrency.training.model.Beer;
import fr.concurrency.training.model.Preferences;
import fr.concurrency.training.model.Vodka;
import fr.concurrency.training.model.dns.Dns;
import fr.concurrency.training.model.gdu.Fonction;
import fr.concurrency.training.model.gdu.UtilisateurRefUtApp;
import lombok.RequiredArgsConstructor;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
public class Apis {

    private final RestClient restClient;

    public Preferences fetchPreferences() {
        return restClient.get()
                .uri("/preferences")
                .retrieve()
                .body(Preferences.class);
    }

    public Beer fetchBeer(Preferences pref) {
        return restClient.get()
                .uri("/beer/" + pref.favoriteBeerType())
                .retrieve()
                .body(Beer.class);
    }

    public Vodka fetchVodka() {
        return restClient.get()
                .uri("/vodka")
                .retrieve()
                .body(Vodka.class);
    }

    public List<UtilisateurRefUtApp> fetchUsers() {
        return restClient.get()
                .uri("/refutilisateurappli/utilisateurs")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<Fonction> fetchFonctionForUser(String uuid) {
        return restClient.get()
                .uri(String.format("/refutilisateurappli/utilisateurs/%s/fonctions?codeApplication=BO_Monetique", uuid))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
    
    public Dns fetchDns(String id){
        return restClient.get()
                .uri(STR."/\{id}")
                .retrieve()
                .body(Dns.class);
    }
}
