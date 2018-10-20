package fr.orgpro.ihm.service;

import fr.orgpro.ihm.project.Message;

import java.io.File;

public class CredentialService {
    private static CredentialService INSTANCE = CredentialService.getInstance();

    public static CredentialService getInstance() {
        return INSTANCE;
    }
    /**
     * Prend un chemin vers un fichier et renvoi true si
     * le fichier existe et que ce n'est pas un dossier
     * @param path
     * @return boolean
     */
    public boolean verifCredentialExist(String path) {
        File f = new File("src/main/resources/" + path + "/batrayer/credentials.json");
        if(f.exists() && !f.isDirectory()) {
            return true;
        } else {
            System.out.println(Message.PROBLEME_LECTURE);
            return false;
        }
    }

}
