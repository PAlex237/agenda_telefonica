package org.example;

import javax.swing.*;
import java.awt.*;
import static org.example.AdaugaIconFrame.setAppIcon;

/**
 * Fereastra pentru adăugarea unui contact nou în agendă.
 */
public class AdaugaContactFrame extends JFrame {
    private JTextField textNume;
    private JTextField textNumarTelefon;
    private JTextField textEmail;
    private JButton btnSalveaza;

    /**
     * Constructor care inițializează fereastra pentru adăugarea unui contact.
     *
     * @param parinte     referință către fereastra principală a aplicației
     * @param agenda      obiectul care gestionează lista de contacte
     * @param esteFavorit indică dacă noul contact este marcat ca favorit
     */
    public AdaugaContactFrame(EcranPrincipal parinte, Agenda agenda, boolean esteFavorit) {
        // Setează iconița aplicației
        setAppIcon(AdaugaContactFrame.this, "images\\Icon.png");

        setTitle("Adaugă Contact");
        setSize(600, 450);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spațiu între componente

        // Adaugă câmpurile pentru introducerea datelor de contact
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nume:"), gbc);
        textNume = new JTextField(20);
        gbc.gridx = 1;
        add(textNume, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Număr de Telefon:"), gbc);
        textNumarTelefon = new JTextField(20);
        gbc.gridx = 1;
        add(textNumarTelefon, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        textEmail = new JTextField(20);
        gbc.gridx = 1;
        add(textEmail, gbc);

        // Buton pentru salvarea contactului
        btnSalveaza = new JButton("Salvează");
        btnSalveaza.setFont(new Font("Arial", Font.BOLD, 14)); // Fontul butonului
        btnSalveaza.setPreferredSize(new Dimension(250, 40));  // Dimensiune buton
        btnSalveaza.setBackground(new Color(0, 123, 255));  // Fundal albastru
        btnSalveaza.setForeground(Color.WHITE);  // Text alb
        btnSalveaza.setFocusPainted(false);  // Elimină conturul la click
        btnSalveaza.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));  // Contur negru gros
        btnSalveaza.addActionListener(e -> salveazaContact(parinte, agenda, esteFavorit));

        // Adăugăm butonul în partea de jos, centrat
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Butonul va ocupa 2 coloane
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(btnSalveaza, gbc);

        // Poziționează fereastra relativ la fereastra principală
        setLocationRelativeTo(parinte);
    }

    /**
     * Constructor care inițializează fereastra pentru adăugarea unui contact
     * fără a-l marca ca favorit.
     *
     * @param parinte referință către fereastra principală a aplicației
     * @param agenda  obiectul care gestionează lista de contacte
     */
    public AdaugaContactFrame(EcranPrincipal parinte, Agenda agenda) {
        this(parinte, agenda, false); // Contactul nu este favorit
    }

    /**
     * Salvează un contact nou, validând datele introduse.
     *
     * @param parinte     referință către fereastra principală
     * @param agenda      obiectul care gestionează lista de contacte
     * @param esteFavorit indică dacă noul contact este marcat ca favorit
     */
    private void salveazaContact(EcranPrincipal parinte, Agenda agenda, boolean esteFavorit) {
        String nume = textNume.getText().trim();
        String numarTelefon = textNumarTelefon.getText().trim();
        String email = textEmail.getText().trim();

        try {
            // Validare nume
            if (nume.isEmpty()) {
                throw new IllegalArgumentException("Numele este obligatoriu.");
            }
            if (!nume.matches("^[a-zA-ZăâîșțĂÂÎȘȚ\\s]+$")) {
                throw new IllegalArgumentException("Numele poate conține doar litere și spații.");
            }

            // Validare număr de telefon
            if (!numarTelefon.matches("^\\d{10}$")) {
                throw new IllegalArgumentException("Numărul de telefon trebuie să conțină exact 10 cifre.");
            }

            // Validare email (dacă este completat)
            if (!email.isEmpty() && !email.matches("^[\\w._%+-]+@[\\w.-]+\\.(com|ro)$")) {
                throw new IllegalArgumentException("Email-ul trebuie să fie valid și să conțină domeniul .com sau .ro.");
            }

            // Creează un nou contact
            Contact contact = new Contact(nume, numarTelefon, email);
            if (esteFavorit) {
                contact.setFavorit("true"); // Marchează contactul ca favorit
            }

            // Adaugă contactul în agendă și actualizează interfața
            agenda.adaugaContact(contact);
            DataBase.incarcareDate(agenda); // Actualizează baza de date
            parinte.actualizeazaListaContacte(""); // Actualizează lista de contacte în ecranul principal

            // Actualizează lista de contacte favorite, dacă fereastra este deschisă
            for (Window window : JFrame.getWindows()) {
                if (window instanceof EcranFavorite) {
                    ((EcranFavorite) window).actualizeazaListaFavorite();
                }
            }

            // Închide fereastra după salvare
            JOptionPane.showMessageDialog(AdaugaContactFrame.this,
                    "Contactul a fost adăugat cu succes!",
                    "Succes",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (IllegalArgumentException ex) {
            // Afișează eroarea într-un pop-up
            JOptionPane.showMessageDialog(AdaugaContactFrame.this,
                    ex.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
