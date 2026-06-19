-- StudyCards Datenbank-Setup

CREATE DATABASE IF NOT EXISTS studycards;
USE studycards;

CREATE TABLE IF NOT EXISTS study_sets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    set_id INT NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    FOREIGN KEY (set_id) REFERENCES study_sets(id) ON DELETE CASCADE
);
