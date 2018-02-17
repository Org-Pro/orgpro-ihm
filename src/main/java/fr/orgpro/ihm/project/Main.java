package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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


        if (args.length > 0) {
            traitementArgs(args);
        }
    }

    private static void traitementArgs(String[] args) {
        switch (args[0].toLowerCase()) {
            case "tache":
                Commande.commandeTache(args, data);
                break;
            case "file":
                Commande.commandeFichier(args, data);
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
