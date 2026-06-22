package com.studycards;

import com.studycards.database.DatabaseManager;
import com.studycards.model.StudySet;
import com.studycards.view.MainView;
import com.studycards.view.SetDetailView;
import com.studycards.view.StudyModeView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hauptklasse der StudyCards-Anwendung.
 * Startet die App und verwaltet die Navigation zwischen den Views.
 */
public class App extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) {
        // Datenbank starten
        DatabaseManager.initialize();

        // Hauptansicht anzeigen
        MainView mainView = new MainView(this);
        scene = new Scene(mainView.getRoot(), 900, 600);

        // Stylesheet laden
        var css = getClass().getResource("/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("StudyCards");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();

        // DB-Verbindung schliessen wenn Fenster zu
        stage.setOnCloseRequest(e -> DatabaseManager.close());
    }

    /** Wechselt zur Hauptansicht. */
    public void showMainView() {
        scene.setRoot(new MainView(this).getRoot());
    }

    /** Wechselt zur Detailansicht eines Sets. */
    public void showSetDetailView(StudySet set) {
        scene.setRoot(new SetDetailView(this, set).getRoot());
    }

    /** Wechselt zum Lernmodus. */
    public void showStudyModeView(StudySet set) {
        scene.setRoot(new StudyModeView(this, set).getRoot());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
