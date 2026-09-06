package Fatjon.Javamory.source;



import javax.swing.*;
/**
 * Die Hauptklasse des Javamory-Spiels.
 * Diese Klasse startet das Spiel, indem sie das Hauptfenster initialisiert und anzeigt.
 * Sie ist der Einstiegspunkt für das Spiel.
 */
public class JavamoryGame {
    /**
     * Die Hauptmethode des Spiels. Sie wird beim Start des Programms aufgerufen.
     * Diese Methode ist verantwortlich für die Initialisierung und das Anzeigen des Hauptfensters des Spiels.
     *
     * @param args Kommandozeilenargumente, die beim Start des Programms übergeben werden.
     *             In dieser Anwendung werden keine Kommandozeilenargumente verwendet.
     */
    public static void main(String[] args) {
        // Verwendung von SwingUtilities.invokeLater, um sicherzustellen, dass die GUI im Event-Dispatch-Thread erstellt wird.
        SwingUtilities.invokeLater(() -> {
            try {
                // Erstellen und Anzeigen des Hauptfensters des Spiels.
                JavamoryFrame frame = JavamoryFrame.getInstanceOf();
                frame.setVisible(true); // Zeigt das Hauptfenster an
            } catch (Exception e) {
                // Ausgabe der Fehlermeldung im Falle einer Ausnahme.
                e.printStackTrace();
            }
        });
    }
}