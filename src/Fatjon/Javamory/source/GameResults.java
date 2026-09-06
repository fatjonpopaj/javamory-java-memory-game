package Fatjon.Javamory.source;



import javax.swing.*;
import java.awt.*;

/**
 * Die GameResults-Klasse stellt ein Panel dar, das die Ergebnisse des Spiels anzeigt.
 * Dieses Panel zeigt die Anzahl der Versuche des Spielers und die benötigte Zeit zur Vollendung des Spiels an.
 */
public class GameResults extends JPanel {

    /**
     * Konstruktor für die GameResults-Klasse.
     * Initialisiert das Panel mit Informationen über die Leistung des Spielers.
     *
     * @param attempts       Die Anzahl der Versuche des Spielers.
     * @param timeInSeconds  Die Zeit, die der Spieler benötigt hat, in Sekunden.
     */
    public GameResults(int attempts, long timeInSeconds) {
        setLayout(new GridLayout(3, 1)); // Verwendet ein GridLayout mit drei Zeilen und einer Spalte
        setBackground(Color.BLACK); // Setzt die Hintergrundfarbe des Panels auf Schwarz

        // Erstellt und fügt Labels hinzu, um die Spielresultate anzuzeigen
        JLabel congratsLabel = new JLabel("Congratulations! You found all the pairs but still didn't win anything", JLabel.CENTER);
        JLabel attemptsLabel = new JLabel("Attempts: " + attempts, JLabel.CENTER);
        JLabel timeLabel = new JLabel("Time: " + formatTime(timeInSeconds), JLabel.CENTER);

        // Definiert die Schriftgröße und -farbe für die Labels
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Color textColor = Color.WHITE; // Definiert die Farbe Weiß für den Text

        // Konfiguriert das Gratulations-Label
        congratsLabel.setFont(labelFont);
        congratsLabel.setForeground(textColor); // Setzt die Schriftfarbe auf Weiß
        congratsLabel.setOpaque(true);
        congratsLabel.setBackground(Color.BLACK); // Setzt den Hintergrund des Labels auf Schwarz

        // Konfiguriert das Versuchs-Label
        attemptsLabel.setFont(labelFont);
        attemptsLabel.setForeground(textColor); // Setzt die Schriftfarbe auf Weiß
        attemptsLabel.setOpaque(true);
        attemptsLabel.setBackground(Color.BLACK); // Setzt den Hintergrund des Labels auf Schwarz

        // Konfiguriert das Zeit-Label
        timeLabel.setFont(labelFont);
        timeLabel.setForeground(textColor); // Setzt die Schriftfarbe auf Weiß
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.BLACK); // Setzt den Hintergrund des Labels auf Schwarz

        // Fügt die Labels zum Panel hinzu
        add(congratsLabel);
        add(attemptsLabel);
        add(timeLabel);
    }

    /**
     * Formatiert die Zeit von Sekunden in das Format Minuten:Sekunden.
     * Diese Methode wird verwendet, um die Spielzeit in einem benutzerfreundlichen Format anzuzeigen.
     *
     * @param timeInSeconds Die Zeit in Sekunden.
     * @return Die formatierte Zeit als String.
     */
    private String formatTime(long timeInSeconds) {
        long minutes = timeInSeconds / 60; // Berechnet die Minuten
        long seconds = timeInSeconds % 60; // Berechnet die verbleibenden Sekunden
        return String.format("%02d:%02d", minutes, seconds); // Formatiert die Zeit im Format MM:SS
    }

    /**
     * Gibt die bevorzugte Größe des Panels zurück.
     * Diese Methode kann überschrieben werden, um eine spezifische Größe für das Panel festzulegen.
     *
     * @return Die bevorzugte Größe des Panels.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(650, 200); // Gibt die bevorzugte Größe des Panels an
    }
}