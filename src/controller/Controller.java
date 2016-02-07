package controller;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
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
            pdfStripper.setEndPage(16);
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
     * We call displayContent to print the output.
     */
    public void printOutput() {
        String text1 = this.getText(SOURCE_FILE);
        String text2 = this.getText(TARGET_FILE);
        diff_match_patch diffMatchPatch = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> list = diffMatchPatch.diff_main(text1, text2);
        displayContent(list);
    }

    /**
     * Create a new frame and display the whole content
     * with colors: WHITE, RED, GREEN.
     *
     * @param list
     */
    public void displayContent(LinkedList<diff_match_patch.Diff> list) {
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
                e.printStackTrace();
            }
        }
    }

    /**
     * this method check if a string is a link
     *
     * @param word
     * @return
     */
    private boolean isLink(String word) {
        return word.startsWith("www");
    }

    /**
     * This method verifies if a string is number
     *
     * @param s
     * @return true/false
     */
    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9')
                return true;
        return false;
    }

    /**
     * check if a word is name
     *
     *
     * @param tableNames
     * @param word
     * @param i
     * @return true/false
     */
    private boolean isName(Table<Integer, Integer, String> tableNames, String word, int i) {
        if (!word.isEmpty()) {
            for (int j = 0; j < tableNames.row(i).size(); j++) {
                if (tableNames.get(i, j + 1).toLowerCase().equals(word.toLowerCase()))
                    return true;
            }
            return false;
        }
        return false;
    }

    /**
     * this method add to dictionary one word
     *
     * @param word
     */
    private void addToDictionary(String word) {
        try {
            File file = new File("G:\\Informatica\\java workspace\\Comparing PDFS\\src\\resources\\dictionary\\wordscopy.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = null;
            PrintWriter pw = null;
            fw = new FileWriter(file, true);
            pw = new PrintWriter(fw);
            pw.write("\n" + word);
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method verify if one word incorrect spelled was printed.
     *
     * @param incorrectSpelled
     * @param word
     * @return true/false
     */
    private boolean wasPrinted(HashMap incorrectSpelled, String word) {
        return incorrectSpelled.get(word) != null;
    }

    /**
     * this method returns the option of the user
     * if he want to add the word to dictionary or not
     *
     * @param word
     * @return 0/1
     */
    private int getOption(String word) {
        System.out.println(word + " does not exists.");
        return JOptionPane.showConfirmDialog(null, word + " doesn't exists.\nDo you want to add this word to dictionary?", "Dictionary", JOptionPane.YES_NO_OPTION);
    }

    /**
     * method that put english words in table
     *
     * @return
     */
    public Table<Integer, Integer, String> tableWords() {
        Table<Integer, Integer, String> table = HashBasedTable.create();
        try {
            BufferedReader br = new BufferedReader(new FileReader("G:\\Informatica\\java workspace\\Comparing PDFS\\src\\resources\\dictionary\\wordscopy.txt"));
            int i = 0;
            for (String word; (word = br.readLine()) != null; ) {
                if (word.charAt(0) >= 'a' && word.charAt(0) <= 'z')
                    i = word.charAt(0) - 97;
                else i = word.charAt(0) - 65;
                table.put(i, table.row(i).size() + 1, word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    public Table<Integer, Integer, String> tableNames() {
        Table<Integer, Integer, String> table = HashBasedTable.create();
        try {
            BufferedReader br = new BufferedReader(new FileReader("G:\\Informatica\\java workspace\\Comparing PDFS\\src\\resources\\names\\all.txt"));
            int i = 0;
            for (String word; (word = br.readLine()) != null; ) {
                if (!isNumber(word)) {
                    if (word.charAt(0) >= 'a' && word.charAt(0) <= 'z')
                        i = word.charAt(0) - 97;
                    else i = word.charAt(0) - 65;
                    table.put(i, table.row(i).size() + 1, word);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }


    /**
     * verify if word exists in table
     *
     * @param table
     * @param word
     * @param i
     * @return true/false
     */
    private boolean verifyInTable(Table<Integer, Integer, String> table, String word, int i) {
        for (int j = 0; j < table.row(i).size(); j++) {
            if (table.get(i, j + 1).toLowerCase().equals(word.toLowerCase()))
                return true;
        }
        return false;
    }

    /**
     * split the words and verify every word if exists
     *
     * @param word
     * @param table
     * @return true/false
     */

    private boolean splittedWordsExists(String word, Table<Integer, Integer, String> table) {
        String words[] = word.split("\\W+");
        for (int k = 0; k < words.length; k++) {
            int x = words[k].charAt(0) >= 'a' && words[k].charAt(0) <= 'z' ? words[k].charAt(0) - 97 : words[k].charAt(0) - 65;
            if (!verifyInTable(table, words[k], x))
                return false;
        }
        return true;
    }


    private boolean containsOnlyLetter(String word) {
        for(int i = 0; i < word.length(); i++)
            if (!Character.isLetter(word.charAt(i)))
                return false;
        return true;
    }

    /**
     * Main function for spell checking
     */
    public void spellChecking() {
        String text;
        String[] words;
        FileChooser fileChooser = new FileChooser();

        fileChooser.chooseFile();
        this.setSource(fileChooser.getFile());
        text = this.getText(SOURCE_FILE);
        words = text.split("\\s+");
        for (int i = 0; i < words.length; i++)
            if (!words[i].contains("-") && !words[i].contains("/"))
                words[i] = words[i].replaceAll("[^\\w]", "");
        Table<Integer, Integer, String> table = tableWords();
        printSpellChecking(table, words);
    }

    /**
     * method that prints the output
     *
     * @param table
     * @param words
     */
    public void printSpellChecking(Table<Integer, Integer, String> table, String[] words) {
        HashMap incorrectSpelled = new HashMap();
        HashMap added = new HashMap();
        Table<Integer, Integer, String> tableNames = tableNames();
        int add = 0, incorrect = 0;
        boolean wasPrintedSmth = false;

        for (int k = 0; k < words.length; k++) {
            if (!words[k].isEmpty()) {
                int i = words[k].charAt(0) >= 'a' && words[k].charAt(0) <= 'z' ? words[k].charAt(0) - 97 : words[k].charAt(0) - 65;
                if (!wasPrinted(incorrectSpelled, words[k])) {
                    if (!isName(tableNames, words[k], i) && !isNumber(words[k]) && !verifyInTable(table, words[k], i) &&
                            added.get(words[k].toLowerCase()) == null && !isLink(words[k])) {

                        if (!containsOnlyLetter(words[k])) {
                            if (!splittedWordsExists(words[k], table)) {
                                String wrongWords[] = getWrongWords(table, words[k]);
                                doTheJob(wrongWords, added, add, incorrectSpelled, incorrect);
                            }
                        }
//                        if (words[k].contains("-") || words[k].contains("/"))
//                            if (!verifySplitWords(table, words[k]))
//                                if (!words[k].startsWith("-") && !words[k].endsWith("-") && getOption(words[k]) == 0) {
//                                    addToDictionary(words[k]);
//                                    added.put(words[k], add++);
//                                    wasPrintedSmth = true;
//                                } else {
//                                    incorrectSpelled.put(words[k], incorrect++);
//                                    wasPrintedSmth = true;
//                                }
                    }
                }
            }
        }
        if (!wasPrintedSmth)
            JOptionPane.showMessageDialog(null, "Nancy, chill!\nAll words are spelled correctly!", "Spell Checking", JOptionPane.INFORMATION_MESSAGE);
        System.out.println(added);
        System.out.println(incorrectSpelled);
    }

    private String[] getWrongWords(Table<Integer, Integer, String> table, String word) {
        String[] words = word.split("\\W+");
        for (int k = 0; k < words.length; k++) {
            int x = words[k].charAt(0) >= 'a' && words[k].charAt(0) <= 'z' ? words[k].charAt(0) - 97 : words[k].charAt(0) - 65;
            if (verifyInTable(table, words[k], x))

        }
    }

    private void doTheJob(String[] wrongWords, HashMap added, int add, HashMap incorrectSpelled, int incorrect) {
    }

}