import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import diff.diff_match_patch;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Class for backend
 */
public class Controller {
    private File source;
    private File target;
    private final int SOURCE_FILE = 1;
    private final int TARGET_FILE = 2;

    public File getSource() {
        return this.source;
    }

    public void setSource(File first) {
        this.source = first;
    }

    public File getTarget() {
        return this.target;
    }

    public void setTarget(File second) {
        this.target = second;
    }

    /**
     * return the text of the source file
     * or the text of the target file
     *
     * @param option
     * @return text
     */
    public String getText(int option) {
        PDFTextStripper pdfStripper;
        PDDocument pdDoc;
        COSDocument cosDoc;
        String text = new String();
        try {
            PDFParser parser;
            if (option == 1)
                parser = new PDFParser(new FileInputStream(this.getSource()));
            else
                parser = new PDFParser(new FileInputStream(this.getTarget()));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(5);
            text = pdfStripper.getText(pdDoc);
            pdDoc.close();
            cosDoc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Get the text of those pdfs and make a list.
     * We call smecherie to print the output.
     */
    public void printOutput() {
        String text1 = this.getText(SOURCE_FILE);
        String text2 = this.getText(TARGET_FILE);
        diff_match_patch diffMatchPatch = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> list = diffMatchPatch.diff_main(text1, text2);
        smecherie(list);
    }

    /**
     * Create a new frame and display the whole content
     * with colors: WHITE, RED, GREEN.
     *
     * @param list
     */
    public void smecherie(LinkedList<diff_match_patch.Diff> list) {
        JTextPane display = new JTextPane();
        display.setFont(new Font("Arial Black", 15, 15));
        display.setEditable(false);
        JScrollPane scroll = new JScrollPane(display);
        StyledDocument doc = display.getStyledDocument();
        Style style = display.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, Color.BLACK);
        JFrame frame = new JFrame();
        frame.add(scroll);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        for (diff_match_patch.Diff diff : list) {
            if (diff.operation.compareTo(diff_match_patch.Operation.EQUAL) == 0)
                StyleConstants.setBackground(style, Color.WHITE);
            else if (diff.operation.compareTo(diff_match_patch.Operation.DELETE) == 0)
                StyleConstants.setBackground(style, Color.RED);
            else if (diff.operation.compareTo(diff_match_patch.Operation.INSERT) == 0)
                StyleConstants.setBackground(style, Color.GREEN);
            try {
                doc.insertString(doc.getLength(), diff.text, style);
            } catch (BadLocationException e) {
            }
        }
    }

    /**
     * Main function for spell checking
     */
    public void spellChecking() {
        String text;
        SpellChecking spellChecking = new SpellChecking();
        spellChecking.chooseFile();
        this.setSource(spellChecking.getFile());
        text = this.getText(SOURCE_FILE);
        String[] words = text.split("\\W+");
        DatabaseWords databaseWords = new DatabaseWords();
        databaseWords.createTable();
        databaseWords.createConnection();
        databaseWords.displayData();

        //processWords(databaseWords, words);
    }

    /**
     * Process the words from db
     *
     * @param databaseWords
     * @param words
     */
    private void processWords(DatabaseWords databaseWords, String[] words) {
        for (String word : words) {
            try {
                int value = Integer.parseInt(word);
            } catch (Exception e) {
                if (databaseWords.searchFor(word) == false)
                    System.out.println(word + " not found.");
            }
        }
    }
}
