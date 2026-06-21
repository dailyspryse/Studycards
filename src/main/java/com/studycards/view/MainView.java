package com.studycards.view;

import com.studycards.App;
import com.studycards.dao.CardDao;
import com.studycards.dao.StudySetDao;
import com.studycards.model.StudySet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Optional;

/**
 * Hauptansicht - zeigt alle Lernsets.
 * Hauptansicht - zeigt alle Lernsets.
 */
public class MainView {

    private App app;
    private BorderPane root;
    private ListView<StudySet> listView;
    private ObservableList<StudySet> setList;
    private Label infoLabel;
    private StudySetDao setDao;
    private CardDao cardDao;

    public MainView(App app) {
        this.app = app;
        this.setDao = new StudySetDao();
        this.cardDao = new CardDao();
        buildUI();
        loadSets();
    }

    private void buildUI() {
        root = new BorderPane();
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("StudyCards");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));

        VBox header = new VBox(5, titleLabel);
        header.setPadding(new Insets(0, 0, 20, 0));
        root.setTop(header);

        listView = new ListView<>();
        setList = FXCollections.observableArrayList();
        listView.setItems(setList);
        listView.setPlaceholder(new Label("Noch keine Lernsets vorhanden."));

        listView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateInfo(newVal));
        root.setCenter(listView);

        infoLabel = new Label("Wähle ein Lernset aus");
        infoLabel.setWrapText(true);

        VBox infoBox = new VBox(10, infoLabel);
        infoBox.setPadding(new Insets(10));
        infoBox.setPrefWidth(250);
        infoBox.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 8;");

        // Erstmal nur Erstellen und Löschen
        Button newSetBtn = new Button("Neues Set");
        Button deleteBtn = new Button("Löschen");

        newSetBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);

        newSetBtn.setOnAction(e -> createNewSet());
        deleteBtn.setOnAction(e -> deleteSelectedSet());

        VBox buttonBox = new VBox(8, newSetBtn, deleteBtn);
        buttonBox.setPadding(new Insets(10));

        VBox rightSide = new VBox(15, infoBox, buttonBox);
        rightSide.setPrefWidth(250);
        root.setRight(rightSide);
        BorderPane.setMargin(rightSide, new Insets(0, 0, 0, 15));
    }

    private void loadSets() {
        setList.clear();
        setList.addAll(setDao.findAll());
    }

    private void updateInfo(StudySet set) {
        if (set == null) {
            infoLabel.setText("Wähle ein Lernset aus");
            return;
        }
        int count = cardDao.countBySetId(set.getId());
        infoLabel.setText("Name: " + set.getName() + "\nKarten: " + count);
    }

    
    private void createNewSet() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Neues Lernset");
        dialog.setHeaderText("Name eingeben:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                StudySet set = new StudySet(name.trim(), "");
                setDao.insert(set);
                loadSets();
            }
        });
    }

    
    private void deleteSelectedSet() {
        StudySet selected = listView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        setDao.delete(selected.getId());
        loadSets();
    }

    public Parent getRoot() {
        return root;
    }
}
