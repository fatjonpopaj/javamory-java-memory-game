package Fatjon.Javamory.source;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Stellt die Hauptkomponente des Spielbretts in Javamory dar.
 * Diese Klasse ist verantwortlich für die Initialisierung des Layouts, die Einrichtung der Spiel-Engine,
 * die Verwaltung des Hacker-Modus und die Aktualisierung des Spielzustands.
 */
public class BoardComponent extends JComponent {
    // Konstanten für den Hacker-Modus
    private static final String HACKER_MODE_ACTION_KEY = "hacker";

    // Komponenten des Spielfelds
    private CardPanel cardPanel;
    private GameEngine gameEngine;
    private UserPanel userPanel;
    private boolean hackerModeActive = false;
    private JavamoryFrame mainFrame;
    private boolean hackerModeUsed = false; // Neuer Flag, um zu überprüfen, ob der Hacker-Modus bereits verwendet wurde

    // Logik Anfang

    /**
     * Konstruktor: Initialisiert das Spielfeld mit einem bestimmten Schwierigkeitsgrad und dem Hauptfenster.
     *
     * @param level     Der Schwierigkeitsgrad des Spiels.
     * @param mainFrame Das Hauptfenster des Spiels.
     */
    public BoardComponent(CardPanel.Level level, JavamoryFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeLayout(level); // Initialisiert das Layout des Spielbretts
        setupGameEngine(); // Setzt die Spiel-Engine auf
    }

    /**
     * Deckt alle Karten vorübergehend auf, wird im Hacker-Modus verwendet.
     */
    private void revealAllCardsTemporarily() {
        for (Card card : cardPanel.getCards()) {
            if (!card.isMatched()) {
                card.flipCard();
            }
        }
    }

    /**
     * Richtet die Spiel-Engine ein und startet sie.
     */
    private void setupGameEngine() {
        gameEngine = new GameEngine(userPanel, this, mainFrame);
        gameEngine.start();
    }

    /**
     * Schaltet den Hacker-Modus um.
     * Aktiviert den Hacker-Modus, wenn er noch nicht verwendet wurde, und deaktiviert ihn danach,
     * sodass er im selben Spiel nicht erneut aktiviert werden kann.
     */
    public void toggleHackerMode() {
        if (hackerModeUsed) {
            // Verhindert die erneute Aktivierung des Hacker-Modus, wenn er bereits verwendet wurde
            return;
        }
        if (isExactlyOneCardOpen()) {
            // Wenn genau eine Karte offen ist, aktiviere den Hacker-Modus nicht
            return;
        }

        hackerModeActive = !hackerModeActive; // Schaltet den Zustand des Hacker-Modus um
        gameEngine.setHackerModeActive(hackerModeActive); // Informiert die Spiel-Engine über den Zustand des Hacker-Modus

        // Wenn der Hacker-Modus aktiviert ist, werden die Karten aufgedeckt und der Nutzer sieht eine Nachricht
        if (hackerModeActive) {
            hackerModeUsed = true; // Setzt den Flag, dass der Hacker-Modus verwendet wurde

            String message = getHackerModeMessage(); // Holt die Nachricht für den Hacker-Modus
            int duration = getHackerModeDuration(); // Holt die Dauer, wie lange der Hacker-Modus aktiv sein soll
            showHackerModePopup(message, duration); // Zeigt ein Popup mit der Nachricht und der Dauer an
            // Deaktiviert alle Karten, damit sie nicht interagierbar sind, während der Hacker-Modus aktiv ist
            for (Card card : cardPanel.getCards()) {
                card.setEnabled(false);
            }
            increaseAttemptsForHackerMode(); // Erhöht die Anzahl der Versuche, die für den Hacker-Modus gezählt werden
            revealAllCardsTemporarily(); // Zeigt vorübergehend alle Karten an
        } else {
            // Verzögerung einführen, bevor die Karten wieder aktiviert werden
            new Timer(5000, e -> {
                // Aktiviert alle Karten wieder, die nicht gematcht wurden, nachdem die Verzögerung abgelaufen ist
                for (Card card : cardPanel.getCards()) {
                    card.setEnabled(!card.isMatched());
                }
                gameEngine.resumeGame(); // Setzt das Spiel fort, nachdem die Karten wieder aktiviert wurden
            }) {{
                setRepeats(false); // Stellt sicher, dass der Timer nicht wiederholt wird
                start(); // Startet den Timer
            }};
        }
    }

    /**
     * Erhöht die Anzahl der Versuche basierend auf dem aktuellen Schwierigkeitsgrad, wenn der Hacker-Modus aktiviert wird.
     */
    public void increaseAttemptsForHackerMode() {
        int additionalAttempts;
        switch (getLevel()) {
            // Abhängig vom Schwierigkeitsgrad werden unterschiedliche zusätzliche Versuche hinzugefügt
            case BEGINNER:
                additionalAttempts = 3;
                break;
            case EASY:
                additionalAttempts = 4;
                break;
            case MEDIUM:
                additionalAttempts = 5;
                break;
            case HARD:
                additionalAttempts = 5;
                break;
            case EXPERT:
                additionalAttempts = 5;
                break;
            default:
                additionalAttempts = 0; // Standardwert, wenn kein Fall zutrifft
        }
        gameEngine.addAttempts(additionalAttempts); // Fügt die zusätzlichen Versuche zur Spiel-Engine hinzu
    }

    /**
     * Gibt eine Nachricht zurück, die abhängig vom Schwierigkeitsgrad die Dauer des Hacker-Modus beschreibt.
     *
     * @return Eine Nachricht für den Hacker-Modus.
     */
    private String getHackerModeMessage() {
        CardPanel.Level currentLevel = getLevel();
        switch (currentLevel) {
            // Jeder Schwierigkeitsgrad hat eine eigene Nachricht mit der entsprechenden Dauer
            case BEGINNER:
                return "You see the cards for 3 seconds and have to wait another 3 seconds";
            case EASY:
                return "You see the cards for 4 seconds and have to wait another 4 seconds";
            case MEDIUM:
                return "You see the cards for 5 seconds and have to wait another 5 seconds";
            case HARD:
                return "You see the cards for 7 seconds and have to wait another 7 seconds";
            case EXPERT:
                return "You see the cards for 10 seconds and have to wait another 10 seconds";
            default:
                return "Hacker-Modus aktiviert!"; // Standardnachricht
        }
    }

    /**
     * Gibt die Dauer des Hacker-Modus in Millisekunden zurück, basierend auf dem aktuellen Schwierigkeitsgrad.
     *
     * @return Die Dauer des Hacker-Modus.
     */
    private int getHackerModeDuration() {
        CardPanel.Level currentLevel = getLevel();
        switch (currentLevel) {
            // Jeder Schwierigkeitsgrad hat eine eigene Dauer für den Hacker-Modus
            case BEGINNER:
                return 6000;
            case EASY:
                return 8000;
            case MEDIUM:
                return 10000;
            case HARD:
                return 14000;
            case EXPERT:
                return 20000;
            default:
                return 2000; // Standarddauer
        }
    }

    /**
     * Überprüft, ob genau eine Karte offen ist und noch nicht gematcht wurde.
     *
     * @return Wahr, wenn genau eine Karte offen ist, ansonsten falsch.
     */
    private boolean isExactlyOneCardOpen() {
        long openCardsCount = cardPanel.getCards().stream()
                .filter(Card::isFlipped) // Filtert alle aufgedeckten Karten
                .filter(card -> !card.isMatched()) // Filtert alle nicht gematchten Karten
                .count(); // Zählt die Anzahl der gefilterten Karten
        return openCardsCount == 1; // Gibt true zurück, wenn genau eine Karte offen ist
    }

    /**
     * Gibt den aktuellen Schwierigkeitsgrad zurück.
     *
     * @return Der aktuelle Schwierigkeitsgrad.
     */
    public CardPanel.Level getLevel() {
        return cardPanel.getLevel(); // Delegiert den Aufruf an das cardPanel
    }

    // Logik Ende


    // GUI Anfang


    /**
     * Initialisiert das Layout des Spielfelds (GUI).
     *
     * @param level Der Schwierigkeitsgrad des Spiels.
     */
    private void initializeLayout(CardPanel.Level level) {
        setLayout(new BorderLayout()); // Setzt das Layout für dieses Panel auf BorderLayout
        cardPanel = new CardPanel(level); // Erstellt ein neues CardPanel mit dem gegebenen Level
        userPanel = new UserPanel(cardPanel.getCards(), gameEngine); // Erstellt ein UserPanel mit den Karten und der Spiel-Engine

        // Fügt das cardPanel in der Mitte und das userPanel im Süden dieses Panels hinzu
        add(cardPanel, BorderLayout.CENTER);
        cardPanel.setOpaque(false); // Macht das cardPanel transparent
        add(userPanel, BorderLayout.SOUTH);

        // Richtet die Tastenbindung für den Hacker-Modus ein
        setupHackerModeKeyBindings();

        setFocusable(true); // Ermöglicht, dass dieses Panel den Tastaturfokus erhält
    }

    /**
     * Richtet die Key Bindings für den Hacker-Modus ein (GUI).
     */
    private void setupHackerModeKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Definiert eine Tastenkombination für den Hacker-Modus und fügt sie der InputMap hinzu
        KeyStroke hackerModeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.SHIFT_DOWN_MASK);
        im.put(hackerModeKeyStroke, HACKER_MODE_ACTION_KEY);

        // Fügt eine Aktion zur ActionMap hinzu, die aufgerufen wird, wenn die Tastenkombination gedrückt wird
        am.put(HACKER_MODE_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleHackerMode(); // Schaltet den Hacker-Modus um
            }
        });
    }

    /**
     * Zeigt ein Popup-Fenster mit einer Nachricht und einer Dauer für den Hacker-Modus an (GUI).
     *
     * @param message         Die anzuzeigende Nachricht im Popup.
     * @param displayDuration Die Dauer, für die das Popup angezeigt wird.
     */
    private void showHackerModePopup(String message, int displayDuration) {
        JDialog dialog = new JDialog();
        dialog.setTitle("As i said don't forget that everything has two sides \uD83D\uDE09"); // Setzen Sie hier Ihren gewünschten Titel
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        // Setzen Sie den Hintergrund des Dialogs auf Schwarz
        dialog.getContentPane().setBackground(Color.BLACK);

        // Erstellen Sie ein JLabel für "Hacker mode" und formatieren Sie es fett, zentriert und in Weiß
        JLabel labelTitle = new JLabel("Hacker Mode");
        labelTitle.setFont(new Font(labelTitle.getFont().getName(), Font.BOLD, 16));
        labelTitle.setForeground(Color.WHITE);
        dialog.add(labelTitle, gbc);

        // Zurücksetzen der GridBagConstraints für den restlichen Text
        gbc.anchor = GridBagConstraints.LINE_START;

        // Erstellen Sie ein weiteres JLabel für den restlichen Text, ebenfalls in Weiß
        JLabel labelMessage = new JLabel(message);
        labelMessage.setForeground(Color.WHITE);
        dialog.add(labelMessage, gbc);

        dialog.setSize(425, 80); // Setzen Sie die gewünschte Größe
        dialog.setLocationRelativeTo(null); // Zentriert das Fenster
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Verhindert das Schließen über das "X"

        // Positionieren Sie das Fenster am unteren Bildschirmrand
        positionDialogAtScreenBottom(dialog);

        // Timer, der das Fenster nach einer bestimmten Zeit schließt
        new Timer(displayDuration, e -> dialog.dispose()).start();

        dialog.setVisible(true);
    }

    /**
     * Platziert ein Dialogfenster am unteren Bildschirmrand (GUI).
     *
     * @param dialog Das zu positionierende Dialogfenster.
     */
    private void positionDialogAtScreenBottom(JDialog dialog) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - dialog.getWidth()) / 2;
        int y = screenSize.height - dialog.getHeight() - 50; // 50 Pixel Abstand vom unteren Rand
        dialog.setLocation(x, y);
    }

    // GUI Ende

}