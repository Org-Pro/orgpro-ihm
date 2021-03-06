package fr.orgpro.ihm.project;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.tasks.model.Task;
import fr.orgpro.api.local.SQLiteConnection;
import fr.orgpro.api.local.SQLiteDataBase;
import fr.orgpro.api.local.models.SQLCollaborateur;
import fr.orgpro.api.local.models.SQLSynchro;
import fr.orgpro.api.project.State;
import fr.orgpro.api.project.Tache;
import fr.orgpro.api.remote.google.GoogleList;
import fr.orgpro.api.remote.google.GoogleStateEnum;
import fr.orgpro.api.remote.google.GoogleTasksService;
import fr.orgpro.api.scrum.Scrum;
import fr.orgpro.ihm.service.CollaborateurService;
import fr.orgpro.ihm.service.CredentialService;


import java.io.*;
import java.net.UnknownHostException;
import java.util.*;

public class Commande {
    private static final String trello = "trello";
    private static final String google = "google";

    private static final String fs = File.separator;
    private static final String PATH = "src" + fs + "main" + fs + "resources" + fs;
    private static final String PATH_TOKEN = "tokens" + fs;
    private static final GoogleList gl = GoogleList.getInstance();
    private static final CollaborateurService cls = CollaborateurService.getInstance();
    private static final GoogleIhm colIhm = GoogleIhm.getInstance();
    private static final CredentialService cdls = CredentialService.getInstance();
    private static final TrelloIhm tihm = TrelloIhm.getInstance();
    public static void commandeTache(String[] args, Data data){
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){

            case "collaborator" : {}
            case "col" : {
                if (verifBadNbArgument(3, args)) {
                    return;
                }
                switch (args[2].toLowerCase()) {
                    // TASK COL ADD <num> <col>
                    case "add": {
                        if (verifBadNbArgument(5, args) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if (verifTacheNotExiste(numTache, data)) {
                            return;
                        }
                        if(data.getListeTache().get(numTache).addCollaborateur(args[4])){
                            SQLSynchro sync = new SQLSynchro(data.getListeTache().get(numTache).getId(), args[4].toLowerCase().trim());
                            SQLiteDataBase.addSynchroTacheCollaborateur(sync);
                            //SQLiteDataBase.synchroAddTacheCollaborateur(data.getListeTache().get(numTache), args[4].toLowerCase().trim(), null, false);
                            SQLiteConnection.closeConnection();

                            data.ecritureListeTaches();
                            System.out.println(Message.TACHE_AJOUT_COLLABORATEUR_SUCCES);
                        }else{
                            System.out.println(Message.TACHE_AJOUT_COLLABORATEUR_ECHEC);
                        }
                        break;
                    }

                    case "delete": {
                        // TASK COL DELETE <num> <col>
                        if (verifBadNbArgument(5, args) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if (verifTacheNotExiste(numTache, data)) {
                            return;
                        }
                        if(data.getListeTache().get(numTache).removeCollaborateur(args[4])){
                            SQLSynchro sync = new SQLSynchro(data.getListeTache().get(numTache).getId(), args[4].toLowerCase().trim());
                            SQLiteDataBase.deleteSynchroTacheCollaborateur(sync);
                            //SQLiteDataBase.synchroDeleteTacheCollaborateur(data.getListeTache().get(numTache), args[4].toLowerCase().trim());
                            SQLiteConnection.closeConnection();

                            data.ecritureListeTaches();
                            System.out.println(Message.TACHE_DELETE_COLLABORATEUR_SUCCES);
                        }else{
                            System.out.println(Message.TACHE_DELETE_COLLABORATEUR_ECHEC);
                        }
                        break;
                    }

                    case "list": {
                        // TASK COL LIST ...
                        if (verifBadNbArgument(4, args)){
                            return;
                        }
                        switch (args[3].toLowerCase()) {
                            case "add": {
                                // TASK COL LIST ADD <google/trello> <col>
                                if (verifBadNbArgument(6, args) || verifBadLectureFichier(data)) {
                                    return;
                                }
                                // Récupère les infos du collaborateur
                                SQLCollaborateur col = SQLiteDataBase.getCollaborateur(args[5].trim());
                                if(col == null) {
                                    System.out.println(Message.BDD_COLLABORATEUR_NULL);
                                    return;
                                }
                                // Pour google
                                if (args[4].equalsIgnoreCase(google)) {
                                    if (!cdls.verifCredentialExist(args[5])) {
                                        return;
                                    }

                                    // Si la liste n'existe pas sur google task
                                    if (col.getGoogle_id_liste() == null) {
                                        try {
                                            // Création de la liste sur google task
                                            col.setGoogle_id_liste(gl.insertList(col.getPseudo(), getProjectName(data)).getId());
                                            SQLiteDataBase.updateCollaborateur(col);
                                            System.out.println(Message.TACHE_API_GOOGLE_AJOUT_LISTE_SUCCES);
                                            return;
                                        }catch (UnknownHostException e){
                                            SQLiteConnection.closeConnection();
                                            System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                                            return;
                                        } catch (IOException e) {
                                            SQLiteConnection.closeConnection();
                                            System.out.println(Message.TACHE_API_ERREUR_INCONNUE);
                                            return;
                                        }
                                    }else{
                                        try {
                                            gl.getList(col.getGoogle_id_liste(), col.getPseudo());
                                            SQLiteConnection.closeConnection();
                                            System.out.println(Message.TACHE_API_GOOGLE_LISTE_EXISTE);
                                            return;
                                        } catch (GoogleJsonResponseException e) {
                                            try {
                                                col.setGoogle_id_liste(gl.insertList(col.getPseudo(),getProjectName(data)).getId());
                                                SQLiteDataBase.updateCollaborateur(col);

                                                SQLiteDataBase.updateAllSynchroGoogleIdTacheNullByCollaborateur(col);
                                                SQLiteDataBase.updateAllSynchroGoogleEstSynchroByCollaborateur(col, false);

                                                SQLiteConnection.closeConnection();
                                                System.out.println(Message.TACHE_API_GOOGLE_AJOUT_NOUVELLE_LISTE_SUCCES);
                                                return;
                                            } catch (IOException e1) {
                                                SQLiteConnection.closeConnection();
                                                System.out.println(Message.TACHE_API_ERREUR_INCONNUE);
                                                return;
                                            }
                                        }catch (UnknownHostException e){
                                            SQLiteConnection.closeConnection();
                                            System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                                            return;
                                        }catch (IOException e) {
                                            SQLiteConnection.closeConnection();
                                            System.out.println(Message.TACHE_API_ERREUR_INCONNUE);
                                            return;
                                        }


                                    }
                                }
                            }
                            break;
                        }
                        break;
                    }

                    // TASK COL imp <trello/google> <col>
                    case "imp": {}
                    case "import": {
                        if (verifBadNbArgument(5, args) || verifBadLectureFichier(data)) {
                            return;
                        }
                        // Récupère les infos du collaborateur
                        SQLCollaborateur col = SQLiteDataBase.getCollaborateur(args[4].trim());
                        if(col == null) {
                            System.out.println(Message.BDD_COLLABORATEUR_NULL);
                            return;
                        }
                        // Pour google
                        if (args[3].equalsIgnoreCase(google)) {
                            if (!cdls.verifCredentialExist(args[4])) {
                                return;
                            }
                            // Si la liste n'existe pas sur google task
                            if(col.getGoogle_id_liste() == null){
                                try {
                                    // Création de la liste sur google task
                                    col.setGoogle_id_liste(gl.insertList(col.getPseudo(), getProjectName(data)).getId());
                                    SQLiteDataBase.updateCollaborateur(col);
                                    System.out.println(Message.TACHE_API_GOOGLE_AJOUT_LISTE_SUCCES);
                                    System.out.println(Message.TACHE_API_GOOGLE_LISTE_VIDE);
                                    return;
                                }catch (UnknownHostException e){
                                    SQLiteConnection.closeConnection();
                                    System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                                    return;
                                } catch (IOException e) {
                                    SQLiteConnection.closeConnection();
                                    System.out.println(Message.TACHE_API_ERREUR_INCONNUE);
                                    return;
                                }
                            }

                            try {
                                // Récupère les tâches de google task selon le collaborateur
                                List<Task> allTask = gl.getAllTask(col.getGoogle_id_liste(), col.getPseudo());
                                if(allTask == null || allTask.size() == 0){
                                    System.out.println(Message.TACHE_API_GOOGLE_LISTE_VIDE);
                                    return;
                                }
                                // Récupère toutes les liaisons entre les tâches et le collaborateur
                                List<SQLSynchro> listeSynchro = SQLiteDataBase.getAllSynchroByCollaborateur(col.getPseudo());
                                List<Task> listeTaskTemp = new ArrayList<Task>();
                                boolean tacheExiste;
                                // Pour chaque tâche de google task
                                for(Task task : allTask){
                                    tacheExiste = false;
                                    // Pour chaque liaison entre tâche et collaborateur déjà existante
                                    for (SQLSynchro synchro : listeSynchro){
                                        // Si la tâche de google task existe déjà en local
                                        if(synchro.getGoogle_id_tache() != null && synchro.getGoogle_id_tache().equals(task.getId())){
                                            // Empêche l'ajout de la tâche dans la liste temporaire
                                            tacheExiste = true;
                                            break;
                                        }
                                    }
                                    // Si la tâche de google task n'existe pas en local
                                    if(!tacheExiste){
                                        // Ajoute la tâche dans une liste temporaire
                                        listeTaskTemp.add(task);
                                    }
                                }

                                // Vérifie si la liste temporaire est vide
                                if (listeTaskTemp.size() == 0){
                                    System.out.println(Message.TACHE_API_GOOGLE_AUCUNE_TACHE_IMPORTABLE);
                                    return;
                                }

                                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                                String lecture;
                                boolean quitter = false;
                                int i = 0;
                                Tache tache;
                                SQLSynchro synchro;
                                Task taskTemp;
                                String[] date;
                                String dateAffichage;

                                // Affiche les tâches que l'utilisateur peut ajouter
                                System.out.println(Message.TACHE_API_GOOGLE_AFFICHAGE_LISTE);
                                for (Task task : listeTaskTemp){
                                    if(task.getDue() != null){
                                        date = task.getDue().toString().split("T");
                                        date = date[0].split("-");
                                        dateAffichage = " ( DEADLINE : " + date[2] + "/" + date[1] + "/" + date[0] + " )";
                                    }else{
                                        dateAffichage = "";
                                    }
                                    System.out.println("n°" + i + " : " + task.getTitle() + dateAffichage);
                                    i++;
                                }

                                while(!quitter){
                                    lecture = bufferRead.readLine();
                                    // Si l'utilisateur ne veut plus ajouter de tâche en local
                                    if(lecture.equalsIgnoreCase("quit")){
                                        // Stop la boucle
                                        quitter = true;
                                    }else if(lecture.equalsIgnoreCase("all")) {

                                        for(Task task : listeTaskTemp){
                                            taskTemp = task;
                                            tache = new Tache(taskTemp.getTitle());
                                            tache.addCollaborateur(col.getPseudo());
                                            if(taskTemp.getDue() != null){
                                                date = taskTemp.getDue().toString().split("T");
                                                date = date[0].split("-");
                                                tache.addDateLimite(date[0] + "-" + date[1] + "-" + date[2]);
                                            }
                                            data.getListeTache().add(tache);
                                            SQLiteDataBase.addTache(tache);
                                            synchro = new SQLSynchro(tache.getId(), col.getPseudo());
                                            synchro.setGoogle_est_synchro(true);
                                            synchro.setGoogle_id_tache(taskTemp.getId());
                                            SQLiteDataBase.addSynchroTacheCollaborateur(synchro);
                                            data.ecritureListeTaches();
                                        }
                                        System.out.println(Message.TACHE_API_GOOGLE_IMPORT_ALL);
                                        break;
                                    }else
                                    {
                                        // Sinon
                                        try {
                                            // Parse et vérifie que l'entrée de l'utilisateur est bien un nombre
                                            i = Integer.parseInt(lecture);
                                            // Vérifie que le numéro la tâche existe dans la liste temporaire
                                            if (i < 0 || i >= listeTaskTemp.size()){
                                                System.out.println(Message.TACHE_API_GOOGLE_NUMERO_LISTE_INCORRECT_ECHEC);
                                                continue;
                                            }

                                            System.out.println(Message.TACHE_API_GOOGLE_AJOUT_TACHE_LOCAL_SUCCES);

                                            // Création de la tâche en local + Création de la liaison entre la tâche et le collaborateur
                                            taskTemp = listeTaskTemp.get(i);
                                            tache = new Tache(taskTemp.getTitle());
                                            tache.addCollaborateur(col.getPseudo());
                                            if(taskTemp.getDue() != null){
                                                date = taskTemp.getDue().toString().split("T");
                                                date = date[0].split("-");
                                                tache.addDateLimite(date[0] + "-" + date[1] + "-" + date[2]);
                                            }
                                            data.getListeTache().add(tache);
                                            SQLiteDataBase.addTache(tache);
                                            synchro = new SQLSynchro(tache.getId(), col.getPseudo());
                                            synchro.setGoogle_est_synchro(true);
                                            synchro.setGoogle_id_tache(taskTemp.getId());
                                            SQLiteDataBase.addSynchroTacheCollaborateur(synchro);
                                            data.ecritureListeTaches();

                                            // Enlève la tâche de la liste temporaire
                                            listeTaskTemp.remove(i);

                                            // Vérifie si la liste temporaire est vide
                                            if (listeTaskTemp.size() == 0){
                                                SQLiteConnection.closeConnection();
                                                System.out.println(Message.TACHE_API_GOOGLE_AUCUNE_TACHE_IMPORTABLE);
                                                return;
                                            }

                                            // Affiche les tâches restantes que l'utilisateur peut ajouter
                                            System.out.println(Message.TACHE_API_GOOGLE_AFFICHAGE_LISTE);
                                            i = 0;
                                            for (Task task : listeTaskTemp){
                                                if(task.getDue() != null){
                                                    date = task.getDue().toString().split("T");
                                                    date = date[0].split("-");
                                                    dateAffichage = " ( DEADLINE : " + date[2] + "/" + date[1] + "/" + date[0] + " )";
                                                }else{
                                                    dateAffichage = "";
                                                }
                                                System.out.println("n°" + i + " : " + task.getTitle() + dateAffichage);
                                                i++;
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println(Message.TACHE_API_GOOGLE_ENTREE_UTILISATEUR_LISTE_INCORRECT_ECHEC);
                                        }
                                    }
                                }

                            } catch (GoogleJsonResponseException e) {
                                col.setGoogle_id_liste(null);
                                SQLiteDataBase.updateCollaborateur(col);
                                SQLiteDataBase.updateAllSynchroGoogleIdTacheNullByCollaborateur(col);
                                SQLiteDataBase.updateAllSynchroGoogleEstSynchroByCollaborateur(col, false);
                                SQLiteConnection.closeConnection();
                                System.out.println(Message.TACHE_API_GOOGLE_LISTE_SUPPRIMEE_ECHEC);
                                return;
                            }catch (UnknownHostException e){
                                SQLiteConnection.closeConnection();
                                System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                                return;
                            }catch (IOException e) {
                                SQLiteConnection.closeConnection();
                                System.out.println(Message.TACHE_API_ERREUR_INCONNUE);
                                return;
                            }

                        }
                        SQLiteConnection.closeConnection();
                        break;
                    }

                    // TASK COL SEND <trello/google> (<numTask> <col>) ou (ALL <col>) ou (<tag> <col>)
                    case "send": {
                        if (verifBadNbArgument(6, args) || verifBadLectureFichier(data)) {
                            return;
                        }

                        // Récupère les infos du collaborateur
                        SQLCollaborateur col = SQLiteDataBase.getCollaborateur(args[5].trim());
                        if (col == null) {
                            System.out.println(Message.BDD_COLLABORATEUR_NULL);
                            return;
                        }

                        Tache tache = null;
                        boolean estUnNombre;

                        try {
                            Integer.parseInt(args[4]);
                            estUnNombre = true;
                        } catch (NumberFormatException e) {
                            estUnNombre = false;
                        }

                        // ------------------------------------ ALL / TAG ---------------------------------------
                        // Dans le cas où l'on envoie toutes les tâches d'un collaborateur
                        if(!estUnNombre) {
                            if (args[3].equalsIgnoreCase(google)) {
                                if (!cdls.verifCredentialExist(args[5])) {
                                    return;
                                }

                                // Si la liste n'existe pas sur google task
                                if(col.getGoogle_id_liste() == null){
                                    try {
                                        // Création de la liste sur google task
                                        col.setGoogle_id_liste(gl.insertList(col.getPseudo(), getProjectName(data)).getId());
                                        SQLiteDataBase.updateCollaborateur(col);
                                        System.out.println(Message.TACHE_API_GOOGLE_AJOUT_LISTE_SUCCES);
                                    }catch (UnknownHostException e){
                                        SQLiteConnection.closeConnection();
                                        System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                                        return;
                                    } catch (IOException e) {
                                        SQLiteConnection.closeConnection();
                                        System.out.println(Message.TACHE_API_ERREUR_INCONNUE);
                                        return;
                                    }
                                }

                                List<SQLSynchro> listeSynchro = SQLiteDataBase.getAllSynchroByCollaborateur(col.getPseudo());
                                if (listeSynchro.size() == 0){
                                    System.out.println(Message.BDD_ALL_SYNCHRO_NULL);
                                    return;
                                }

                                boolean all = true;
                                boolean tagExiste;

                                if(!args[4].equalsIgnoreCase("all")){
                                    all = false;
                                    System.out.println(Message.TACHE_API_GOOGLE_TAG_LIBELLE + args[4]);
                                }

                                for (SQLSynchro synchro : listeSynchro) {
                                    if(!synchro.isGoogle_est_synchro()){
                                        tache = data.getTacheByUuid(synchro.getUuid_tache());
                                        if(tache == null){
                                            continue;
                                        }
                                        // Si ce n'est pas "ALL"
                                        if(!all){
                                            // On vérifie le TAG
                                            tagExiste = false;
                                            for(String tag : tache.getTagListe()){
                                                if(tag.equalsIgnoreCase(args[4])){
                                                    tagExiste = true;
                                                    break;
                                                }
                                            }
                                            // Si pas de tag valide
                                            if(!tagExiste){
                                                continue;
                                            }
                                        }

                                        try {
                                            // Si la tâche existe sur google task
                                            if(synchro.getGoogle_id_tache() != null) {
                                                Task t = gl.updateTask(tache, synchro.getGoogle_id_tache(), col.getGoogle_id_liste(), col.getPseudo(), null);
                                                // Si la tâche a été supprimée de google task
                                                if(t.getDeleted() != null && t.getDeleted() ){
                                                    synchro.setGoogle_est_synchro(false);
                                                    synchro.setGoogle_id_tache(null);
                                                    SQLiteDataBase.updateSynchroTacheCollaborateur(synchro);
                                                    // Création de la tâche sur google task
                                                    t = gl.insertTask(tache, col.getGoogle_id_liste(), col.getPseudo());
                                                    synchro.setGoogle_id_tache(t.getId());
                                                    synchro.setGoogle_est_synchro(true);
                                                    SQLiteDataBase.updateSynchroTacheCollaborateur(synchro);
                                                    System.out.println(Message.TACHE_LIBELLE + "'" + tache.getTitre() + "'" + Message.TACHE_API_GOOGLE_NOUVEL_AJOUT_ALL_TACHE_LOCAL_SUCCES);

                                                    // Sinon
                                                }else{
                                                    // Mise à jour de la tâche sur google task
                                                    synchro.setGoogle_est_synchro(true);
                                                    SQLiteDataBase.updateSynchroTacheCollaborateur(synchro);
                                                    System.out.println(Message.TACHE_LIBELLE + "'" + tache.getTitre() + "'" + Message.TACHE_API_GOOGLE_UPDATE_ALL_TACHE_LOCAL_SUCCES);
                                                }
                                                // Sinon
                                            }else{
                                                // Création de la tâche sur google task
                                                Task t = gl.insertTask(tache, col.getGoogle_id_liste(), col.getPseudo());
                                                synchro.setGoogle_id_tache(t.getId());
                                                synchro.setGoogle_est_synchro(true);
                                                SQLiteDataBase.updateSynchroTacheCollaborateur(synchro);
                                                System.out.println( Message.TACHE_LIBELLE + "'" + tache.getTitre() + "'" + Message.TACHE_API_GOOGLE_AJOUT_ALL_TACHE_LOCAL_SUCCES);
                                            }
                                        } catch (GoogleJsonResponseException e) {
                                            col.setGoogle_id_liste(null);
                                            SQLiteDataBase.updateCollaborateur(col);
                                            SQLiteDataBase.updateAllSynchroGoogleIdTacheNullByCollaborateur(col);
                                            SQLiteDataBase.updateAllSynchroGoogleEstSynchroByCollaborateur(col, false);
                                            SQLiteConnection.closeConnection();
                                            System.out.println(Message.TACHE_API_GOOGLE_LISTE_SUPPRIMEE_ECHEC);
                                            return;
                                        }catch (UnknownHostException e){
                                            SQLiteConnection.closeConnection();
                                            System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                                            return;
                                        }catch (IOException e) {
                                            SQLiteConnection.closeConnection();
                                            System.out.println(Message.TACHE_API_ERREUR_INCONNUE);
                                            return;
                                        }
                                    }
                                }
                            }else{
                                System.out.println(Message.NOT_SENDABLE + args[3]);
                                return; // TODO trello ?
                            }
                            System.out.println("Toutes les tâches ont été synchronisées.");
                            SQLiteConnection.closeConnection();
                            return;
                        }
                        // ---------------------------------------------------------------------------

                        /*if (verifArgNotNombre(args[4])) {
                            return;
                        }*/

                        int numTache = Integer.parseInt(args[4]);
                        if (verifTacheNotExiste(numTache, data)) {
                            return;
                        }
                        tache = data.getListeTache().get(numTache);

                        // Pour google
                        if (args[3].equalsIgnoreCase(google)) {
                            if (!cdls.verifCredentialExist(args[5])) {
                                return;
                            }
                            // Si la liste n'existe pas sur google task
                            if(col.getGoogle_id_liste() == null){
                                try {
                                    // Création de la liste sur google task
                                    col.setGoogle_id_liste(gl.insertList(col.getPseudo(), getProjectName(data)).getId());
                                    SQLiteDataBase.updateCollaborateur(col);
                                    System.out.println(Message.TACHE_API_GOOGLE_AJOUT_LISTE_SUCCES);
                                }catch (UnknownHostException e){
                                    SQLiteConnection.closeConnection();
                                    System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                                    return;
                                } catch (IOException e) {
                                    SQLiteConnection.closeConnection();
                                    System.out.println(Message.TACHE_API_ERREUR_INCONNUE);
                                    return;
                                }
                            }

                            // Récupère la liaison entre la tâche et le collaborateur
                            SQLSynchro synchro = SQLiteDataBase.getSynchroTacheCollaborateur(col, tache);
                            if (synchro == null){
                                System.out.println(Message.BDD_SYNCHRO_NULL);
                                return;
                            }

                            try {
                                // Si la tâche existe sur google task
                                if(synchro.getGoogle_id_tache() != null) {
                                    Task t = gl.updateTask(tache, synchro.getGoogle_id_tache(), col.getGoogle_id_liste(), col.getPseudo(), null);
                                    // Si la tâche a été supprimée de google task
                                    if(t.getDeleted() != null && t.getDeleted() ){
                                        synchro.setGoogle_est_synchro(false);
                                        synchro.setGoogle_id_tache(null);
                                        SQLiteDataBase.updateSynchroTacheCollaborateur(synchro);
                                        SQLiteConnection.closeConnection();
                                        System.out.println(Message.TACHE_API_GOOGLE_TACHE_SUPPRIMEE_ECHEC);
                                        return;
                                        // Sinon
                                    }else{
                                        // Mise à jour de la tâche sur google task
                                        synchro.setGoogle_est_synchro(true);
                                        SQLiteDataBase.updateSynchroTacheCollaborateur(synchro);
                                        System.out.println(Message.TACHE_API_GOOGLE_UPDATE_TACHE_SUCCES);
                                    }
                                    // Sinon
                                }else{
                                    // Création de la tâche sur google task
                                    Task t = gl.insertTask(tache, col.getGoogle_id_liste(), col.getPseudo());
                                    synchro.setGoogle_id_tache(t.getId());
                                    synchro.setGoogle_est_synchro(true);
                                    SQLiteDataBase.updateSynchroTacheCollaborateur(synchro);
                                    System.out.println(Message.TACHE_API_GOOGLE_AJOUT_TACHE_SUCCES);
                                }
                            } catch (GoogleJsonResponseException e) {
                                col.setGoogle_id_liste(null);
                                SQLiteDataBase.updateCollaborateur(col);
                                SQLiteDataBase.updateAllSynchroGoogleIdTacheNullByCollaborateur(col);
                                SQLiteDataBase.updateAllSynchroGoogleEstSynchroByCollaborateur(col, false);
                                SQLiteConnection.closeConnection();
                                System.out.println(Message.TACHE_API_GOOGLE_LISTE_SUPPRIMEE_ECHEC);
                                return;
                            }catch (UnknownHostException e){
                                SQLiteConnection.closeConnection();
                                System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                                return;
                            }catch (IOException e) {
                                SQLiteConnection.closeConnection();
                                System.out.println(Message.TACHE_API_ERREUR_INCONNUE);
                                return;
                            }
                        } else if (args[3].equalsIgnoreCase(trello)) {
                            tihm.send(col, tache);
                        } else {
                            System.out.println(Message.NOT_SENDABLE + args[3]);
                        }
                        SQLiteConnection.closeConnection();

                        break;
                    }

                    // TASK COL SYNC <collaboName> optionel:<ONGOING>
                    case "sync": {
                        if(verifBadLectureFichier(data) && !cdls.verifCredentialExist(args[3])) {
                            return;
                        }
                        String name = args[3];
                        SQLCollaborateur col = SQLiteDataBase.getCollaborateur(name);
                        if(!cls.verifColaborateurExist(name)) {
                            return;
                        }
                        if (col == null) {
                            return;
                        }
                        if (args.length == 5) {
                            State state = State.stringIsState(args[3]);
                            if (state != null) {
                                colIhm.syncStatusTaskUser(col, data, state);
                            } else {
                                System.out.println(Message.ARGUMENT_INVALIDE);
                            }
                        } else if (args.length == 4) {
                            colIhm.syncUser(col, data);
                        } else {
                            System.out.println(Message.ARGUMENT_INVALIDE);
                            break;
                        }
                        return;

                    }
                    case "help" : {
                        System.out.println(Message.TACHE_COL_HELP);
                        break;
                    }
                    default:
                        System.out.println(Message.ARGUMENT_INVALIDE);
                        break;
                }
                break;
            }

            case "clock" : {
                // TACHE CLOCK <num>
                if (verifBadNbArgument(3, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)) {
                    return;
                }
                if (data.getListeTache().get(numTache).useMinuteurParPropriete()) {
                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_MINUTEUR_LANCER_SUCCES);
                } else {
                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_MINUTEUR_STOPPER_SUCCES);
                }
                break;
            }

            case "state": {
                // TASK STATE <num> <state ou next>
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)){
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)) {
                    return;
                }

                if(args[3].trim().equalsIgnoreCase("next")){
                    if(data.getListeTache().get(numTache).setEtatSuivant()){
                        data.ecritureListeTaches();
                        System.out.println(Message.TACHE_STATE_SUIVANT_UPDATE_SUCCES);
                    }else{
                        System.out.println(Message.TACHE_STATE_SUIVANT_UPDATE_ECHEC);
                    }
                    break;
                }

                State state = State.stringIsState(args[3]);
                if(state == null){
                    System.out.println(Message.TACHE_STATE_INTROUVABLE_ECHEC);
                    return;
                }

                if(data.getListeTache().get(numTache).setEtat(state)){
                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_STATE_UPDATE_SUCCES);
                }else{
                    System.out.println(Message.TACHE_STATE_UPDATE_ECHEC);
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
                        if(data.getListeTache().get(numTache).addPropriete(args[4], args[5], false)){
                            data.ecritureListeTaches();
                            System.out.println(Message.TACHE_AJOUT_PROPRIETE_SUCCES);
                        }else{
                            System.out.println(Message.TACHE_AJOUT_PROPRIETE_ECHEC);
                        }
                        break;
                    }
                    case "delete": {
                        // TASK PROPERTY DELETE <num> <name prop>
                        if (verifBadNbArgument(5, args) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                            return;
                        }
                        int numTache = Integer.parseInt(args[3]);
                        if (verifTacheNotExiste(numTache, data)) {
                            return;
                        }
                        if(data.getListeTache().get(numTache).removePropriete(args[4],  false)){
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
                        if(data.getListeTache().get(numTache).addTag(args[4])){
                            data.ecritureListeTaches();
                            System.out.println(Message.TACHE_AJOUT_TAG_SUCCES);
                        }else {
                            System.out.println(Message.TACHE_AJOUT_TAG_ECHEC);
                        }
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
                        if(data.getListeTache().get(numTache).removeTag(args[4])){
                            data.ecritureListeTaches();
                            System.out.println(Message.TACHE_DELETE_TAG_SUCCES);
                        }else {
                            System.out.println(Message.TACHE_DELETE_TAG_ECHEC);
                        }
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
                // TACHE DL <num> <date>
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)) {
                    return;
                }

                String date = args[3].replace("/", "-");
                if (data.getListeTache().get(numTache).addDateLimite(date)) {

                    Tache tache = data.getListeTache().get(numTache);
                    SQLiteDataBase.updateAllSynchroEstSynchroByTache(tache, false);
                    SQLiteConnection.closeConnection();

                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_AJOUT_DEADLINE_SUCCES);
                } else {
                    System.out.println(Message.TACHE_AJOUT_DEADLINE_ECHEC);
                }
                break;
            }

            case "closed":{}
            case "cl": {
                // TACHE closed <num> <date>
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)) {
                    return;
                }

                String date = args[3].replace("/", "-");
                if (data.getListeTache().get(numTache).addDateFin(date)) {
                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_AJOUT_CLOSED_SUCCES);
                } else {
                    System.out.println(Message.TACHE_AJOUT_CLOSED_ECHEC);
                }
                break;
            }

            case "scheduled":{}
            case "sd": {
                // TACHE scheduled <num> <date>
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if (verifTacheNotExiste(numTache, data)) {
                    return;
                }

                String date = args[3].replace("/", "-");
                if (data.getListeTache().get(numTache).addDateDebut(date)) {
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
                Tache tache = data.getListeTache().get(numTache);
                data.getListeTache().get(numTache).setTitre(args[3]);

                SQLiteDataBase.updateAllSynchroEstSynchroByTache(tache, false);
                SQLiteConnection.closeConnection();

                data.ecritureListeTaches();
                System.out.println(Message.TACHE_RENAME_SUCESS);
                break;
            }

            case "add": {
                // TACHE ADD <nom> (num dep)
                if(args.length == 4){
                    if (verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                        return;
                    }
                    int numTache = Integer.parseInt(args[3]);
                    if (verifTacheNotExiste(numTache, data)){
                        return;
                    }
                    Tache tache = new Tache(args[2]);
                    data.getListeTache().add(tache);
                    Tache.setDependanceListe(data.getListeTache(), data.getListeTache().size() - 1, numTache);

                    SQLiteDataBase.addTache(tache);
                    SQLiteConnection.closeConnection();

                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_AJOUT_AVEC_DEP_SUCCES);
                }else{
                    if (verifBadNbArgument(3, args) || verifBadLectureFichier(data)) {
                        return;
                    }
                    Tache tache = new Tache(args[2]);
                    data.getListeTache().add(tache);

                    SQLiteDataBase.addTache(tache);
                    SQLiteConnection.closeConnection();
                    //tache.writeFichier(data.getPath(), true);
                    data.ecritureListeTaches();
                    System.out.println(Message.TACHE_AJOUT_SUCCES);
                }
                break;
            }

            case "cost": {
                // Task cost <numTask> <valCost>

                if(verifBadNbArgument(4, args) || verifArgNotNombre(args[2]) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)){
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                int cout = Integer.parseInt(args[3]);

                if (verifTacheNotExiste(numTache, data)){
                    return;
                }

                if(data.getListeTache().get(numTache).addCout(cout)){
                    System.out.println(Message.TACHE_COUTS);
                }else{
                    System.out.println(Message.TACHE_COUTF);
                }
                data.ecritureListeTaches();
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
                Tache tache = data.getListeTache().get(numTache);
                Tache.removeTache(data.getListeTache(), numTache);
                data.ecritureListeTaches();

                SQLiteDataBase.deleteTache(tache);
                SQLiteConnection.closeConnection();
                System.out.println(Message.TACHE_DELETE_SUCCES);
                break;
            }

            case "list": {
                // TACHE LIST
                if(verifBadLectureFichier(data)){
                    return;
                }
                System.out.print(data.getListeTache().size() + " result(s).\n");
                int i = 0;
                String msg;
                for (Tache tache : data.getListeTache()) {
                    msg = "n°" + i + " " + tache.getTitre() + " " + tache.getId();
                    if(tache.getMinuteur() != null){
                        msg += " " + tache.getMinuteurTexte();
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
                        if(Tache.removeDependanceListe(data.getListeTache(), numTache)){
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
            // case task sync <num_task>
            case "sync":
                int numtask;
                Tache tache;
                if(verifBadLectureFichier(data)) return;
                if(verifBadNbArgument(3, args)) return;
                if(verifArgNotNombre(args[2])){
                    return;
                } else {
                    numtask = Integer.parseInt(args[2]);
                }
                if(verifTacheNotExiste(numtask, data)) {
                    return;
                }else{
                    tache = data.getListeTache().get(numtask);
                }
                List<SQLCollaborateur> cols = new ArrayList<>();
                for (String colString: data.getListeTache().get(numtask).getCollaborateur()) {
                    cols.add(SQLiteDataBase.getCollaborateur(colString));
                }
                Map<SQLCollaborateur, Task> map = new HashMap<>();
                try {
                    for (SQLCollaborateur sql: cols) {
                        if (sql.getGoogle_id_liste() != null) {
                            SQLSynchro synchro = SQLiteDataBase.getSynchroTacheCollaborateur(sql, tache);
                            if (synchro != null) {
                                Task t = gl.getTask(synchro.getGoogle_id_tache(), sql.getGoogle_id_liste(), sql.getPseudo());
                                System.out.println("status :  " + tache.getEtat() + " google status : " + t.getStatus());
                                if (tache.getEtat().equals(State.ONGOING) || tache.getEtat().equals(State.TODO)) {
                                    if (GoogleStateEnum.stringIsGoogleStateEnum(t.getStatus()) != GoogleStateEnum.NEEDSACTION) {
                                        map.put(sql, t);
                                    }
                                } else if (tache.getEtat().equals(State.CANCELLED) || tache.getEtat().equals(State.DONE)) {
                                    if (GoogleStateEnum.stringIsGoogleStateEnum(t.getStatus()) != GoogleStateEnum.COMPLETED) {
                                        map.put(sql, t);
                                    }
                                }
                            }
                        }
                    }
                    boolean quitter;
                    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                    String lecture;
                    int choix;
                    for (Map.Entry<SQLCollaborateur, Task> entry : map.entrySet()) {
                        System.out.print(Message.COLLABORATEUR_TASK_PAS_A_JOUR_1 + entry.getKey().getPseudo());
                        System.out.print(Message.COLLABORATEUR_TASK_PAS_A_JOUR_2 + entry.getValue().getTitle());
                        System.out.println(Message.COLLABORATEUR_TASK_PAS_A_JOUR_3);
                        lecture = bufferRead.readLine();
                        // Si l'utilisateur ne veut plus ajouter de tâche en local
                        if (lecture.equalsIgnoreCase("quit") || lecture.equalsIgnoreCase("q")) {
                            break;
                        } else {
                            try {
                                tache.addRegistre("- \"" + entry.getKey().getPseudo() + "\" => " +
                                        entry.getValue().getStatus().toUpperCase() + " => \"" +
                                        entry.getValue().getTitle() + "\" ");
                                choix = Integer.parseInt(lecture);
                                if (choix == 0) {
                                    if (GoogleStateEnum.stringIsGoogleStateEnum(entry.getValue().getStatus()) == GoogleStateEnum.COMPLETED) {
                                        System.out.println(Message.COLLABORATEUR_TASK_PAS_A_JOUR_4);
                                        lecture = bufferRead.readLine();
                                        choix = Integer.parseInt(lecture);
                                        if (choix == 0) {
                                            tache.setEtat(State.DONE);
                                            data.ecritureListeTaches();
                                        } else if (choix == 1) {
                                            tache.setEtat(State.CANCELLED);
                                            data.ecritureListeTaches();
                                        } else{
                                            System.out.println(Message.ARGUMENT_INVALIDE);
                                        }
                                    }else if (GoogleStateEnum.stringIsGoogleStateEnum(entry.getValue().getStatus()) == GoogleStateEnum.NEEDSACTION) {
                                        System.out.println(Message.RETOUR_ON_GOING_IMPOSSIBLE);
                                    }
                                } else if (choix == 1) {
                                    if (tache.getEtat().equals(State.ONGOING) || tache.getEtat().equals(State.TODO)) {
                                        gl.updateTask(tache, entry.getValue().getId(),
                                                entry.getKey().getGoogle_id_liste(), entry.getKey().getPseudo(),
                                                GoogleStateEnum.NEEDSACTION);
                                    }else if (tache.getEtat().equals(State.CANCELLED) || tache.getEtat().equals(State.DONE)) {
                                        gl.updateTask(tache, entry.getValue().getId(),
                                                entry.getKey().getGoogle_id_liste(), entry.getKey().getPseudo(),
                                                GoogleStateEnum.COMPLETED);
                                    }
                                } else {
                                    System.out.println(Message.ARGUMENT_INVALIDE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(Message.ARGUMENT_INVALIDE);
                            }
                        }
                    }
                    if (map.isEmpty()) {
                        System.out.println(Message.COLLABORATEUR_TASK_PAS_A_JOUR_5);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                System.out.println(Message.FICHIER_HELP);
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

            case "t" : {
                // LIST TS <State>
                if(verifBadNbArgument(3, args) || verifBadLectureFichier(data)){
                    return;
                }
                if(data.getListeTache().isEmpty()){
                    System.out.print(Message.LIST_AUCUN_RESULTAT + "\n");
                    break;
                }
                affichageState(data, args[2]);
                break;
            }
            case "kanban" : {

            }
            case "k" : {
                // LIST ATS
                verifBadLectureFichier(data);
                if(data.getListeTache().isEmpty()){
                    System.out.print(Message.LIST_AUCUN_RESULTAT + "\n");
                    break;
                }
                affichageState(data, "TODO");
                affichageState(data, "ONGOING");
                affichageState(data, "DONE");
                affichageState(data, "CANCELLED");

                break;
            }

            case "notstarted" : {
                // LIST SD
                verifBadLectureFichier(data);
                if(data.getListeTache().isEmpty()){
                    System.out.print(Message.LIST_AUCUN_RESULTAT + "\n");
                    break;
                }
                List<Tache> taches = Scrum.listTacheDateDebut(data.getListeTache());
                System.out.print(affichage(data, taches));

                break;
            }

            case "tech" : {
                //LIST TECH
                verifBadLectureFichier(data);
                if(data.getListeTache().isEmpty()){
                    System.out.print(Message.LIST_AUCUN_RESULTAT + "\n");
                    break;
                }
                List<Tache> taches = Scrum.listTacheTag(data.getListeTache(),"technical");
                System.out.print(affichage(data,taches));
                break;
            }

            case "func" : {
                //LIST FUNC
                verifBadLectureFichier(data);
                if(data.getListeTache().isEmpty()){
                    System.out.print(Message.LIST_AUCUN_RESULTAT + "\n");
                    break;
                }
                List<Tache> taches = Scrum.listTacheTag(data.getListeTache(),"functional");
                System.out.print(affichage(data,taches));
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

    /* N'est plus utilisé
    public static void commandeCost(String[] args, Data data){
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            case "ite" :
                verifBadLectureFichier(data);
                Integer dif = Scrum.compareCout(data.getListeTache());
                if(dif == null){
                    System.out.println(Message.COST_ITE);
                }else if(dif == 0){
                    System.out.println(Message.COST_ITE_0);
                }else if(dif > 0){
                    System.out.println(Message.COST_ITE_SUP + dif.toString());
                }else{
                    dif = dif * -1;
                    System.out.println(Message.COST_ITE_INF + dif.toString());
                }
                break;
            case "help" :
                System.out.println(Message.COST_HELP);
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }
    }*/

    public static void commandeHeader(String[] args, Data data) {
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            /* N'est plus utilisé
            case "cost" : {
                // HEAD COST <valeur>
                if(verifBadNbArgument(3, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)){
                    return;
                }
                if(!Tache.setEnTete(Tache.HEADER_COST,args[2],true)){
                    Tache.addEnTete(Tache.HEADER_COST, args[2], true);
                }
                System.out.println(Message.HEADER_COSTS);
                data.ecritureListeTaches();
                break;
            }*/
            case "get" : {
                // HEAD GET <clef>
                if(verifBadNbArgument(3, args) || verifBadLectureFichier(data)){
                    return;
                }
                String header = Tache.getEnTete(args[2]);
                if (header != null) {
                    System.out.println(header);
                } else {
                    System.out.println(Message.HEADER_GET_ECHEC);
                }
                break;
            }

            case "add" : {
                // HEAD ADD <clef> <valeur>
                if(verifBadNbArgument(4, args) || verifBadLectureFichier(data)){
                    return;
                }
                if (Tache.addEnTete(args[2], args[3], false)) {
                    data.ecritureListeTaches();
                    System.out.println(Message.HEADER_ADD_SUCCES);
                } else {
                    System.out.println(Message.HEADER_ADD_ECHEC);
                }
                break;
            }

            case "set" : {
                // HEAD SET <clef> <valeur>
                if(verifBadNbArgument(4, args) || verifBadLectureFichier(data)){
                    return;
                }
                if (Tache.setEnTete(args[2], args[3], false)) {
                    data.ecritureListeTaches();
                    System.out.println(Message.HEADER_SET_SUCCES);
                } else {
                    System.out.println(Message.HEADER_SET_ECHEC);
                }
                break;
            }

            case "delete" : {
                // HEAD DELETE <clef>
                if(verifBadNbArgument(3, args) || verifBadLectureFichier(data)){
                    return;
                }
                if (Tache.removeEnTete(args[2], false)) {
                    data.ecritureListeTaches();
                    System.out.println(Message.HEADER_DELETE_SUCCES);
                } else {
                    System.out.println(Message.HEADER_DELETE_ECHEC);
                }
                break;
            }

            case "help" :
                System.out.println(Message.HEADER_HELP);
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }
    }

    public static void commandeCollaborateur(String[] args, Data data){
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            case "directory" : {
                if(verifBadLectureFichier(data)){
                    return;
                }
                List<String> list = Tache.getCollaborateurEnTeteListe();
                for (String col: list) {
                    if(!cls.creerDossierCollaboSiPasExistant(col)) {
                        break;
                    }
                }
                return;
            }
            case "add" :
                // COL ADD <nom>
                if(verifBadNbArgument(3, args) || verifBadLectureFichier(data)){
                    return;
                }
                if(Tache.addCollaborateurEnTete(args[2])) {
                    if(!cls.creerDossierCollaboSiPasExistant(args[2])) {
                        break;
                    }

                    SQLCollaborateur col = new SQLCollaborateur(args[2].toLowerCase().trim());
                    SQLiteDataBase.addCollaborateur(col);
                    //SQLiteDataBase.addCollaborateur(args[2].toLowerCase().trim(), null, null, null);
                    SQLiteConnection.closeConnection();

                    data.ecritureListeTaches();
                    System.out.print(Message.COLLABORATEUR_AJOUT_SUCCES + "\n");
                }else{
                    System.out.println(Message.COLLABORATEUR_AJOUT_ECHEC);
                }
                break;
            // COL TRELLO <nom> ???
            case "trello": {
                if(verifBadLectureFichier(data)) {
                    return;
                }
                if (args.length < 3) {
                    System.out.println(Message.ARGUMENT_MANQUANT);
                    return;
                }
                if (args[2].equalsIgnoreCase("help")) {
                    System.out.println(Message.COL_TRELLO_HELP);
                    break;
                }
                if (!cls.verifColaborateurExist(args[2])){
                    return;
                }
                tihm.chooseWork(args, data);
                return;
            }
            case "set" :
                // COL set <old nom> <new nom>
                if(verifBadNbArgument(4, args) || verifBadLectureFichier(data)){
                    return;
                }
                if(Tache.setCollaborateurEnTete(data.getListeTache(), args[2], args[3])){
                    File dir = new File(PATH + args[2]);
                    File newDir = new File(PATH + args[3]);
                    if (!cls.changeDirectory(dir, newDir)) break;
                    dir = new File(PATH_TOKEN + args[2]);
                    newDir = new File(PATH_TOKEN + args[3]);
                    if (!cls.changeDirectory(dir, newDir)) break;

                    SQLiteDataBase.updateCollaborateurPseudo(args[2].toLowerCase().trim(), args[3].toLowerCase().trim());
                    SQLiteConnection.closeConnection();

                    data.ecritureListeTaches();
                    System.out.println(Message.COLLABORATEUR_SET_SUCCES);
                }else{
                    System.out.println(Message.COLLABORATEUR_SET_ECHEC);
                }
                break;

            case "delete" :
                // COL delete <nom>
                if(verifBadNbArgument(3, args) || verifBadLectureFichier(data)){
                    return;
                }
                if(Tache.removeCollaborateurEnTete(data.getListeTache(), args[2])){
                    File dir = new File(PATH + args[2]);
                    if(!cls.deleteDirectory(dir)){
                        System.out.println(Message.COLLABORATEUR_SUPPRESSION_DOSSIER_FAILURE + dir.getPath());
                    }
                    dir = new File(PATH_TOKEN, args[2]);
                    if(!cls.deleteDirectory(dir)){
                        System.out.println(Message.COLLABORATEUR_SUPPRESSION_DOSSIER_FAILURE + dir.getPath());
                    }

                    SQLiteDataBase.deleteCollaborateur(args[2].toLowerCase().trim());
                    SQLiteConnection.closeConnection();

                    data.ecritureListeTaches();
                    System.out.println(Message.COLLABORATEUR_DELETE_SUCCES);
                }else{
                    System.out.println(Message.COLLABORATEUR_DELETE_ECHEC);
                }
                break;

            case "list": {
                // TACHE LIST
                if(verifBadLectureFichier(data)){
                    return;
                }
                List<String> list = Tache.getCollaborateurEnTeteListe();
                if (list == null){
                    System.out.print("No résult.\n");
                    break;
                }else {
                    System.out.print(list.size() + " résult(s).\n");
                }
                int i = 0;
                String msg;
                for (String col : list) {
                    msg = "n°" + i + " " + col;
                    System.out.print(msg + "\n");
                    i++;
                }
                break;
            }

            case "help" :
                System.out.println(Message.COLLABORATEUR_HELP);
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }
    }

    public static void commandeTag(String[] args, Data data) {
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            case "technical": {}
            case "tech": {
                // TAG TECH <num tache>
                if (verifBadNbArgument(3, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if(verifTacheNotExiste(numTache, data)){
                    return;
                }
                if(data.getListeTache().get(numTache).addTag("technical")){
                    data.ecritureListeTaches();
                    System.out.println(Message.TAG_TECH_SUCCES);
                }else{
                    System.out.println(Message.TAG_TECH_ECHEC);
                }
                break;
            }

            case "functional": {}
            case "func": {
                // TAG FUNC <num tache>
                if (verifBadNbArgument(3, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache = Integer.parseInt(args[2]);
                if(verifTacheNotExiste(numTache, data)){
                    return;
                }
                if(data.getListeTache().get(numTache).addTag("functional")){
                    data.ecritureListeTaches();
                    System.out.println(Message.TAG_FUNC_SUCCES);
                }else{
                    System.out.println(Message.TAG_FUNC_ECHEC);
                }
                break;
            }

            case "help":
                System.out.println(Message.TAG_HELP);
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }

    }

    public static void commandeSprint(String[] args, Data data) {
        if (verifBadNbArgument(2, args)){
            return;
        }
        switch (args[1].toLowerCase()){
            case "get":
                if (verifBadNbArgument(3, args) || verifBadLectureFichier(data)) {
                    return;
                }

                switch (args[2].toLowerCase()){
                    case "num":
                        System.out.println(Message.SPRINT_GET_NUM + Tache.getSprint().toString());
                        break;
                    case "date":
                        String date = Tache.getDateSprint();
                        if(date == null)
                        {
                            System.out.println(Message.SPRINT_GET_DATE_ECHEC);
                        }else{
                            System.out.println(Message.SPRINT_GET_DATE_SUCCES + date);
                        }
                        break;
                    default:
                        System.out.println(Message.ARGUMENT_INVALIDE);
                        break;
                }
                break;

            case "next":
                if (verifBadLectureFichier(data)) {
                    return;
                }
                Tache.incrementeSprint();
                System.out.println(Message.SPRINT_NEXT + Tache.getSprint().toString());
                break;

            case "date":
                if (verifBadNbArgument(3, args) || verifBadLectureFichier(data)) {
                    return;
                }
                if(Tache.setDateSprint(args[2])){
                    System.out.println(Message.SPRINT_DATE_SUCCES);
                }else{
                    System.out.println(Message.SPRINT_DATE_ECHEC);
                }
                break;

            case "task":
                if (verifBadNbArgument(4, args) || verifArgNotNombre(args[3]) || verifBadLectureFichier(data)) {
                    return;
                }
                int numTache;
                switch (args[2].toLowerCase()){
                    case "num":
                        //SPRINT TASK NUM <numTache>
                        numTache = Integer.parseInt(args[3]);
                        data.getListeTache().get(numTache).addSprint();
                        System.out.println(Message.SPRINT_TASK_NUM_SUCCES);
                        break;

                    case "delnum":
                        //SPRINT TASK DELNUM <numTache>
                        numTache = Integer.parseInt(args[3]);
                        data.getListeTache().get(numTache).deleteSprint(Tache.getSprint());
                        System.out.println(Message.SPRINT_TASK_DELNUM_SUCCES);
                        break;

                    case "date":
                        //SPRINT TASK DATE <numTache>
                        numTache = Integer.parseInt(args[3]);
                        if(data.getListeTache().get(numTache).addDateSprint()){
                            System.out.println(Message.SPRINT_TASK_DATE_SUCCES);
                        }else{
                            System.out.println(Message.SPRINT_TASK_DATE_ECHEC);
                        }
                        break;

                    case "all":
                        //SPRINT TASK ALL <numTache>
                        numTache = Integer.parseInt(args[3]);
                        if(data.getListeTache().get(numTache).addDateSprint()){
                            data.getListeTache().get(numTache).addSprint();
                            System.out.println(Message.SPRINT_TASK_ALL_SUCCES);
                        }else{
                            System.out.println(Message.SPRINT_TASK_ALL_ECHEC);
                        }
                        break;

                    case "next":
                        //SPRINT TASK NEXT <numTache>
                        numTache = Integer.parseInt(args[3]);
                        int result = Scrum.etatSuivant(data.getListeTache(),numTache);
                        if(result == 1){
                            data.getListeTache().get(numTache).setEtatSuivant();
                            System.out.println(Message.SPRINT_TASK_NEXT_SUCCES);
                        }else if(result == 0){
                            System.out.println(Message.SPRINT_TASK_NEXT_ECHEC_0);
                        }else if(result == 2){
                            System.out.println(Message.SPRINT_TASK_NEXT_ECHEC_2);
                        }else if(result == 3){
                            System.out.println(Message.SPRINT_TASK_NEXT_ECHEC_3);
                        }
                        break;

                    default:
                        System.out.println(Message.ARGUMENT_INVALIDE);
                        break;
                }
                break;

            case "cost" : {
                // SPRINT COST <valeur>
                if(verifBadNbArgument(3, args) || verifArgNotNombre(args[2]) || verifBadLectureFichier(data)){
                    return;
                }
                if(!Tache.setEnTete(Tache.HEADER_COST,args[2],true)){
                    Tache.addEnTete(Tache.HEADER_COST, args[2], true);
                }
                System.out.println(Message.HEADER_COSTS);
                break;
            }

            case "help":
                System.out.println(Message.SPRINT_HELP);
                break;

            default:
                System.out.println(Message.ARGUMENT_INVALIDE);
                break;
        }
        data.ecritureListeTaches();
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

    private static void affichageState(Data data,String state){
        List<Tache> taches = Scrum.listTacheEtat(data.getListeTache(), State.stringIsState(state));

        if(State.TODO.toString().equals(state)){
            System.out.print("\n" + Message.LIST_STATE_TODO + "\n");
        }else if(State.ONGOING.toString().equals(state)){
            System.out.print("\n" + Message.LIST_STATE_ONGOING + "\n");
        }else if(State.DONE.toString().equals(state)){
            System.out.print("\n" + Message.LIST_STATE_DONE + "\n");
        }else if(State.CANCELLED.toString().equals(state)){
            System.out.print("\n" + Message.LIST_STATE_CANCELLED + "\n");
        }

        if(taches.isEmpty()){
            System.out.print(Message.LIST_AUCUN_RESULTAT + "\n");
            return;
        }

        System.out.print(affichage(data,taches));
        //return;
    }

    public static String affichage(Data data,List<Tache> taches){
        int i = 0;
        int j = 0;
        String msg = "";
        for (Tache tache : data.getListeTache()) {
            if(taches.contains(tache)) {
                msg += "n°" + i + " " + tache.getTitre() + " " + tache.getId();
                if (tache.getMinuteur() != null) {
                    msg += " " + tache.getMinuteurTexte();
                }
                //System.out.print(msg + "\n");
                msg += "\n";
                j++;
            }
            i++;
        }
        //System.out.print(j + " résultat(s).\n");
        msg += j + " result(s).\n";

        return msg;
    }

    private static String getProjectName(Data data) {
        return data.getFichierCourant().substring(0, data.getFichierCourant().length()-4);
    }
}