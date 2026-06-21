package com.studycards;

import com.studycards.database.DatabaseManager;
import com.studycards.model.StudySet;
import com.studycards.view.MainView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hauptklasse der StudyCards-Anwendung.
 * Hauptklasse der StudyCards-Anwendung.
 */
public class App extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) {
        DatabaseManager.initialize();

        MainView mainView = new MainView(this);
        scene = new Scene(mainView.getRoot(), 900, 600);

        var css = getClass().getResource("/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("StudyCards");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> DatabaseManager.close());
    }

    public void showMainView() {
        scene.setRoot(new MainView(this).getRoot());
    }


    public void showSetDetailView(StudySet set) {
        System.out.println("SetDetailView kommt noch...");
    }

    public void showStudyModeView(StudySet set) {
        System.out.println("StudyModeView kommt noch...");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
