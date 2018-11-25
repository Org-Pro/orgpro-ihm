package fr.orgpro.ihm.project;

import fr.orgpro.api.collaborateur.Collaborateur;
import fr.orgpro.api.local.models.SQLCollaborateur;
import fr.orgpro.api.project.State;
import fr.orgpro.api.project.Tache;
import fr.orgpro.api.remote.google.GoogleList;
import fr.orgpro.ihm.service.CollaborateurService;

import java.util.ArrayList;
import java.util.List;

public class GoogleIhm {
    private static GoogleIhm INSTANCE = new GoogleIhm();
    private static CollaborateurService cl = CollaborateurService.getInstance();
    private static final GoogleList gl = GoogleList.getInstance();

    public void syncUser(SQLCollaborateur col, Data data){
        List<Tache> toSend;
        toSend = cl.getListTacheFromCollabo(col.getPseudo(), data);
        if(toSend != null) {
            for(Tache t : toSend) {
                gl.postTache(col.getPseudo(), t.getTitre(), t.getDateLimite());
            }
        }
        return;
    }

    public void syncStatusTaskUser(SQLCollaborateur col, Data data, State etat){
        List<Tache> toSend;
        toSend = cl.getListTacheFromCollabo(col.getPseudo(), data);
        for(Tache t : toSend) {
            if(t.getEtat().equals(etat)) {
                gl.postTache(col.getPseudo(), t.getTitre(), t.getDateLimite());
            }
        }
    }

    public static GoogleIhm getInstance() {
        return INSTANCE;
    }
}
