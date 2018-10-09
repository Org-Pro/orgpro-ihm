package fr.orgpro.ihm.project;

import java.io.File;
import java.io.IOException;

public class Main {
    private static Data data;

    public static void main(String[] args) throws IOException {
        //args= new String[]{"file", "select", "google"};
        //args = new String[]{"task", "add", "test2"};
        //args = new String[]{"task", "list"};
        //args = new String[]{"task", "clock", "use", "1"};
        //args = new String[]{"task", "dep", "set", "0", "2"};
         //args = new String[]{"col", "set", "baptiste", "batrayer"};
        // args = new String[]{"col", "delete", "batrayer"};
        //args = new String[]{"task", "col", "send", "0", "Batrayer"};
        data = Data.getInstance();

        if (data.getFichierCourant().isEmpty()){
            System.out.println(Message.MAIN_AUNCUN_FICHIER);
            File[] files = new File(data.getDossierCourant()).listFiles();
            // Si le Dossier n'existe pas, on le crée
            if(files == null){
                new File(data.getDossierCourant()).mkdirs();
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
        if(data.getFichierCourant().isEmpty() && !args[0].toLowerCase().equals("file")){
            return;
        }
        switch (args[0].toLowerCase()){
            case "collaborator": {}
            case "col" :
                Commande.commandeCollaborateur(args, data);
                break;
            case "task":
                Commande.commandeTache(args, data);
                break;
            case "head": {}
            case "header":
                Commande.commandeHeader(args, data);
                break;
            case "file":
                Commande.commandeFichier(args, data);
                break;
            case "list":
                Commande.commandeListe(args, data);
                break;
            case "cost":
                Commande.commandeCost(args, data);
                break;
            case "tag":
                Commande.commandeTag(args, data);
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
