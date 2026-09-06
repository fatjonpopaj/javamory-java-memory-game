package Fatjon.Javamory.source;



import javax.swing.*;

/**
 * Eine benutzerdefinierte Implementierung des DefaultButtonModel.
 * Diese Klasse erweitert DefaultButtonModel und behält die Standardimplementierung der setEnabled-Methode bei.
 * Sie kann verwendet werden, um zusätzliche Verhaltensweisen für Buttons zu definieren, ohne die Grundfunktionalität zu verändern.
 */
public class CustomButtonModel extends DefaultButtonModel {

    /**
     * Setzt den Aktivierungszustand des Buttons.
     * Diese Methode behält die Standardimplementierung von DefaultButtonModel bei.
     * Sie kann bei Bedarf in Unterklassen überschrieben werden, um spezifisches Verhalten zu implementieren.
     *
     * @param b Der boolesche Wert, der angibt, ob der Button aktiviert (true) oder deaktiviert (false) sein soll.
     */
    @Override
    public void setEnabled(boolean b){
        super.setEnabled(b); // Ruft die setEnabled-Methode der Basisklasse auf
    }
}