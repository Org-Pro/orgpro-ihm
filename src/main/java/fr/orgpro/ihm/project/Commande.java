package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

public class Commande {
    public static void commandeTache(String[] args, Data data){
        if (verifNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            /*case "clock" : {
                if (verifNbArgument(3, args)) {
                    return;
                }
                switch (args[2].toLowerCase()){
                    case "use" : {
                        // TACHE CLOCK USE <num>
                        if (verifNbArgument(4, args) || verifArgEstUnNombre(args[3])) {
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
                        if (verifNbArgument(4, args) || verifArgEstUnNombre(args[3])) {
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
                if (verifNbArgument(4, args) || verifArgEstUnNombre(args[2]) || verifArgEstUnNombre(args[3])) {
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

            /*case "rename": {
                // TACHE RENAME <num> <nom>
                if (verifNbArgument(4, args) || verifArgEstUnNombre(args[2])) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if(verifTacheExiste(numTache, data)){
                    return;
                }
                data.getListeTache().get(numTache).changeTitle(args[3]);
                System.out.println(Message.TACHE_RENAME_SUCESS);
                break;
            }*/

            case "add": {
                // TACHE ADD <nom>

                break;
            }

            /*case "list": {
                // TACHE LIST
                System.out.println(data.getListeTache().size() + " résultat(s).");
                int i = 0;
                String msg;
                for (Tache tache : data.getListeTache()) {
                    msg = "n°" + i + " " + tache.getTitle();
                    if(tache.getClock() != null){
                        msg += " " + tache.getClock();
                    }
                    System.out.println(msg);
                    i++;
                }
                break;
            }*/

            /*case "dep": {
                if (verifNbArgument(3, args)) {
                    return;
                }
                switch (args[2].toLowerCase()) {
                    case "set": {
                        // TACHE DEP SET <num> <num>
                        if (verifNbArgument(5, args) || verifArgEstUnNombre(args[3]) || verifArgEstUnNombre(args[4])) {
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
                        if (verifNbArgument(4, args) || verifArgEstUnNombre(args[3])) {
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

    /*public static void commandeFichier(String[] args, Data data) {
        if (verifNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            // FILE SAVE
            case "save" : {
                for(Tache tache : data.getListeTache()) {
                    tache.ecritureFichier("test.org", true);
                }
                System.out.println(Message.FICHIER_SAVE_SUCCES);
                break;
            }

            case "help" :
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }

    }*/

    public static void commandeListe(String[] args, Data data) {
        if (verifNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            case "tache" : {
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

    private static boolean verifTacheExiste(int numTache, Data data){
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

    private static boolean verifArgEstUnNombre(String val){
        try {
            Integer.parseInt(val);
            return false;
        } catch (NumberFormatException e) {
            System.out.println(Message.ARGUMENT_INVALIDE);
            return true;
        }
    }

    private static boolean verifNbArgument(int val, String[] args){
        if (val > args.length){
            System.out.println(Message.ARGUMENT_MANQUANT);
            return true;
        }else{
            return false;
        }
    }



}
