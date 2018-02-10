package fr.orgpro.ihm.project;

public enum Message {
    COMMANDE_INCONNUE("La commande est inconnue."),
    ARGUMENT_MANQUANT("Il manque des arguments."),
    ARGUMENT_INVALIDE("Un des arguments n'est pas valide."),
    TACHE_AJOUT_SUCESS("Tâche créée avec sucess.")
    ;

    private String message;

    Message(String message){
        this.message = message;
    }

    public String toString(){
        return message;
    }
}
