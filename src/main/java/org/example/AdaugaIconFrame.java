package org.example;

import javax.swing.*;

/**
 * Clasa utilitara pentru setarea unei iconite personalizate la ferestrele aplicatiei.
 */
public class AdaugaIconFrame {

    /**
     * Seteaza o iconita personalizata pentru o fereastra JFrame.
     *
     * @param frame    instanta JFrame pentru care se seteaza iconita
     * @param iconPath calea catre fisierul imaginii utilizate ca iconita
     */
    public static void setAppIcon(JFrame frame, String iconPath) {
        try {
            ImageIcon image = new ImageIcon(iconPath);
            frame.setIconImage(image.getImage());
        } catch (Exception ex) {
            System.out.println("Nu s-a putut incarca imaginea pentru iconita: " + ex.getMessage());
        }
    }
}

