package Fatjon.Javamory.source;



import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Die GameEngine-Klasse steuert den Spielablauf und verwaltet den Spielzustand in Javamory.
 * Sie fungiert als zentraler Kontrollpunkt für die Spiellogik, die Überwachung des Spielstatus,
 * die Verwaltung von Spieleraktionen und die Aktualisierung der Benutzeroberfläche.
 */

public class GameEngine extends Thread {
        private volatile boolean playing; // Flag, das angibt, ob das Spiel läuft
        private volatile int attempts; // Anzahl der Versuche des Spielers
        private int goodPairs; // Anzahl der korrekt gefundenen Kartenpaare
        private final UserPanel userPanel; // Panel zur Anzeige von Spielerinformationen
        private final BoardComponent boardComponent; // Das Spielfeld
        private JavamoryFrame mainFrame; // Das Hauptfenster des Spiels
        private long startTime; // Startzeit des Spiels
        private List<Boolean> matchedCards = new ArrayList<>(); // Liste, die den Abgleichstatus jeder Karte speichert
        private boolean hackerModeActive = false; // Flag für den Hacker-Modus
        private volatile boolean closingCards = false; // Flag, das angibt, ob Karten gerade geschlossen werden


    /**
     * Konstruktor für die GameEngine-Klasse.
     * Initialisiert die Spielengine mit den erforderlichen UI-Komponenten und Spielvariablen.
     *
     * @param userPanel       Das UserPanel zur Anzeige von Benutzerinformationen.
     * @param boardComponent  Das Spielfeld-Panel.
     * @param mainFrame       Das Hauptfenster des Spiels.
     */
    public GameEngine(UserPanel userPanel, BoardComponent boardComponent, JavamoryFrame mainFrame) {
        this.userPanel = userPanel;
        this.boardComponent = boardComponent;
        this.mainFrame = mainFrame;
        this.attempts = 0;
        this.goodPairs = 0;
        this.playing = true;
        // Initialisiert die Liste matchedCards basierend auf der Anzahl der Karten
        this.matchedCards = new ArrayList<>(userPanel.getCards().size());
        for (int i = 0; i < userPanel.getCards().size(); i++) {
            matchedCards.add(false); // Setzt den anfänglichen Status aller Karten auf "nicht abgeglichen"
        }
    }

    /**
     * Die Hauptspielschleife, die während des Spiels ausgeführt wird.
     * Diese Methode steuert den Spielablauf und reagiert auf Spieleraktionen und Spielzustände.
     */
    @Override
    public void run() {
        // Hauptspiel-Schleife
        startTime = System.currentTimeMillis();
        userPanel.getTimer().start(); // Startet den Timer des Spiels
        List<Card> cards = userPanel.getCards();

        while (!Thread.currentThread().isInterrupted() && playing) {
            if (hackerModeActive) {
                handleHackerMode(); // Behandelt die Logik im Hacker-Modus
            } else {
                handleNormalGameplay(cards); // Behandelt die normale Spiellogik
            }

            // Kurze Pause zur Vermeidung von CPU-Überlastung
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Behandelt die Logik des Hacker-Modus.
     * In diesem Modus werden alle Karten vorübergehend aufgedeckt und nach einer bestimmten Zeit wieder zurückgesetzt.
     */
    private void handleHackerMode() {
        disableAllCards(); // Deaktiviert alle Karten
        hackerModeActive = true;

        int hackerModeDuration = getHackerModeDuration(); // Dauer des Hacker-Modus
        Timer hackerModeTimer = new Timer(hackerModeDuration, e -> {
            hackerModeActive = false; // Deaktiviert den Hacker-Modus nach Ablauf der Zeit
            resetCardsToPreHackerState(); // Setzt die Karten auf ihren vorherigen Zustand zurück

            // Verzögerung nach dem Ende des Hacker-Modus
            int delayAfterHackerMode = getPostHackerModeDelay();
            new Timer(delayAfterHackerMode, delay -> {
                enableAllCards(); // Reaktiviert die Karteninteraktion nach der Verzögerung
            }) {{
                setRepeats(false);
                start();
            }};
        });
        hackerModeTimer.setRepeats(false);
        hackerModeTimer.start();
    }

    /**
     * Gibt die Verzögerung nach dem Ende des Hacker-Modus zurück.
     * Diese Methode bestimmt, wie lange es dauert, bis die Karten nach dem Hacker-Modus wieder aktiviert werden.
     *
     * @return Die Verzögerung in Millisekunden.
     */
    public int getPostHackerModeDelay() {
        CardPanel.Level currentLevel = boardComponent.getLevel();

        switch (currentLevel) {
            case BEGINNER:
                return 3000; // 3 Sekunde für Beginner
            case EASY:
                return 4000;  // 4 Sekunden für Easy
            case MEDIUM:
                return 5000;  // 5 Sekunden für Medium
            case HARD:
                return 7000;  // 7 Sekunden für Hard
            case EXPERT:
                return 10000;  // 10 Sekunden für Expert
            default:
                return 2000; // Standardwert
        }
    }

    /**
     * Gibt die Dauer des Hacker-Modus zurück.
     * Diese Methode bestimmt, wie lange der Hacker-Modus aktiv bleibt, bevor die Karten zurückgesetzt werden.
     *
     * @return Die Dauer des Hacker-Modus in Millisekunden.
     */
    public int getHackerModeDuration() {
        CardPanel.Level currentLevel = boardComponent.getLevel();

        switch (currentLevel) {
            case BEGINNER:
                return 3000; // 3 Sekunden für Beginner
            case EASY:
                return 4000; // 4 Sekunden für Easy
            case MEDIUM:
                return 5000; // 5 Sekunden für Medium
            case HARD:
                return 7000; // 7 Sekunden für Hard
            case EXPERT:
                return 10000; // 10 Sekunde für Expert
            default:
                return 2000; // Standardwert
        }
    }

    /**
     * Deaktiviert alle Karten, um Interaktionen während bestimmter Spielzustände zu verhindern.
     */
    private void disableAllCards() {
        SwingUtilities.invokeLater(() -> {
            for (Card card : userPanel.getCards()) {
                card.setEnabled(false);
            }
        });
    }

    /**
     * Aktiviert alle Karten, die nicht als Teil eines gefundenen Paares markiert sind.
     */
    private void enableAllCards() {
        SwingUtilities.invokeLater(() -> {
            for (Card card : userPanel.getCards()) {
                if (!card.isMatched()) {
                    card.setEnabled(true);
                }
            }
        });
    }

    /**
     * Setzt die Karten auf ihren Zustand vor dem Hacker-Modus zurück.
     * Diese Methode wird verwendet, um die Karten nach dem Ende des Hacker-Modus zurückzusetzen.
     */
    private void resetCardsToPreHackerState() {
        SwingUtilities.invokeLater(() -> {
            for (Card card : userPanel.getCards()) {
                if (!card.isMatched()) {
                    card.resetCard();
                }
            }
        });
    }

    /**
     * Behandelt die normale Spiellogik außerhalb des Hacker-Modus.
     * Diese Methode kontrolliert das Aufdecken der Karten und das Überprüfen auf Paare.
     *
     * @param cards Die Liste der Karten im Spiel.
     */
    private void handleNormalGameplay(List<Card> cards) {
        int first = -1; // Index der ersten umgedrehten Karte
        int second = -1; // Index der zweiten umgedrehten Karte

        // Durchsucht die Karten, um zwei umgedrehte, nicht gematchte und nicht verarbeitete Karten zu finden
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).isFlipped() && !cards.get(i).isMatched() && !cards.get(i).isBeingProcessed()) {
                if (first == -1) {
                    first = i;
                } else {
                    second = i;
                    break;
                }
            }
        }

        // Wenn zwei Karten gefunden wurden, verarbeitet sie
        if (!closingCards && first != -1 && second != -1) {
            final int finalFirst = first;
            final int finalSecond = second;

            closingCards = true; // Setzt das Flag, dass Karten geschlossen werden
            cards.get(finalFirst).setBeingProcessed(true);
            cards.get(finalSecond).setBeingProcessed(true);

            SwingUtilities.invokeLater(() -> {
                for (Card card : cards) card.setEnabled(false);
            });

            // Verzögerung vor der Überprüfung der Karten
            new Timer(800, e -> {
                SwingUtilities.invokeLater(() -> {
                    checkAndProcessCards(cards, finalFirst, finalSecond);
                    closingCards = false; // Setzt das Flag zurück, dass Karten nicht mehr geschlossen werden
                    for (Card card : cards) {
                        if (!card.isMatched()) {
                            card.setEnabled(true); // Aktiviert alle Karten, die nicht gematcht sind
                        }
                    }
                });
            }) {{
                setRepeats(false);
                start();
            }};
        }
    }

    /**
     * Aktiviert die Interaktionen für alle nicht gepaarten Karten.
     * Diese Methode wird aufgerufen, um die Interaktivität der Karten wiederherzustellen,
     * zum Beispiel nach dem Abschluss einer Aktion oder am Ende des Hacker-Modus.
     *
     * @param cards Die Liste der Karten im Spiel.
     */
    private void enableCardInteractions(List<Card> cards) {
        SwingUtilities.invokeLater(() -> {
            for (Card card : cards) {
                if (!card.isMatched()) {
                    card.setEnabled(true);
                }
            }
        });
    }

    /**
     * Überprüft und verarbeitet die umgedrehten Karten.
     * Diese Methode wird aufgerufen, um zu prüfen, ob zwei ausgewählte Karten ein Paar bilden.
     *
     * @param cards  Die Liste der Karten.
     * @param first  Der Index der ersten umgedrehten Karte.
     * @param second Der Index der zweiten umgedrehten Karte.
     */
    private void checkAndProcessCards(List<Card> cards, int first, int second) {
        Card firstCard = cards.get(first);
        Card secondCard = cards.get(second);
        boolean matchFound = firstCard.isMatching(secondCard);

        SwingUtilities.invokeLater(() -> {
            if (matchFound) {
                // Wenn ein Paar gefunden wurde
                firstCard.setMatched(true);
                secondCard.setMatched(true);
                goodPairs++;
            } else {
                // Kein Paar gefunden
                firstCard.resetCard();
                secondCard.resetCard();
            }

            // Aktualisiere Versuche und GUI
            if (!hackerModeActive) {
                attempts++;
                updateAttemptsLabel();
            }

            firstCard.setBeingProcessed(false);
            secondCard.setBeingProcessed(false);

            // Reaktiviere Karten, die nicht gepaart sind
            reEnableNonMatchedCards(cards);

            // Überprüfe, ob das Spiel gewonnen wurde
            checkForWin();
        });
    }

    /**
     * Reaktiviert alle Karten, die noch kein Paar gefunden haben.
     * Diese Methode durchläuft die Liste aller Karten und aktiviert jede Karte, die
     * nicht Teil eines bereits gefundenen Paares ist und die gerade nicht in einem
     * Überprüfungsprozess involviert ist. Dies stellt sicher, dass Spieler weiterhin
     * mit allen relevanten Karten interagieren können, ohne dass bereits gematchte
     * oder momentan überprüfte Karten fälschlicherweise wieder spielbar werden.
     *
     * @param cards Die Liste der Karten im Spiel. Jede Karte wird auf ihren aktuellen
     *              Zustand überprüft, um zu entscheiden, ob sie reaktiviert werden soll.
     */
    private void reEnableNonMatchedCards(List<Card> cards) {
        for (Card card : cards) {
            if (!card.isMatched() && !card.isBeingProcessed()) {
                card.setEnabled(true);
            }
        }
    }

    /**
     * Überprüft, ob das Spiel gewonnen wurde.
     * Diese Methode wird aufgerufen, um zu überprüfen, ob der Spieler alle Kartenpaare erfolgreich gefunden hat.
     * Bei einem Gewinn wird das Spiel beendet, der Timer gestoppt und das Gewinn-Dialogfenster angezeigt.
     */
    private void checkForWin() {
        // Überprüft, ob alle Paare gefunden wurden
        if (goodPairs == boardComponent.getLevel().getHowManyCards() / 2) {
            playing = false; // Setzt das Spiel auf nicht laufend
            userPanel.getTimer().stop(); // Stoppt den Timer
            showWinDialog(); // Zeigt das Gewinn-Dialogfenster an
        }
    }

    /**
     * Setzt den Spielstatus.
     * Diese Methode ändert den Spielstatus zwischen laufendem und pausiertem Zustand.
     *
     * @param playing Der Spielstatus (true für laufendes Spiel, false für pausiertes Spiel).
     */
    public void setPlaying(boolean playing) {
        this.playing = playing; // Aktualisiert den Spielstatus
    }

    /**
     * Gibt die Anzahl der Versuche zurück.
     * Diese Methode liefert die Anzahl der bisherigen Versuche des Spielers.
     *
     * @return Die Anzahl der Versuche.
     */
    public int getAttempts() {
        return attempts; // Gibt die Anzahl der bisherigen Versuche zurück
    }

    /**
     * Aktualisiert das Anzeigelabel für die Anzahl der Versuche im UserPanel.
     * Diese Methode wird verwendet, um die Anzeige der Versuche zu aktualisieren, wenn der Spieler eine Aktion durchführt.
     */
    private void updateAttemptsLabel() {
        // Aktualisiert das Label für die Anzahl der Versuche auf dem UserPanel
        SwingUtilities.invokeLater(() -> userPanel.setAttemptsLabelText(String.format("%03d", attempts)));
    }

    /**
     * Zeigt das Gewinn-Dialogfenster an, wenn der Spieler alle Paare gefunden hat.
     * Diese Methode berechnet die Spielzeit und zeigt die Ergebnisse an.
     */
    private void showWinDialog() {
        long endTime = System.currentTimeMillis(); // Erfasst die Endzeit des Spiels
        long timeInSeconds = (endTime - startTime) / 1000; // Berechnet die Spielzeit in Sekunden
        mainFrame.showGameResults(getAttempts(), timeInSeconds); // Zeigt das Dialogfenster mit den Spielergebnissen an
    }

    /**
     * Aktiviert den Hacker-Modus.
     * Diese Methode ändert den Status des Hacker-Modus im Spiel.
     *
     * @param hackerModeActive Der Status des Hacker-Modus (true für aktiv, false für inaktiv).
     */
    public void setHackerModeActive(boolean hackerModeActive) {
        this.hackerModeActive = hackerModeActive; // Aktualisiert den Status des Hacker-Modus
    }

    /**
     * Fügt zusätzliche Versuche hinzu, wenn der Hacker-Modus aktiv ist.
     * Diese Methode wird verwendet, um die Anzahl der Versuche zu erhöhen, wenn der Spieler den Hacker-Modus verwendet.
     *
     * @param additionalAttempts Die Anzahl der zusätzlichen Versuche.
     */
    public void addAttempts(int additionalAttempts) {
        if (hackerModeActive) {
            attempts += additionalAttempts; // Fügt die zusätzlichen Versuche hinzu
            updateAttemptsLabel(); // Aktualisiert das Label für die Anzahl der Versuche
        }
    }

    /**
     * Setzt das Spiel fort, nachdem es pausiert wurde.
     * Diese Methode wird verwendet, um das Spiel nach einer Pause wieder aufzunehmen.
     */
    public void resumeGame() {
        synchronized (this) {
            notifyAll(); // Weckt den Thread auf, falls er pausiert wurde
        }
        enableCardInteractions(userPanel.getCards()); // Aktiviert die Karteninteraktion wieder
    }
}
