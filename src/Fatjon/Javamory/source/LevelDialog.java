package Fatjon.Javamory.source;



import javax.swing.*;
import java.awt.*;

/**
 * LevelDialog ist ein Dialog, der dem Benutzer ermöglicht, den Schwierigkeitsgrad für ein neues Spiel auszuwählen.
 * Der Dialog stellt verschiedene Optionen für die Schwierigkeitsstufen zur Verfügung und ermöglicht es dem Benutzer,
 * eine Auswahl zu treffen, bevor ein neues Spiel gestartet wird.
 */
public class LevelDialog extends JDialog {
    // Konstanten für das Erscheinungsbild des "New Game"-Buttons
    private CardPanel.Level level; // Das ausgewählte Schwierigkeitslevel
    private boolean selected; // Gibt an, ob der "New Game"-Button ausgewählt wurde

    /**
     * Konstruktor für LevelDialog.
     * Initialisiert einen neuen Dialog zur Auswahl des Schwierigkeitsgrads für das Spiel.
     *
     * @param owner Das übergeordnete Fenster, zu dem dieser Dialog gehört.
     * @param title Der Titel des Dialogs.
     * @param modal Gibt an, ob der Dialog modal sein soll.
     */
    public LevelDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);

        SwingUtilities.invokeLater(this::initializeComponents);
    }

    /**
     * Initialisiert die GUI-Komponenten des Dialogs.
     * Diese Methode erstellt und ordnet die notwendigen Benutzeroberflächenelemente an,
     * um dem Benutzer die Auswahl des Schwierigkeitsgrads zu ermöglichen.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Hinzufügen des SelectLevelPanel
        SelectLevelPanel selectLevelPanel = new SelectLevelPanel(this);
        add(selectLevelPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Setzt die Position des Dialogs in der Mitte des Bildschirms
    }

    /**
     * Gibt das ausgewählte Schwierigkeitslevel zurück.
     *
     * @return Das ausgewählte Schwierigkeitslevel.
     */
    public CardPanel.Level getLevel() {
        return level;
    }

    /**
     * Setzt das ausgewählte Schwierigkeitslevel.
     *
     * @param level Das zu setzende Schwierigkeitslevel.
     */
    public void setLevel(CardPanel.Level level) {
        this.level = level;
    }

    /**
     * Gibt zurück, ob der "New Game"-Button ausgewählt wurde.
     *
     * @return true, wenn der "New Game"-Button ausgewählt wurde, sonst false.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Setzt den Auswahlstatus des "New Game"-Buttons.
     *
     * @param selected Der Auswahlstatus des "New Game"-Buttons.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}