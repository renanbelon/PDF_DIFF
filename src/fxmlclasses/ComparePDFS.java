package fxmlclasses;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class ComparePDFS extends Application {

    private static Parent content;
    private static int value;

    public static void main(String[] args) {
        value = JOptionPane.showOptionDialog(null, "Choose the option you want to use", "PDF",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{"Compare PDFS", "Spell checking"}, null);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            if (value == 0)
                content = FXMLLoader.load(getClass().getResource("/resources/fxml/design.fxml"));
            else if (value == 1)
                content = FXMLLoader.load(getClass().getResource("/resources/fxml/spellCheckingDesign.fxml"));
            Scene scene = new Scene(content);
            primaryStage.setTitle("Nancy's Dream");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
