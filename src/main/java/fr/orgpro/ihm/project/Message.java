package fr.orgpro.ihm.project;

public enum Message {
    COMMANDE_INCONNUE("ECHEC : La commande est inconnue."),
    ARGUMENT_MANQUANT("ECHEC : Il manque des arguments."),
    ARGUMENT_INVALIDE("ECHEC : Un des arguments n'est pas valide."),
    TACHE_AJOUT_SUCESS("SUCCES : Tâche créée avec sucess."),
    TACHE_INVALIDE("ECHEC : Numéro de tâche invalide."),
    TACHE_SET_DEPENDANCE("SUCCES : La dépendance a bien été ajouté / changé."),
    TACHES_NON_IDENTIQUES("ECHEC : Les tâches indiquées ne doivent pas être identiques."),
    ;

    private String message;

    Message(String message){
        this.message = message;
    }

    public String toString(){
        return message;
    }
}
