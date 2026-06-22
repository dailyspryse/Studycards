package com.studycards.view;

import com.studycards.App;
import com.studycards.dao.CardDao;
import com.studycards.model.Card;
import com.studycards.model.StudySet;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * Lernmodus: Zeigt Karten einzeln an.
 * Frage wird gezeigt, Benutzer deckt die Antwort auf
 * und bewertet ob er es wusste oder nicht.
 */
public class StudyModeView {

    private App app;
    private StudySet studySet;

    private List<Card> cards;
    private int currentIndex;
    private boolean showingAnswer;

    // Zähler für diese Lernsession
    private int correctCount;
    private int wrongCount;

    // UI-Elemente
    private BorderPane root;
    private Label cardLabel;
    private Label progressLabel;
    private Button flipBtn;
    private Button prevBtn;
    private Button nextBtn;

    public StudyModeView(App app, StudySet studySet) {
        this.app = app;
        this.studySet = studySet;
        this.currentIndex = 0;
        this.showingAnswer = false;
        this.correctCount = 0;
        this.wrongCount = 0;

        // Karten laden
        CardDao cardDao = new CardDao();
        this.cards = cardDao.findBySetId(studySet.getId());

        buildUI();
        showCurrentCard();
    }

    private void buildUI() {
        root = new BorderPane();
        root.setPadding(new Insets(20));

        // Header
        Button backBtn = new Button("<- Zurück");
        backBtn.setOnAction(e -> app.showSetDetailView(studySet));

        Label titleLabel = new Label("Lernmodus: " + studySet.getName());
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));

        progressLabel = new Label();

        HBox headerTop = new HBox(15, backBtn, titleLabel);
        headerTop.setAlignment(Pos.CENTER_LEFT);

        VBox header = new VBox(8, headerTop, progressLabel);
        header.setPadding(new Insets(0, 0, 20, 0));
        root.setTop(header);

        // Karten-Anzeige in der Mitte (sieht aus wie eine Karteikarte)
        cardLabel = new Label();
        cardLabel.setFont(Font.font("System", 18));
        cardLabel.setWrapText(true);
        cardLabel.setTextAlignment(TextAlignment.CENTER);
        cardLabel.setAlignment(Pos.CENTER);
        cardLabel.setPrefSize(500, 280);
        cardLabel.setMaxWidth(500);
        cardLabel.setStyle(
                "-fx-background-color: white; " +
                "-fx-border-color: #ccc; " +
                "-fx-border-radius: 12; " +
                "-fx-background-radius: 12; " +
                "-fx-padding: 40; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        // Klick auf Karte = umdrehen
        cardLabel.setOnMouseClicked(e -> flipCard());

        VBox centerBox = new VBox(cardLabel);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);

        // Buttons unten
        prevBtn = new Button("<- Vorherige");
        prevBtn.setOnAction(e -> previousCard());
        prevBtn.setPrefWidth(120);

        flipBtn = new Button("Antwort zeigen");
        flipBtn.setOnAction(e -> flipCard());
        flipBtn.setPrefWidth(150);

        nextBtn = new Button("Nächste ->");
        nextBtn.setOnAction(e -> nextCard());
        nextBtn.setPrefWidth(120);

        HBox navBox = new HBox(10, prevBtn, flipBtn, nextBtn);
        navBox.setAlignment(Pos.CENTER);

        Button correctBtn = new Button("Gewusst");
        correctBtn.setOnAction(e -> markCorrect());
        correctBtn.setStyle("-fx-base: #4CAF50;");
        correctBtn.setPrefWidth(120);

        Button wrongBtn = new Button("Nicht gewusst");
        wrongBtn.setOnAction(e -> markWrong());
        wrongBtn.setStyle("-fx-base: #f44336;");
        wrongBtn.setPrefWidth(140);

        HBox ratingBox = new HBox(10, correctBtn, wrongBtn);
        ratingBox.setAlignment(Pos.CENTER);

        VBox bottomBox = new VBox(10, navBox, ratingBox);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20, 0, 0, 0));
        root.setBottom(bottomBox);
    }

    /**
     * Zeigt die aktuelle Karte an (Frage oder Antwort).
     */
    private void showCurrentCard() {
        if (cards.isEmpty()) return;

        Card card = cards.get(currentIndex);

        if (showingAnswer) {
            cardLabel.setText(card.getAnswer());
            cardLabel.setStyle(
                    "-fx-background-color: #e8f5e9; " +
                    "-fx-border-color: #4CAF50; " +
                    "-fx-border-radius: 12; " +
                    "-fx-background-radius: 12; " +
                    "-fx-padding: 40; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);");
            flipBtn.setText("Frage zeigen");
        } else {
            cardLabel.setText(card.getQuestion());
            cardLabel.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-border-color: #ccc; " +
                    "-fx-border-radius: 12; " +
                    "-fx-background-radius: 12; " +
                    "-fx-padding: 40; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);");
            flipBtn.setText("Antwort zeigen");
        }

        // Buttons deaktivieren wenn am Anfang/Ende
        prevBtn.setDisable(currentIndex == 0);
        nextBtn.setDisable(currentIndex == cards.size() - 1);

        progressLabel.setText("Karte " + (currentIndex + 1) + " von " + cards.size()
                + "   |   Gewusst: " + correctCount + "   Nicht gewusst: " + wrongCount);
    }

    /**
     * Dreht die Karte um.
     */
    private void flipCard() {
        showingAnswer = !showingAnswer;
        showCurrentCard();
    }

    /**
     * Karte als "gewusst" markieren, dann weiter zur nächsten.
     */
    private void markCorrect() {
        correctCount++;
        goToNextOrFinish();
    }

    /**
     * Karte als "nicht gewusst" markieren, dann weiter zur nächsten.
     */
    private void markWrong() {
        wrongCount++;
        goToNextOrFinish();
    }

    /**
     * Geht zur nächsten Karte, oder zeigt das Ergebnis am Ende.
     */
    private void goToNextOrFinish() {
        if (currentIndex < cards.size() - 1) {
            currentIndex++;
            showingAnswer = false;
            showCurrentCard();
        } else {
            showResult();
        }
    }

    /**
     * Zeigt das Ergebnis der Lernsession.
     */
    private void showResult() {
        int total = correctCount + wrongCount;
        int percent = total > 0 ? (correctCount * 100 / total) : 0;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fertig!");
        alert.setHeaderText("Ergebnis: " + studySet.getName());
        alert.setContentText(
                "Gewusst: " + correctCount + "\n" +
                "Nicht gewusst: " + wrongCount + "\n" +
                "Erfolgsquote: " + percent + "%");
        alert.showAndWait();
    }

    private void previousCard() {
        if (currentIndex > 0) {
            currentIndex--;
            showingAnswer = false;
            showCurrentCard();
        }
    }

    private void nextCard() {
        if (currentIndex < cards.size() - 1) {
            currentIndex++;
            showingAnswer = false;
            showCurrentCard();
        }
    }

    public Parent getRoot() {
        return root;
    }
}
