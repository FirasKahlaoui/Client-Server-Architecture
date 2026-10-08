package library;

import java.util.ArrayList;
import java.util.List;

/**
 * Business class of the service. It contains only business processing:
 * it opens no socket and builds no network message.
 */
public class GestionBibliotheque {

    private final Bibliotheque bibliotheque;

    public GestionBibliotheque(Bibliotheque bibliotheque) {
        if (bibliotheque == null) {
            throw new IllegalArgumentException("library must not be null");
        }
        this.bibliotheque = bibliotheque;
    }

    /** Returns every book, in catalogue order. */
    public List<Livre> listerLivres() {
        return new ArrayList<>(bibliotheque.getLivres());
    }

    /**
     * Returns the books associated with a theme, in catalogue order.
     * The whole theme is compared, ignoring case and leading/trailing spaces.
     *
     * @throws IllegalArgumentException if the theme is null, empty or blank
     */
    public List<Livre> rechercherParTheme(String theme) {
        if (theme == null || theme.trim().isEmpty()) {
            throw new IllegalArgumentException("theme must not be null or blank");
        }
        List<Livre> resultat = new ArrayList<>();
        for (Livre livre : bibliotheque.getLivres()) {
            if (livre.aPourTheme(theme)) {
                resultat.add(livre);
            }
        }
        return resultat;
    }
}
