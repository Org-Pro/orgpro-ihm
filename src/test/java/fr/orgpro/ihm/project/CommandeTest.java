package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;
import org.junit.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        Main.main(new String[]{"task", "clock", "use", "0"});
        Main.main(new String[]{"task", "clock", "use", "0"});
        outContent.reset();
        Main.main(new String[]{"task", "list"});
        int i = 0;
        StringBuilder msg = new StringBuilder(data.getListeTache().size() + " résultat(s).\n");
        for (Tache tache : data.getListeTache()) {
            msg.append("n°").append(i).append(" ").append(tache.getTitle()).append(" ").append(tache.getId());
            if(tache.getClock() != null){
                msg.append(" ").append(tache.getClockString());
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
    public void testTaskDeleteOneTask() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        outContent.reset();
        Main.main(new String[]{"task", "delete", "0"});
        outContent.reset();
        File fw = null;
        fw = new File(data.getPath());
        assertEquals(fw.length(),0);
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
        assertEquals(outContent.toString().trim(), Message.TACHE_STATE_INTROUVABLE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "state", "0", "ongoing"});
        assertEquals(outContent.toString().trim(), Message.TACHE_STATE_UPDATE_SUCCES.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "state", "0", "ongoing"});
        assertEquals(outContent.toString().trim(), Message.TACHE_STATE_UPDATE_ECHEC.toString().trim());
        outContent.reset();
    }

    @Test
    public void testListTSIF() throws Exception {
        Main.main(new String[]{"list","ts"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();
    }

    @Test
    public void testListTS() throws Exception {
        Main.main(new String[]{"list","ts","TODO"});
        assertEquals(outContent.toString().trim(), Message.LIST_AUCUN_RESULTAT.toString().trim());
        outContent.reset();
        String title = "tache 1";
        Main.main(new String[]{"task", "add", title});
        Main.main(new String[]{"task", "clock", "use", "0"});
        Main.main(new String[]{"task", "clock", "use", "0"});
        outContent.reset();
        Main.main(new String[]{"list","ts","DONE"});
        StringBuilder msg2 = new StringBuilder();
        msg2.append(Message.LIST_STATE_DONE + "\n");
        msg2.append(Message.LIST_AUCUN_RESULTAT);
        assertEquals(outContent.toString().trim(), msg2.toString().trim());
        outContent.reset();
        Main.main(new String[]{"list", "ts", "TODO"});
        List<Tache> taches = new ArrayList<Tache>();
        Tache t = data.getListeTache().get(0);
        taches.add(t);
        StringBuilder msg = new StringBuilder();
        msg.append("\n" + Message.LIST_STATE_TODO + "\n");
        int i = 0;
        for (Tache tache : data.getListeTache()) {
            if(taches.contains(tache)) {
                msg.append("n°").append(i).append(" ").append(tache.getTitle()).append(" ").append(tache.getId());
                if (tache.getClock() != null) {
                    msg.append(" ").append(tache.getClockString());
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

    @Test
    public void testListATSIF() throws Exception {
        Main.main(new String[]{"list","ats"});
        assertEquals(outContent.toString().trim(), Message.LIST_AUCUN_RESULTAT.toString().trim());
        outContent.reset();
    }

    @Test
    public void testListATS() throws Exception {
        String title1 = "tache 1";
        String title2 = "tache 2";
        String title3 = "tache 3";
        String title4 = "tache 4";
        Main.main(new String[]{"task", "add", title1});
        outContent.reset();
        Main.main(new String[]{"task", "add", title2});
        outContent.reset();
        Main.main(new String[]{"task", "add", title3});
        outContent.reset();
        Main.main(new String[]{"task", "add", title4});
        outContent.reset();
        Main.main(new String[]{"task", "state", "1", "ongoing"});
        outContent.reset();
        Main.main(new String[]{"task", "state", "2", "done"});
        outContent.reset();
        Main.main(new String[]{"task", "state", "3", "cancelled"});
        outContent.reset();
        Main.main(new String[]{"list", "ats"});
        List<Tache> taches = new ArrayList<Tache>();

        StringBuilder msg = new StringBuilder();

        Tache t = new Tache("temp");
        int k;
        for(k = 1 ; k < 5 ; k++){
            if(k == 1){
                msg.append("\n" + Message.LIST_STATE_TODO + "\n");
                t = data.getListeTache().get(0);
                taches.add(t);
            }else if(k == 2){
                msg.append("\n" + Message.LIST_STATE_ONGOING + "\n");
                taches.clear();
                t = data.getListeTache().get(1);
                taches.add(t);
            }else if (k == 3){
                msg.append("\n" + Message.LIST_STATE_DONE + "\n");
                taches.clear();
                t = data.getListeTache().get(2);
                taches.add(t);
            }else if(k == 4){
                msg.append("\n" + Message.LIST_STATE_CANCELLED + "\n");
                taches.clear();
                t = data.getListeTache().get(3);
                taches.add(t);
            }
            int i = 0;
            int j = 0;
            for (Tache tache : data.getListeTache()) {
                if(taches.contains(tache)) {
                    msg.append("n°").append(i).append(" ").append(tache.getTitle()).append(" ").append(tache.getId());
                    if (tache.getClock() != null) {
                        msg.append(" ").append(tache.getClockString());
                    }
                    msg.append("\n");
                    j++;
                }
                i++;
            }
            msg.append(j + " résultat(s).\n");
        }

        msg = new StringBuilder(msg.toString().trim());
        assertEquals(outContent.toString().trim(), msg.toString());
        outContent.reset();
    }

    @Test
    public void testListSdSIF() throws Exception {
        Main.main(new String[]{"list","sd"});
        assertEquals(outContent.toString().trim(), Message.LIST_AUCUN_RESULTAT.toString().trim());
        outContent.reset();
    }

    @Test
    public void testListSd() throws  Exception {
        String title1 = "tache 1";
        String title2 = "tache 2";
        String title3 = "tache 3";
        String title4 = "tache 4";
        Main.main(new String[]{"task", "add", title1});
        outContent.reset();
        Main.main(new String[]{"task", "add", title2});
        outContent.reset();
        Main.main(new String[]{"task", "add", title3});
        outContent.reset();
        Main.main(new String[]{"task", "add", title4});
        outContent.reset();

        Date date = new Date();
        long time = 1000000;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Main.main(new String[]{"task", "sd", "0", df.format(new Date(date.getTime() - time))});
        outContent.reset();
        Main.main(new String[]{"task", "sd", "1", df.format(new Date(date.getTime() + time))});
        outContent.reset();
        Main.main(new String[]{"task", "sd", "2", df.format(new Date(date.getTime() + time))});
        outContent.reset();
        Main.main(new String[]{"task", "sd", "3", df.format(new Date(date.getTime() - time))});
        outContent.reset();
        Main.main(new String[]{"list", "sd"});

        List<Tache> taches = new ArrayList<Tache>();

        taches.add(data.getListeTache().get(0));
        taches.add(data.getListeTache().get(1));
        taches.add(data.getListeTache().get(2));
        taches.add(data.getListeTache().get(3));

        String msg = Commande.affichage(data, taches);
        assertEquals(outContent.toString().trim(), msg.trim());
        outContent.reset();
    }

    @Test
    public void testTaskAdd() throws Exception {
        Main.main(new String[]{"task", "add"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "add", "aze", "aze"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "add", "test", "-1"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "add", "test"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_SUCCES.toString().trim());
        outContent.reset();

        data.loadFichier();
        assertEquals(data.getListeTache().size(), 1);

        Main.main(new String[]{"task", "add", "tache avec dep", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_AVEC_DEP_SUCCES.toString().trim());
        outContent.reset();

        data.loadFichier();
        assertEquals(data.getListeTache().size(), 2);
    }

    @Test
    public void testClock() throws Exception {
        Main.main(new String[]{"task","clock"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task","clock", "aaezaeaea"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();
    }

    @Test
    public void testClockUse() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        outContent.reset();

        Main.main(new String[]{"task", "clock", "use"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "clock", "use", "aze"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "clock", "use", "-1"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "clock", "use", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_MINUTEUR_LANCER_SUCCES.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "clock", "use", "0"});
        assertEquals(outContent.toString().trim(), Message.TACHE_MINUTEUR_STOPPER_SUCCES.toString().trim());
        outContent.reset();
    }

    @Test
    public void testCollaborator() throws Exception {
        Main.main(new String[]{"task","col"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task","col", "aaezaeaea"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();
    }

    @Test
    public void testCollaboratorAdd() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        outContent.reset();

        Main.main(new String[]{"task", "col", "add"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "col", "add", "aze", "aze"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "col", "add", "-1", "aze"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "col","add", "0", "bob:"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_COLLABORATEUR_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "col", "add", "0", "bob"});
        assertEquals(outContent.toString().trim(), Message.TACHE_AJOUT_COLLABORATEUR_SUCCES.toString().trim());
        outContent.reset();
    }

    @Test
    public void testCollaboratorDelete() throws Exception {
        Main.main(new String[]{"task", "add", "tache 1"});
        outContent.reset();

        Main.main(new String[]{"task", "col", "delete"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "col", "delete", "aze", "aze"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "col", "delete", "-1", "aze"});
        assertEquals(outContent.toString().trim(), Message.TACHE_INVALIDE_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "col", "delete", "0", "bob:"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_COLLABORATEUR_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"task", "col", "delete", "0", "bob"});
        assertEquals(outContent.toString().trim(), Message.TACHE_DELETE_COLLABORATEUR_SUCCES.toString().trim());
        outContent.reset();
    }

    @Test
    public void testListDefault() throws Exception {
        Main.main(new String[]{"list", "zzzzzzz"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();
        Main.main(new String[]{"zzzzzzz"});
        assertEquals(outContent.toString().trim(), Message.COMMANDE_INCONNUE.toString().trim());
    }

    @Test
    public void testListHelp() throws Exception {
        Main.main(new String[]{"list", "help"});
        assertEquals(outContent.toString().trim(), Message.LIST_HELP.toString().trim());
        outContent.reset();
        Main.main(new String[]{"help"});
        assertEquals(outContent.toString().trim(), Message.MAIN_HELP.toString().trim());
    }

    @Test
    public void testHeader() throws Exception {
        Main.main(new String[]{"head", "help"});
        assertEquals(outContent.toString().trim(), Message.HEADER_HELP.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "aze"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_INVALIDE.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();
    }

    @Test
    public void testHeaderGet() throws Exception {
        Main.main(new String[]{"head", "add", "test", "valeur"});
        outContent.reset();

        Main.main(new String[]{"head", "get"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "get", "bob"});
        assertEquals(outContent.toString().trim(), Message.HEADER_GET_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "get", "test"});
        assertEquals(outContent.toString().trim(), "valeur");
        outContent.reset();

        Main.main(new String[]{"head", "delete", "test"});
        outContent.reset();
    }

    @Test
    public void testHeaderAdd() throws Exception {
        Main.main(new String[]{"head", "add"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "add", "test", "bob"});
        assertEquals(outContent.toString().trim(), Message.HEADER_ADD_SUCCES.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "add", " ", "bob"});
        assertEquals(outContent.toString().trim(), Message.HEADER_ADD_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "delete", "test"});
        outContent.reset();
    }

    @Test
    public void testHeaderSet() throws Exception {
        Main.main(new String[]{"head", "add", "test", "valeur"});
        outContent.reset();

        Main.main(new String[]{"head", "set"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "set", "test", "bob"});
        assertEquals(outContent.toString().trim(), Message.HEADER_SET_SUCCES.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "set", "aze", "bob"});
        assertEquals(outContent.toString().trim(), Message.HEADER_SET_ECHEC.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "delete", "test"});
        outContent.reset();
    }

    @Test
    public void testHeaderDelete() throws Exception {
        Main.main(new String[]{"head", "add", "test", "valeur"});
        outContent.reset();

        Main.main(new String[]{"head", "delete"});
        assertEquals(outContent.toString().trim(), Message.ARGUMENT_MANQUANT.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "delete", "test"});
        assertEquals(outContent.toString().trim(), Message.HEADER_DELETE_SUCCES.toString().trim());
        outContent.reset();

        Main.main(new String[]{"head", "delete", "aze"});
        assertEquals(outContent.toString().trim(), Message.HEADER_DELETE_ECHEC.toString().trim());
        outContent.reset();
    }

}
