package fr.orgpro.ihm.project;

public enum Message {
    COMMANDE_INCONNUE("FAILURE : Unknown command."),
    ARGUMENT_MANQUANT("FAILURE : Missing argument(s)."),
    ARGUMENT_INVALIDE("FAILURE : One of the argument is invalid."),
    PROBLEME_LECTURE("FAILURE : Trouble reading the file."),

    MAIN_HELP(""),
    MAIN_AUNCUN_FICHIER("You must create or upload a file.\n" +
            "Command : FILE SELECT <Name of the file to create / load>"),
    MAIN_LISTE_FICHIER("You can load a file from the list :"),

    TACHE_AJOUT_SUCCES("SUCCESS : Task created."),
    TACHE_AJOUT_AVEC_DEP_SUCCES("SUCCESS : Task created with the dependence."),
    TACHE_AJOUT_DEADLINE_SUCCES("SUCCESS : Deadline add/modified."),
    TACHE_AJOUT_CLOSED_SUCCES("SUCCESS : Closed add/modified."),
    TACHE_AJOUT_SCHEDULED_SUCCES("SUCCESS : Scheduled add/modified."),
    TACHE_AJOUT_TAG_SUCCES("SUCCESS : Tag add."),
    TACHE_AJOUT_PROPRIETE_SUCCES("SUCCESS : Property add."),
    TACHE_DELETE_PROPRIETE_SUCCES("SUCCESS : Property deleted."),
    TACHE_DELETE_TAG_SUCCES("SUCCESS : Tag deleted."),
    TACHE_DELETE_SUCCES("SUCCESS : Task deleted."),
    //TACHE_DELETE_DEADLINE_SUCCES("SUCCES : Deadline supprimée."),
    //TACHE_DELETE_CLOSED_SUCCES("SUCCES : Closed supprimée."),
    //TACHE_DELETE_SCHEDULED_SUCCES("SUCCES : Scheduled supprimée."),
    //TACHE_MINUTEUR_RESET_SUCCES("SUCCES : Minuteur de la tâche remit à zéro."),
    //TACHE_MINUTEUR_LANCER_SUCCES("SUCCES : Minuteur lancé."),
    //TACHE_MINUTEUR_STOPPER_SUCCES("SUCCES : Minuteur stoppé."),
    TACHE_SET_DEPENDANCE_SUCCES("SUCCESS : Dependency has been added / modified."),
    TACHE_RENAME_SUCESS("SUCCESS : The task has been renamed."),
    TACHE_DELETE_DEPENDANCE_SUCCES("SUCCESS : Dependency deleted."),
    //TACHE_LEVEL_SUCCES("SUCCES : Le niveau de la tâche a bien été changé."),
    TACHE_AJOUT_PROPRIETE_ECHEC("FAILURE : Cannot add property ID or DEPENDENCE."),
    TACHE_SET_DEPENDANCE_ECHEC("FAILURE : Dependency cannot be updated."),
    TACHE_DELETE_DEPENDANCE_ECHEC("FAILURE : Dependency cannot be deleted."),
    TACHE_DELETE_PROPRIETE_ECHEC("FAILURE : Cannot delete property ID or DEPENDENCE."),
    //TACHE_LEVEL_DEP_ECHEC("ECHEC : Une dépendance empêche le changement de niveau."),
    //TACHE_LEVEL_NEGATIF_ECHEC("ECHEC : Le niveau de la tâche doit être supérieur ou égal à 1."),
    TACHE_INVALIDE_ECHEC("FAILURE : Invalid task number."),
    TACHES_NON_IDENTIQUES_ECHEC("FAILURE : tasks must not be identical."),
    TACHE_AJOUT_DEADLINE_ECHEC("FAILURE : Wrong date format. (YYYY/MM/DD)."),
    TACHE_AJOUT_CLOSED_ECHEC("FAILURE : Wrong date format. (YYYY/MM/DD)."),
    TACHE_AJOUT_SCHEDULED_ECHEC("FAILURE : Wrong date format. (YYYY/MM/DD)."),
    TACHE_HELP("TASK ADD <Title of task> -> Add a task\n"



    //TODO


    ),

    //FICHIER_SAVE_SUCCES("SUCCESS : Saved file."),
    FICHIER_CREATION("SUCCESS : File does not exist but it was created."),
    FICHIER_LOAD("SUCCESS : Loaded file."),
    FICHIER_LISTE("List of file available :"),
    FICHIER_LISTE_VIDE("No file found."),

    LIST_AUCUN_RESULTAT("No result."),
    LIST_HELP("LIST TASK -> displays the tasks of a file\n" +
    "LIST TS <State> (TODO,ONGOING,DONE,CANCELLED) -> displays the tasks that correspond to the given state"),

    STATE_UPDATE_SUCCES("SUCCESS : Changed State."),
    STATE_UPDATE_ECHEC("FAILURE : Unchanged STate."),
    STATE_INTROUVABLE("FAILURE : State does not exist.")
    ;

    private String message;

    Message(String message){
        this.message = message;
    }

    public String toString(){
        return message;
    }
}
