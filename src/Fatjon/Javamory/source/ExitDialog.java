package Fatjon.Javamory.source;



import javax.swing.*;
import java.awt.*;
import java.awt.Dialog;

/**
 * Die ExitDialog-Klasse erweitert die Dialog-Klasse und repräsentiert einen Dialog,
 * der dem Benutzer die Möglichkeit gibt, das Spiel zu verlassen oder fortzusetzen.
 * Der Dialog enthält verschiedene Schaltflächen für die entsprechenden Aktionen.
 */
public class ExitDialog extends Dialog {
    // Klassenvariablen und UI-Komponenten
    private static final Dimension BUTTON_SIZE = new Dimension(220, 60);
    private JLabel messageLabel; // Label für die Nachricht im Dialog
    private MenuButton continueButton; // Button, um das Spiel fortzusetzen
    private MenuButton goMenuButton; // Button, um zum Hauptmenü zurückzukehren
    private MenuButton quitGameButton; // Button, um das Spiel zu beenden
    private JPanel contentPanel; // Panel, das die Inhalte des Dialogs enthält
    private GameEngine gameEngine; // Instanz der Spiel-Engine für die Spielsteuerung

    /**
     * Konstruktor für die ExitDialog-Klasse.
     * Erstellt einen modalen Dialog mit dem angegebenen Titel.
     *
     * @param title       Der Titel des Dialogs.
     * @param gameEngine  Die Instanz der Spiel-Engine für die Spielsteuerung.
     * @param parentFrame Das übergeordnete Fenster, zu dem dieser Dialog gehört.
     */
    public ExitDialog(String title, GameEngine gameEngine, JFrame parentFrame) {
        super(parentFrame, title, true); // Erstellt einen modalen Dialog mit dem angegebenen Titel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.BLACK); // Setzt die Hintergrundfarbe des Panels auf Schwarz
        add(contentPanel); // Fügt das contentPanel zum Dialog hinzu

        this.gameEngine = gameEngine; // Speichert die Instanz der GameEngine

        initializeMessageLabel(); // Initialisiert das Nachrichten-Label
        addButtonsPanel(); // Fügt die Buttons zum Dialog hinzu

        setSize(750, 170); // Setzt die Größe des Dialogs
        setLocationRelativeTo(parentFrame); // Zentriert den Dialog relativ zum übergeordneten Fenster
    }

    /**
     * Initialisiert das Nachrichten-Label des Dialogs.
     */
    private void initializeMessageLabel() {
        String message = "  Where are you going ?";
        messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 35)); // Setzt die Schriftart und -größe des Labels
        messageLabel.setForeground(Color.WHITE); // Setzt die Schriftfarbe auf Weiß
        this.getContentPanel().add(messageLabel, BorderLayout.CENTER); // Fügt das Label zum Inhaltspanel hinzu
    }

    /**
     * Gibt das Inhalts-Panel des Dialogs zurück.
     *
     * @return Das Panel, das die Inhalte des Dialogs enthält.
     */
    public JPanel getContentPanel() {
        return contentPanel;
    }

    /**
     * Fügt die Schaltflächen zum Dialog hinzu und definiert deren Aktionen.
     * Enthält Schaltflächen zum Fortsetzen des Spiels, Rückkehr zum Hauptmenü und Beenden des Spiels.
     */
    private void addButtonsPanel() {
        // Erstellt die Schaltflächen mit spezifischen Texten und Farben
        continueButton = new MenuButton("It was accidentally", new Color(0, 100, 0), 20, BUTTON_SIZE, Color.WHITE);
        goMenuButton = new MenuButton("Straight to the Menu", Color.WHITE, 20, BUTTON_SIZE, Color.BLACK);
        quitGameButton = new MenuButton("I want to leave", Color.RED, 20, BUTTON_SIZE, Color.WHITE);

        configureButtonActions(); // Konfiguriert die Aktionen der Schaltflächen

        JPanel buttonPanel = new JPanel(new FlowLayout()); // Erstellt ein Panel für die Schaltflächen
        buttonPanel.add(continueButton); // Fügt die Schaltfläche zum Fortsetzen hinzu
        buttonPanel.add(goMenuButton); // Fügt die Schaltfläche zum Menü hinzu
        buttonPanel.add(quitGameButton); // Fügt die Schaltfläche zum Beenden hinzu
        buttonPanel.setOpaque(false); // Macht das Panel transparent
        this.getContentPanel().add(buttonPanel, BorderLayout.SOUTH); // Fügt das Panel zum Inhalts-Panel hinzu
    }

    /**
     * Konfiguriert die Aktionen der Schaltflächen.
     * Definiert das Verhalten für die "Fortsetzen", "Zum Menü" und "Spiel beenden" Schaltflächen.
     */
    private void configureButtonActions() {
        // Fügt den Aktionen die entsprechenden Funktionalitäten hinzu
        continueButton.addActionListener(event -> handleContinueAction());
        goMenuButton.addActionListener(event -> handleGoMenuAction());
        quitGameButton.addActionListener(e -> {
            
            JavamoryFrame.getInstanceOf().stopBackgroundMusic();

            JavamoryFrame.getInstanceOf().dispose();

            JavamoryFrame.running = false;
            if (UserPanel.exitDialog != null)
                UserPanel.exitDialog.handleGoMenuAction();

        });
    }

    /**
     * Behandelt die Aktion für die "Fortsetzen"-Schaltfläche.
     * Schließt den Dialog und setzt das Spiel fort, wenn es nicht pausiert ist.
     */
    private void handleContinueAction() {
        setVisible(false); // Schließt den Dialog
        UserPanel userPanel = findUserPanel(); // Sucht das UserPanel
        if (userPanel != null && !userPanel.isPaused()) {
            gameEngine.setPlaying(true); // Setzt das Spiel fort
            userPanel.getTimer().setTimeRunning(true); // Setzt den Timer fort
        }
    }

    /**
     * Sucht das UserPanel über die SwingUtilities-Klasse.
     *
     * @return Das gefundene UserPanel oder null, wenn es nicht gefunden wurde.
     */
    private UserPanel findUserPanel() {
        // Ermittelt das übergeordnete Fenster dieses Dialogs
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow instanceof JFrame) {
            JFrame parentFrame = (JFrame) parentWindow;
            // Durchläuft alle Komponenten des übergeordneten Fensters, um das UserPanel zu finden
            for (Component component : parentFrame.getContentPane().getComponents()) {
                if (component instanceof UserPanel) {
                    return (UserPanel) component; // Gibt das gefundene UserPanel zurück
                }
            }
        }
        return null; // Gibt null zurück, wenn kein UserPanel gefunden wurde
    }

    /**
     * Behandelt die Aktion für die "Zum Menü"-Schaltfläche.
     * Schließt den Dialog und kehrt zum Hauptmenü zurück.
     */
    public void handleGoMenuAction() {
        setVisible(false); // Schließt den Dialog
        JavamoryFrame memory = JavamoryFrame.getInstanceOf(); // Ruft die Instanz des Hauptfensters ab
        memory.getContentPane().removeAll(); // Entfernt alle Inhalte des Hauptfensters
        memory.add(new MenuC(memory)); // Fügt das Hauptmenü zum Hauptfenster hinzu
        memory.revalidate(); // Aktualisiert das Layout des Hauptfensters
        memory.repaint(); // Zeichnet das Hauptfenster neu
    }
}