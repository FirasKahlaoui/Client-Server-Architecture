package ex3;

import java.util.List;
import library.GestionBibliotheque;
import library.Livre;

public class ProtocoleBibliotheque {

    public static final String ERR_UNKNOWN_COMMAND = "ERR;COMMANDE_INCONNUE";
    public static final String ERR_INVALID_REQUEST = "ERR;REQUETE_INVALIDE";

    private final GestionBibliotheque gestion;

    public ProtocoleBibliotheque(GestionBibliotheque gestion) {
        this.gestion = gestion;
    }

    public String traiter(String requete) {
        if (requete == null || requete.trim().isEmpty()) {
            return ERR_INVALID_REQUEST;
        }

      
        String[] champs = requete.split(";", -1);
        for (int i = 0; i < champs.length; i++) {
            champs[i] = champs[i].trim();
        }

        String commande = champs[0];
        if (commande.isEmpty()) {
            return ERR_INVALID_REQUEST;
        }

        switch (commande) {
            case "LISTE":
                if (champs.length != 1) {
                    return ERR_INVALID_REQUEST;
                }
                return formaterLivres(gestion.listerLivres());

            case "RECHERCHE":
                if (champs.length != 2 || champs[1].isEmpty() || contientCaractereReserve(champs[1])) {
                    return ERR_INVALID_REQUEST;
                }
                try {
                    return formaterLivres(gestion.rechercherParTheme(champs[1]));
                } catch (IllegalArgumentException e) {
                    return ERR_INVALID_REQUEST;
                }

            default:
                return ERR_UNKNOWN_COMMAND;
        }
    }

    private static boolean contientCaractereReserve(String donnee) {
        return donnee.indexOf(';') >= 0 || donnee.indexOf(',') >= 0 || donnee.indexOf(':') >= 0
                || donnee.indexOf('\n') >= 0 || donnee.indexOf('\r') >= 0;
    }

    private static String formaterLivres(List<Livre> livres) {
        StringBuilder sb = new StringBuilder("OK;").append(livres.size());
        for (Livre l : livres) {
            sb.append(';').append(l.getIdentifiant()).append(':').append(l.getTitre());
        }
        return sb.toString();
    }
}
