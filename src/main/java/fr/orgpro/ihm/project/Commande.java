package fr.orgpro.ihm.project;

import fr.orgpro.api.project.State;
import fr.orgpro.api.project.Tache;
import fr.orgpro.api.scrum.Scrum;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

            case "state": {
                // TASK STATE <num> <state ou next>
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)){
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)) {
                    return;
                }
                State state = State.stringIsState(args[3]);
                if(state == null){
                    System.out.println(Message.STATE_INTROUVABLE);
                    return;
                }
                if(data.getListeTache().get(numTache).changeState(state)){
                    data.ecritureListeTaches();
                    System.out.println(Message.STATE_UPDATE_SUCCES);
                }else{
                    System.out.println(Message.STATE_UPDATE_ECHEC);
                }
                break;
            }

            case "prop": {}
            case "property": {
                // TASK PROPERTY ...
                if (verifBadNbArgument(3, args)){
                    return;
                }
                switch (args[2].toLowerCase()){
                    case "add": {
                        // TASK PROPERTY ADD <num> <name prop> <val prop>
                        if (verifBadNbArgument(6, args) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if (verifTacheNotExiste(numTache, data)) {
                            return;
                        }
                        if(data.getListeTache().get(numTache).ajoutProperty(args[4], args[5], false)){
                            data.ecritureListeTaches();
                            System.out.println(Message.TACHE_AJOUT_PROPRIETE_SUCCES);
                        }else{
                            System.out.println(Message.TACHE_AJOUT_PROPRIETE_ECHEC);
                        }
                        break;
                    }
                    case "delete": {
                        // TASK PROPERTY ADD <num> <name prop>
                        if (verifBadNbArgument(5, args) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if (verifTacheNotExiste(numTache, data)) {
                            return;
                        }
                        if(data.getListeTache().get(numTache).supprimerProperty(args[4],  false)){
                            data.ecritureListeTaches();
                            System.out.println(Message.TACHE_DELETE_PROPRIETE_SUCCES);
                        }else{
                            System.out.println(Message.TACHE_DELETE_PROPRIETE_ECHEC);
                        }
                        break;
                    }
                    default:
                        System.out.println(Message.ARGUMENT_INVALIDE);
                        break;
                }
                break;
            }

            case "tag": {
                // TASK TAG ...
                if (verifBadNbArgument(3, args)){
                    return;
                }
                switch (args[2].toLowerCase()){
                    case "add": {
                        // TASK TAG ADD <num> <tag>
                        if (verifBadNbArgument(5, args) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if (verifTacheNotExiste(numTache, data)) {
                            return;
                        }
                        data.getListeTache().get(numTache).ajoutTag(args[4]);
                        data.ecritureListeTaches();
                        System.out.println(Message.TACHE_AJOUT_TAG_SUCCES);
                        break;
                    }
                    case "delete": {
                        // TASK TAG DELETE <num> <tag>
                        if (verifBadNbArgument(5, args) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if (verifTacheNotExiste(numTache, data)) {
                            return;
                        }
                        data.getListeTache().get(numTache).supprimerTag(args[4]);
                        data.ecritureListeTaches();
                        System.out.println(Message.TACHE_DELETE_TAG_SUCCES);
                        break;
                    }
                    default:
                        System.out.println(Message.ARGUMENT_INVALIDE);
                        break;
                }

                break;
            }

            case "deadline":{}
            case "dl": {
                // TACHE DL <num> <date ou 0>
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)) {
                    return;
                }

                /*if(args[3].trim().equals("0")){
                    // TODO
                    //data.getListeTache().get(numTache)

                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_DELETE_DEADLINE_SUCCES);
                    return;
                }*/

                String date = args[3].replace("/", "-");
                if (data.getListeTache().get(numTache).ajoutDeadline(date)) {
                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_AJOUT_DEADLINE_SUCCES);
                } else {
                    System.out.println(Message.TACHE_AJOUT_DEADLINE_ECHEC);
                }
                break;
            }

            case "closed":{}
            case "cl": {
                // TACHE closed <num> <date ou 0>
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)) {
                    return;
                }

                /*if(args[3].trim().equals("0")){
                    // TODO
                    //data.getListeTache().get(numTache)

                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_DELETE_CLOSED_SUCCES);
                    return;
                }*/

                String date = args[3].replace("/", "-");
                if (data.getListeTache().get(numTache).ajoutClosed(date)) {
                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_AJOUT_CLOSED_SUCCES);
                } else {
                    System.out.println(Message.TACHE_AJOUT_CLOSED_ECHEC);
                }
                break;
            }

            case "scheduled":{}
            case "sd": {
                // TACHE scheduled <num> <date ou 0>
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)) {
                    return;
                }

                /*if(args[3].trim().equals("0")){
                    // TODO
                    //data.getListeTache().get(numTache)

                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_DELETE_SCHEDULED_SUCCES);
                    return;
                }*/

                String date = args[3].replace("/", "-");
                if (data.getListeTache().get(numTache).ajoutScheduled(date)) {
                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_AJOUT_SCHEDULED_SUCCES);
                } else {
                    System.out.println(Message.TACHE_AJOUT_SCHEDULED_ECHEC);
                }
                break;
            }

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
                Tache.supprimerTache(data.getListeTache(), numTache);
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

            case "dep": {
                if (verifBadNbArgument(3, args)) {
                    return;
                }
                switch (args[2].toLowerCase()) {
                    case "set": {
                        // TACHE DEP SET <num> <num>
                        if (verifBadNbArgument(5, args) || verifArgNotNombre(args[3]) || verifArgNotNombre(args[4]) || verifBadLectureFichier(data)) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        int numTacheDep = Integer.parseInt(args[4]);
                        if(verifTacheNotExiste(numTache, data) || verifTacheNotExiste(numTacheDep, data)){
                            return;
                        }
                        if (numTacheDep == numTache) {
                            System.out.println(Message.TACHES_NON_IDENTIQUES_ECHEC);
                            return;
                        }
                        if (Tache.setDependanceListe(data.getListeTache(), numTache, numTacheDep)){
                            data.ecritureListeTaches();
                            System.out.println(Message.TACHE_SET_DEPENDANCE_SUCCES);
                        }else{
                            System.out.println(Message.TACHE_SET_DEPENDANCE_ECHEC);
                        }
                        break;
                    }
                    case "delete": {
                        // TACHE DEP DELETE <num>
                        if (verifBadNbArgument(4, args) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if(verifTacheNotExiste(numTache, data)){
                            return;
                        }
                        if(Tache.deleteDependanceListe(data.getListeTache(), numTache)){
                            data.ecritureListeTaches();
                            System.out.println(Message.TACHE_DELETE_DEPENDANCE_SUCCES);
                        }else{
                            System.out.println(Message.TACHE_DELETE_DEPENDANCE_ECHEC);
                        }
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
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
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
            case "task" : {
                // LIST TACHE
                if(verifBadLectureFichier(data)){
                    return;
                }
                for(Tache tache : data.getListeTache()){
                    System.out.print(tache.toString());
                }
                break;
            }

            case "ts" : {
                // LIST TS <State>
                if(verifBadNbArgument(3, args) || verifBadLectureFichier(data)){
                    return;
                }
                List<Tache> taches = Scrum.listerTacheState(data.getListeTache(), State.stringIsState(args[2]));
                /*if(taches.isEmpty()){
                    System.out.print(Message.LIST_AUCUN_RESULTAT + "\n");
                    break;
                }*/
                int i = 0;
                int j = 0;
                String msg;
                for (Tache tache : data.getListeTache()) {
                    if(taches.contains(tache)) {
                        msg = "n°" + i + " " + tache.getTitle() + " " + tache.getId();
                        if (tache.getClock() != null) {
                            msg += " " + tache.getClock();
                        }
                        System.out.print(msg + "\n");
                        j++;
                    }
                    i++;
                }
                if(j == 0){
                    System.out.print(Message.LIST_AUCUN_RESULTAT + "\n");
                }
                else{
                    System.out.print(j + " résultat(s).\n");
                }
                break;
            }

            case "help" :
                System.out.println(Message.LIST_HELP);
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
