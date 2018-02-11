package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

public class Commande {
    public static void commandeTache(String[] args, Data data){
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
                        data.addListeTache(tache);
                        System.out.println(Message.TACHE_AJOUT_SUCESS);
                    }else{
                        System.out.println(Message.ARGUMENT_MANQUANT);
                    }
                    break;

                case "list":
                    // TACHE LIST
                    System.out.println(data.getListeTache().size() + " résultat(s).");
                    int i = 0;
                    for(Tache tache : data.getListeTache()){
                        System.out.println("n°" + i + " " + tache.getTitle());
                        i++;
                    }
                    break;

                case "set":
                    if(args.length >= 5){
                        switch (args[2]){
                            case "dep":
                                int numTacheRecoit, numTacheDonne;
                                try {
                                    numTacheRecoit = Integer.parseInt(args[3]);
                                    numTacheDonne = Integer.parseInt(args[4]);
                                }catch (NumberFormatException e){
                                    System.out.println(Message.ARGUMENT_INVALIDE);
                                    return;
                                }
                                if(numTacheRecoit < 0 || numTacheDonne < 0){
                                    System.out.println(Message.TACHE_INVALIDE);
                                    return;
                                }
                                if(numTacheDonne == numTacheRecoit){
                                    System.out.println(Message.TACHES_NON_IDENTIQUES);
                                    return;
                                }
                                int tailleList = data.getListeTache().size();
                                if(tailleList - 1 < numTacheDonne || tailleList - 1 < numTacheRecoit){
                                    System.out.println(Message.TACHE_INVALIDE);
                                    System.out.println("lol");
                                    return;
                                }
                                data.getListeTache().get(numTacheRecoit).setDependance(data.getListeTache().get(numTacheDonne));
                                System.out.println(Message.TACHE_SET_DEPENDANCE);
                                break;
                            default:
                                System.out.println(Message.ARGUMENT_INVALIDE);
                                break;
                        }
                    }else{
                        System.out.println(Message.ARGUMENT_MANQUANT);
                    }
                    break;

                case "help":
                    System.out.println("TACHE ADD <Nom de la tâche> <Level de la tâche> -> Ajoute une tâche");
                    System.out.println("TACHE LIST -> Affiche les tâches");
                    System.out.println("TACHE SET DEP <Numéro de la tâche qui reçoit> <Numéro de la tâche qui donne> -> Ajoute une dépendance à la tâche");
                    break;
                default:
                    System.out.println(Message.ARGUMENT_INVALIDE);
                    break;
            }
        }else{
            System.out.println(Message.ARGUMENT_MANQUANT);
        }
    }
}
