package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;
import org.junit.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Test
    public void testTaskRename() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "rename", "1"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "rename", "42", "test"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "rename", "aze", "test"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "rename", "1", "test"});
        assertEquals(outContent.toString().trim(), Message.TACHE_RENAME_SUCESS.toString().trim());
        outContent.reset();

        assertEquals(data.getListeTache().get(1).getTitle(), "test");
    }

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
        Main.main(new String[]{"list", "task"});
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
    public void testTaskDelete() throws Exception {
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

    @Test
    public void testTaskDeadline() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "dl", "1"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dl", "42", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dl", "0", "2018/02/42"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_DEADLINE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dl", "0", "2018/02-02"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_DEADLINE_SUCCES.toString().trim());
        outContent.reset();

        Date date = new Date("2018/02/02");
        assertEquals(data.getListeTache().get(0).getDeadline(), date);

        /*Main.main(new String[]{"task", "dl", "0", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_DEADLINE_SUCCES.toString().trim());
        outContent.reset();*/

        // TODO
        // assertEquals(data.getListeTache().get(0).getDeadline().toString(), null);
    }

    @Test
    public void testTaskClosed() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "cl", "1"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "cl", "42", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "cl", "0", "2018/02/42"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_CLOSED_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "cl", "0", "2018/02-02"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_CLOSED_SUCCES.toString().trim());
        outContent.reset();

        Date date = new Date("2018/02/02");
        assertEquals(data.getListeTache().get(0).getClosed(), date);

        /*Main.main(new String[]{"task", "cl", "0", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_CLOSED_SUCCES.toString().trim());
        outContent.reset();*/

        // TODO
        // assertEquals(data.getListeTache().get(0).getDeadline().toString(), null);
    }

    @Test
    public void testTaskScheduled() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "sd", "1"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "sd", "42", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "sd", "0", "2018/02/42"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_SCHEDULED_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "sd", "0", "2018/02-02"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_SCHEDULED_SUCCES.toString().trim());
        outContent.reset();

        Date date = new Date("2018/02/02");
        assertEquals(data.getListeTache().get(0).getScheduled(), date);

        /*Main.main(new String[]{"task", "sd", "0", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_SCHEDULED_SUCCES.toString().trim());
        outContent.reset();*/

        // TODO
        // assertEquals(data.getListeTache().get(0).getDeadline().toString(), null);
    }

    @Test
    public void testTaskTagAdd() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "tag", "add"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "tag", "add", "0"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "tag", "add", "aze", "test"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "tag", "add", "42", "test"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "tag", "add", "1", "test"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_TAG_SUCCES.toString().trim());
        outContent.reset();

        assertEquals("test", data.getListeTache().get(1).getTags().get(0));
    }

    @Test
    public void testTaskTagDelete() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "tag", "delete"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "tag", "delete", "0"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "tag", "delete", "aze", "test"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "tag", "delete", "42", "test"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "tag", "add", "1", "test"});
        Main.main(new String[]{"task", "tag", "add", "1", "autre"});
        outContent.reset();

        Main.main(new String[]{"task", "tag", "delete", "1", "autre"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_TAG_SUCCES.toString().trim());
        outContent.reset();

        assertEquals("test", data.getListeTache().get(1).getTags().get(0));
        assertEquals(1, data.getListeTache().get(1).getTags().size());
    }

    @Test
    public void testTaskTag() throws Exception {
        Main.main(new String[]{"task", "tag"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "tag", "zfzfzf"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();
    }

    @Test
    public void testTaskProperty() throws Exception {
        Main.main(new String[]{"task", "property"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "zfzfzf"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();
    }

    @Test
    public void testTaskPropertyAdd() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "property", "add"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "add", "0"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "add", "aze", "clef", "je suis une valeur"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "add", "-1", "clef", "je suis une valeur"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "add", "1", "clef", "je suis une valeur"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_PROPRIETE_SUCCES.toString().trim());
        outContent.reset();

        assertEquals("je suis une valeur", data.getListeTache().get(1).getProperties().get("clef"));

        Main.main(new String[]{"task", "property", "add", "1", "iD", "je suis une valeur"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_PROPRIETE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "add", "1", "DePendence", "je suis une valeur"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_PROPRIETE_ECHEC.toString().trim());
        outContent.reset();
    }

    @Test
    public void testTaskPropertyDelete() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "property", "delete"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "delete", "0"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "delete", "aze", "clef"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "delete", "-1", "clef",});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "add", "1", "clef", "je suis une valeur"});
        outContent.reset();

        Main.main(new String[]{"task", "property", "delete", "1", "clef", "je suis une valeur"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_PROPRIETE_SUCCES.toString().trim());
        outContent.reset();

        assertEquals(null, data.getListeTache().get(1).getProperties().get("clef"));

        Main.main(new String[]{"task", "property", "delete", "1", "iD", "je suis une valeur"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_PROPRIETE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "property", "delete", "1", "DePendence", "je suis une valeur"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_PROPRIETE_ECHEC.toString().trim());
        outContent.reset();
    }

    @Test
    public void testTaskDep() throws Exception {
        Main.main(new String[]{"task", "dep"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "zfzfzf"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();
    }

    @Test
    public void testTaskDepSet() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "dep", "set"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "set", "aze", "clef"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "set", "-1", "2",});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "set", "1", "1",});
        assertEquals(outContent.toString().trim(), Message.TACHES_NON_IDENTIQUES_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "set", "0", "1",});
        assertEquals(outContent.toString().trim(), Message.TACHE_SET_DEPENDANCE_SUCCES.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "set", "0", "1",});
        assertEquals(outContent.toString().trim(), Message.TACHE_SET_DEPENDANCE_ECHEC.toString().trim());
        outContent.reset();
    }

    @Test
    public void testTaskDepDelete() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "dep", "delete"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "delete", "aze"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "delete", "-1"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "set", "0", "1"});
        outContent.reset();

        Main.main(new String[]{"task", "dep", "delete", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_DEPENDANCE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "dep", "delete", "1"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_DEPENDANCE_SUCCES.toString().trim());
        outContent.reset();
    }

    @Test
    public void testTaskState() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        Main.main(new String[]{"task", "add", "tache 2"});
        Main.main(new String[]{"task", "add", "tache 32"});
        outContent.reset();

        Main.main(new String[]{"task", "state"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "state", "aze", "aze"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "state", "-1", "todo"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "state", "0", "azeaez"});
        assertEquals(outContent.toString().trim(), Message.STATE_INTROUVABLE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "state", "0", "ongoing"});
        assertEquals(outContent.toString().trim(), Message.STATE_UPDATE_SUCCES.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "state", "0", "ongoing"});
        assertEquals(outContent.toString().trim(), Message.STATE_UPDATE_ECHEC.toString().trim());
        outContent.reset();
    }

    @Test
    public void testListTS() throws Exception {
        Main.main(new String[]{"list","ts"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();
        Main.main(new String[]{"list","ts","TODO"});
        assertEquals(outContent.toString().trim(), Message.LIST_AUCUN_RESULTAT.toString().trim());
        outContent.reset();
        String title = "tache 1";
        Main.main(new String[]{"task", "add", title});
        outContent.reset();
        Main.main(new String[]{"list","ts","DONE"});
        assertEquals(outContent.toString().trim(), Message.LIST_AUCUN_RESULTAT.toString().trim());
        outContent.reset();
        Main.main(new String[]{"list", "ts", "TODO"});
        List<Tache> taches = new ArrayList<Tache>();
        Tache t = data.getListeTache().get(0);
        taches.add(t);
        StringBuilder msg = new StringBuilder();
        int i = 0;
        for (Tache tache : data.getListeTache()) {
            if(taches.contains(tache)) {
                msg.append("n°").append(i).append(" ").append(tache.getTitle()).append(" ").append(tache.getId());
                if (tache.getClock() != null) {
                    msg.append(" ").append(tache.getClock());
                }
                msg.append("\n");
            }
            i++;
        }
        msg.append(data.getListeTache().size() + " résultat(s).\n");
        msg = new StringBuilder(msg.toString().trim());
        assertEquals(outContent.toString().trim(), msg.toString());
        outContent.reset();
    }
}
