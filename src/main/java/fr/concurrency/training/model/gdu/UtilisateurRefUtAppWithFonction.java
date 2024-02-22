package fr.concurrency.training.model.gdu;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Utilisateur RefUtApp avec ses fonctions associées
 *
 * @author gfourny
 */
public record UtilisateurRefUtAppWithFonction(
        UtilisateurRefUtApp utilisateurRefUtApp,
        List<Fonction> fonctions
) implements Utilisateur {
    public UtilisateurRefUtAppWithFonction(UtilisateurRefUtApp utilisateurRefUtApp, List<Fonction> fonctions) {
        this.utilisateurRefUtApp = requireNonNull(utilisateurRefUtApp);
        this.fonctions = List.copyOf(fonctions);
    }
}
