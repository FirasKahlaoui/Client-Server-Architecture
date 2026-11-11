package library;

import java.util.ArrayList;
import java.util.List;

public class GestionBibliotheque {

  private final Bibliotheque bibliotheque;

  public GestionBibliotheque(Bibliotheque bibliotheque) {
    if (bibliotheque == null) {
      throw new IllegalArgumentException("library must not be null");
    }
    this.bibliotheque = bibliotheque;
  }

  public List<Livre> listerLivres() {
    return new ArrayList<>(bibliotheque.getLivres());
  }

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
