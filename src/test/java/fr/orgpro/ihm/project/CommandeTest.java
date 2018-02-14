package fr.orgpro.ihm.project;

import fr.orgpro.api.project.Tache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class CommandeTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private Data data;

    @Before
    public void data(){
        data = Data.getInstance();
        data.getListeTache().add(new Tache("titre 1"));
        data.getListeTache().add(new Tache("test"));
        data.getListeTache().add(new Tache("je suis une tache"));
        data.getListeTache().add(new Tache("chameaux"));
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
    }
}
