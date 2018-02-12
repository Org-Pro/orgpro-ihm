package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<Tache> listeTache;
    private static Data data = null;

    public static Data getInstance(){
        if(data == null)
            data = new Data();
        return data;
    }

    private Data() {
        listeTache = new ArrayList<Tache>();
    }

    public List<Tache> getListeTache() {
        return listeTache;
    }

}
