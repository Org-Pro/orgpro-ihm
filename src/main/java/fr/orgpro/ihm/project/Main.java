package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static Data data;

    public static void main(String[] args) throws IOException {
        data = Data.getInstance();
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String[] tabArg;
        boolean quitter = false;

        //////////////////////////////////////////////
        data.addListeTache(new Tache("aze", 5));
        data.addListeTache(new Tache("aaa", 5));
        data.addListeTache(new Tache("bbb", 5));
        data.addListeTache(new Tache("ccc", 5));
        data.addListeTache(new Tache("ddd", 5));
        //////////////////////////////////////////////

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
                Commande.commandeTache(args, data);
                break;
            case "help":
                break;
            default:
                System.out.println(Message.COMMANDE_INCONNUE);
                break;
        }
    }



    private static void affichageArgs(String[] args){
        for(String ele : args){
            System.out.println(ele);
        }
    }
}
