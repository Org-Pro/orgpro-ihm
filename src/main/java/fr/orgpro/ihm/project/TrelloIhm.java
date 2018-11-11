package fr.orgpro.ihm.project;

import fr.orgpro.api.local.SQLiteConnection;
import fr.orgpro.api.local.SQLiteDataBase;
import fr.orgpro.api.local.models.SQLCollaborateur;
import fr.orgpro.api.remote.trello.TrelloApi;
import fr.orgpro.api.remote.trello.models.TrelloBoard;
import fr.orgpro.api.remote.trello.models.TrelloList;
import fr.orgpro.api.remote.trello.services.TrelloBoardService;
import fr.orgpro.api.remote.trello.services.TrelloListService;

import java.io.IOException;

public class TrelloIhm {
    private static TrelloIhm INSTANCE = new TrelloIhm();

    public void chooseWork (String[] args, Data data){
        switch (args[3]) {
            case "set" : {
                switch (args[4]) {
                    case "board" : {
                        System.out.println("set board");
                        return;
                    }
                }

            }
            case "generate" : {
                this.generate(args, data);
            }
        }
    }

    public void generate(String[] args, Data data) {
        SQLCollaborateur col = SQLiteDataBase.getCollaborateur(args[2]);
        String boardID = null;
        String listID = null;
        if (col == null) {
            System.out.println(Message.COLLABORATEUR_NON_TROUVE);
            SQLiteConnection.closeConnection();
            return;
        }
        if (col.getTrello_id_board() == null) {
            System.out.println(Message.COLLABORATEUR_SANS_TRELLO_BOARD);
            try {
                col.setTrello_id_liste(null);
                TrelloBoard board = TrelloApi.createService(TrelloBoardService.class)
                        .addBoard("9d1700060f2c7e529ca335a393ee7bfb", "449750a688839fe428d68d5610e464963d8c17545500b8d742be7decc3c18faa", "OrgProBoard")
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
                        .addList("9d1700060f2c7e529ca335a393ee7bfb", "449750a688839fe428d68d5610e464963d8c17545500b8d742be7decc3c18faa",
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
    public static TrelloIhm getInstance() {
        return INSTANCE;
    }
}
