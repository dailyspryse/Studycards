# StudyCards

Lernkarten-App für den Desktop — erstellt im Rahmen des Semesterprojekts in Java Development with JavaFX.

Die Idee ist simpel: Man erstellt Lernsets, packt Karten mit Frage und Antwort rein und kann sich dann im Lernmodus selber abfragen.

## Voraussetzungen

- Java 21 oder höher
- Maven
- MySQL (z.B. über XAMPP)

## Setup

1. MySQL starten
2. Datenbank anlegen — entweder das Script ausführen:
   ```
   mysql -u root < setup.sql
   ```
   oder die `setup.sql` manuell in MySQL Workbench öffnen und ausführen.

Falls euer MySQL ein Passwort hat: in `DatabaseManager.java` bei `PASSWORD` eintragen.

## Starten

```
mvn javafx:run
```

## Was kann die App?

- Lernsets anlegen, bearbeiten, löschen
- Karten mit Frage + Antwort erstellen und verwalten
- Lernmodus: Karte umdrehen, als gewusst/nicht gewusst bewerten
- Am Ende einer Session wird die Erfolgsquote angezeigt

## Projektstruktur

```
src/main/java/com/studycards/
├── App.java                  Startpunkt, Navigation zwischen Views
├── model/
│   ├── StudySet.java         Lernset (Name, Beschreibung)
│   └── Card.java             Lernkarte (Frage, Antwort)
├── database/
│   └── DatabaseManager.java  MySQL-Verbindung, Tabellen erstellen
├── dao/
│   ├── StudySetDao.java      CRUD für Lernsets
│   └── CardDao.java          CRUD für Karten
└── view/
    ├── MainView.java         Übersicht aller Sets
    ├── SetDetailView.java    Karten eines Sets verwalten
    └── StudyModeView.java    Lernmodus
```

## Aufbau

Drei-Schichten-Architektur:
- **Model** — einfache Java-Klassen die beschreiben wie ein Set/eine Karte aussieht
- **DAO** — alles was mit der Datenbank zu tun hat (SQL-Queries etc.)
- **View** — die Oberfläche die der Benutzer sieht, komplett in JavaFX

Die `App.java` verbindet die drei Schichten und wechselt zwischen den Views.

## Technologien

- Java 21
- JavaFX 21.0.2
- MySQL mit JDBC
- Maven

## Hinweis zur Verwendung von KI-Tools

Bei der Recherche und beim Debugging habe ich teilweise ChatGPT und ähnliche Tools als Hilfsmittel genutzt — hauptsächlich um Fehlermeldungen zu verstehen und JavaFX-Syntax nachzuschlagen. Der Code wurde von mir geschrieben und verstanden.
