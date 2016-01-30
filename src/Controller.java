import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

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
            }
        }
    }

    /**
     * This method verify if the string is abbreviation.
     *
     * @param word
     * @return true/false
     */
    private boolean isAbbreviation(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) < 'A' || word.charAt(i) > 'Z')
                return false;
        }
        return true;
    }

    /**
     * This method verify if the string is deviation of the word.
     *
     * @param word
     * @return true/false
     */
    private boolean isDeviation(String dictionary[], String word) {
        for (String s : dictionary) {
            if (s.length() != 1 && (s.startsWith(word) || s.endsWith(word)))
                return true;
        }
        return false;
    }


    /**
     * this method check if a string is a link
     *
     * @param word
     * @return
     */
    private boolean isLink(String word) {
        if (word.startsWith("www"))
            return true;
        return false;
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
     * We read the dictionary file text
     * and we add the new word to our String[] fileWords
     *
     * @param fileWords
     * @param newWord
     * @return myWords
     */
    private String[] addNextWord(String[] fileWords, String newWord) {
        String[] myWords = new String[fileWords.length + 1];
        int i;

        for (i = 0; i < fileWords.length; i++)
            myWords[i] = fileWords[i];
        myWords[i] = newWord;
        return myWords;
    }

    /**
     * This method verify if the word exists in the dictionary.
     * If the word exists => true
     * Otherwise => false
     *
     * @param dictionary
     * @param word
     * @return true/false
     */
    private boolean wordExist(String[] dictionary, String word) {
        for (int i = 0; i < dictionary.length; i++) {
            if (!word.isEmpty() && dictionary[i].toLowerCase().equals(word.toLowerCase()))
                return true;
        }
        return false;
    }

    private int getOption(String word) {
        System.out.println(word + " does not exists.");
        int option = JOptionPane.showConfirmDialog(null, word + " doesn't exists.\nDo you want to add this word to dictionary?", "Dictionary", JOptionPane.YES_NO_OPTION);
        return option;
    }

    /**
     * Get the english words and put them into String[]
     *
     * @return fileWords
     */
    private String[] getEnglishWords() {
        String[] fileWords = new String[0];

        try (BufferedReader br = new BufferedReader(new FileReader("G:\\Informatica\\java workspace\\Comparing PDFS\\src\\resources\\dictionary\\wordscopy.txt"))) {
            for (String word; (word = br.readLine()) != null; ) {
                fileWords = addNextWord(fileWords, word.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileWords;
    }

    /**
     * Main function for spell checking
     */
    public void spellChecking() {
        String text;
        String[] dictionary;
        String[] words;
        SpellChecking spellChecking = new SpellChecking();

        spellChecking.chooseFile();
        this.setSource(spellChecking.getFile());
        text = this.getText(SOURCE_FILE);
        words = text.split("\\s+");
        for (int i = 0; i < words.length; i++)
            if (!words[i].contains("-") && !words[i].contains("/"))
                words[i] = words[i].replaceAll("[^\\w]", "");
        dictionary = getEnglishWords();
        printSpellChecking(dictionary, words);
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
        if (incorrectSpelled.get(word) == null)
            return false;
        return true;
    }

    /**
     * This method print the words that are not spelled correctly.
     *
     * @param dictionary
     * @param words
     */
    private void printSpellChecking(String[] dictionary, String[] words) {
        FileOutputStream f = null;
        HashMap incorrectSpelled = new HashMap();
        HashMap added = new HashMap();

        try {
            f = new FileOutputStream("output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintStream stdout = System.out;

        System.setOut(new PrintStream(f));
        for (int i = 0, j = 0, k = 0; i < words.length; i++) {
            if (!wasPrinted(incorrectSpelled, words[i])) {
                if (!isAbbreviation(words[i]) && !isNumber(words[i]) && !wordExist(dictionary, words[i]) &&
                        added.get(words[i].toLowerCase()) == null && !isLink(words[i])) {

                    if (getOption(words[i]) == 0) {
                        addToDictionary(words[i]);
                        added.put(words[i], j++);
                    } else {
                        incorrectSpelled.put(words[i], k++);
                    }
                }
            }
        }
        System.setOut(stdout);
        System.out.println(added);
        System.out.println(incorrectSpelled);
    }

}
