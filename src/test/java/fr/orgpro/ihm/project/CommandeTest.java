package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;
import org.junit.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

import static org.junit.Assert.assertEquals;

public class CommandeTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private static Data data;
    private static File file;

    @BeforeClass
    public static void setup() throws Exception {
        Main.main(new String[]{"file", "select", "test"});
        data = Data.getInstance();
        file = new File(data.getPath());
    }

    @AfterClass
    public static void deleteSetup() throws Exception {
        new File(data.getDossierCourant()).delete();
    }

    @Before
    public void data() throws Exception {
        file = new File(data.getPath());
    }

    @After
    public void resetData(){
        outContent.reset();
        file.delete();
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
        Main.main(new String[]{"task"});
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
        Main.main(new String[]{"task", "help"});
        assertEquals(Message.TACHE_HELP.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }

    @Test
    public void testTacheDefault() throws Exception {
        Main.main(new String[]{"task", "azeaze"});
        assertEquals(Message.ARGUMENT_INVALIDE.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }

    @Test
    public void testCommandeList() throws Exception {
        Main.main(new String[]{"task", "add", "1"});
        Main.main(new String[]{"task", "add", "2"});
        outContent.reset();
        Main.main(new String[]{"list", "tache"});
        StringBuilder s = new StringBuilder();
        for (Tache tache : data.getListeTache()){
            s.append(tache.toString());
        }
        assertEquals(s.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }

    /*@Test
    public void testFileSelect() throws Exception {
        //new File(data.DOSSIER_COURANT).mkdirs();

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
    }*/

    @Test
    public void testTaskList() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 3"});
        outContent.reset();
        Main.main(new String[]{"task", "list"});
        int i = 0;
        StringBuilder msg = new StringBuilder(data.getListeTache().size() + " résultat(s).\n");
        for (Tache tache : data.getListeTache()) {
            msg.append("n°").append(i).append(" ").append(tache.getTitle()).append(" ").append(tache.getId());
            if(tache.getClock() != null){
                msg.append(" ").append(tache.getClock());
            }
            msg.append("\n");
            i++;
        }
        msg = new StringBuilder(msg.toString().trim());
        assertEquals(data.getListeTache().size(), 3);
        assertEquals(outContent.toString().trim(), msg.toString());
        outContent.reset();
    }

    @Test
    public void testDeleteTask() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "delete"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "delete", "42"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "delete", "aze"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "delete", "1"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_SUCCES.toString().trim());
        outContent.reset();

        assertEquals(data.getListeTache().size(), 2);

    }
}
