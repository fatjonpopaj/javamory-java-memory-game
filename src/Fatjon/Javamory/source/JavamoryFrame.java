package Fatjon.Javamory.source;



import javax.swing.*;

import java.net.URL;
import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Die JavamoryFrame-Klasse ist das Hauptfenster für das Memory-Spiel Javamory.
 * Sie initialisiert und verwaltet die Hauptbenutzeroberfläche des Spiels, einschließlich der Menüs und Dialoge.
 */
public class JavamoryFrame extends JFrame {
    // Konstanten für die Standardgröße des Fensters
    public static final int DEFAULT_WIDTH = 1280; // Standardbreite des Fensters
    public static final int DEFAULT_HEIGHT = 1024; // Standardhöhe des Fensters

    /**
     * Privater Konstruktor für JavamoryFrame.
     * Initialisiert das Hauptfenster mit den notwendigen Einstellungen und Inhalten.
     */
    private JavamoryFrame() {
        initializeFrame(); // Initialisiert das Hauptfenster
    }

    /**
     * Initialisiert das Hauptfenster mit Titel, Größe, Verhalten beim Schließen und weiteren Einstellungen.
     */
    private void initializeFrame() {
        setTitle("JAVAMORY"); // Setzt den Fenstertitel

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Bestimmt das Verhalten beim Schließen
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            JavamoryFrame.getInstanceOf().stopBackgroundMusic();
            JavamoryFrame.getInstanceOf().dispose();
            JavamoryFrame.running = false;
            if (UserPanel.exitDialog != null)
                UserPanel.exitDialog.handleGoMenuAction();

            }
        });

        setResizable(false); // Verhindert, dass die Größe des Fensters geändert wird

        loadFrameIcon(); // Lädt das Icon für das Fenster

        add(new MenuC(this)); // Fügt das Hauptmenü zum Fenster hinzu

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT); // Setzt die Größe des Fensters

        setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
    }

    /**
     * Lädt das Icon für das Frame.
     * Diese Methode versucht, ein Icon-Bild zu laden und es als Fenstersymbol zu setzen.
     */
    private void loadFrameIcon() {
        URL imageUrl = getClass().getResource("Bilder/Anonymous/anonymous.png"); // Pfad zum Icon-Bild
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            setIconImage(icon.getImage()); // Setzt das Icon für das Fenster
        } else {
            System.err.println("Icon image not found"); // Fehlermeldung, falls das Icon nicht gefunden wird
        }
    }

    /**
     * Innere statische Klasse, die das Lazy Initialization Holder Idiom verwendet.
     * Stellt sicher, dass die Instanz von JavamoryFrame erst beim ersten Zugriff erstellt wird.
     */
    private static class JavamoryFrameHolder {
        private static final JavamoryFrame INSTANCE = new JavamoryFrame();
    }

    /**
     * Gibt eine Instanz von JavamoryFrame zurück.
     * Diese Methode stellt sicher, dass nur eine Instanz von JavamoryFrame existiert (Singleton-Muster).
     *
     * @return Eine Instanz von JavamoryFrame.
     */
    public static JavamoryFrame getInstanceOf() {
        return JavamoryFrameHolder.INSTANCE;
    }

    public static boolean running;
    
    /**
     * Hauptmethode zum Starten der Anwendung.
     * Diese Methode initialisiert das Hauptfenster und macht es sichtbar.
     *
     * @param args Argumente der Kommandozeile (nicht verwendet).
     */
    public static void main(String[] args) {
        
        running = true;
        
        SwingUtilities.invokeLater(() -> {
            try {
                JavamoryFrame frame = JavamoryFrame.getInstanceOf();
                frame.playBackgroundMusic(); // Spielt Hintergrundmusik ab
                frame.setVisible(true); // Macht das Fenster sichtbar
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
         
        while(running){
            
          
                try {
                    Thread.sleep(200);
                    if (!running) {
                        break;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            
        }
    }

    /**
     * Zeigt die Spielresultate in einem Dialog an.
     * Erstellt und zeigt einen Dialog mit den Ergebnissen des Spiels an, einschließlich Versuche und benötigter Zeit.
     *
     * @param attempts      Die Anzahl der Versuche des Spielers.
     * @param timeInSeconds Die Zeit, die der Spieler benötigt hat, in Sekunden.
     */
    public void showGameResults(int attempts, long timeInSeconds) {
        SwingUtilities.invokeLater(() -> {
            GameResults resultsPanel = new GameResults(attempts, timeInSeconds);
            JDialog resultsDialog = new JDialog(this, "You've won!", true);
            resultsDialog.getContentPane().add(resultsPanel);
            resultsDialog.setSize(650, 200); // Setzt die Größe des Ergebnisdialogs
            resultsDialog.setLocationRelativeTo(this); // Zentriert den Dialog relativ zum Hauptfenster
            resultsDialog.setVisible(true); // Macht den Dialog sichtbar
        });
    }

    Clip clip;
    
    /**
     * Spielt Hintergrundmusik ab.
     * Diese Methode versucht, eine Audiodatei zu laden und als Hintergrundmusik in einer Endlosschleife abzuspielen.
     */
    public void playBackgroundMusic() {
        // Versuch, die Hintergrundmusik abzuspielen
        try {
            URL musicURL = getClass().getResource("Bilder/Musik/Musik.wav");
            if (musicURL != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicURL);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // Musik in Endlosschleife abspielen
                clip.start();
            } else {
                System.err.println("Musikdatei nicht gefunden");
            }
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Das Audioformat wird nicht unterstützt");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Datei");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Audio Line ist nicht verfügbar");
            e.printStackTrace();
        }
    }
    
    public void stopBackgroundMusic() {
        // Versuch, die Hintergrundmusik abzuspielen
        clip.stop();
    }
}
