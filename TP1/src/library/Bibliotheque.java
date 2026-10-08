package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bibliotheque {

    private final String nom;
    private final List<Livre> livres = new ArrayList<>();

    public Bibliotheque(String nom) {
        this.nom = nom;
    }

    public void ajouterLivre(Livre livre) {
        livres.add(livre);
    }

    public String getNom() {
        return nom;
    }

    public List<Livre> getLivres() {
        return Collections.unmodifiableList(livres);
    }

    public static Bibliotheque catalogueInitial() {
        Bibliotheque b = new Bibliotheque("Bibliotheque universitaire");
        b.ajouterLivre(new Livre("L1", "Programmation Java", "JAVA", "POO"));
        b.ajouterLivre(new Livre("L2", "Réseaux informatiques", "RESEAUX", "TCP"));
        b.ajouterLivre(new Livre("L3", "Systèmes distribués", "JAVA", "DISTRIBUE"));
        b.ajouterLivre(new Livre("L4", "Bases de données", "SQL", "DONNEES"));
        b.ajouterLivre(new Livre("L5", "Architecture logicielle", "ARCHITECTURE", "POO"));
        return b;
    }
}
