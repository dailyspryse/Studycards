package com.studycards.model;

/**
 * Repräsentiert ein Lernset, das mehrere Lernkarten enthält.
 * Ein Set hat einen Namen und eine optionale Beschreibung.
 */
public class StudySet {

    private int id;
    private String name;
    private String description;

    /**
     * Konstruktor für neue Sets (ohne ID, weil die DB die ID vergibt).
     */
    public StudySet(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Konstruktor für Sets aus der Datenbank (mit ID).
     */
    public StudySet(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Wird von der ListView benutzt um den Set-Namen anzuzeigen.
     */
    @Override
    public String toString() {
        return name;
    }
}
