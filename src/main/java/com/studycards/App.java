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
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();

        stage.setOnCloseRequest(e -> DatabaseManager.close());
    }

    public void showMainView() {
        scene.setRoot(new MainView(this).getRoot());
    }

    public void showSetDetailView(StudySet set) {
        scene.setRoot(new SetDetailView(this, set).getRoot());
    }

    public void showStudyModeView(StudySet set) {
        scene.setRoot(new StudyModeView(this, set).getRoot());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
