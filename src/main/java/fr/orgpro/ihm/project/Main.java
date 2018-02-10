package fr.orgpro.ihm.project;

import fr.trellorg.api.project.Tache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String[] tabArg;
        boolean quitter = false;
        while(!quitter){
            tabArg = bufferRead.readLine().split("( )+");
            if(tabArg.length > 0){
                if(tabArg[0].equals("quit")){
                    quitter = true;
                }else{
                    traitementArgs(tabArg);
                }
            }
            //affichageArgs(tabArg);
        }
    }

    private static void traitementArgs(String[] args){
        switch (args[0]){
            case "tache":
                commandeTache(args);

                break;
            default:
                System.out.println(Message.COMMANDE_INCONNUE);
                break;
        }
    }

    private static void commandeTache(String[] args){
        if(args.length >= 2){
            switch (args[1]){
                case "add":
                    // TACHE ADD nom level
                    if(args.length >= 4){
                        int level;
                        try {
                            level = Integer.parseInt(args[3]);
                        }catch (NumberFormatException e){
                            System.out.println(Message.ARGUMENT_INVALIDE);
                            return;
                        }
                        Tache tache = new Tache(args[2], level);
                        tache.ecritureFichier("test.org", false);
                        System.out.println(Message.TACHE_AJOUT_SUCESS);
                        System.out.println(tache.toString());
                    }else{
                        System.out.println(Message.ARGUMENT_MANQUANT);
                    }
                    break;

                default:
                    System.out.println(Message.ARGUMENT_INVALIDE);
                    break;
            }
        }else{
            System.out.println(Message.ARGUMENT_MANQUANT);
        }
    }

    private static void affichageArgs(String[] args){
        for(String ele : args){
            System.out.println(ele);
        }
    }
}
