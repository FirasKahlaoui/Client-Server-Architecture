package ex1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import library.Bibliotheque;
import library.GestionBibliotheque;
import library.Livre;

public class TestBibliothequeLocal {

    private static int failures = 0;

    public static void main(String[] args) {
        GestionBibliotheque gestion = new GestionBibliotheque(Bibliotheque.catalogueInitial());

        check("List all books", getIds(gestion.listerLivres()), Arrays.asList("L1", "L2", "L3", "L4", "L5"));
        check("Search \"JAVA\"", getIds(gestion.rechercherParTheme("JAVA")), Arrays.asList("L1", "L3"));
        check("Search \" java \"", getIds(gestion.rechercherParTheme(" java ")), Arrays.asList("L1", "L3"));
        check("Search \"HISTOIRE\"", getIds(gestion.rechercherParTheme("HISTOIRE")), new ArrayList<>());

        checkException("Search with null theme", gestion, null);
        checkException("Search with empty theme", gestion, "");
        checkException("Search with blank theme", gestion, "   ");

        System.out.println(failures == 0 ? "\nALL LOCAL TESTS PASSED" : "\n" + failures + " TEST(S) FAILED");
        System.exit(failures == 0 ? 0 : 1);
    }

    private static List<String> getIds(List<Livre> livres) {
        List<String> getIds = new ArrayList<>();
        for (Livre l : livres) {
            getIds.add(l.getIdentifiant());
        }
        return getIds;
    }

    private static void check(String name, List<String> actual, List<String> expected) {
        boolean ok = actual.equals(expected);
        if (!ok) failures++;
        System.out.printf("[%s] %s -> %s (expected %s)%n", ok ? "PASS" : "FAIL", name, actual, expected);
    }

    private static void checkException(String name, GestionBibliotheque gestion, String theme) {
        boolean ok = false;
        try {
            gestion.rechercherParTheme(theme);
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        if (!ok) failures++;
        System.out.printf("[%s] %s -> IllegalArgumentException expected%n", ok ? "PASS" : "FAIL", name);
    }
}
