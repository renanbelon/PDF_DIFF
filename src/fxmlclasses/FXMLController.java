package fxmlclasses;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLController implements Initializable {

    @FXML
    private Button sourceButton;
    @FXML
    private Button targetButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label sourcePane;
    @FXML
    private Label targetPane;

    private File sourceFile;
    private File targetFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sourceButton.setText("Select source file");
        targetButton.setText("Select target file");
        resetButton.setText("Reset");
        exitButton.setText("Exit");
        setSourcePaneText("Initialize source pane");
        setTargetPaneText("Initialize target pane");
        reset();
    }

    public void setSourcePaneText(String text) {
        sourcePane.setText(text);
    }

    public String getSourcePaneText() {
        return sourcePane.getText();
    }

    public void setTargetPaneText(String text) {
        targetPane.setText(text);
    }

    public String getTargetPaneText() {
        return targetPane.getText();
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
        this.setSourcePaneText("Please select source file!");
        this.setTargetPaneText("Please select target file!");
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
            this.setSourcePaneText("");
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
            this.setTargetPaneText("");
        } else {
            this.setTargetFile(null);
        }
    }

    @FXML
    public void startFindingDifferences() {
        //TODO
        if (this.getSourceFile() == null)
            this.setSourcePaneText("YOU DIDN'T SELECT THE SOURCE FILE!!!");
        if (this.getTargetFile() == null)
            this.setTargetPaneText("YOU DIDN'T SELECT THE TARGET FILE!!!");
        if (this.getSourceFile() != null && this.getTargetFile() != null) {
            this.setSourcePaneText(this.getSourceFile().getAbsolutePath());
            this.setTargetPaneText(this.getTargetFile().getAbsolutePath());
        }
    }
}
