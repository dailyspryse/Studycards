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
 * Hauptansicht der Anwendung.
 * Zeigt alle Lernsets in einer Liste mit Verwaltungs-Buttons.
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

    /**
     * Baut die Oberfläche auf.
     */
    private void buildUI() {
        root = new BorderPane();
        root.setPadding(new Insets(20));

        // Titel oben
        Label titleLabel = new Label("StudyCards");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitleLabel = new Label("Deine digitalen Lernkarten");
        subtitleLabel.setStyle("-fx-text-fill: #666;");

        VBox header = new VBox(5, titleLabel, subtitleLabel);
        header.setPadding(new Insets(0, 0, 20, 0));
        root.setTop(header);

        // Liste der Lernsets in der Mitte
        listView = new ListView<>();
        setList = FXCollections.observableArrayList();
        listView.setItems(setList);
        listView.setPlaceholder(new Label("Noch keine Lernsets vorhanden.\nKlicke 'Neues Set' um eins zu erstellen!"));

        // Bei Auswahl die Info rechts aktualisieren
        listView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateInfo(newVal));

        root.setCenter(listView);

        // Info-Box rechts oben
        infoLabel = new Label("Wähle ein Lernset aus");
        infoLabel.setWrapText(true);

        VBox infoBox = new VBox(10, infoLabel);
        infoBox.setPadding(new Insets(10));
        infoBox.setPrefWidth(250);
        infoBox.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 8;");

        // Buttons rechts unten
        Button newSetBtn = new Button("Neues Set");
        Button editBtn = new Button("Bearbeiten");
        Button deleteBtn = new Button("Löschen");
        Button openBtn = new Button("Karten anzeigen");
        Button learnBtn = new Button("Lernen starten");

        newSetBtn.setMaxWidth(Double.MAX_VALUE);
        editBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        openBtn.setMaxWidth(Double.MAX_VALUE);
        learnBtn.setMaxWidth(Double.MAX_VALUE);
        learnBtn.setStyle("-fx-base: #4CAF50;");

        newSetBtn.setOnAction(e -> createNewSet());
        editBtn.setOnAction(e -> editSelectedSet());
        deleteBtn.setOnAction(e -> deleteSelectedSet());
        openBtn.setOnAction(e -> openSelectedSet());
        learnBtn.setOnAction(e -> startLearning());

        VBox buttonBox = new VBox(8, newSetBtn, editBtn, deleteBtn,
                new Separator(), openBtn, learnBtn);
        buttonBox.setPadding(new Insets(10));

        VBox rightSide = new VBox(15, infoBox, buttonBox);
        rightSide.setPrefWidth(250);
        root.setRight(rightSide);
        BorderPane.setMargin(rightSide, new Insets(0, 0, 0, 15));
    }

    /**
     * Lädt alle Sets aus der Datenbank in die Liste.
     */
    private void loadSets() {
        setList.clear();
        setList.addAll(setDao.findAll());
    }

    /**
     * Zeigt Infos zum ausgewählten Set an.
     */
    private void updateInfo(StudySet set) {
        if (set == null) {
            infoLabel.setText("Wähle ein Lernset aus");
            return;
        }
        int count = cardDao.countBySetId(set.getId());
        String info = "Name: " + set.getName() + "\n\n";
        if (set.getDescription() != null && !set.getDescription().isEmpty()) {
            info += "Beschreibung:\n" + set.getDescription() + "\n\n";
        }
        info += "Anzahl Karten: " + count;
        infoLabel.setText(info);
    }

    /**
     * Dialog zum Erstellen eines neuen Sets.
     */
    private void createNewSet() {
        Dialog<StudySet> dialog = new Dialog<>();
        dialog.setTitle("Neues Lernset");
        dialog.setHeaderText("Neues Lernset erstellen");

        ButtonType createBtn = new ButtonType("Erstellen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createBtn, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextArea descField = new TextArea();
        descField.setPromptText("Beschreibung (optional)");
        descField.setPrefRowCount(3);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Beschreibung:"), 0, 1);
        grid.add(descField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == createBtn && !nameField.getText().trim().isEmpty()) {
                return new StudySet(nameField.getText().trim(), descField.getText().trim());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(set -> {
            setDao.insert(set);
            loadSets();
        });
    }

    /**
     * Dialog zum Bearbeiten des ausgewählten Sets.
     */
    private void editSelectedSet() {
        StudySet selected = listView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Bitte wähle zuerst ein Lernset aus.");
            return;
        }

        Dialog<StudySet> dialog = new Dialog<>();
        dialog.setTitle("Lernset bearbeiten");

        ButtonType saveBtn = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField nameField = new TextField(selected.getName());
        TextArea descField = new TextArea(selected.getDescription());
        descField.setPrefRowCount(3);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Beschreibung:"), 0, 1);
        grid.add(descField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == saveBtn && !nameField.getText().trim().isEmpty()) {
                selected.setName(nameField.getText().trim());
                selected.setDescription(descField.getText().trim());
                return selected;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(set -> {
            setDao.update(set);
            loadSets();
        });
    }

    /**
     * Löscht das ausgewählte Set nach Bestätigung.
     */
    private void deleteSelectedSet() {
        StudySet selected = listView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Bitte wähle zuerst ein Lernset aus.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Löschen");
        confirm.setHeaderText("\"" + selected.getName() + "\" wirklich löschen?");
        confirm.setContentText("Alle Karten werden mitgelöscht.");

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                setDao.delete(selected.getId());
                loadSets();
            }
        });
    }

    private void openSelectedSet() {
        StudySet selected = listView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Bitte wähle zuerst ein Lernset aus.");
            return;
        }
        app.showSetDetailView(selected);
    }

    private void startLearning() {
        StudySet selected = listView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Bitte wähle zuerst ein Lernset aus.");
            return;
        }
        if (cardDao.countBySetId(selected.getId()) == 0) {
            showAlert("Dieses Set hat noch keine Karten!");
            return;
        }
        app.showStudyModeView(selected);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Parent getRoot() {
        return root;
    }
}
