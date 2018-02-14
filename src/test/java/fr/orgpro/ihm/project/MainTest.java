package fr.orgpro.ihm.project;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;


public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

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
    public void testMainHelp() throws Exception {
        Main.main(new String[]{"help"});
        assertEquals(Message.MAIN_HELP.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }

    @Test
    public void testMainDefault() throws Exception {
        Main.main(new String[]{"uzfhgizfhzih"});
        assertEquals(Message.COMMANDE_INCONNUE.toString().trim(), outContent.toString().trim());
        outContent.reset();
    }

}
