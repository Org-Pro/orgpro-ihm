package fr.orgpro.ihm.project;

import java.io.File;
import java.io.IOException;

public class Main {
    private static Data data;

    public static void main(String[] args) throws IOException {
        //args = new String[]{"file", "select", "testA"};
        //args = new String[]{"tache", "help"};

        data = Data.getInstance();


        //////////////////////////////////////////////
        /*data.getListeTache().add((new Tache("aze", 5)));
        data.getListeTache().add((new Tache("aaa", 5)));
        data.getListeTache().add((new Tache("bbb", 5)));
        data.getListeTache().add((new Tache("ccc", 5)));
        data.getListeTache().add((new Tache("ddd", 5)));*/
        //////////////////////////////////////////////


        if (data.getFICHIER_COURANT().isEmpty()){
            System.out.println(Message.MAIN_AUNCUN_FICHIER);
            File[] files = new File(data.DOSSIER_COURANT).listFiles();
            // Si le Dossier n'existe pas, on le crée
            if(files == null){
                new File(data.DOSSIER_COURANT).mkdirs();
            // Sinon, s'il existe déjà des fichiers dans le dossier, on les affiche
            }else if (files.length > 0){
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
        // Si aucun fichier n'est chargé, seule la commande "file" est autorisée
        if(data.getFICHIER_COURANT().isEmpty() && !args[0].toLowerCase().equals("file")){
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
