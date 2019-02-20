package fr.orgpro.ihm.project;

import com.google.api.client.util.DateTime;
import fr.orgpro.api.collaborateur.Collaborateur;
import fr.orgpro.api.local.SQLiteConnection;
import fr.orgpro.api.local.SQLiteDataBase;
import fr.orgpro.api.local.models.SQLCollaborateur;
import fr.orgpro.api.local.models.SQLSynchro;
import fr.orgpro.api.project.Tache;
import fr.orgpro.api.remote.trello.TrelloApi;
import fr.orgpro.api.remote.trello.models.TrelloBoard;
import fr.orgpro.api.remote.trello.models.TrelloCard;
import fr.orgpro.api.remote.trello.models.TrelloList;
import fr.orgpro.api.remote.trello.services.TrelloBoardService;
import fr.orgpro.api.remote.trello.services.TrelloCardService;
import fr.orgpro.api.remote.trello.services.TrelloListService;
import fr.orgpro.ihm.service.CollaborateurService;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

public class TrelloIhm {
    private static TrelloIhm INSTANCE = new TrelloIhm();
    private static CollaborateurService cl = CollaborateurService.getInstance();

    public void chooseWork (String[] args, Data data){
        SQLCollaborateur col = this.checkColExist(args[2]);
        switch (args[3]) {
            case "set" : {
                /*
                switch (args[4]) {
                    case "board" : {
                        System.out.println("set board");
                        return;
                    }
                }*/
                return;
            }
            // col trello <name> credentials <apikey> <token>
            case "credentials" : {
                if (args.length < 5) {
                    System.out.println(Message.ARGUMENT_MANQUANT);
                } else {
                    setupCredentialTrelloUser(args, col);
                }
                return;
            }
            case "generate" : {
                this.generate(col, data);
                return;
            }
            case "sync" : {
                this.syncUser(col, data);
                return;
            }
            default: {
                System.out.println(Message.ARGUMENT_MANQUANT);
            }
        }
    }

    public void generate(SQLCollaborateur col, Data data) {
        if (col == null) {
            return;
        }
        if(!checkColCredential(col)) {
            System.out.println(Message.COLLABORATEUR_NO_TRELLLO_CREDENTIALS);
            String[] args = this.askCredentials(col);
            this.setupCredentialTrelloUser(args, col);
            checkColCredential(col);
            this.chooseWork(args, data);
        }
        String boardID = null;
        String listID = null;
        if (col.getTrello_id_board() == null) {
            System.out.println(Message.COLLABORATEUR_SANS_TRELLO_BOARD);
            try {
                col.setTrello_id_liste(null);
                TrelloBoard board = TrelloApi.createService(TrelloBoardService.class)
                        .addBoard(col.getTrello_key(), col.getTrello_token(), getProjectName(data))
                        .execute().body();
                boardID = board.getId();
                System.out.println(Message.COLLABORATEUR_GENERATION_TRELLO_BOARD_SUCCESS);
            } catch (IOException e) {
                SQLiteConnection.closeConnection();
                System.out.println(Message.COLLABORATEUR_GENERATION_TRELLO_BOARD_FAILURE);
                e.printStackTrace();
            }
        } else {
            System.out.println(Message.COLLABORATEUR_AVEC_TRELLO_BOARD);
           boardID = col.getTrello_id_board();
        }
        if (col.getTrello_id_liste() == null) {
            try {
                System.out.println(Message.COLLABORATEUR_SANS_TRELLO_LIST);
                TrelloList list = TrelloApi.createService(TrelloListService.class)
                        .addList(col.getTrello_key(), col.getTrello_token(),
                                "OrgProList", boardID)
                        .execute()
                        .body();
                listID = list.getId();
                System.out.println(Message.COLLABORATEUR_GENERATION_TRELLO_LIST_SUCCESS);
            }catch (IOException e) {
                System.out.println(Message.COLLABORATEUR_GENERATION_TRELLO_LIST_FAILURE);
                SQLiteConnection.closeConnection();
                e.printStackTrace();
            }
        } else {
            System.out.println(Message.COLLABORATEUR_AVEC_TRELLO_LIST);
            listID = col.getTrello_id_liste();
        }
        col.setTrello_id_board(boardID);
        col.setTrello_id_liste(listID);
        SQLiteDataBase.updateCollaborateur(col);
        SQLiteConnection.closeConnection();
    }

    public void syncUser(SQLCollaborateur col, Data data) {
        if (col == null) {
            return;
        }
        List<Tache> toSend;
        toSend = cl.getListTacheFromCollabo(col.getPseudo(), data);
        if(toSend != null) {
            TrelloCard tc;
            for(Tache t : toSend) {
                tc = setupCard(t, col);
                try {
                    System.out.println("try");
                    tc = TrelloApi.createService(TrelloCardService.class)
                            .addCard(col.getTrello_key(), col.getTrello_token(),
                                    tc.getIdList(),
                                    tc.getName(),
                                    tc.getDate()).execute().body();
                } catch (IOException e) {
                    System.out.println(Message.COLLABORATEUR_SYNC_TRELLO_FAILURE);
                    e.printStackTrace();
                    return;
                }

            }
            SQLiteConnection.closeConnection();
            System.out.println(Message.COLLABORATEUR_SYNC_TRELLO_SUCCESS);
        }
    }

    public void send(String name, Tache tache, Data data) {
        SQLCollaborateur col = checkColExist(name);
        if(col == null) {
            return;
        }
        if(!checkColCredential(col)) {
            System.out.println(Message.COLLABORATEUR_NO_TRELLLO_CREDENTIALS);
            String[] args = this.askCredentials(col);
            col = this.setupCredentialTrelloUser(args, col);
            this.generate(col, data);
        }
        try {
            TrelloCard tc = setupCard(tache, col);
            tc = TrelloApi.createService(TrelloCardService.class)
                    .addCard(col.getTrello_key(), col.getTrello_token(),
                            tc.getIdList(),
                            tc.getName(),
                            tc.getDate()).execute().body();
            System.out.println(Message.COLLABORATEUR_TASK_TRELLO_SEND_SUCCESS);
        }catch (Exception e){
            System.out.println(Message.COLLABORATEUR_TASK_TRELLO_SEND_FAILURE);
            e.printStackTrace();
        }
    }


    public void send(SQLCollaborateur col, Tache tache){
        if(!checkColCredential(col)) {
            System.out.println(Message.COLLABORATEUR_NO_TRELLLO_CREDENTIALS);
            return;
        }
        if (col.getTrello_id_board() == null) {
            System.out.println(Message.COLLABORATEUR_SANS_TRELLO_BOARD);
            try {
                TrelloBoard board = TrelloApi.createService(TrelloBoardService.class)
                        .addBoard(col.getTrello_key(), col.getTrello_token(), "OrgProBoard")
                        .execute().body();
                if(board == null){
                    System.out.println(Message.TACHE_API_TRELLO_AJOUT_BOARD_ECHEC);
                    SQLiteConnection.closeConnection();
                    return;
                }
                col.setTrello_id_liste(null);
                col.setTrello_id_board(board.getId());
                SQLiteDataBase.updateCollaborateur(col);
                System.out.println(Message.COLLABORATEUR_GENERATION_TRELLO_BOARD_SUCCESS);
            }catch (UnknownHostException e){
                SQLiteConnection.closeConnection();
                System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                return;
            } catch (IOException e) {
                SQLiteConnection.closeConnection();
                System.out.println(Message.COLLABORATEUR_GENERATION_TRELLO_BOARD_FAILURE);
                return;
            }
        }
        if (col.getTrello_id_liste() == null) {
            try {
                System.out.println(Message.COLLABORATEUR_SANS_TRELLO_LIST);
                TrelloList list = TrelloApi.createService(TrelloListService.class)
                        .addList(col.getTrello_key(), col.getTrello_token(),
                                "OrgProList", col.getTrello_id_board())
                        .execute().body();
                if(list == null){
                    System.out.println(Message.TACHE_API_TRELLO_AJOUT_LISTE_ECHEC);
                    SQLiteConnection.closeConnection();
                    return;
                }
                col.setTrello_id_liste(list.getId());
                SQLiteDataBase.updateCollaborateur(col);
                System.out.println(Message.COLLABORATEUR_GENERATION_TRELLO_LIST_SUCCESS);
            }catch (UnknownHostException e){
                SQLiteConnection.closeConnection();
                System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                return;
            }catch (IOException e) {
                SQLiteConnection.closeConnection();
                System.out.println(Message.COLLABORATEUR_GENERATION_TRELLO_LIST_FAILURE);
                return;
            }
        }

        // Récupère la liaison entre la tâche et le collaborateur
        SQLSynchro synchro = SQLiteDataBase.getSynchroTacheCollaborateur(col, tache);
        if (synchro == null){
            System.out.println(Message.BDD_SYNCHRO_NULL);
            return;
        }

        if(synchro.getTrello_id_card() == null){
            try {
                TrelloCard tc = setupCard(tache, col);
                tc = TrelloApi.createService(TrelloCardService.class)
                        .addCard(col.getTrello_key(), col.getTrello_token(),
                                tc.getIdList(),
                                tc.getName(),
                                tc.getDate()).execute().body();
                if(tc == null){
                    System.out.println(Message.TACHE_API_TRELLO_AJOUT_CARD_ECHEC);
                    SQLiteConnection.closeConnection();
                    return;
                }
                synchro.setTrello_id_card(tc.getId());
                synchro.setTrello_est_synchro(true);
                SQLiteDataBase.updateSynchroTacheCollaborateur(synchro);
                System.out.println(Message.COLLABORATEUR_TASK_TRELLO_SEND_SUCCESS);
            }catch (UnknownHostException e){
                SQLiteConnection.closeConnection();
                System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                return;
            }catch (Exception e){
                SQLiteConnection.closeConnection();
                System.out.println(Message.COLLABORATEUR_TASK_TRELLO_SEND_FAILURE);
                return;
            }
        }else {
            try {
                TrelloCard tc = setupCard(tache, col);
                tc = TrelloApi.createService(TrelloCardService.class)
                        .updateCard(synchro.getTrello_id_card(), col.getTrello_key(), col.getTrello_token(),
                                tc.getIdList(),
                                tc.getName(),
                                tc.getDate()).execute().body();
                if(tc == null){
                    System.out.println(Message.TACHE_API_TRELLO_MODIFIER_CARD_ECHEC);
                    SQLiteConnection.closeConnection();
                    return;
                }
                if(tc.isClosed()){
                    System.out.println(Message.TACHE_API_TRELLO_AJOUT_CARD_ARCHIVER_ECHEC);
                    SQLiteConnection.closeConnection();
                    return;
                }
                synchro.setTrello_est_synchro(true);
                SQLiteDataBase.updateSynchroTacheCollaborateur(synchro);
                System.out.println(Message.COLLABORATEUR_TASK_TRELLO_SEND_UPDATE_SUCCESS);
            }catch (UnknownHostException e){
                SQLiteConnection.closeConnection();
                System.out.println(Message.TACHE_API_AUCUNE_CONNEXION);
                return;
            }catch (Exception e){
                SQLiteConnection.closeConnection();
                System.out.println(Message.COLLABORATEUR_TASK_TRELLO_SEND_FAILURE);
                return;
            }
        }
    }

    private TrelloCard setupCard(Tache t, SQLCollaborateur col) {
        TrelloCard tc = new TrelloCard();
        tc.setIdBoard(col.getTrello_id_board());
        tc.setIdList(col.getTrello_id_liste());
        if (t.getDateLimite() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(t.getDateLimite());
            c.add(Calendar.DATE, 1);
            DateTime d = new DateTime(c.getTime());
            tc.setDate(d.toString());
        }
        tc.setName(t.getTitre());
        return tc;
    }
    private SQLCollaborateur checkColExist(String name) {
        SQLCollaborateur col = SQLiteDataBase.getCollaborateur(name);
        if (col == null) {
            System.out.println(Message.COLLABORATEUR_NON_TROUVE);
            SQLiteConnection.closeConnection();
            return null;
        }
        return col;
    }
    private boolean checkColCredential(SQLCollaborateur col) {
        return col.getTrello_key() != null && col.getTrello_token() != null;
    }
    private String[] askCredentials(SQLCollaborateur col) {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println(Message.COLLABORATEUR_NO_TRELLLO_CREDENTIALS_ADD_APIKEY);
            String apiKey =  bufferRead.readLine();
            System.out.println(Message.COLLABORATEUR_NO_TRELLLO_CREDENTIALS_ADD_TOKEN);
            String apiToken = bufferRead.readLine();
            String[] args = {"col", "trello", col.getPseudo(), "credentials", apiKey, apiToken};
            return args;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private SQLCollaborateur setupCredentialTrelloUser(String[] args, SQLCollaborateur col) {
        if (col == null) {
            return null;
        }
        col.setTrello_key(args[4]);
        col.setTrello_token(args[5]);
        SQLiteDataBase.updateCollaborateur(col);
        System.out.println(Message.COLLABORATEUR_UPDATED);
        return col;
    }
    private String getProjectName(Data data) {
        return data.getFichierCourant().substring(0, data.getFichierCourant().length()-4);
    }

    public static TrelloIhm getInstance() {
        return INSTANCE;
    }
}
