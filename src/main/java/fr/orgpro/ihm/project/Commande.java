package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

public class Commande {
    public static void commandeTache(String[] args, Data data){
        if (verifNbArgument(2, args)){
            return;
        }
        switch (args[1]){
            case "level": {
                // TACHE LEVEL <num> <level>
                if (verifNbArgument(4, args) || verifArgEstUnNombre(args[2]) || verifArgEstUnNombre(args[3])) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (numTache < 0) {
                    System.out.println(Message.TACHE_INVALIDE_ECHEC);
                    return;
                }
                if (data.getListeTache().size() - 1 < numTache) {
                    System.out.println(Message.TACHE_INVALIDE_ECHEC);
                    return;
                }
                if (data.getListeTache().get(numTache).changeLevel(Integer.parseInt(args[3]))) {
                    System.out.println(Message.TACHE_LEVEL_SUCCES);
                } else {
                    System.out.println(Message.TACHE_LEVEL_ECHEC);
                }
                break;
            }

            case "rename": {
                // TACHE RENAME <num> <nom>
                if (verifNbArgument(4, args) || verifArgEstUnNombre(args[2])) {
                    return;
                }
                data.getListeTache().get(Integer.parseInt(args[2])).changeTitle(args[3]);
                System.out.println(Message.TACHE_RENAME_SUCESS);
                break;
            }

            case "add": {
                // TACHE ADD <nom> <level>
                if (verifNbArgument(4, args) || verifArgEstUnNombre(args[3])) {
                    return;
                }
                data.addListeTache(new Tache(args[2], Integer.parseInt(args[3])));
                System.out.println(Message.TACHE_AJOUT_SUCCES);
                break;
            }

            case "list": {
                // TACHE LIST
                System.out.println(data.getListeTache().size() + " résultat(s).");
                int i = 0;
                String msg;
                for (Tache tache : data.getListeTache()) {
                    msg = "n°" + i + " " + tache.getTitle();
                    if(tache.getClock() != null){
                        //msg += " " + tach
                    }
                    System.out.println(msg);
                    i++;
                }
                break;
            }

            case "dep": {
                if (verifNbArgument(3, args)) {
                    return;
                }
                switch (args[2]) {
                    case "set": {
                        // TACHE DEP SET <num> <num>
                        if (verifNbArgument(5, args) || verifArgEstUnNombre(args[3]) || verifArgEstUnNombre(args[4])) {
                            return;
                        }
                        int numTacheRecoit = Integer.parseInt(args[3]);
                        int numTacheDonne = Integer.parseInt(args[4]);
                        if (numTacheRecoit < 0 || numTacheDonne < 0) {
                            System.out.println(Message.TACHE_INVALIDE_ECHEC);
                            return;
                        }
                        if (numTacheDonne == numTacheRecoit) {
                            System.out.println(Message.TACHES_NON_IDENTIQUES_ECHEC);
                            return;
                        }
                        int tailleList = data.getListeTache().size();
                        if (tailleList - 1 < numTacheDonne || tailleList - 1 < numTacheRecoit) {
                            System.out.println(Message.TACHE_INVALIDE_ECHEC);
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
                        data.getListeTache().get(Integer.parseInt(args[3])).removeDependance();
                        System.out.println(Message.TACHE_REMOVE_DEPENDANCE_SUCCES);
                        break;
                    }
                    default:
                        System.out.println(Message.ARGUMENT_INVALIDE);
                        break;
                }
                break;
            }

            case "help":
                System.out.println(Message.TACHE_HELP);
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }
    }

    public static void commandeFichier(String[] args, Data data) {
        if (verifNbArgument(2, args)){
            return;
        }
        switch (args[1]){
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
        }

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
