package Fatjon.Javamory.source;



import javax.swing.*;
import java.awt.*;

/**
 * Repräsentiert eine Karte im Spiel Javamory.
 * Diese Klasse verwaltet die Darstellung der Vorder- und Rückseite der Karte,
 * sowie den Zustand der Karte bezüglich des Umdrehens und des Abgleichens mit anderen Karten.
 */
public class Card extends JButton {
    // Felder für die Bilder der Vorder- und Rückseite und Zustandsvariablen
    private ImageIcon frontImage;
    private ImageIcon backImage;

    // Zustandsvariablen der Karte
    private boolean isFlipped = false; // Speichert, ob die Karte umgedreht ist
    private boolean isMatched = false; // Speichert, ob die Karte ein passendes Paar gefunden hat
    private String imageId; // Eindeutige ID der Karte, um Paare zu identifizieren
    private boolean ninjaModeActive = false; // Speichert, ob der Ninja-Modus aktiv ist
    private boolean inNinjaMode = false; // Speichert, ob die Karte im Ninja-Modus umgedreht wurde

    /**
     * Konstruktor: Initialisiert eine Karte mit Vorder- und Rückseitenbildern sowie einer eindeutigen ID.
     *
     * @param frontImage Bild der Vorderseite der Karte.
     * @param backImage  Bild der Rückseite der Karte.
     * @param imageId    Eindeutige ID der Karte zur Identifizierung von Paaren.
     */
    public Card(ImageIcon frontImage, ImageIcon backImage, String imageId) {
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.imageId = imageId;
        configureButton(); // Konfiguriert die Darstellung des Buttons
        setIcon(backImage); // Standardmäßig wird die Rückseite angezeigt
        setModel(new CustomButtonModel()); // Setzt ein benutzerdefiniertes Button-Modell für erweiterte Funktionalität
    }

    /**
     * Konfiguriert das Aussehen des Kartenbuttons.
     */
    private void configureButton() {
        // Setzt die optischen Eigenschaften des Buttons
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Dreht die Karte um. Ändert das sichtbare Bild zwischen Vorder- und Rückseite.
     */
    public void flipCard() {
        isFlipped = !isFlipped; // Ändert den Umdrehstatus der Karte
        setIcon(isFlipped ? frontImage : backImage); // Setzt das entsprechende Bild je nach Umdrehstatus
    }

    /**
     * Setzt die Karte auf den Anfangszustand zurück, zeigt die Rückseite an.
     */
    public void resetCard() {
        isFlipped = false; // Setzt den Umdrehstatus zurück
        setIcon(backImage); // Zeigt die Rückseite der Karte
    }

    /**
     * Überprüft, ob die Karte umgedreht ist.
     *
     * @return true, wenn die Karte umgedreht ist, sonst false.
     */
    public boolean isFlipped() {
        return isFlipped; // Gibt den Umdrehstatus der Karte zurück
    }

    /**
     * Überprüft, ob diese Karte mit einer anderen Karte übereinstimmt.
     *
     * @param otherCard Die andere Karte, mit der verglichen wird.
     * @return true, wenn die Karten übereinstimmen, sonst false.
     */
    public boolean isMatching(Card otherCard) {
        // Vergleicht die eindeutigen IDs der beiden Karten, um festzustellen, ob sie ein Paar bilden
        return this.imageId.equals(otherCard.imageId);
    }

    /**
     * Getter für den Abgleich-Status der Karte.
     *
     * @return true, wenn die Karte ein Paar gefunden hat, sonst false.
     */
    public boolean isMatched() {
        return isMatched; // Gibt zurück, ob die Karte einem Paar angehört
    }

    /**
     * Setter für den Abgleich-Status der Karte.
     *
     * @param matched Neuer Status, ob die Karte ein Paar gefunden hat.
     */
    public void setMatched(boolean matched) {
        this.isMatched = matched; // Setzt den Abgleich-Status der Karte
        if (matched) {
            setEnabled(false); // Deaktiviert die Karte, wenn sie ein Paar gefunden hat
        }
    }

    /**
     * Aktiviert oder deaktiviert den Hacker-Modus für diese Karte.
     *
     * @param activate Gibt an, ob der Hacker-Modus aktiviert oder deaktiviert werden soll.
     */
    public void toggleNinjaMode(boolean activate) {
        ninjaModeActive = activate; // Aktualisiert den Status des Ninja-Modus
        if (!isMatched) {
            if (activate) {
                if (!isFlipped) {
                    flipCard(); // Deckt die Karte auf, wenn sie noch nicht umgedreht ist
                }
            } else {
                if (inNinjaMode && !isFlipped) {
                    flipCard(); // Setzt die Karte zurück, falls sie im Ninja-Modus umgedreht wurde
                }
            }
        }
        inNinjaMode = activate; // Aktualisiert den internen Status des Ninja-Modus
    }

    /**
     * Zeichnet die Karte auf dem Bildschirm, entweder die Vorder- oder die Rückseite.
     *
     * @param g Das Graphics-Objekt, das für das Zeichnen verwendet wird.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        // Setzt Rendering-Hinweise für verbesserte Grafikqualität
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        ImageIcon imageToDraw = isFlipped ? frontImage : backImage;
        // Zeichnet das Bild der Karte (Vorder- oder Rückseite) auf das Graphics-Objekt
        g2d.drawImage(imageToDraw.getImage(), 0, 0, getWidth(), getHeight(), this);
        g2d.dispose(); // Gibt das Graphics2D-Objekt frei
    }

    // Flag, das angibt, ob die Karte gerade verarbeitet wird
    private boolean beingProcessed = false;

    /**
     * Gibt zurück, ob die Karte gerade verarbeitet wird.
     *
     * @return true, wenn die Karte gerade verarbeitet wird, sonst false.
     */
    public boolean isBeingProcessed() {
        return beingProcessed; // Gibt den aktuellen Verarbeitungsstatus der Karte zurück
    }

    /**
     * Setzt den Verarbeitungsstatus der Karte.
     *
     * @param beingProcessed Neuer Status, ob die Karte gerade verarbeitet wird.
     */
    public void setBeingProcessed(boolean beingProcessed) {
        this.beingProcessed = beingProcessed; // Aktualisiert den Verarbeitungsstatus der Karte
    }
}