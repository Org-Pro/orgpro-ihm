package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Data {
    private final String PREF_FICHIER_COURANT = "FILE";
    private final String dossierCourant = "files";
    private String fichierCourant;
    private String path;
    private final Preferences prefs;

    private List<Tache> listeTache;
    private static Data data = null;

    public final static String COULEUR_ECHEC = "\033[0;31m";
    public final static String COULEUR_SUCCES = "\033[1;32m";
    public final static String COULEUR_RESET = "\033[0m";


    public static Data getInstance(){
        if(data == null)
            data = new Data();
        return data;
    }

    private Data() {
        listeTache = new ArrayList<Tache>();
        prefs = Preferences.userNodeForPackage(fr.orgpro.ihm.project.Data.class);
        fichierCourant = prefs.get(PREF_FICHIER_COURANT, "");

        // Vérifie si le chemin vers le fichier existe
        if(!new File(dossierCourant + "/" + fichierCourant).exists()) {
            prefs.remove(PREF_FICHIER_COURANT);
            fichierCourant = "";
        }
        path = dossierCourant + "/" + fichierCourant;
    }

    public String setFichierCourant(String fichierP){
        String fichier = fichierP;
        if(!fichier.endsWith(".org")){
            fichier += ".org";
        }
        prefs.put(PREF_FICHIER_COURANT, fichier);
        fichierCourant = fichier;
        path = dossierCourant + "/" + fichierCourant;
        return fichier;
    }

    public boolean loadFichier(){
        File file = new File(path);
        if(file.length() == 0){
            listeTache = new ArrayList<Tache>();
            return true;
        }
        listeTache = Tache.readFichier(path);
        if(listeTache == null){
            return false;
        }else {
            return true;
        }
    }

    public void ecritureListeTaches() {
        boolean premier = true;
        if (listeTache.isEmpty()){
            FileWriter fw = null;
            try {
                fw = new FileWriter(path, false);
                fw.write("");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        for (Tache t : listeTache){
            if (premier){
                t.writeFichier(path, false);
                premier = false;
            }else {
                t.writeFichier(path, true);
            }
        }
    }

    public String getFichierCourant() {
        return fichierCourant;
    }

    public List<Tache> getListeTache() {
        return listeTache;
    }

    public String getPath() {
        return path;
    }

    public String getDossierCourant() {
        return dossierCourant;
    }
}
