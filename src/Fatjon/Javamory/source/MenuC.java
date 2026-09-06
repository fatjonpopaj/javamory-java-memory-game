package Fatjon.Javamory.source;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.border.EmptyBorder;

/**
 * Die MenuC-Klasse stellt das Hauptmenü des Spiels Javamory dar.
 * Dieses Menü bietet Optionen wie Spielstart, Anleitung und Spielbeenden und ist mit einem Hintergrundbild versehen.
 */
public class MenuC extends JComponent {
    private static final String BACKGROUND_IMAGE = "Bilder/Menu/background.png";
    // Konstanten für das Layout und die Gestaltung des Menüs
    private static final int GRID_ROWS = 5;
    private static final int GRID_COLUMNS = 1;
    private static final int VERTICAL_GAP = 40;
    private static final int HORIZONTAL_GAP = 20;
    private static final int BORDER_TOP = 40;
    private static final int BORDER_LEFT = 180;
    private static final int BORDER_BOTTOM = 80;
    private static final int BORDER_RIGHT = 180;
    private static final int BUTTON_FONT_SIZE = 44;

    private JPanel mainPanel;
    private Image image;
    private LevelDialog levelDialog;
    private JavamoryFrame mainFrame;

    /**
     * Konstruktor für die MenuC-Klasse.
     * Initialisiert das Menü mit einem Hintergrundbild und Menüoptionen.
     *
     * @param mainFrame Das Hauptfenster des Spiels.
     */
    public MenuC(JavamoryFrame mainFrame) {
        setLayout(new BorderLayout());
        loadImage();
        mainPanel = new JPanel(new GridLayout(GRID_ROWS, GRID_COLUMNS, HORIZONTAL_GAP, VERTICAL_GAP));
        mainPanel.setBorder(new EmptyBorder(250, BORDER_LEFT, BORDER_BOTTOM, BORDER_RIGHT));
        addTitle(); // Titel hier hinzufügen
        addMenuComponents();
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.setOpaque(false);
        this.mainFrame = mainFrame;
    }

    /**
     * Methode zum Hinzufügen der Menükomponenten.
     * Fügt die Schaltflächen für Spielstart, Spielanleitung und Spielbeenden hinzu.
     */
    private void addMenuComponents() {
        // Hier fügen Sie nur die Schaltflächen hinzu
        addMenuButton("Start Game", new Color(0, 100, 0), this::startGame);
        addMenuButton("How To", new Color(0, 100, 0), this::howTo);
        addMenuButton("Exit", new Color(0, 100, 0), e -> {
            
            JavamoryFrame.getInstanceOf().stopBackgroundMusic();

            JavamoryFrame.getInstanceOf().dispose();

            JavamoryFrame.running = false;
            if (UserPanel.exitDialog != null)
                UserPanel.exitDialog.handleGoMenuAction();


        });
    }

    /**
     * Lädt das Hintergrundbild für das Menü.
     */
    private void loadImage() {
        try {
            URL imageUrl = getClass().getResource(BACKGROUND_IMAGE);
            if (imageUrl == null) {
                throw new IllegalArgumentException("Hintergrundbild nicht gefunden: " + BACKGROUND_IMAGE);
            }
            image = new ImageIcon(imageUrl).getImage();
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
    }

    /**
     * Fügt den Titel "JAVAMORY" zum Menü hinzu.
     */
    private void addTitle() {
        JLabel titleLabel = new JLabel("JAVAMORY");
        titleLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 120));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setOpaque(false);
        titleLabel.setPreferredSize(new Dimension(getWidth(), 150)); // Sie können die Größe nach Bedarf anpassen
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    }

    /**
     * Erstellt und fügt eine Menüschaltfläche hinzu.
     *
     * @param text Der Text des Buttons.
     * @param backgroundColor Die Hintergrundfarbe des Buttons.
     * @param actionListener Der ActionListener für den Button.
     */
    private void addMenuButton(String text, Color backgroundColor, java.awt.event.ActionListener actionListener) {
        Color textColor = Color.WHITE; // Or whatever the default text color should be
        Dimension buttonSize = new Dimension(300, 60); // Or whatever the default size should be
        MenuButton button = new MenuButton(text, backgroundColor, BUTTON_FONT_SIZE, buttonSize, textColor);
        button.addActionListener(actionListener);
        mainPanel.add(button);
    }

    /**
     * Methode zum Starten des Spiels.
     * Öffnet den LevelDialog zur Auswahl des Schwierigkeitsgrads und startet das Spiel.
     *
     * @param event Das ActionEvent des Buttons.
     */
    private void startGame(ActionEvent event) {
        if (levelDialog == null) {
            levelDialog = new LevelDialog(mainFrame, "Choose Level", true);
        }
        levelDialog.setLocationRelativeTo(null);
        levelDialog.setSelected(false);
        levelDialog.setVisible(true);

        if (levelDialog.isSelected()) {
            CardPanel.Level selectedLevel = levelDialog.getLevel();
            this.mainFrame.getContentPane().removeAll();
            BoardComponent boardComponent = new BoardComponent(selectedLevel, mainFrame);
            this.mainFrame.getContentPane().add(boardComponent);
            this.mainFrame.revalidate();
            this.mainFrame.repaint();
        }
    }

    /**
     * Methode zum Anzeigen der Spielanleitung.
     * Zeigt einen Dialog mit Anweisungen zum Spiel an.
     *
     * @param event Das ActionEvent des Buttons.
     */
    private void howTo(ActionEvent event) {
        // Erstellen eines JTextArea-Objekts für den Anleitungstext
        JTextArea instructionsText = new JTextArea();
        instructionsText.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 17)); // Schriftart auf Arial setzen
        instructionsText.setForeground(Color.WHITE); // Textfarbe auf Weiß setzen
        instructionsText.setBackground(Color.BLACK); // Hintergrundfarbe auf Schwarz setzen
        instructionsText.setText(
                "• Start Game: Click \"Start Game\" to begin.\n" +
                        "• Select level of difficulty: Choose from 5 levels of difficulty. More cards increase the challenge\n" +
                        "• Find Pairs: Flip cards over to find matching pairs. Remember their positions\n" +
                        "• Play smart: Complete the game in as few attempts as possible!\n" +
                        "• Play again: Try to beat yours or your friends' attempts or beat a higher difficulty level.\n" +
                        "• Enjoy the game and strive to find the pairs efficiently!\n" +
                        "• Don't overestimate yourself and if you want you can press \"Shift N\" in the game. But don't forget that everything has two sides \uD83D\uDE09"
        );
        instructionsText.setEditable(false);
        instructionsText.setOpaque(false);

        // Größe des JTextArea-Objekts anpassen
        instructionsText.setPreferredSize(new Dimension(1005, 200)); // Größe anpassen

        // Erstellen eines JScrollPane-Objekts, falls der Text länger ist
        JScrollPane scrollPane = new JScrollPane(instructionsText);
        scrollPane.setOpaque(true); // Stellen Sie sicher, dass der Hintergrund gezeichnet wird
        scrollPane.setBackground(Color.BLACK); // Setzen Sie den Hintergrund des ScrollPane auf Schwarz
        scrollPane.setBorder(null); // Entfernen des Rahmens um den JScrollPane
        scrollPane.getViewport().setOpaque(true); // Stellen Sie sicher, dass der Hintergrund des Viewport gezeichnet wird
        scrollPane.getViewport().setBackground(Color.BLACK); // Setzen Sie den Hintergrund des Viewport auf Schwarz

        // Ändern der UIManager-Einstellungen für JOptionPane
        UIManager.put("OptionPane.background", Color.BLACK);
        UIManager.put("Panel.background", Color.BLACK);

        // Anzeigen des Dialogs mit den Spielanleitungen
        JOptionPane.showMessageDialog(null, scrollPane, "How To Play", JOptionPane.INFORMATION_MESSAGE);

        // Zurücksetzen der UIManager-Einstellungen, um andere JOptionPane-Dialoge nicht zu beeinflussen
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Diese Zeile stellt sicher, dass Kindkomponenten zuerst gezeichnet werden
        if (image != null) {
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}