package fr.concurrency.training.model.gdu;

import java.util.List;

import lombok.Builder;

import static java.util.Objects.requireNonNull;

/**
 * Utilisateur RefUtApp avec ses fonctions associées
 *
 * @author gfourny
 */
@Builder
public record UtilisateurRefUtAppWithFonction(
        UtilisateurRefUtApp utilisateurRefUtApp,
        List<Fonction> fonctions
) implements Utilisateur {
    public UtilisateurRefUtAppWithFonction(UtilisateurRefUtApp utilisateurRefUtApp, List<Fonction> fonctions) {
        this.utilisateurRefUtApp = requireNonNull(utilisateurRefUtApp);
        this.fonctions = List.copyOf(fonctions);
    }
}
