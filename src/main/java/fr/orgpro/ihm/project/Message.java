package fr.orgpro.ihm.project;

public enum Message {
    COMMANDE_INCONNUE("ECHEC : La commande est inconnue."),
    ARGUMENT_MANQUANT("ECHEC : Il manque des arguments."),
    ARGUMENT_INVALIDE("ECHEC : Un des arguments n'est pas valide."),
    PROBLEME_LECTURE("ECHEC : Problème lors de la lecture du fichier."),

    MAIN_HELP(""),
    MAIN_AUNCUN_FICHIER("Vous devez créer ou charger un fichier.\n" +
            "Commande : FILE SELECT <Nom du fichier à créer / charger>"),
    MAIN_LISTE_FICHIER("Vous pouvez charger un fichier parmi la liste :"),

    TACHE_AJOUT_SUCCES("SUCCES : Tâche créée."),
    TACHE_AJOUT_DEADLINE_SUCCES("SUCCES : Deadline ajoutée/modifiée."),
    TACHE_AJOUT_CLOSED_SUCCES("SUCCES : Closed ajoutée/modifiée."),
    TACHE_AJOUT_SCHEDULED_SUCCES("SUCCES : Scheduled ajoutée/modifiée."),
    TACHE_AJOUT_TAG_SUCCES("SUCCES : Tag ajouté."),
    TACHE_AJOUT_PROPRIETE_SUCCES("SUCCES : Property ajoutée."),
    TACHE_DELETE_PROPRIETE_SUCCES("SUCCES : Property supprimée."),
    TACHE_DELETE_TAG_SUCCES("SUCCES : Tag supprimé."),
    TACHE_DELETE_SUCCES("SUCCES : Tâche supprimée."),
    TACHE_DELETE_DEADLINE_SUCCES("SUCCES : Deadline supprimée."),
    TACHE_DELETE_CLOSED_SUCCES("SUCCES : Closed supprimée."),
    TACHE_DELETE_SCHEDULED_SUCCES("SUCCES : Scheduled supprimée."),
    TACHE_MINUTEUR_RESET_SUCCES("SUCCES : Minuteur de la tâche remit à zéro."),
    TACHE_MINUTEUR_LANCER_SUCCES("SUCCES : Minuteur lancé."),
    TACHE_MINUTEUR_STOPPER_SUCCES("SUCCES : Minuteur stoppé."),
    TACHE_SET_DEPENDANCE_SUCCES("SUCCES : La dépendance a bien été ajoutée / modifiée."),
    TACHE_RENAME_SUCESS("SUCCES : La tâche a bien été renommée."),
    TACHE_DELETE_DEPENDANCE_SUCCES("SUCCES : La dépendance a bien été supprimée."),
    TACHE_LEVEL_SUCCES("SUCCES : Le niveau de la tâche a bien été changé."),
    TACHE_AJOUT_PROPRIETE_ECHEC("ECHEC : La property ne doit pas contenir : ID et DEPENDENCE."),
    TACHE_SET_DEPENDANCE_ECHEC("ECHEC : La dépendance ne peut pas être modifiée."),
    TACHE_DELETE_DEPENDANCE_ECHEC("ECHEC : La dépendance ne peut pas être supprimée."),
    TACHE_DELETE_PROPRIETE_ECHEC("ECHEC : La property ne doit pas contenir : ID et DEPENDENCE."),
    TACHE_LEVEL_DEP_ECHEC("ECHEC : Une dépendance empêche le changement de niveau."),
    TACHE_LEVEL_NEGATIF_ECHEC("ECHEC : Le niveau de la tâche doit être supérieur ou égal à 1."),
    TACHE_INVALIDE_ECHEC("ECHEC : Numéro de tâche invalide."),
    TACHES_NON_IDENTIQUES_ECHEC("ECHEC : Les tâches indiquées ne doivent pas être identiques."),
    TACHE_AJOUT_DEADLINE_ECHEC("ECHEC : Le format de la date est incorrect. (AAAA/MM/JJ)"),
    TACHE_AJOUT_CLOSED_ECHEC("ECHEC : Le format de la date est incorrect. (AAAA/MM/JJ)"),
    TACHE_AJOUT_SCHEDULED_ECHEC("ECHEC : Le format de la date est incorrect. (AAAA/MM/JJ)"),
    TACHE_HELP("TACHE ADD <Nom de la tâche> -> Ajoute une tâche\n" /*+
            "TACHE LIST -> Affiche les tâches\n" +
            "TACHE DEP SET <Numéro de la tâche qui reçoit> <Numéro de la tâche qui donne> -> Ajoute une dépendance à la tâche\n" +
            "TACHE DEP REMOVE <Numéro de la tâche> -> Supprime la dépendance de la tâche.\n" +
            "TACHE RENAME <Numéro de la tâche> <Nouveau titre> -> Change le titre de la tâche.\n" +
            "TACHE LEVEL <Numéro de la tâche> <Niveau de la tâche> -> Change le niveau de la tâche.\n" +
            "TACHE CLOCK RESET <Numéro de la tâche> -> Remet à zéro le minuteur de la tâche.\n" +
            "TACHE CLOCK USE <Numéro de la tâche> -> Lance ou stop le minuteur de la tâche."*/),

    FICHIER_SAVE_SUCCES("SUCCES : Fichier enregistré."),
    FICHIER_CREATION("SUCCES : Le fichier n'existe pas mais il a bien été créé."),
    FICHIER_LOAD("SUCCES : Chargement du fichier réussi."),
    FICHIER_LISTE("Liste des fichiers disponibles :"),
    FICHIER_LISTE_VIDE("Auncun fichier trouvé.")
    ;

    private String message;

    Message(String message){
        this.message = message;
    }

    public String toString(){
        return message;
    }
}
