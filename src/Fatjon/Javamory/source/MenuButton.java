package Fatjon.Javamory.source;



import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Die MenuButton-Klasse repräsentiert eine benutzerdefinierte Schaltfläche für das Menü.
 * Sie bietet eine visuell ansprechende Darstellung für Buttons im Menü mit anpassbaren Eigenschaften wie Farbe, Größe und Schrift.
 */
public class MenuButton extends JButton {

    private static final String DEFAULT_FONT_FAMILY = "Arial"; // Standard-Schriftart für den Button
    private static final int DEFAULT_CURSOR_TYPE = Cursor.HAND_CURSOR; // Standard-Cursor-Typ

    /**
     * Konstruktor für die Klasse MenuButton.
     * Erstellt eine neue Schaltfläche mit dem angegebenen Text, Hintergrundfarbe, Schriftgröße und Größe.
     *
     * @param text Der anzuzeigende Text auf dem Button.
     * @param backgroundColor Die Hintergrundfarbe des Buttons.
     * @param fontSize Die Schriftgröße des Textes.
     * @param size Die Größe des Buttons.
     * @param textColor Die Farbe des Textes auf dem Button.
     */
    public MenuButton(String text, Color backgroundColor, int fontSize, Dimension size, Color textColor) {
        super(text);
        setFont(new Font(DEFAULT_FONT_FAMILY, Font.BOLD, fontSize));
        setForeground(Color.WHITE);
        setForeground(textColor); // Setzen der Schriftfarbe
        setCursor(new Cursor(DEFAULT_CURSOR_TYPE));
        setBackground(backgroundColor);
        setPreferredSize(size);
        createButtonBorder();
    }

    /**
     * Erstellt eine Rahmenumrandung für den Button.
     * Diese Methode konfiguriert den visuellen Stil des Button-Rahmens.
     */
    private void createButtonBorder() {
        Border raisedBevel = BorderFactory.createRaisedBevelBorder();
        Border loweredBevel = BorderFactory.createLoweredBevelBorder();
        setBorder(BorderFactory.createCompoundBorder(raisedBevel, loweredBevel));
    }

    /**
     * Überschreibt die setEnabled-Methode, um die Schriftfarbe entsprechend zu ändern.
     * Wenn der Button deaktiviert ist, wird die Schriftfarbe auf Grau gesetzt, ansonsten auf Weiß.
     *
     * @param enabled true, wenn der Button aktiviert werden soll, sonst false.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        // Ändere die Schriftfarbe entsprechend (Weiß, wenn aktiv, Grau, wenn deaktiviert)
        setForeground(enabled ? Color.WHITE : Color.GRAY);
        // Weitere Anpassungen, falls erforderlich
    }
}