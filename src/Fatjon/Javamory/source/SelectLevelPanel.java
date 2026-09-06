package Fatjon.Javamory.source;



import javax.swing.*;
import java.awt.*;

/**
 * Die SelectLevelPanel-Klasse stellt ein Panel dar, das die Auswahl des Schwierigkeitsgrads für das Spiel ermöglicht.
 * Dieses Panel wird in einem LevelDialog verwendet, um dem Benutzer verschiedene Schwierigkeitsstufen zur Auswahl anzubieten.
 */
public class SelectLevelPanel extends JPanel {
    private static final String[] LEVELS = {"Beginner", "Easy", "Medium", "Hard", "Expert"};
    private static final int BUTTON_FONT_SIZE = 20; // Schriftgröße für die Buttons
    private Dimension buttonDimension = new Dimension(120, 50); // Standardgröße für die Buttons

    private LevelDialog levelDialog; // Referenz auf den umschließenden LevelDialog

    /**
     * Konstruktor für SelectLevelPanel.
     * Erstellt ein Panel zur Auswahl des Schwierigkeitsgrads.
     *
     * @param levelDialog Der LevelDialog, der dieses Panel enthält.
     */
    public SelectLevelPanel(LevelDialog levelDialog) {
        this.levelDialog = levelDialog;
        initialize();
    }

    /**
     * Initialisiert das Panel, indem es die Layouts, Hintergrundfarben und Buttons festlegt.
     */
    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Ändert das Layout zu
        setBackground(Color.BLACK); // Setzen der Hintergrundfarbe auf Schwarz
        setOpaque(true); // Stellt sicher, dass das Panel den Hintergrund malt

        // Erzeugt einen unsichtbaren Platzhalter
        Component verticalStrut = Box.createVerticalStrut(10); // 100 ist die Höhe des Platzhalters
        add(verticalStrut);

        // Erzeugt ein Panel für die Buttons mit FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false); // Hier wird die Opazität des buttonPanel auf false gesetzt
        for (String level : LEVELS) {
            JButton button = createButton(level);
            buttonPanel.add(button);
        }

        add(buttonPanel); // Fügt das Button-Panel zum SelectLevelPanel hinzu
        buttonPanel.revalidate();
        buttonPanel.repaint();
        setVisible(true);
    }

    /**
     * Erstellt einen Button für einen bestimmten Schwierigkeitsgrad.
     * Konfiguriert den Button und fügt einen ActionListener hinzu.
     *
     * @param levelText Der Text des Buttons, der den Schwierigkeitsgrad repräsentiert.
     * @return Der erstellte JButton.
     */
    private JButton createButton(String levelText) {
        JButton button = new JButton(levelText);
        button.setOpaque(true); // Setzt die Opazität des Buttons auf false
        button.setContentAreaFilled(true); // Verhindert das Füllen der Button-Inhaltsfläche
        button.setBorderPainted(true); // Deaktiviert das Malen des Button-Randes
        button.setForeground(Color.BLACK); // Setzen der Button-Hintergrundfarbe auf Weiß
        button.setBackground(Color.WHITE); // Setzen der Schriftfarbe auf Schwarz
        button.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        button.setPreferredSize(buttonDimension);

        button.addActionListener(event -> {
            CardPanel.Level level = CardPanel.Level.valueOf(levelText.toUpperCase());
            levelDialog.setLevel(level);
            levelDialog.setSelected(true);
            levelDialog.setVisible(false);
        });

        return button;
    }
}