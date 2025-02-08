package org.example;

/**
 * Clasa principala a aplicatiei care initializeaza si porneste interfata grafica.
 */
public class App {

    /**
     * Metoda principala care porneste aplicatia.
     *
     * @param args argumentele liniei de comanda (neutilizate in aceasta aplicatie)
     */
    public static void main(String[] args) {
        // Creeaza o instanta a ecranului principal si il porneste
        EcranPrincipal ecranPrincipal = new EcranPrincipal();
        ecranPrincipal.setVisible(true);  // Afiseaza ecranul principal
    }
}

