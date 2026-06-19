package com.studycards.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Verwaltet die Verbindung zur MySQL-Datenbank.
 * Erstellt beim Start automatisch die benötigten Tabellen.
 */
public class DatabaseManager {

    // Verbindungsdaten - bei Bedarf anpassen
    private static final String URL = "jdbc:mysql://localhost:3306/studycards";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Die aktive Datenbankverbindung
    private static Connection connection;

    /**
     * Stellt die Verbindung her und erstellt die Tabellen.
     */
    public static void initialize() {
        try {
            // Datenbank erstellen falls sie noch nicht existiert
            Connection tempConn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/", USER, PASSWORD);
            Statement stmt = tempConn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS studycards");
            stmt.close();
            tempConn.close();

            // Mit der studycards-Datenbank verbinden
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            createTables();
            System.out.println("Datenbankverbindung hergestellt.");

        } catch (SQLException e) {
            System.err.println("Fehler bei der Datenbankverbindung: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gibt die aktive Datenbankverbindung zurück.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Erstellt die Tabellen falls sie noch nicht existieren.
     */
    private static void createTables() throws SQLException {
        Statement stmt = connection.createStatement();

        // Tabelle für Lernsets
        stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS study_sets (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """);

        // Tabelle für Lernkarten
        stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS cards (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    set_id INT NOT NULL,
                    question TEXT NOT NULL,
                    answer TEXT NOT NULL,
                    FOREIGN KEY (set_id) REFERENCES study_sets(id) ON DELETE CASCADE
                )
                """);

        stmt.close();
    }

    /**
     * Schließt die Datenbankverbindung.
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Datenbankverbindung geschlossen.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
