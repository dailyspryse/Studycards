# StudyCards

Eine Desktop-Anwendung zur Erstellung, Verwaltung und Nutzung digitaler Lernkarten.
Entwickelt mit Java 17 und JavaFX.

## Voraussetzungen

- **Java 17** oder höher
- **Maven** (mindestens 3.8)
- **MySQL Server** (z.B. über XAMPP oder direkte Installation)

## Datenbank einrichten

1. MySQL Server starten
2. Das SQL-Setup-Script ausführen:
   ```
   mysql -u root -p < setup.sql
   ```
   Alternativ: Die Datei `setup.sql` in MySQL Workbench oeffnen und ausführen.

**Hinweis:** Falls dein MySQL-Benutzer ein Passwort hat, muss dieses in
`DatabaseManager.java` bei der Konstante `PASSWORD` eingetragen werden.

## Anwendung starten

```
mvn javafx:run
```

## Funktionen

- **Lernsets verwalten** - Erstellen, bearbeiten und löschen von Lernsets
- **Karten verwalten** - Erstellen, bearbeiten und löschen von Lernkarten
- **Lernmodus** - Karten abfragen, Antwort aufdecken, als gewusst/nicht gewusst bewerten
- **Statistik** - Erfolgsquote am Ende jeder Lernsession
- **Favoriten** - Karten als Favorit markieren
- **Mischen** - Karten in zufaelliger Reihenfolge lernen

## Projektstruktur

```
src/main/java/com/studycards/
├── App.java                    - Hauptklasse und Navigation
├── model/
│   ├── StudySet.java           - Datenmodell für Lernsets
│   └── Card.java               - Datenmodell für Lernkarten
├── database/
│   └── DatabaseManager.java    - Datenbankverbindung und Tabellenerstellung
├── dao/
│   ├── StudySetDao.java        - Datenbankzugriff für Lernsets
│   └── CardDao.java            - Datenbankzugriff für Karten
└── view/
    ├── MainView.java           - Hauptansicht (Übersicht)
    ├── SetDetailView.java      - Detailansicht (Kartenverwaltung)
    └── StudyModeView.java      - Lernmodus
```

## Architektur

Die Anwendung folgt einer dreischichtigen Architektur:

- **Model-Schicht** (`model/`): Datenklassen die die Struktur von Lernsets und Karten abbilden
- **Datenzugriffsschicht** (`dao/`): Data Access Objects die alle SQL-Operationen kapseln
- **Darstellungsschicht** (`view/`): JavaFX-Views die die Benutzeroberfläche aufbauen

Die `App`-Klasse dient als zentrale Steuerung und verwaltet die Navigation zwischen den Ansichten.

## Verwendete Technologien

- Java 17
- JavaFX 17.0.2 (javafx-controls)
- MySQL 8 mit JDBC-Treiber
- Maven als Build-Tool
