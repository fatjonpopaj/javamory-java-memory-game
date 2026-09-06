package Fatjon.Javamory.source;



import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repräsentiert das Panel, das die Spielkarten in Javamory enthält.
 * Diese Klasse verwaltet die Erstellung und Anordnung der Karten auf dem Spielfeld
 * und beinhaltet Logik für die Spielinteraktionen.
 */
public class CardPanel extends JPanel {
    private final List<Card> cards = new ArrayList<>(); // Liste, die alle Karten speichert
    private final Level level; // Der Schwierigkeitsgrad des Spiels
    private final int howManyCards; // Anzahl der Karten, basierend auf dem Schwierigkeitsgrad
    private Card flippedCard; // Speichert die aktuell umgedrehte Karte
    private boolean ninjaModeActive = false; // Flag für den Ninja-Modus

    /**
     * Konstruktor: Initialisiert das Panel mit einem bestimmten Schwierigkeitsgrad.
     * Erstellt und fügt Karten basierend auf dem Schwierigkeitsgrad zum Panel hinzu.
     *
     * @param level Der Schwierigkeitsgrad des Spiels.
     */
    public CardPanel(Level level) {
        this.level = level;
        this.howManyCards = level.getHowManyCards();
        setLayout(createBoardLayout()); // Setzt das Layout des Panels
        createCards(level); // Erstellt die Karten
        addCards(); // Fügt die Karten zum Panel hinzu
        flippedCard = null;

        setBackground(Color.WHITE); // Setzt die Hintergrundfarbe des Panels
        setOpaque(true); // Stellt sicher, dass das Panel nicht transparent ist
    }

    /**
     * Erstellt ein GridLayout basierend auf der Anzahl der Karten.
     * Die Anzahl der Reihen und Spalten wird so berechnet, dass alle Karten passen.
     *
     * @return Das GridLayout für das Panel.
     */
    private GridLayout createBoardLayout() {
        int gridSize = (int) Math.sqrt(this.howManyCards); // Berechnet die Größe des Grids
        GridLayout layout = new GridLayout(gridSize, gridSize); // Erstellt ein neues GridLayout
        layout.setHgap(10); // Setzt horizontalen Abstand zwischen den Karten
        layout.setVgap(10); // Setzt vertikalen Abstand zwischen den Karten
        return layout;
    }

    /**
     * Erstellt die Karten und fügt sie dem Panel hinzu.
     * Jede Karte erhält ein Vorder- und Rückseitenbild sowie eine eindeutige ID.
     *
     * @param level Der Schwierigkeitsgrad, der die Anzahl und Art der Karten bestimmt.
     */
    private void createCards(Level level) {
        ImageIcon[] icons = loadIconsForLevel(level); // Lädt die Icons für die Karten
        // Erstellt Paare von Karten mit denselben Icons
        for (int i = 0; i < howManyCards / 2; i++) {
            ImageIcon frontIcon = icons[i];
            ImageIcon backIcon = new ImageIcon(getClass().getResource("Bilder/Anonymous/anonymous.png")); // Lädt das Bild für die Rückseite
            String imageId = "image" + i; // Eindeutiger Identifikator für das Bild
            Card card1 = new Card(frontIcon, backIcon, imageId);
            Card card2 = new Card(frontIcon, backIcon, imageId);
            cards.add(card1);
            cards.add(card2);
        }
        Collections.shuffle(cards); // Mischt die Karten
    }

    /**
     * Fügt die Karten dem Panel hinzu und setzt ActionListeners für jede Karte.
     * Der ActionListener behandelt das Klicken auf die Karten.
     */
    private void addCards() {
        for (Card card : cards) {
            this.add(card); // Fügt jede Karte zum Panel hinzu
            card.addActionListener(e -> handleCardClick(card)); // Fügt einen ActionListener für Klicks auf die Karte hinzu
        }
    }

    /**
     * Gibt die Liste der Karten zurück.
     *
     * @return Die Liste der Karten auf dem Panel.
     */
    public List<Card> getCards() {
        return cards; // Gibt die Liste der Karten zurück
    }

    /**
     * Behandelt das Klicken auf eine Karte.
     * Überprüft, ob eine Karte umgedreht werden kann und initiiert die Überprüfung von Kartenpaaren.
     *
     * @param clickedCard Die angeklickte Karte.
     */
    private void handleCardClick(Card clickedCard) {
        if (ninjaModeActive) {
            return; // Ignoriert Klicks, wenn der Ninja-Modus aktiv ist
        }

        if (flippedCard == null) {
            flippedCard = clickedCard; // Speichert die erste umgedrehte Karte
            clickedCard.flipCard(); // Dreht die erste Karte um
        } else if (flippedCard != clickedCard && !clickedCard.isFlipped()) {
            clickedCard.flipCard(); // Dreht die zweite Karte um
            processFlippedCards(flippedCard, clickedCard); // Verarbeitet die beiden umgedrehten Karten
        }
    }

    /**
     * Verarbeitet zwei umgedrehte Karten, um zu sehen, ob sie übereinstimmen.
     * Bei Nichtübereinstimmung werden sie wieder zurückgedreht.
     *
     * @param firstCard  Die erste umgedrehte Karte.
     * @param secondCard Die zweite umgedrehte Karte.
     */
    private void processFlippedCards(Card firstCard, Card secondCard) {
        if (ninjaModeActive) {
            return; // Unterbricht die Verarbeitung, wenn Ninja-Modus aktiv ist
        }
        disableAllCards(); // Deaktiviert alle Karten während der Überprüfung
        Timer timer = new Timer(1000, e -> {
            if (!firstCard.isMatching(secondCard)) {
                firstCard.resetCard(); // Dreht die erste Karte zurück, wenn sie nicht übereinstimmt
                secondCard.resetCard(); // Dreht die zweite Karte zurück, wenn sie nicht übereinstimmt
            } else {
                firstCard.setMatched(true); // Markiert die erste Karte als gematcht
                secondCard.setMatched(true); // Markiert die zweite Karte als gematcht
            }
            flippedCard = null; // Setzt die Referenz auf die umgedrehte Karte zurück
            enableAllCards(); // Aktiviert alle Karten nach der Überprüfung
        });
        timer.setRepeats(false);
        timer.start(); // Startet den Timer für die Überprüfungsdauer
    }


    /**
     * Deaktiviert alle Karten, außer der aktuell umgedrehten Karte.
     * Wird verwendet, um Interaktionen während der Überprüfung der Karten zu verhindern.
     */
    public void disableAllCards() {
        for (Card card : cards) {
            if (card != flippedCard) {
                card.setEnabled(false); // Deaktiviert jede Karte, die nicht die umgedrehte Karte ist
            }
        }
    }

    /**
     * Aktiviert alle Karten.
     * Wird verwendet, um Interaktionen nach der Überprüfung der Karten zu ermöglichen.
     */
    public void enableAllCards() {
        for (Card card : cards) {
            card.setEnabled(true); // Aktiviert jede Karte
        }
    }

    /**
     * Lädt die Icons für die Karten basierend auf dem Schwierigkeitsgrad.
     * Jeder Schwierigkeitsgrad hat eine eigene Reihe von Bildern.
     *
     * @param level Der Schwierigkeitsgrad, der die Art der zu ladenden Bilder bestimmt.
     * @return Ein Array von ImageIcons, die den Karten zugewiesen werden.
     */
    private ImageIcon[] loadIconsForLevel(CardPanel.Level level) {
        int numImages = level.getHowManyCards() / 2; // Bestimmt die Anzahl der verschiedenen Bilder, basierend auf der Kartenanzahl
        ImageIcon[] icons = new ImageIcon[numImages]; // Erstellt ein Array für die Icons

        // Dynamische Größeneinstellungen je nach Level
        Dimension iconSize = getIconSizeForLevel(level);

        // Lädt jedes Bild und passt es an die gewünschte Größe an
        for (int i = 0; i < numImages; i++) {
            String imagePath = "Bilder/" + level.name() + "/image" + (i + 1) + ".png"; // Pfad zum Bild
            URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imgUrl);
                Image image = originalIcon.getImage();
                Image scaledImage = originalIcon.getImage().getScaledInstance(iconSize.width, iconSize.height, Image.SCALE_SMOOTH); // Skaliert das Bild
                icons[i] = new ImageIcon(scaledImage);
            } else {
                System.err.println("Bild konnte nicht geladen werden: " + imagePath);
                // Behandlung des Fehlers, wenn das Bild nicht geladen werden kann.
                // Setzen Sie ein Standardbild oder eine Fehlermeldung als Platzhalter
                icons[i] = new ImageIcon(new BufferedImage(iconSize.width, iconSize.height, BufferedImage.TYPE_INT_ARGB));
            }
        }
        return icons;
    }

    /**
     * Bestimmt die Größe der Icons basierend auf dem Schwierigkeitsgrad.
     * Größere Schwierigkeitsgrade können kleinere Icons haben, um auf dem Panel Platz zu sparen.
     *
     * @param level Der Schwierigkeitsgrad, der die Größe der Icons bestimmt.
     * @return Die Dimension der Icons.
     */
    private Dimension getIconSizeForLevel(CardPanel.Level level) {
        // Weist jedem Schwierigkeitsgrad eine bestimmte Bildgröße zu
        switch (level) {
            case BEGINNER:
                return new Dimension(240, 225); // Größe für Anfänger
            case EASY:
                return new Dimension(220, 220); // Größe für Einfach
            case MEDIUM:
                return new Dimension(110, 150); // Größe für Mittel
            case HARD:
                return new Dimension(160, 180); // Größe für Schwer
            case EXPERT:
                return new Dimension(110, 110); // Größe für Experte
            default:
                return new Dimension(100, 150); // Standardgröße
        }
    }

    /**
     * Gibt den aktuellen Schwierigkeitsgrad des Spiels zurück.
     *
     * @return Der aktuelle Schwierigkeitsgrad.
     */
    public Level getLevel () {
        return level; // Gibt den aktuellen Schwierigkeitsgrad zurück
    }

    /**
     * Enumeration für die verschiedenen Schwierigkeitsgrade des Spiels.
     * Jeder Schwierigkeitsgrad definiert die Anzahl der Karten im Spiel.
     */
    public enum Level {
        BEGINNER(16),
        EASY(24),
        MEDIUM(36),
        HARD(56),
        EXPERT(90);

        private final int howManyCards; // Anzahl der Karten für jeden Schwierigkeitsgrad

        Level(int howManyCards) {
            this.howManyCards = howManyCards;
        }

        /**
         * Gibt die Anzahl der Karten für den jeweiligen Schwierigkeitsgrad zurück.
         *
         * @return Die Anzahl der Karten.
         */
        public int getHowManyCards() {
            return howManyCards; // Gibt die Anzahl der Karten zurück
        }
    }

    /**
     * Zeichnet den Hintergrund des Panels.
     * Kann angepasst werden, um spezielle Hintergrundgrafiken oder -farben anzuzeigen.
     *
     * @param g Das Graphics-Objekt, das für das Zeichnen verwendet wird.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE); // Setzt die Farbe auf Weiß
        g.fillRect(0, 0, getWidth(), getHeight()); // Füllt den gesamten Hintergrund mit Weiß
    }
}
