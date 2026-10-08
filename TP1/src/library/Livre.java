package library;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class Livre {

    private final String identifiant;
    private final String titre;
    private final Set<String> themes;

    public Livre(String identifiant, String titre, String... themes) {
        this.identifiant = identifiant;
        this.titre = titre;
        Set<String> normalises = new LinkedHashSet<>();
        for (String t : themes) {
            normalises.add(normaliser(t));
        }
        this.themes = Collections.unmodifiableSet(normalises);
    }

    public static String normaliser(String theme) {
        return theme.trim().toUpperCase(Locale.ROOT);
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getTitre() {
        return titre;
    }

    public Set<String> getThemes() {
        return themes;
    }

    public boolean aPourTheme(String theme) {
        return themes.contains(normaliser(theme));
    }

    @Override
    public String toString() {
        return identifiant + " - " + titre + " " + themes;
    }
}
