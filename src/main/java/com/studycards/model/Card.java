package com.studycards.model;

/**
 * Repräsentiert eine einzelne Lernkarte mit Frage und Antwort.
 */
public class Card {

    private int id;
    private int setId;
    private String question;
    private String answer;

    /**
     * Konstruktor für neue Karten (ohne ID).
     */
    public Card(int setId, String question, String answer) {
        this.setId = setId;
        this.question = question;
        this.answer = answer;
    }

    /**
     * Konstruktor für Karten aus der Datenbank (mit ID).
     */
    public Card(int id, int setId, String question, String answer) {
        this.id = id;
        this.setId = setId;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSetId() {
        return setId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return question;
    }
}
