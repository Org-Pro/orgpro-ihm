package fr.orgpro.ihm.project;

import static fr.orgpro.ihm.project.Data.*;

public enum Message {

    COMMANDE_INCONNUE(COULEUR_ECHEC +  "FAILURE : Unknown command." + COULEUR_RESET),
    ARGUMENT_MANQUANT(COULEUR_ECHEC + "FAILURE : Wrong argument(s)." + COULEUR_RESET),
    //ARGUMENT_SUP("FAILURE : too much argument(s)."),
    ARGUMENT_INVALIDE(COULEUR_ECHEC + "FAILURE : One of the argument is invalid." + COULEUR_RESET),
    PROBLEME_LECTURE(COULEUR_ECHEC + "FAILURE : Trouble reading the file." + COULEUR_RESET),

    MAIN_HELP("COL ... -> command related to collaborator\n " +
            "TASK ... -> command related to task\n" +
            "HEAD ... -> command related to header\n" +
            "FILE ... -> command related to the read and write file\n" +
            "LIST ... -> command related to sort of task\n" +
            "SPRINT ... -> command related to the sprint\n" +
            "TAG ... -> command that allow to mark task to functionnal or technical\n"),
    MAIN_AUNCUN_FICHIER("You must create or upload a file.\n" +
            "Command : FILE SELECT <Name of the file to create / load>"),
    MAIN_LISTE_FICHIER("You can load a file from the list :"),

    TACHE_LIBELLE("The task : "),
    TACHE_AJOUT_SUCCES(COULEUR_SUCCES + "SUCCESS : Task created." + COULEUR_RESET),
    TACHE_AJOUT_AVEC_DEP_SUCCES(COULEUR_SUCCES + "SUCCESS : Task created with dependency." + COULEUR_RESET),
    TACHE_AJOUT_DEADLINE_SUCCES(COULEUR_SUCCES + "SUCCESS : Deadline add/modified." + COULEUR_RESET),
    TACHE_AJOUT_CLOSED_SUCCES(COULEUR_SUCCES + "SUCCESS : Closed add/modified." + COULEUR_RESET),
    TACHE_AJOUT_SCHEDULED_SUCCES(COULEUR_SUCCES + "SUCCESS : Scheduled add/modified." + COULEUR_RESET),
    TACHE_AJOUT_TAG_SUCCES(COULEUR_SUCCES + "SUCCESS : Tag add." + COULEUR_RESET),
    TACHE_AJOUT_PROPRIETE_SUCCES(COULEUR_SUCCES + "SUCCESS : Property add." + COULEUR_RESET),
    TACHE_AJOUT_COLLABORATEUR_SUCCES(COULEUR_SUCCES + "SUCCESS : Collaborator add." + COULEUR_RESET),
    TACHE_DELETE_COLLABORATEUR_SUCCES(COULEUR_SUCCES + "SUCCESS : Collaborator deleted." + COULEUR_RESET),
    TACHE_DELETE_PROPRIETE_SUCCES(COULEUR_SUCCES + "SUCCESS : Property deleted." + COULEUR_RESET),
    TACHE_DELETE_TAG_SUCCES(COULEUR_SUCCES + "SUCCESS : Tag deleted." + COULEUR_RESET),
    TACHE_DELETE_SUCCES(COULEUR_SUCCES + "SUCCESS : Task deleted." + COULEUR_RESET),
    TACHE_STATE_UPDATE_SUCCES(COULEUR_SUCCES + "SUCCESS : Changed State." + COULEUR_RESET),
    TACHE_STATE_SUIVANT_UPDATE_SUCCES(COULEUR_SUCCES + "SUCCESS : Changed State." + COULEUR_RESET),
    TACHE_MINUTEUR_LANCER_SUCCES(COULEUR_SUCCES + "SUCCESS : Timer started." + COULEUR_RESET),
    TACHE_MINUTEUR_STOPPER_SUCCES(COULEUR_SUCCES + "SUCCESS : Timer stopped." + COULEUR_RESET),
    TACHE_SET_DEPENDANCE_SUCCES(COULEUR_SUCCES + "SUCCESS : Dependency has been added / modified." + COULEUR_RESET),
    TACHE_RENAME_SUCESS(COULEUR_SUCCES + "SUCCESS : The task has been renamed." + COULEUR_RESET),
    TACHE_DELETE_DEPENDANCE_SUCCES(COULEUR_SUCCES + "SUCCESS : Dependency deleted." + COULEUR_RESET),
    TACHE_AJOUT_COLLABORATEUR_ECHEC(COULEUR_ECHEC + "FAILURE : Collaborator's name cannot contains \":\"." + COULEUR_RESET),
    TACHE_DELETE_COLLABORATEUR_ECHEC(COULEUR_ECHEC + "FAILURE : Collaborator's name cannot contains \":\"" + COULEUR_RESET),
    TACHE_AJOUT_PROPRIETE_ECHEC(COULEUR_ECHEC + "FAILURE : Cannot add property ID or DEPENDENCE." + COULEUR_RESET),
    TACHE_SET_DEPENDANCE_ECHEC(COULEUR_ECHEC + "FAILURE : Dependency cannot be updated." + COULEUR_RESET),
    TACHE_DELETE_DEPENDANCE_ECHEC(COULEUR_ECHEC + "FAILURE : Dependency cannot be deleted." + COULEUR_RESET),
    TACHE_AJOUT_TAG_ECHEC(COULEUR_ECHEC + "FAILURE : Tag cannot be add." + COULEUR_RESET),
    TACHE_DELETE_TAG_ECHEC(COULEUR_ECHEC + "FAILURE : Tag cannot be deleted." + COULEUR_RESET),
    TACHE_DELETE_PROPRIETE_ECHEC(COULEUR_ECHEC + "FAILURE : Cannot delete property ID or DEPENDENCE." + COULEUR_RESET),
    TACHE_INVALIDE_ECHEC(COULEUR_ECHEC + "FAILURE : Invalid task number." + COULEUR_RESET),
    TACHES_NON_IDENTIQUES_ECHEC(COULEUR_ECHEC + "FAILURE : tasks must not be identical." + COULEUR_RESET),
    TACHE_AJOUT_DEADLINE_ECHEC(COULEUR_ECHEC + "FAILURE : Wrong date format. (YYYY/MM/DD)." + COULEUR_RESET),
    TACHE_AJOUT_CLOSED_ECHEC(COULEUR_ECHEC + "FAILURE : Wrong date format. (YYYY/MM/DD)." + COULEUR_RESET),
    TACHE_AJOUT_SCHEDULED_ECHEC(COULEUR_ECHEC + "FAILURE : Wrong date format. (YYYY/MM/DD)." + COULEUR_RESET),
    TACHE_STATE_UPDATE_ECHEC(COULEUR_ECHEC + "FAILURE : Unchanged State." + COULEUR_RESET),
    TACHE_STATE_SUIVANT_UPDATE_ECHEC(COULEUR_ECHEC + "FAILURE : Unchanged State." + COULEUR_RESET),
    TACHE_STATE_INTROUVABLE_ECHEC(COULEUR_ECHEC + "FAILURE : State does not exist." + COULEUR_RESET),
    TACHE_COUTS(COULEUR_SUCCES + "SUCCESS : Cost added" + COULEUR_RESET),
    TACHE_COUTF(COULEUR_ECHEC + "FAILURE : Cost not added" + COULEUR_RESET),
    TACHE_HELP("TASK COL ADD <numTask> <nameCol> -> add a collaborator to a task if the collaborator exist in the header\n" +
                "TASK COL DELETE <numTask> <nameCol> -> delete a collaborator to a task\n" +
                "TASK CLOCK <numTask> -> start/end the timer\n" +
                "TASK STATE <numTask> <state (TODO,ONGOING,DONE,CANCELLED)> -> add a state to a task\n" +
                "TASK STATE <numTask> <next> -> set a task to the next state\n" +
                "TASK PROP ADD <numTask> <nameProp> <value> -> add a property in a task\n" +
                "TASK PROP DELETE <numTask> <nameProp> -> delete a property in a task\n" +
                "TASK TAG ADD <numTask> <nameTag> -> add a tag in a task\n" +
                "TASK TAG DELETE <numTask> <nameTag> -> delete a tag in a task\n" +
                "TASK DL <numTask> <date (yyyy-MM-dd or yyyy/MM/dd)> -> add a deadline to the task\n" +
                "TASK CL <numTask> <date (yyyy-MM-dd or yyyy/MM/dd)> -> add a the date when the task was finished\n" +
                "TASK SD <numTask> <date (yyyy-MM-dd or yyyy/MM/dd)> -> add a scheduled date to the task\n" +
                "TASK RENAME <numTask> <newName> -> rename a task\n" +
                "TASK ADD <name> -> create a task\n" +
                "TASK ADD <name> <numTask> -> create a task with dependency to another task\n" +
                "TASK COST <numTask> <value> -> add a cost to a task\n" +
                "TASK DELETE <numTask> -> delete a task\n" +
                "TASK LIST -> llist all the task with their number\n" +
                "TASK DEP SET <numTask1> <numTask2> -> make task1 dependent to task2\n" +
                "TASK DEP DELETE <numTask> -> delete a dependency\n" +
                "TASK COL imp <google> <nameCol> -> import task from the list OrgPro in google calendar\n" +
                "TASK COL SEND <trello/google> <numTask> <nameCol> -> send a task to google calendar or trello\n" +
                "TASK COL SYNC <nameCol> optional:<ONGOING> -> send all the task of a collaborator to google calendar or only the ONGOING task\n"),

    BDD_COLLABORATEUR_NULL(COULEUR_ECHEC + "FAILURE : Collaborator does not exist." + COULEUR_RESET),
    BDD_SYNCHRO_NULL(COULEUR_ECHEC + "FAILURE : The task is not assigned to the specified collaborator." + COULEUR_RESET),
    BDD_ALL_SYNCHRO_NULL(COULEUR_ECHEC + "FAILURE : No task assigned to the specified collaborator." + COULEUR_RESET),

    TACHE_API_GOOGLE_AJOUT_LISTE_SUCCES(COULEUR_SUCCES + "SUCCESS : \n" + "The list was created on google task." + COULEUR_RESET),
    TACHE_API_GOOGLE_AJOUT_TACHE_SUCCES(COULEUR_SUCCES + "SUCCESS : \n" + "The task was created on google task\n." + COULEUR_RESET),
    TACHE_API_GOOGLE_UPDATE_TACHE_SUCCES(COULEUR_SUCCES + "SUCCESS : The task has been updated on google task." + COULEUR_RESET),
    TACHE_API_GOOGLE_LISTE_SUPPRIMEE_ECHEC(COULEUR_ECHEC + "FAILURE : \n" + "The list from google task no longer exists. Please restart the command to create a new list on google task." + COULEUR_RESET),
    TACHE_API_GOOGLE_TACHE_SUPPRIMEE_ECHEC(COULEUR_ECHEC + "FAILURE : The task from google task no longer exists. Please restart the command to create a new task on google task." + COULEUR_RESET),
    TACHE_API_GOOGLE_LISTE_VIDE("\n" + "The list on google task does not contain any tasks."),
    TACHE_API_GOOGLE_IMPORT_ALL("\n" + "All the stains were imported"),
    TACHE_API_GOOGLE_AUCUNE_TACHE_IMPORTABLE("\n" + "All the tasks in the list on google task are already on the local file."),
    TACHE_API_GOOGLE_AFFICHAGE_LISTE("\n" + "Addable task list: (Please select a number or enter all to import or type 'QUIT' to exit)"),
    TACHE_API_GOOGLE_NUMERO_LISTE_INCORRECT_ECHEC(COULEUR_ECHEC + "\n" + "The number shown is not valid, please try again." + COULEUR_RESET),
    TACHE_API_GOOGLE_ENTREE_UTILISATEUR_LISTE_INCORRECT_ECHEC(COULEUR_ECHEC + "\n" + "Please select a number or press 'QUIT' to exit" + COULEUR_RESET),
    TACHE_API_GOOGLE_AJOUT_TACHE_LOCAL_SUCCES(COULEUR_SUCCES + "\n" + "The task has been added to the local file." + COULEUR_RESET),
    TACHE_API_GOOGLE_LISTE_EXISTE("\n" + "The list already exists on google task."),
    TACHE_API_GOOGLE_AJOUT_NOUVELLE_LISTE_SUCCES(COULEUR_SUCCES + "SUCCESS : \n" + "The list no longer exists but a new one has been created on google task." + COULEUR_RESET),
    TACHE_API_GOOGLE_TAG_LIBELLE("Tag used : "),


    TACHE_API_TRELLO_AJOUT_BOARD_ECHEC("\n" + "Trello credentials are no longer valid."),
    TACHE_API_TRELLO_AJOUT_LISTE_ECHEC("Trello credentials are no longer valid or the targeted board has been removed."),
    TACHE_API_TRELLO_AJOUT_CARD_ECHEC("\n" + "Trello credentials are no longer valid or the targeted board has been removed."),
    TACHE_API_TRELLO_MODIFIER_CARD_ECHEC("\n" + "Trello credentials are no longer valid or the targeted card has been deleted."),
    TACHE_API_TRELLO_AJOUT_CARD_ARCHIVER_ECHEC("\n" + "The card has been archived."),

    TACHE_API_GOOGLE_UPDATE_ALL_TACHE_LOCAL_SUCCES(COULEUR_SUCCES + " \n" + " has been updated on google task." + COULEUR_RESET),
    TACHE_API_GOOGLE_NOUVEL_AJOUT_ALL_TACHE_LOCAL_SUCCES(COULEUR_SUCCES + " was removed from google task, so it was created again." + COULEUR_RESET),
    TACHE_API_GOOGLE_AJOUT_ALL_TACHE_LOCAL_SUCCES(COULEUR_SUCCES + " \n" + " was created on google task." + COULEUR_RESET),


    TACHE_API_AUCUNE_CONNEXION(COULEUR_ECHEC + "FAILURE : No connexion." + COULEUR_RESET),
    TACHE_API_ERREUR_INCONNUE(COULEUR_ECHEC + "FAILURE : \n" + " Unknown error." + COULEUR_RESET),


    //FICHIER_SAVE_SUCCES("SUCCESS : Saved file."),
    FICHIER_CREATION(COULEUR_SUCCES + "SUCCESS : File does not exist but it was created." + COULEUR_RESET),
    FICHIER_LOAD(COULEUR_SUCCES + "SUCCESS : Loaded file." + COULEUR_RESET),
    FICHIER_LISTE("List of file available :"),
    FICHIER_LISTE_VIDE("No file found."),
    FICHIER_HELP("FILE LIST -> list existing file\n" +
                "FILE SELECT <name of file> -> create/select a file to write in\n"),

    LIST_AUCUN_RESULTAT("No result."),
    LIST_STATE_TODO("TODO list"),
    LIST_STATE_ONGOING("ONGOING list"),
    LIST_STATE_DONE("DONE list"),
    LIST_STATE_CANCELLED("CANCELLED list"),
    LIST_HELP("LIST TASK -> displays the tasks of a file\n" +
    "LIST T <State> (TODO,ONGOING,DONE,CANCELLED) -> displays the tasks that correspond to the given state" +
    "LIST K/ LIST KANBAN -> displays all the task sort by state" +
    "LIST NOTSTARTED -> shows tasks that have been scheduled but are still in the todo list"),

    HEADER_HELP("HEADER COST <value> -> Add a general cost.\n" +
            "HEADER GET <key> -> Return the value of key.\n" +
            "HEADER ADD <key> <value> -> Add a new key/value in the header.\n" +
            "HEADER SET <key> <value> -> Changes the value related to the key in the header.\n" +
            "HEADER DELETE <key> -> Remove the key/value from the header."),
    HEADER_COSTS(COULEUR_SUCCES + "SUCCES : Iteration cost added" + COULEUR_RESET),
    HEADER_ADD_SUCCES(COULEUR_SUCCES + "SUCCESS : Header add." + COULEUR_RESET),
    HEADER_SET_SUCCES(COULEUR_SUCCES + "SUCCESS : Header modified." + COULEUR_RESET),
    HEADER_DELETE_SUCCES(COULEUR_SUCCES + "SUCCESS : Header deleted." + COULEUR_RESET),
    HEADER_GET_ECHEC("No result."),
    HEADER_ADD_ECHEC(COULEUR_ECHEC + "FAILURE : Key or Value cannot be empty and Key cannot be COST." + COULEUR_RESET),
    HEADER_DELETE_ECHEC(COULEUR_ECHEC + "FAILURE : Key or Value cannot be empty and Key cannot be COST." + COULEUR_RESET),
    HEADER_SET_ECHEC(COULEUR_ECHEC + "FAILURE : Key or Value cannot be empty and Key cannot be COST." + COULEUR_RESET),

    COST_ITE("Task list is empty"),
    COST_ITE_0("The cost of the tasks is equal to the cost of an iteration"),
    COST_ITE_SUP("the cost of the tasks is less than "),
    COST_ITE_INF("the cost of the tasks is higher than "),
    COST_HELP("COST ITE -> gives the difference between the cost of an iteration and the sum of the costs of ongoing tasks"),


    COLLABORATEUR_HELP("COL ADD <name> -> add a collaborator in the header\n" +
            "COL SET <old name> <new name> -> update the name of a collaborator\n" +
            "COL DELETE <name> -> delete a collaborator\n" +
            "COL LIST -> list all collaborator"),
    COLLABORATEUR_AJOUT_SUCCES(COULEUR_SUCCES + "SUCCESS : Collaborator add." + COULEUR_RESET),
    COLLABORATEUR_SET_SUCCES(COULEUR_SUCCES + "SUCCESS : Collaborator modified." + COULEUR_RESET),
    COLLABORATEUR_DELETE_SUCCES(COULEUR_SUCCES + "SUCCESS : Collaborator deleted." + COULEUR_RESET),
    COLLABORATEUR_AJOUT_DOSSIER_GOOGLE_SUCCES(COULEUR_SUCCES + "SUCCESS : Collaborator directory for google connection created" + COULEUR_RESET),
    COLLABORATEUR_AJOUT_ECHEC(COULEUR_ECHEC + "FAILURE : Collaborator cannot be add. Collaborator's name cannot contains \":\"" + COULEUR_RESET),
    COLLABORATEUR_SET_ECHEC(COULEUR_ECHEC + "FAILURE : Collaborator cannot be modified. Collaborator's name cannot contains \":\"" + COULEUR_RESET),
    COLLABORATEUR_DELETE_ECHEC(COULEUR_ECHEC + "FAILURE : Collaborator cannot be deleted. Collaborator's name cannot contains \":\"" + COULEUR_RESET),
    COLLABORATEUR_AJOUT_DOSSIER_GOOGLE_ALREADY_EXIST(COULEUR_SUCCES + "INFO : Collaborator directory for google already exist therefore was not created"+ COULEUR_RESET),
    COLLABORATEUR_DOSSIER_EXISTE_DEJA_ECHEC(COULEUR_ECHEC + "FAILURE : The directory already exist" + COULEUR_RESET),
    COLLABORATEUR_AJOUT_DOSSIER_GOOGLE_FAILURE(COULEUR_ECHEC + "FAILURE : Collaborator directory for google connection was not created" + COULEUR_RESET),
    COLLABORATEUR_SUPPRESSION_DOSSIER_FAILURE(COULEUR_ECHEC + "FAILURE : The directory could not be deleted : " + COULEUR_RESET),
    COLLABORATEUR_SUPPRESSION_FICHIER_FAILURE(COULEUR_ECHEC + "FAILURE : The file could not be deleted : " + COULEUR_RESET),
    COLLABORATEUR_LISTE_VIDE(COULEUR_ECHEC + "INFO : No colaborator found in the file" + COULEUR_RESET),
    COLLABORATEUR_NON_TROUVE(COULEUR_ECHEC + "ERROR : No colaborateur with this name found" + COULEUR_RESET),
    COLLABORATEUR_SANS_TRELLO_BOARD(COULEUR_ECHEC + "INFO : Collaborator without trelloboard" + COULEUR_RESET),
    COLLABORATEUR_AVEC_TRELLO_BOARD(COULEUR_SUCCES+ "INFO : Collaborator already have trelloboard" + COULEUR_RESET),
    COLLABORATEUR_SANS_TRELLO_LIST(COULEUR_ECHEC + "INFO : Collaborator without trelloList" + COULEUR_RESET),
    COLLABORATEUR_AVEC_TRELLO_LIST(COULEUR_SUCCES+ "INFO : Collaborator already have trellolist" + COULEUR_RESET),
    COLLABORATEUR_GENERATION_TRELLO_BOARD_SUCCESS(COULEUR_SUCCES + "SUCCESS : Board created on trello" + COULEUR_RESET),
    COLLABORATEUR_GENERATION_TRELLO_BOARD_FAILURE(COULEUR_ECHEC + "FAILURE : Board could not be created" + COULEUR_RESET),
    COLLABORATEUR_GENERATION_TRELLO_LIST_SUCCESS(COULEUR_SUCCES + "SUCCESS : List created on trello" + COULEUR_RESET),
    COLLABORATEUR_GENERATION_TRELLO_LIST_FAILURE(COULEUR_ECHEC + "FAILURE : List could not be created" + COULEUR_RESET),
    COLLABORATEUR_SYNC_TRELLO_SUCCESS(COULEUR_SUCCES + "SUCCESS : Task(s) added to trello" + COULEUR_RESET),
    COLLABORATEUR_SYNC_TRELLO_FAILURE(COULEUR_ECHEC + "FAILURE : Task(s) could not be added to trello" + COULEUR_RESET),
    COLLABORATEUR_TASK_TRELLO_SEND_SUCCESS(COULEUR_SUCCES + "SUCCESS : Task send to trello" + COULEUR_RESET),
    COLLABORATEUR_TASK_TRELLO_SEND_UPDATE_SUCCESS(COULEUR_SUCCES + "SUCCESS : Task updated to trello" + COULEUR_RESET),
    COLLABORATEUR_TASK_TRELLO_SEND_FAILURE(COULEUR_ECHEC + "FAILURE : Task could not be send to trello" + COULEUR_RESET),
    COLLABORATEUR_UPDATED(COULEUR_SUCCES + "SUCCESS : Collaborator updated" + COULEUR_RESET),
    COLLABORATEUR_NO_TRELLLO_CREDENTIALS(COULEUR_ECHEC + "FAILURE : Collaborator has no trello credentials" + COULEUR_RESET),
    COLLABORATEUR_NO_TRELLLO_CREDENTIALS_ADD_APIKEY(COULEUR_SUCCES + "Please input your API KEY for trello" + COULEUR_RESET),
    COLLABORATEUR_NO_TRELLLO_CREDENTIALS_ADD_TOKEN(COULEUR_SUCCES + "Please input your TOKEN for trello" + COULEUR_RESET),
    COLLABORATEUR_TASK_PAS_A_JOUR_1(COULEUR_ECHEC + "Collaborateur : " + COULEUR_RESET),
    COLLABORATEUR_TASK_PAS_A_JOUR_2(COULEUR_ECHEC + "Is not synchronized for task : " + COULEUR_RESET),
    COLLABORATEUR_TASK_PAS_A_JOUR_3(COULEUR_ECHEC + "Update local : 0, Update Collaborator : 1, Quit : q" + COULEUR_RESET),
    COLLABORATEUR_TASK_PAS_A_JOUR_4(COULEUR_ECHEC + "Done : 0, Cancelled  : 1, Quit : q" + COULEUR_RESET),
    COLLABORATEUR_TASK_PAS_A_JOUR_5(COULEUR_SUCCES + "Local already synchronized" + COULEUR_RESET),
    RETOUR_ON_GOING_IMPOSSIBLE(COULEUR_ECHEC + "ERROR : Impossible to go back from done" + COULEUR_RESET),
    NOT_SENDABLE(COULEUR_ECHEC + "FAILURE : datasource not found for : " + COULEUR_RESET),

    SPRINT_GET_NUM("\n" + "You are in sprint number "),
    SPRINT_GET_DATE_SUCCES(COULEUR_SUCCES + "\n" + "The current sprint deadline is " + COULEUR_RESET),
    SPRINT_GET_DATE_ECHEC(COULEUR_ECHEC + "\n" + "The sprint deadline was not fixed " + COULEUR_RESET),
    SPRINT_NEXT(COULEUR_SUCCES + "You have just switched to sprint number " + COULEUR_RESET),
    SPRINT_DATE_SUCCES(COULEUR_SUCCES + "The sprint deadline has been fixed " + COULEUR_RESET),
    SPRINT_DATE_ECHEC(COULEUR_ECHEC + "The deadline is in the wrong format or the date has passed " + COULEUR_RESET),
    SPRINT_TASK_NUM_SUCCES(COULEUR_SUCCES + "\n" + "The stain has been well marked " + COULEUR_RESET),
    SPRINT_TASK_DATE_SUCCES(COULEUR_SUCCES + "\n" + "The stain has been well marked " + COULEUR_RESET),
    SPRINT_TASK_DATE_ECHEC(COULEUR_ECHEC + "\n" + "The sprint deadline was not fixed " + COULEUR_RESET),
    SPRINT_TASK_DELNUM_SUCCES(COULEUR_SUCCES + "\n" + "The sprint tag has been deleted " + COULEUR_RESET),
    SPRINT_TASK_ALL_SUCCES(COULEUR_SUCCES + "\n" + "The stain has been well marked " + COULEUR_RESET),
    SPRINT_TASK_ALL_ECHEC(COULEUR_ECHEC + "\n" + "The sprint deadline was not fixed " + COULEUR_RESET),
    SPRINT_TASK_NEXT_SUCCES(COULEUR_SUCCES + "The task has changed state " + COULEUR_RESET),
    SPRINT_TASK_NEXT_ECHEC_0(COULEUR_ECHEC + "\n" + "The number of the task is not in the list " + COULEUR_RESET),
    SPRINT_TASK_NEXT_ECHEC_2(COULEUR_ECHEC + "\n" + "One of the sub stains does not allow the change of state " + COULEUR_RESET),
    SPRINT_TASK_NEXT_ECHEC_3(COULEUR_ECHEC + "\n" + "The task can not change state " + COULEUR_RESET),
    SPRINT_HELP("SPRINT GET NUM -> Give the number of the actual sprint\n" +
                "SPRINT GET DATE -> Give the deadline of the actual sprint\n" +
                "SPRINT NEXT -> Increment the number of the sprint\n" +
                "SPRINT DATE <YYYY-MM-DD> -> set the deadline of the actual sprint\n" +
                "SPRINT TASK NUM <numTask> -> Add a task to the actual sprint\n" +
                "SPRINT TASK DELNUM <numTask> -> Delete a task of the actual sprint\n" +
                "SPRINT TASK DATE <numTask> -> Add the deadline of the sprint to a task\n" +
                "SPRINT TASK ALL <numTask> -> Add a task to a sprint and add the deadline of the sprint to the task\n" +
                "SPRINT TASK NEXT <numTask> -> Set the state of the task to the next logical state if it is possible\n" +
                "SPRINT COST <value> -> Set cost of a sprint"),

    TAG_FUNC_SUCCES(COULEUR_SUCCES + "SUCCESS : Task added as fonctional." + COULEUR_RESET),
    TAG_TECH_SUCCES(COULEUR_SUCCES + "SUCCESS : Task added as technical." + COULEUR_RESET),
    TAG_FUNC_ECHEC(COULEUR_ECHEC + "FAILURE : Task cannot be fonctional. Collaborator's name cannot contains \":\"" + COULEUR_RESET),
    TAG_TECH_ECHEC(COULEUR_ECHEC + "FAILURE : Task cannot be technical. Collaborator's name cannot contains \":\"" + COULEUR_RESET),
    TAG_HELP("TAG TECH <numTask> -> Tag a task to technical\n" +
            "TAG FUNC <numTask> -> Tag a task to functional"),

    ;

    private String message;

    Message(String message){
        this.message = message;
    }

    public String toString(){
        return message;
    }
}
