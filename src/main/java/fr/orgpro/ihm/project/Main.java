package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Main {
    private static Data data;

    public static void main(String[] args) throws IOException {
        data = Data.getInstance();


        //////////////////////////////////////////////
        /*data.getListeTache().add((new Tache("aze", 5)));
        data.getListeTache().add((new Tache("aaa", 5)));
        data.getListeTache().add((new Tache("bbb", 5)));
        data.getListeTache().add((new Tache("ccc", 5)));
        data.getListeTache().add((new Tache("ddd", 5)));*/
        //////////////////////////////////////////////


        if (data.FICHIER_COURANT.isEmpty()){
            System.out.println(Message.MAIN_AUNCUN_FICHIER);
            File[] files = new File(data.DOSSIER_COURANT).listFiles();
            if(files == null || files.length == 0){
                new File(data.DOSSIER_COURANT).mkdirs();
            }else {
                System.out.println(Message.MAIN_LISTE_FICHIER);
                for (File file : files) {
                    if (file.getName().endsWith(".org")) {
                        System.out.println(file.getName());
                    }
                }
            }
        }

        if(args.length > 0){
            traitementArgs(args);
        }
    }

    private static void traitementArgs(String[] args){
        if(data.FICHIER_COURANT.isEmpty() && !args[0].toLowerCase().equals("file")){
            return;
        }
        switch (args[0].toLowerCase()){
            case "tache":
                Commande.commandeTache(args, data);
                break;
            case "file":
                Commande.commandeFichier(args, data);
                break;
            case "list":
                Commande.commandeListe(args, data);
                break;
            case "help":
                System.out.println(Message.MAIN_HELP);
                break;
            default:
                System.out.println(Message.COMMANDE_INCONNUE);
                break;
        }
    }
}
