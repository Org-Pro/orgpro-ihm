package fr.orgpro.ihm.project;

import fr.trellorg.api.project.Tache;

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
                    System.out.println(data.getListeTache().size() + " résultat(s).");
                    int i = 0;
                    for(Tache tache : data.getListeTache()){
                        System.out.println("n°" + i + " " + tache.getTitle());
                        i++;
                    }
                    break;
                case "help":
                    System.out.println("TACHE ADD <Nom de la tâche> <Level de la tâche> -> Ajoute une tâche");
                    System.out.println("TACHE LIST -> Affiche les tâches");
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
