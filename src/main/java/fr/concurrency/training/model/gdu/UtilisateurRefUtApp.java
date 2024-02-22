package fr.concurrency.training.model.gdu;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * Utilisateur provenant de RefUtApp
 *
 * @author gfourny
 */
public record UtilisateurRefUtApp(
        String login,
        String uid,
        String nom,
        String prenom,
        String commentaire,
        String email,
        boolean desactive,
        Boolean collaborateurMagasin,
        List<Organisation> listeOrganisations

) {
    public UtilisateurRefUtApp(String login, String uid, String nom, String prenom, String commentaire, String email, boolean desactive,
            Boolean collaborateurMagasin, List<Organisation> listeOrganisations) {
        this.login = requireNonNull(login);
        this.uid = requireNonNull(uid);
        this.nom = nom;
        this.prenom = prenom;
        this.commentaire = commentaire;
        this.email = email;
        this.desactive = desactive;
        this.collaborateurMagasin = collaborateurMagasin;
        this.listeOrganisations = isNull(listeOrganisations) ? null : List.copyOf(listeOrganisations);
    }
}
