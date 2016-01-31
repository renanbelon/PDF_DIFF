package view;

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

    public static void main(String[] args) {
        int value = JOptionPane.showOptionDialog(null, "Choose the option you want to use", "PDF",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{"Compare PDFS", "Spell checking"}, null);
        Controller controller = new Controller();
        if (value == 0)
            launch(args);
        else if (value == 1)
            controller.spellChecking();

    }

    @Override
    public void start(Stage primaryStage) {
        try {
            content = FXMLLoader.load(getClass().getResource("/view/design/design.fxml"));
            Scene scene = new Scene(content);
            primaryStage.setTitle("Nancy's Dream");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
