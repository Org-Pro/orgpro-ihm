package fr.orgpro.ihm.service;

import fr.orgpro.api.project.Tache;
import fr.orgpro.ihm.project.Data;
import fr.orgpro.ihm.project.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollaborateurService {
    private static CollaborateurService INSTANCE = new CollaborateurService();
    private static final String fs = File.separator;
    private static final String PATH = "src" + fs + "main" + fs + "resources" + fs;
    public boolean verifColaborateurExist(String name) {
        List<String> list = Tache.getCollaborateurEnTeteListe();
        if (list == null) {
            System.out.print(Message.COLLABORATEUR_LISTE_VIDE);
            return false;
        }
        for(String col : list) {
            if (name.equalsIgnoreCase(col)) {
                return true;
            }
        }
        System.out.println(Message.COLLABORATEUR_NON_TROUVE);
        return false;
    }

    public boolean creerDossierCollaboSiPasExistant(String name) {
        File file = new File(PATH + name);
        if (!file.exists() && !file.isDirectory()) {
            if(!file.mkdirs()) {
                System.out.println(Message.COLLABORATEUR_AJOUT_DOSSIER_GOOGLE_FAILURE);
                return false;
            }
            System.out.println(Message.COLLABORATEUR_AJOUT_DOSSIER_GOOGLE_SUCCES);
        } else {
            System.out.println(Message.COLLABORATEUR_AJOUT_DOSSIER_GOOGLE_ALREADY_EXIST);
        }
        return true;
    }
    public boolean deleteDirectory (File dir) {
        File[] allFiles = dir.listFiles();
        if(null != allFiles) {
            for (File f: allFiles) {
                if(!f.delete()) {
                    System.out.println(Message.COLLABORATEUR_SUPPRESSION_FICHIER_FAILURE + f.getPath());
                    return false;
                }
            }
        }
        if(!dir.delete()) {
            System.out.println(Message.COLLABORATEUR_SUPPRESSION_DOSSIER_FAILURE + dir.getPath());
            return false;
        }
        return true;
    }
    public boolean changeDirectory(File dir, File newDir) {
        if (dir.exists() && dir.isDirectory() && !newDir.exists()) {
            dir.renameTo(newDir);
        } else if (newDir.exists()) {
            System.out.println(Message.COLLABORATEUR_DOSSIER_EXISTE_DEJA_ECHEC);
            return false;
        }else {
            dir.mkdir();
        }
        return true;
    }

    public List<Tache> getListTacheFromCollabo(String name, Data data) {
        System.out.println(name);
        List<Tache> toSend = new ArrayList<>();
        for (Tache task: data.getListeTache()) {
            for(String c : task.getCollaborateurFromTache(task)) {
                if (c.equalsIgnoreCase(name)) {
                    toSend.add(task);
                    break;
                }
            }
        }
        return toSend;
    }

    public static CollaborateurService getInstance() {
        return INSTANCE;
    }
}
