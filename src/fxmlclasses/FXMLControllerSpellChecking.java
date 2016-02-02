/*
 *                                                                                                                                                                                                                                                                                                                                     98
 *
 */

package fxmlclasses;

import com.google.common.collect.Table;
import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Covaciu on 02/02/2016.
 */
public class FXMLControllerSpellChecking implements Initializable {

    @FXML
    private Button startButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button chooseFileButton;
    @FXML
    private TextArea textArea;

    private File spellFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startButton.setText("START");
        exitButton.setText("Exit");
        resetButton.setText("Reset file");
        chooseFileButton.setText("Select file");
        textArea.setPromptText("The text will be displayed here with red words if are incorrect spelled.");
        startButton.setDisable(true);
    }

    @FXML
    public void chooseSpellFile() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            spellFile = fc.getSelectedFile();
            textArea.setPromptText("");
            startButton.setDisable(false);
        } else {
            spellFile = null;
            startButton.setDisable(true);
            textArea.setPromptText("PLEASE SELECT THE FILE!!!!");
        }
    }

    @FXML
    public void exit() {
        System.exit(-1);
    }

    @FXML
    public void reset() {
        spellFile = null;
        textArea.setText("");
        textArea.setPromptText("The text will be displayed here with red words if are incorrect spelled.");
        startButton.setDisable(true);
    }

    @FXML
    public void startSpellChecking() {
        Controller controller = new Controller();
        String text;
        String[] words;

        controller.setSource(spellFile);
        text = controller.getText(1);
        textArea.setText(text);
        words = text.split("\\s+");
        for (int i = 0; i < words.length; i++)
            if (!words[i].contains("-") && !words[i].contains("/"))
                words[i] = words[i].replaceAll("[^\\w]", "");
        Table<Integer, Integer, String> table = controller.tableWords();
        controller.printSpellChecking(table, words);
    }
}
