package com.studycards;

import com.studycards.database.DatabaseManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hauptklasse der StudyCards-Anwendung.
 * Hauptklasse der Anwendung.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        DatabaseManager.initialize();

        // Platzhalter - wird spaeter durch richtige Views ersetzt
        Label placeholder = new Label("StudyCards - GUI kommt noch...");
        placeholder.setStyle("-fx-font-size: 20px;");

        StackPane root = new StackPane(placeholder);
        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("StudyCards");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> DatabaseManager.close());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
