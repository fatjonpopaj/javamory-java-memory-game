package Fatjon.Javamory.source;



import javax.swing.Timer;

/**
 * Die GameTimer-Klasse ist verantwortlich für die Verwaltung eines Spiel-Timers.
 * Sie nutzt einen Swing-Timer, um regelmäßige Aktionen auszuführen und die verstrichene Zeit zu verfolgen.
 */
public class GameTimer {
    private Timer timer; // Swing-Timer für regelmäßige Aktionen
    private long startTimeMillis; // Startzeit in Millisekunden
    private TimeUpdateListener timeUpdateListener; // Listener für Zeitaktualisierungen
    private boolean isRunning; // Flag, das anzeigt, ob der Timer läuft
    private long elapsedTime; // Gespeicherte verstrichene Zeit

    /**
     * Konstruktor für GameTimer.
     * Initialisiert den Timer mit einem spezifischen Zeitaktualisierungs-Listener und einer festgelegten Verzögerung.
     *
     * @param timeUpdateListener Listener für Zeitaktualisierungen.
     * @param delay              Verzögerung in Millisekunden zwischen Timer-Aktionen.
     */
    public GameTimer(TimeUpdateListener timeUpdateListener, int delay) {
        this.timeUpdateListener = timeUpdateListener;
        this.isRunning = false;
        timer = new Timer(delay, e -> {
            if (isRunning) {
                updateTimer(); // Aktualisiert den Timer bei jedem Tick
            }
        });
        startTimeMillis = 0; // Initialisiert die Startzeit mit 0
    }

    /**
     * Aktualisiert den Timer und benachrichtigt den Listener über die aktuelle Zeit.
     * Diese Methode wird bei jedem Tick des Timers aufgerufen.
     */
    private void updateTimer() {
        long currentTimeMillis = System.currentTimeMillis();
        long timeInSeconds = (currentTimeMillis - startTimeMillis) / 1000;
        String time = formatTime(timeInSeconds);
        if (timeUpdateListener != null) {
            timeUpdateListener.onTimeUpdate(time); // Informiert den Listener über die aktualisierte Zeit
        }
    }

    /**
     * Startet den Timer.
     * Setzt die Startzeit und beginnt mit der Zeitmessung.
     */
    public void start() {
        if (!isRunning) {
            startTimeMillis = System.currentTimeMillis() - elapsedTime; // Setzt die Startzeit unter Berücksichtigung der bereits verstrichenen Zeit
            isRunning = true;
            timer.start(); // Startet den Timer
        }
    }

    /**
     * Stoppt den Timer.
     * Speichert die verstrichene Zeit und beendet die Zeitmessung.
     */
    public void stop() {
        if (isRunning) {
            elapsedTime += System.currentTimeMillis() - startTimeMillis; // Speichert die verstrichene Zeit
            isRunning = false;
            timer.stop(); // Stoppt den Timer
        }
    }

    /**
     * Gibt die verstrichene Zeit in Millisekunden zurück.
     *
     * @return Die verstrichene Zeit in Millisekunden.
     */
    public long getTimeInMillis() {
        return isRunning ? System.currentTimeMillis() - startTimeMillis : 0; // Berechnet die aktuelle verstrichene Zeit
    }


    /**
     * Setzt den Timer-Status auf laufend oder gestoppt.
     *
     * @param running true, um den Timer zu starten, false, um ihn zu stoppen.
     */
    public void setTimeRunning(boolean running) {
        if (running) {
            start(); // Startet den Timer
        } else {
            stop(); // Stoppt den Timer
        }
    }

    /**
     * Formatiert die Zeit von Sekunden in das Format Minuten:Sekunden.
     *
     * @param timeInSeconds Die Zeit in Sekunden.
     * @return Die formatierte Zeit als String.
     */
    private String formatTime(long timeInSeconds) {
        long minutes = timeInSeconds / 60; // Berechnet die Minuten
        long seconds = timeInSeconds % 60; // Berechnet die verbleibenden Sekunden
        return String.format("%02d:%02d", minutes, seconds); // Gibt die Zeit im Format MM:SS zurück
    }
}

/**
 * Interface für Klassen, die über Zeitaktualisierungen benachrichtigt werden möchten.
 * Klassen, die dieses Interface implementieren, können auf Änderungen der Zeit reagieren.
 */

interface TimeUpdateListener {
    /**
     * Wird aufgerufen, wenn sich die aktuelle Zeit aktualisiert.
     *
     * @param time Die aktuelle Zeit als formatierter String.
     */
    void onTimeUpdate(String time);
}