package fr.concurrency.training.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.concurrency.training.model.gdu.UtilisateurRefUtAppWithFonction;
import lombok.RequiredArgsConstructor;

/**
 * @author gfourny
 */
@Service
@RequiredArgsConstructor
public class GduService {
    
    private final Apis apis;
    
    public List<UtilisateurRefUtAppWithFonction> retrieveUsersWithFonctions(){
        /*
        TODO écrire le code bloquant qui:
        - Appel l'API de récupération de la liste des utilisateurs
        - Pour chaque utilisateur, appel l'API de récupération des fonctions de l'utilisateur
        - Retourne la liste des utilisateurs avec leurs fonctions
         */
        return List.of();
    }
}
