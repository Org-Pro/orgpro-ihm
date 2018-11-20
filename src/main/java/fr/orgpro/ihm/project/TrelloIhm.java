package fr.orgpro.ihm.project;

import com.google.api.client.util.DateTime;
import fr.orgpro.api.local.SQLiteConnection;
import fr.orgpro.api.local.SQLiteDataBase;
import fr.orgpro.api.local.models.SQLCollaborateur;
import fr.orgpro.api.project.Tache;
import fr.orgpro.api.remote.trello.TrelloApi;
import fr.orgpro.api.remote.trello.models.TrelloBoard;
import fr.orgpro.api.remote.trello.models.TrelloCard;
import fr.orgpro.api.remote.trello.models.TrelloList;
import fr.orgpro.api.remote.trello.services.TrelloBoardService;
import fr.orgpro.api.remote.trello.services.TrelloCardService;
import fr.orgpro.api.remote.trello.services.TrelloListService;
import fr.orgpro.ihm.service.CollaborateurService;

import java.io.IOException;
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
        }
    }

    public void generate(SQLCollaborateur col, Data data) {
        if (col == null) {
            return;
        }
        if(!checkColCredential(col)) {
            System.out.println(Message.COLLABORATEUR_NO_TRELLLO_CREDENTIALS);
            return;
        }
        String boardID = null;
        String listID = null;
        if (col.getTrello_id_board() == null) {
            System.out.println(Message.COLLABORATEUR_SANS_TRELLO_BOARD);
            try {
                col.setTrello_id_liste(null);
                TrelloBoard board = TrelloApi.createService(TrelloBoardService.class)
                        .addBoard(col.getTrello_key(), col.getTrello_token(), "OrgProBoard")
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
                tc = new TrelloCard();
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
        System.out.println(col.getTrello_token());
        return col.getTrello_key() != null && col.getTrello_token() != null;
    }
    private void setupCredentialTrelloUser(String[] args, SQLCollaborateur col) {
        if (col == null) {
            return;
        }
        col.setTrello_key(args[4]);
        col.setTrello_token(args[5]);
        SQLiteDataBase.updateCollaborateur(col);
        System.out.println(Message.COLLABORATEUR_UPDATED);
    }
    public static TrelloIhm getInstance() {
        return INSTANCE;
    }
}