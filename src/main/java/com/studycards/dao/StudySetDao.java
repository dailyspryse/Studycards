package com.studycards.dao;

import com.studycards.database.DatabaseManager;
import com.studycards.model.StudySet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object für Lernsets.
 * Hier sind alle Datenbankoperationen für die study_sets-Tabelle.
 */
public class StudySetDao {

    /**
     * Speichert ein neues Lernset in der Datenbank.
     */
    public StudySet insert(StudySet set) {
        String sql = "INSERT INTO study_sets (name, description) VALUES (?, ?)";

        try (PreparedStatement pstmt = DatabaseManager.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, set.getName());
            pstmt.setString(2, set.getDescription());
            pstmt.executeUpdate();

            // Die von der DB generierte ID auslesen
            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                set.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Fehler beim Einfügen: " + e.getMessage());
        }

        return set;
    }

    /**
     * Lädt alle Lernsets aus der Datenbank.
     */
    public List<StudySet> findAll() {
        List<StudySet> sets = new ArrayList<>();
        String sql = "SELECT * FROM study_sets ORDER BY created_at DESC";

        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                StudySet set = new StudySet(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                sets.add(set);
            }

        } catch (SQLException e) {
            System.err.println("Fehler beim Laden: " + e.getMessage());
        }

        return sets;
    }

    /**
     * Aktualisiert ein bestehendes Lernset.
     */
    public void update(StudySet set) {
        String sql = "UPDATE study_sets SET name = ?, description = ? WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, set.getName());
            pstmt.setString(2, set.getDescription());
            pstmt.setInt(3, set.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fehler beim Aktualisieren: " + e.getMessage());
        }
    }

    /**
     * Löscht ein Lernset (Karten werden durch CASCADE mitgelöscht).
     */
    public void delete(int id) {
        String sql = "DELETE FROM study_sets WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen: " + e.getMessage());
        }
    }
}
