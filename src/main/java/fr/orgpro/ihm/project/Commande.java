package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

import java.io.File;
import java.io.IOException;

public class Commande {
    public static void commandeTache(String[] args, Data data){
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            /*case "clock" : {
                if (verifBadNbArgument(3, args)) {
                    return;
                }
                switch (args[2].toLowerCase()){
                    case "use" : {
                        // TACHE CLOCK USE <num>
                        if (verifBadNbArgument(4, args) || verifArgEstUnNombre(args[3])) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if (verifTacheExiste(numTache, data)) {
                            return;
                        }
                        if(data.getListeTache().get(numTache).minuteur()){
                            System.out.println(Message.TACHE_MINUTEUR_LANCER_SUCCES);
                        }else{
                            System.out.println(Message.TACHE_MINUTEUR_STOPPER_SUCCES);
                        }
                        break;
                    }
                    case "reset": {
                        // TACHE CLOCK RESET <num>
                        if (verifBadNbArgument(4, args) || verifArgEstUnNombre(args[3])) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if (verifTacheExiste(numTache, data)) {
                            return;
                        }
                        data.getListeTache().get(numTache).resetMinuteur();
                        System.out.println(Message.TACHE_MINUTEUR_RESET_SUCCES);
                        break;
                    }

                    default:
                        System.out.println(Message.ARGUMENT_INVALIDE);
                        break;
                }
                break;
            }*/

            /*case "level": {
                // TACHE LEVEL <num> <level>
                if (verifBadNbArgument(4, args) || verifArgEstUnNombre(args[2]) || verifArgEstUnNombre(args[3])) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                int level = Integer.parseInt(args[3]);
                if(level < 1){
                    System.out.println(Message.TACHE_LEVEL_NEGATIF_ECHEC);
                    return;
                }
                if(verifTacheExiste(numTache, data)){
                    return;
                }
                if (data.getListeTache().get(numTache).changeLevel(Integer.parseInt(args[3]))) {
                    System.out.println(Message.TACHE_LEVEL_SUCCES);
                } else {
                    System.out.println(Message.TACHE_LEVEL_DEP_ECHEC);
                }
                break;
            }*/

            case "rename": {
                // TACHE RENAME <num> <nom>
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)){
                    return;
                }
                data.getListeTache().get(numTache).changeTitle(args[3]);
                data.ecritureListeTaches();
                System.out.println(Message.TACHE_RENAME_SUCESS);
                break;
            }

            case "add": {
                // TACHE ADD <nom>
                if (verifBadNbArgument(3, args)) {
                    return;
                }
                Tache tache = new Tache(args[2]);
                tache.ecritureFichier(data.getPath(), true);
                System.out.println(Message.TACHE_AJOUT_SUCCES);
                break;
            }

            case "delete": {
                // TACHE DELETE <num>
                if (verifBadNbArgument(3, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)){
                    return;
                }
                data.getListeTache().remove(numTache);
                data.ecritureListeTaches();
                System.out.println(Message.TACHE_DELETE_SUCCES);
                break;
            }

            case "list": {
                // TACHE LIST
                if(verifBadLectureFichier(data)){
                    return;
                }
                System.out.print(data.getListeTache().size() + " résultat(s).\n");
                int i = 0;
                String msg;
                for (Tache tache : data.getListeTache()) {
                    msg = "n°" + i + " " + tache.getTitle() + " " + tache.getId();
                    if(tache.getClock() != null){
                        msg += " " + tache.getClock();
                    }
                    System.out.print(msg + "\n");
                    i++;
                }
                break;
            }

            /*case "dep": {
                if (verifBadNbArgument(3, args)) {
                    return;
                }
                switch (args[2].toLowerCase()) {
                    case "set": {
                        // TACHE DEP SET <num> <num>
                        if (verifBadNbArgument(5, args) || verifArgEstUnNombre(args[3]) || verifArgEstUnNombre(args[4])) {
                            return;
                        }
                        int numTacheRecoit = Integer.parseInt(args[3]);
                        int numTacheDonne = Integer.parseInt(args[4]);
                        if(verifTacheExiste(numTacheRecoit, data) || verifTacheExiste(numTacheDonne, data)){
                            return;
                        }
                        if (numTacheDonne == numTacheRecoit) {
                            System.out.println(Message.TACHES_NON_IDENTIQUES_ECHEC);
                            return;
                        }
                        data.getListeTache().get(numTacheRecoit).setDependance(data.getListeTache().get(numTacheDonne));
                        System.out.println(Message.TACHE_SET_DEPENDANCE_SUCCES);
                        break;
                    }
                    case "remove": {
                        // TACHE DEP REMOVE <num>
                        if (verifBadNbArgument(4, args) || verifArgEstUnNombre(args[3])) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if(verifTacheExiste(numTache, data)){
                            return;
                        }
                        data.getListeTache().get(numTache).removeDependance();
                        System.out.println(Message.TACHE_REMOVE_DEPENDANCE_SUCCES);
                        break;
                    }
                    default:
                        System.out.println(Message.ARGUMENT_INVALIDE);
                        break;
                }
                break;
            }*/

            case "help":
                System.out.println(Message.TACHE_HELP);
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }
    }

    public static void commandeFichier(String[] args, Data data) {
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            /*case "save": {
                // FILE SAVE
                for(Tache tache : data.getListeTache()) {
                    tache.ecritureFichier("test.org", true);
                }
                System.out.println(Message.FICHIER_SAVE_SUCCES);
                break;
            }*/
            case "list": {
                // FILE LIST
                if(verifBadLectureFichier(data)){
                    return;
                }
                File[] files = new File(data.getDossierCourant()).listFiles();
                if (files == null){
                    System.out.println(Message.FICHIER_LISTE_VIDE);
                }else {
                    System.out.println(Message.FICHIER_LISTE);
                    for (File file : files) {
                        if (file.getName().endsWith(".org")) {
                            System.out.println(file.getName());
                        }
                    }
                }
                break;
            }
            case "select": {
                // FILE SELECT <nom>
                if(verifBadNbArgument(3, args)){
                    return;
                }
                String fichier = data.setFichierCourant(args[2]);
                File file = new File(data.getDossierCourant() + "/" + fichier );
                if(!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Message.FICHIER_CREATION);
                }else{
                    System.out.println(Message.FICHIER_LOAD);
                }
                break;
            }

            case "help":
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }

    }

    public static void commandeListe(String[] args, Data data) {
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            case "tache" : {
                // LIST TACHE
                if(verifBadLectureFichier(data)){
                    return;
                }
                for(Tache tache : data.getListeTache()){
                    System.out.print(tache.toString());
                }
                break;
            }

            case "help" :
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }
    }


    private static boolean verifBadLectureFichier(Data data){
        if (data.loadFichier()){
            return false;
        }else {
            System.out.println(Message.PROBLEME_LECTURE);
            return true;
        }
    }


    private static boolean verifTacheNotExiste(int numTache, Data data){
        if (numTache < 0) {
            System.out.println(Message.TACHE_INVALIDE_ECHEC);
            return true;
        }
        if (data.getListeTache().size() - 1 < numTache) {
            System.out.println(Message.TACHE_INVALIDE_ECHEC);
            return true;
        }
        return false;
    }

    private static boolean verifArgNotNombre(String val){
        try {
            Integer.parseInt(val);
            return false;
        } catch (NumberFormatException e) {
            System.out.println(Message.ARGUMENT_INVALIDE);
            return true;
        }
    }

    private static boolean verifBadNbArgument(int val, String[] args){
        if (val > args.length){
            System.out.println(Message.ARGUMENT_MANQUANT);
            return true;
        }else{
            return false;
        }
    }



}
