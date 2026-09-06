package Fatjon.Javamory.source;



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Die UserPanel-Klasse stellt das Benutzerpanel des Spiels dar und enthält Anzeigen für Versuche, Zeit und Schaltflächen.
 * Sie dient als Schnittstelle für den Spieler, um das Spiel zu steuern und Feedback zu erhalten.
 */
public class UserPanel extends JPanel implements TimeUpdateListener {
    private static final int REDUCED_FONT_SIZE = 30; // Neue, kleinere Schriftgröße
    private static final int PANEL_HEIGHT = 70; // Reduzierte Höhe des unteren Panels

    private JLabel attemptsLabel; // Label zur Anzeige der Versuche
    private JLabel timeLabel; // Label zur Anzeige der Zeit
    private List<Card> cards; // Liste der Karten im Spiel
    private GameTimer timer; // Timer für das Spiel
    private int attempts; // Anzahl der Versuche
    private GameEngine gameEngine; // Verweis auf die Spiel-Engine
    private boolean paused; // Flag, das angibt, ob das Spiel pausiert ist

    public static ExitDialog exitDialog;

    // Logik Anfang

    /**
     * Konstruktor für die UserPanel-Klasse.
     *
     * @param cards      Die Liste der Karten im Spiel.
     * @param gameEngine Die Instanz der GameEngine, die das Spiel steuert.
     */
    public UserPanel(List<Card> cards, GameEngine gameEngine) {
        this.cards = cards;
        this.gameEngine = gameEngine;
        setOpaque(false);
        setLayout(new BorderLayout());
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.attempts = 0;
        createPanel();

        // Timer initialisieren und starten
        this.timer = new GameTimer(this, 1000);
        this.timer.start(); // Spiel-Timer starten

        setBackground(Color.WHITE);
        setOpaque(true);
        exitDialog = new ExitDialog("Exit Confirmation", gameEngine, (JFrame) SwingUtilities.getWindowAncestor(this));
    }

    /**
     * Setzt den Text des Versuche-Labels.
     *
     * @param text Der Text, der im Versuche-Label angezeigt werden soll.
     */
    public void setAttemptsLabelText(String text) {
        attemptsLabel.setText("Attempts: " + text); // Anzeigetext für die Versuche aktualisieren
    }

    /**
     * Wird aufgerufen, wenn der Timer aktualisiert wird.
     * Aktualisiert das Zeit-Label mit der neuen Zeit.
     *
     * @param time Die aktuelle Zeit als formatierter String.
     */
    @Override
    public void onTimeUpdate(String time) {
        setTimeLabelText(time);
    }

    /**
     * Setzt den Text des Zeit-Labels.
     *
     * @param text Der Text, der im Zeit-Label angezeigt werden soll.
     */
    public void setTimeLabelText(String text) {
        timeLabel.setText(text); // Anzeigetext für die Zeit aktualisieren
    }

    /**
     * Gibt den aktuellen Spiel-Timer zurück.
     *
     * @return Das GameTimer-Objekt.
     */
    public GameTimer getTimer() {
        return timer; // Timer-Objekt zurückgeben
    }

    /**
     * Gibt die Liste der Karten im Spiel zurück.
     *
     * @return Die Liste der Karten.
     */
    public List<Card> getCards() {
        return cards; // Liste der Karten zurückgeben
    }

    /**
     * Überprüft, ob das Spiel aktuell pausiert ist.
     *
     * @return true, wenn das Spiel pausiert ist, sonst false.
     */
    public boolean isPaused() {
        return paused; // Aktuellen Pause-Zustand zurückgeben
    }

    // Logik Ende



    // GUI Anfang

    /**
     * Initialisiert das Panel, indem es die Layouts, Hintergrundfarben und Komponenten festlegt.
     */
    private void createPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        // Hinzufügen von Attempts Label ganz links
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST; // Links ausrichten
        addAttemptsLabel();
        mainPanel.add(attemptsLabel, gbc);

        // Unsichtbare Füllkomponente zwischen Attempts und Time
        gbc.gridx = 1;
        gbc.weightx = 1.8;
        mainPanel.add(Box.createHorizontalGlue(), gbc);

        // Hinzufügen von Time Label in der Mitte
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Zentrieren
        addTimeLabel();
        mainPanel.add(timeLabel, gbc);

        // Weitere unsichtbare Füllkomponente zwischen Time und Buttons
        gbc.gridx = 3;
        gbc.weightx = 1.70;
        mainPanel.add(Box.createHorizontalGlue(), gbc);

        // Hinzufügen von Pause und Exit Buttons ganz rechts
        gbc.gridx = 4;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST; // Rechts ausrichten

        gbc.gridx = 4; // Erhöhen der x-Position für den Exit-Button
        JButton exitButton = createButton("Exit", e -> showExitDialog());
        exitButton.setForeground(Color.RED);
        mainPanel.add(exitButton, gbc);

        // Hinzufügen des Hauptpanels zum UserPanel
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Erstellt und fügt einen Button zum Panel hinzu.
     * Konfiguriert den Button und fügt einen ActionListener hinzu.
     *
     * @param text       Der Text des Buttons.
     * @param action     Die Aktion, die beim Klicken des Buttons ausgeführt wird.
     * @return           Der erstellte JButton.
     */
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text); // Schaltfläche mit dem gegebenen Text erstellen
        button.setFont(new Font("Arial", Font.BOLD, REDUCED_FONT_SIZE)); // Schriftart und Größe festlegen
        button.setBackground(Color.WHITE); // Hintergrundfarbe auf Weiß setzen
        button.setBorderPainted(false); // Rahmen der Schaltfläche ausblenden
        button.setFocusPainted(false); // Fokusstil ausblenden
        button.setContentAreaFilled(false); // Füllung der Schaltfläche ausblenden
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Mauszeigerstil festlegen
        button.setOpaque(false); // Hintergrund der Schaltfläche transparent machen
        button.addActionListener(action); // ActionListener hinzufügen
        return button;
    }

    /**
     * Fügt das Label für die Anzahl der Versuche zum Panel hinzu.
     * Konfiguriert das Label und fügt es zum Layout hinzu.
     */
    private void addAttemptsLabel() {
        attemptsLabel = new JLabel("Attempts: " + attempts); // Label mit Anzahl der Versuche erstellen
        configureLabel(attemptsLabel, new Insets(0, 15, 0, 0)); // Label-Konfiguration anpassen
        add(attemptsLabel, BorderLayout.WEST); // Label zur linken Seite hinzufügen
    }

    /**
     * Fügt das Label für die verstrichene Zeit zum Panel hinzu.
     * Konfiguriert das Label und fügt es zum Layout hinzu.
     */
    private void addTimeLabel() {
        timeLabel = new JLabel("00:00"); // Label mit Anfangswert für die Zeit erstellen
        configureLabel(timeLabel, new Insets(0, 0, 0, 140)); // Label-Konfiguration anpassen
        add(timeLabel, BorderLayout.CENTER); // Label in der Mitte hinzufügen
    }

    /**
     * Konfiguriert ein JLabel mit spezifischen Eigenschaften.
     *
     * @param label   Das zu konfigurierende JLabel.
     * @param insets  Der Rand um das Label.
     */
    private void configureLabel(JLabel label, Insets insets) {
        label.setFont(new Font("Arial", Font.BOLD, REDUCED_FONT_SIZE)); // Schriftart und Größe festlegen
        label.setForeground(Color.BLACK); // Textfarbe auf Weiß setzen
        label.setOpaque(false); // Hintergrund transparent machen
        label.setBorder(new EmptyBorder(insets)); // Leeren Rand um das Label hinzufügen
        label.setPreferredSize(new Dimension(300, label.getPreferredSize().height)); // Feste Breite festlegen
    }


    /**
     * Überschreibt die Methode getPreferredSize, um eine bevorzugte Größe für das Panel festzulegen.
     *
     * @return Die bevorzugte Größe des Panels.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, PANEL_HEIGHT); // Bevorzugte Größe des Panels festlegen
    }

    /**
     * Zeigt einen Dialog an, der den Spieler fragt, ob er das Spiel beenden möchte.
     */
    private void showExitDialog() {
        exitDialog.setVisible(true);
    }

    // GUI Ende

}