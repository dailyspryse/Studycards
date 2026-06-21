package com.studycards.dao;

import com.studycards.database.DatabaseManager;
import com.studycards.model.Card;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object für Lernkarten.
 * Hier sind alle Datenbankoperationen für die cards-Tabelle.
 */
public class CardDao {

    /**
     * Speichert eine neue Karte in der Datenbank.
     */
    public Card insert(Card card) {
        String sql = "INSERT INTO cards (set_id, question, answer) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = DatabaseManager.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, card.getSetId());
            pstmt.setString(2, card.getQuestion());
            pstmt.setString(3, card.getAnswer());
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                card.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Fehler beim Einfügen der Karte: " + e.getMessage());
        }

        return card;
    }

    /**
     * Lädt alle Karten eines bestimmten Lernsets.
     */
    public List<Card> findBySetId(int setId) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM cards WHERE set_id = ?";

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, setId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Card card = new Card(
                        rs.getInt("id"),
                        rs.getInt("set_id"),
                        rs.getString("question"),
                        rs.getString("answer")
                );
                cards.add(card);
            }

        } catch (SQLException e) {
            System.err.println("Fehler beim Laden der Karten: " + e.getMessage());
        }

        return cards;
    }

    /**
     * Aktualisiert Frage und Antwort einer bestehenden Karte.
     */
    public void update(Card card) {
        String sql = "UPDATE cards SET question = ?, answer = ? WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, card.getQuestion());
            pstmt.setString(2, card.getAnswer());
            pstmt.setInt(3, card.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fehler beim Aktualisieren: " + e.getMessage());
        }
    }

    /**
     * Löscht eine Karte.
     */
    public void delete(int id) {
        String sql = "DELETE FROM cards WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen: " + e.getMessage());
        }
    }

    /**
     * Zählt die Karten in einem Lernset.
     */
    public int countBySetId(int setId) {
        String sql = "SELECT COUNT(*) FROM cards WHERE set_id = ?";

        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, setId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Zaehlen: " + e.getMessage());
        }

        return 0;
    }
}
