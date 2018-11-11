package fr.orgpro.ihm.project;

import fr.orgpro.api.project.State;
import fr.orgpro.api.project.Tache;
import fr.orgpro.api.remote.google.GoogleList;

import java.util.ArrayList;
import java.util.List;

public class CollaborateurIhm {
    private static CollaborateurIhm INSTANCE = new CollaborateurIhm();
    private static final GoogleList gl = GoogleList.getInstance();

    public void syncUser(String name, Data data){
        List<Tache> toSend;
        toSend = getListTacheFromCollabo(name, data);
        if(toSend != null) {
            for(Tache t : toSend) {
                gl.postTache(name, t.getTitre(), t.getDateLimite());
            }
        }
        return;
    }

    public void syncStatusTaskUser(String name, Data data, State etat){
        List<Tache> toSend;
        toSend = getListTacheFromCollabo(name, data);
        for(Tache t : toSend) {
            if(t.getEtat().equals(etat)) {
                gl.postTache(name, t.getTitre(), t.getDateLimite());
            }
        }
    }

    private List<Tache> getListTacheFromCollabo(String name, Data data) {
        List<Tache> toSend = new ArrayList<>();
        for (Tache task: data.getListeTache()) {
            for(String c : task.getCollaborateurFromTache(task)) {
                if (c.equalsIgnoreCase(name)) {
                    toSend.add(task);
                    break;
                }
            }
        }
        System.out.println(toSend.get(0).toString());
        return toSend;
    }
    public static CollaborateurIhm getInstance() {
        return INSTANCE;
    }
}
