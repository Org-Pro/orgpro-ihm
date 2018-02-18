package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Data {
    private final String PREF_FICHIER_COURANT = "FILE";
    public final String DOSSIER_COURANT = "files";
    private String FICHIER_COURANT;
    public final String PATH;
    private final Preferences prefs;

    private List<Tache> listeTache;
    private static Data data = null;

    public static Data getInstance(){
        if(data == null)
            data = new Data();
        return data;
    }

    private Data() {
        listeTache = new ArrayList<Tache>();
        prefs = Preferences.userNodeForPackage(fr.orgpro.ihm.project.Data.class);
        FICHIER_COURANT = prefs.get(PREF_FICHIER_COURANT, "");
        // Vérifie si le chemin vers le fichier existe
        if(!new File(DOSSIER_COURANT + "/" + FICHIER_COURANT).exists()) {
            prefs.remove(PREF_FICHIER_COURANT);
            FICHIER_COURANT = "";
        }
        PATH = DOSSIER_COURANT + "/" + FICHIER_COURANT;
    }

    public String setFichierCourant(String fichier){
        if(!fichier.endsWith(".org")){
            fichier += ".org";
        }
        prefs.put(PREF_FICHIER_COURANT, fichier);
        FICHIER_COURANT = fichier;
        return fichier;
    }

    public boolean loadFichier(){
        listeTache = Tache.lectureFichier(PATH);
        if(listeTache == null){
            return false;
        }else {
            return true;
        }
    }

    public String getFICHIER_COURANT() {
        return FICHIER_COURANT;
    }

    public List<Tache> getListeTache() {
        return listeTache;
    }

}
