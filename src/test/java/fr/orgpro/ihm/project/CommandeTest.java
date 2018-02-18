package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

import static org.junit.Assert.assertEquals;

public class CommandeTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private Data data;
    private Preferences prefs;
    private final String PREF_FICHIER_COURANT = "FILE";

    private File file;


    @Before
    public void data(){
        data = Data.getInstance();
        prefs = Preferences.userNodeForPackage(fr.orgpro.ihm.project.Data.class);
        data.getListeTache().add(new Tache("titre 1"));
        data.getListeTache().add(new Tache("test"));
        data.getListeTache().add(new Tache("je suis une tache"));
        data.getListeTache().add(new Tache("chameaux"));
        data.setFichierCourant("test");
        //new File(data.DOSSIER_COURANT).mkdirs();

    }

    @After
    public void resetData(){
        data.getListeTache().clear();
        prefs.remove(PREF_FICHIER_COURANT);
        try {
            Files.deleteIfExists(Paths.get(data.DOSSIER_COURANT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void testTacheArgsMinimum() throws Exception {
        Main.main(new String[]{"tache"});
        assertEquals(Message.ARGUMENT_MANQUANT.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }

    /*@Test
    public void testTacheRename() throws Exception {
        Main.main(new String[]{"tache", "rename", "4", "nouveau titre"});
        assertEquals(Message.TACHE_INVALIDE_ECHEC.toString().trim(), outContent.toString().trim());
        outContent.reset();

        data.getListeTache().add(new Tache("tache"));
        Main.main(new String[]{"tache", "rename", "4", "nouveau titre"});
        assertEquals(Message.TACHE_RENAME_SUCESS.toString().trim(), outContent.toString().trim());
        assertEquals(data.getListeTache().get(4).getTitle(), "nouveau titre");
        outContent.reset();

        Main.main(new String[]{"tache", "rename", "4"});
        assertEquals(Message.ARGUMENT_MANQUANT.toString().trim(), outContent.toString().trim());
        outContent.reset();

        Main.main(new String[]{"tache", "rename", "azeaze", "nouveau titre"});
        assertEquals(Message.ARGUMENT_INVALIDE.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }*/

    @Test
    public void testTacheHelp() throws Exception {
        Main.main(new String[]{"tache", "help"});
        assertEquals(Message.TACHE_HELP.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }

    @Test
    public void testTacheDefault() throws Exception {
        Main.main(new String[]{"tache", "azeaze"});
        assertEquals(Message.ARGUMENT_INVALIDE.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }

    /*@Test
    public void testCommandeList() throws Exception {
        StringBuilder s = new StringBuilder();
        for (Tache tache : data.getListeTache()){
            s.append(tache.toString());
        }
        Main.main(new String[]{"list", "tache"});
        assertEquals(s.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }*/

    @Test
    public void testFileSelect() throws Exception {

        Main.main(new String[]{"file", "select"});
        assertEquals(Message.ARGUMENT_MANQUANT.toString().trim(), outContent.toString().trim());
        outContent.reset();

        Main.main(new String[]{"file", "select", "testFile"});
        assertEquals(Message.FICHIER_CREATION.toString().trim(), outContent.toString().trim());
        outContent.reset();

        Main.main(new String[]{"file", "select", "testFile"});
        assertEquals(Message.FICHIER_LOAD.toString().trim(), outContent.toString().trim());
        outContent.reset();

        File file = new File(data.PATH);
        file.delete();
    }
}
