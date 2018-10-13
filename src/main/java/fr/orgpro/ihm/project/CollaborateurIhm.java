package fr.orgpro.ihm.project;

import fr.orgpro.api.project.State;
import fr.orgpro.api.project.Tache;
import fr.orgpro.api.remote.GoogleList;
import fr.orgpro.ihm.service.ColaborateurService;
import fr.orgpro.ihm.service.CredentialService;

import java.util.ArrayList;
import java.util.List;

public class CollaborateurIhm {
    private static CollaborateurIhm INSTANCE = new CollaborateurIhm();
    private CredentialService cs = CredentialService.getInstance();
    private static final ColaborateurService cls = ColaborateurService.getInstance();
    private static final GoogleList gl = GoogleList.getInstance();

    public void syncUser(String name, Data data){
        List<Tache> toSend;
        toSend = getListTacheFromCollabo(name, data);
        for(Tache t : toSend) {
            gl.postTache(name, t.getTitre());
        }
        return;
    }

    public void syncOngoingUser(String name, Data data){
        List<Tache> toSend;
        toSend = getListTacheFromCollabo(name, data);
        for(Tache t : toSend) {
            if(t.getEtat().equals(State.ONGOING)) {
                gl.postTache(name, t.getTitre());
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
        return toSend;
    }
    public static CollaborateurIhm getInstance() {
        return INSTANCE;
    }
}
