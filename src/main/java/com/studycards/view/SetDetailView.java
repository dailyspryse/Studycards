package com.studycards.view;

import com.studycards.App;
import com.studycards.dao.CardDao;
import com.studycards.model.Card;
import com.studycards.model.StudySet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Optional;

/**
 * Detailansicht für ein Lernset.
 * Zeigt alle Karten in einer Tabelle mit Buttons zum Verwalten.
 */
public class SetDetailView {

    private App app;
    private StudySet studySet;
    private BorderPane root;
    private TableView<Card> tableView;
    private ObservableList<Card> cardList;
    private CardDao cardDao;

    public SetDetailView(App app, StudySet studySet) {
        this.app = app;
        this.studySet = studySet;
        this.cardDao = new CardDao();
        buildUI();
        loadCards();
    }

    private void buildUI() {
        root = new BorderPane();
        root.setPadding(new Insets(20));

        // Header mit Zurück-Button
        Button backBtn = new Button("<- Zurück");
        backBtn.setOnAction(e -> app.showMainView());

        Label titleLabel = new Label(studySet.getName());
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        HBox headerTop = new HBox(15, backBtn, titleLabel);
        headerTop.setAlignment(Pos.CENTER_LEFT);

        VBox header = new VBox(5, headerTop);
        header.setPadding(new Insets(0, 0, 15, 0));
        root.setTop(header);

        // Tabelle mit Frage- und Antwort-Spalten
        tableView = new TableView<>();
        cardList = FXCollections.observableArrayList();
        tableView.setItems(cardList);
        tableView.setPlaceholder(new Label("Noch keine Karten. Klicke 'Neue Karte'!"));

        TableColumn<Card, String> questionCol = new TableColumn<>("Frage");
        questionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
        questionCol.setPrefWidth(370);

        TableColumn<Card, String> answerCol = new TableColumn<>("Antwort");
        answerCol.setCellValueFactory(new PropertyValueFactory<>("answer"));
        answerCol.setPrefWidth(370);

        tableView.getColumns().add(questionCol);
        tableView.getColumns().add(answerCol);
        root.setCenter(tableView);

        // Buttons rechts
        Button addBtn = new Button("Neue Karte");
        Button editBtn = new Button("Bearbeiten");
        Button deleteBtn = new Button("Löschen");
        Button learnBtn = new Button("Lernen starten");

        addBtn.setMaxWidth(Double.MAX_VALUE);
        editBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        learnBtn.setMaxWidth(Double.MAX_VALUE);
        learnBtn.setStyle("-fx-base: #4CAF50;");

        addBtn.setOnAction(e -> addCard());
        editBtn.setOnAction(e -> editCard());
        deleteBtn.setOnAction(e -> deleteCard());
        learnBtn.setOnAction(e -> {
            if (!cardList.isEmpty()) app.showStudyModeView(studySet);
        });

        VBox buttonBox = new VBox(8, addBtn, editBtn, deleteBtn,
                new Separator(), learnBtn);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setPrefWidth(170);
        root.setRight(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(0, 0, 0, 15));
    }

    private void loadCards() {
        cardList.clear();
        cardList.addAll(cardDao.findBySetId(studySet.getId()));
    }

    /**
     * Dialog zum Hinzufügen einer neuen Karte.
     */
    private void addCard() {
        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("Neue Karte");
        dialog.setHeaderText("Neue Lernkarte erstellen");

        ButtonType createBtn = new ButtonType("Erstellen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createBtn, ButtonType.CANCEL);

        TextArea questionField = new TextArea();
        questionField.setPromptText("Frage");
        questionField.setPrefRowCount(3);

        TextArea answerField = new TextArea();
        answerField.setPromptText("Antwort");
        answerField.setPrefRowCount(3);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Frage:"), 0, 0);
        grid.add(questionField, 1, 0);
        grid.add(new Label("Antwort:"), 0, 1);
        grid.add(answerField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == createBtn
                    && !questionField.getText().trim().isEmpty()
                    && !answerField.getText().trim().isEmpty()) {
                return new Card(studySet.getId(),
                        questionField.getText().trim(),
                        answerField.getText().trim());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(card -> {
            cardDao.insert(card);
            loadCards();
        });
    }

    /**
     * Dialog zum Bearbeiten einer Karte.
     */
    private void editCard() {
        Card selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Bitte wähle zuerst eine Karte aus.");
            return;
        }

        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("Karte bearbeiten");

        ButtonType saveBtn = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextArea questionField = new TextArea(selected.getQuestion());
        questionField.setPrefRowCount(3);
        TextArea answerField = new TextArea(selected.getAnswer());
        answerField.setPrefRowCount(3);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Frage:"), 0, 0);
        grid.add(questionField, 1, 0);
        grid.add(new Label("Antwort:"), 0, 1);
        grid.add(answerField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == saveBtn
                    && !questionField.getText().trim().isEmpty()
                    && !answerField.getText().trim().isEmpty()) {
                selected.setQuestion(questionField.getText().trim());
                selected.setAnswer(answerField.getText().trim());
                return selected;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(card -> {
            cardDao.update(card);
            loadCards();
        });
    }

    /**
     * Löscht die ausgewählte Karte nach Bestätigung.
     */
    private void deleteCard() {
        Card selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Bitte wähle zuerst eine Karte aus.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Löschen");
        confirm.setHeaderText("Karte wirklich löschen?");

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                cardDao.delete(selected.getId());
                loadCards();
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public Parent getRoot() {
        return root;
    }
}
