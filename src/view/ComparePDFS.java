package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ComparePDFS extends Application {

    private static Parent content;

    public static void main(String[] args) {
        launch(args);
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
