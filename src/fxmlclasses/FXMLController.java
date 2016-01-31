package fxmlclasses;

import controller.Controller;
import diff.diff_match_patch;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class FXMLController implements Initializable {

    @FXML
    private TextArea sourceArea;
    @FXML
    private TextArea targetArea;
    @FXML
    private Button sourceButton;
    @FXML
    private Button targetButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button exitButton;

    private File sourceFile;
    private File targetFile;
    private Controller controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sourceButton.setText("Select source file");
        targetButton.setText("Select target file");
        resetButton.setText("Reset");
        exitButton.setText("Exit");
        sourceArea.setEditable(false);
        targetArea.setEditable(false);
        setSourceAreaText("Please select source file!");
        setTargetAreaText("Please select target file!");
        reset();
    }

    public void setSourceAreaText(String text) {
        sourceArea.setText(text);
    }

    public String getSourceAreaText() {
        return sourceArea.getText();
    }

    public void setTargetAreaText(String text) {
        targetArea.setText(text);
    }

    public String getTargetAreaText() {
        return targetArea.getText();
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public File getTargetFile() {
        return targetFile;
    }

    @FXML
    public void reset() {
        this.setSourceFile(null);
        this.setTargetFile(null);
        this.setSourceAreaText("Please select source file!");
        this.setTargetAreaText("Please select target file!");
    }

    @FXML
    public void exit() {
        System.exit(-1);
    }

    @FXML
    public void chooseSourceFile() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            this.setSourceFile(selectedFile);
            this.setSourceAreaText("");
        } else {
            this.setSourceFile(null);
        }
    }

    @FXML
    public void chooseTargetFile() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            this.setTargetFile(selectedFile);
            this.setTargetAreaText("");
        } else {
            this.setTargetFile(null);
        }
    }

    public void printOutput() {
        controller.setSource(this.getSourceFile());
        controller.setTarget(this.getTargetFile());
        String sourceAreaText = controller.getText(1);
        String targetAreaText = controller.getText(2);
        diff_match_patch diffMatchPatch = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> list = diffMatchPatch.diff_main(sourceAreaText, targetAreaText);
        for (diff_match_patch.Diff diff : list) {
            if (diff.operation.compareTo(diff_match_patch.Operation.EQUAL) == 0) {
                sourceArea.setStyle("-fx-highlight-fill: black; -fx-highlight-text-fill: white; -fx-font-size: 20px;");
                targetArea.setStyle("-fx-highlight-fill: black; -fx-highlight-text-fill: white; -fx-font-size: 20px;");
                setSourceAreaText(getSourceAreaText() + diff.text);
                setTargetAreaText(getTargetAreaText() + diff.text);
            } else if (diff.operation.compareTo(diff_match_patch.Operation.DELETE) == 0) {
                sourceArea.setStyle("-fx-highlight-fill: black; -fx-highlight-text-fill: red; -fx-font-size: 20px;");
                setSourceAreaText(getSourceAreaText() +  diff.text);
            } else if (diff.operation.compareTo(diff_match_patch.Operation.INSERT) == 0) {
                targetArea.setStyle("-fx-highlight-fill: black; -fx-highlight-text-fill: green; -fx-font-size: 20px;");
                setTargetAreaText(getTargetAreaText() + diff.text);
            }
        }
    }

    @FXML
    public void startFindingDifferences() {
        //TODO
        if (this.getSourceFile() == null)
            this.setSourceAreaText("YOU DIDN'T SELECT THE SOURCE FILE!!!");
        if (this.getTargetFile() == null)
            this.setTargetAreaText("YOU DIDN'T SELECT THE TARGET FILE!!!");
        if (this.getSourceFile() != null && this.getTargetFile() != null) {
            controller = new Controller();
            controller.setSource(this.getSourceFile());
            controller.setTarget(this.getTargetFile());
            printOutput();
        }

    }
}
