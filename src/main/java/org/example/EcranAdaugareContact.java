package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.example.AdaugaIconFrame.*;

/**
 * Clasa EcranAdaugareContact reprezintă interfața grafică pentru adăugarea unui contact nou în agendă.
 * Oferă câmpuri pentru introducerea numelui, numărului de telefon și email-ului unui contact,
 * precum și validări pentru aceste informații.
 */
public class EcranAdaugareContact extends JFrame {
    private JTextField textNume;
    private JTextField textNumarTelefon;
    private JTextField textEmail;
    private JButton btnSalveaza;

    /**
     * Constructorul creează o fereastră pentru adăugarea unui contact nou.
     *
     * @param parinte Fereastra principală care apelează acest ecran.
     * @param agenda  Obiectul de tip Agenda în care se vor salva contactele.
     */
    public EcranAdaugareContact(EcranPrincipal parinte, Agenda agenda) {

        // Setează iconița aplicației
        setAppIcon(EcranAdaugareContact.this, "images\\Icon.png");

        setTitle("Adaugă Contact");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panoul principal pentru câmpurile de introducere
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new GridLayout(4, 2, 10, 20)); // Spațiu între componente
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Marginile

        // Câmp pentru introducerea numelui
        panelMain.add(new JLabel("Nume:"));
        textNume = new JTextField();
        panelMain.add(textNume);

        // Câmp pentru introducerea numărului de telefon
        panelMain.add(new JLabel("Număr de Telefon:"));
        textNumarTelefon = new JTextField();
        panelMain.add(textNumarTelefon);

        // Câmp pentru introducerea email-ului
        panelMain.add(new JLabel("Email (opțional):"));
        textEmail = new JTextField();
        panelMain.add(textEmail);

        // Configurare buton "Salvează"
        btnSalveaza = new JButton("Salvează");
        btnSalveaza.setFont(new Font("Arial", Font.BOLD, 14)); // Fontul textului
        btnSalveaza.setPreferredSize(new Dimension(250, 40));  // Dimensiune buton
        btnSalveaza.setBackground(new Color(0, 123, 255));  // Fundal albastru
        btnSalveaza.setForeground(Color.WHITE);  // Text alb
        btnSalveaza.setFocusPainted(false);  // Elimină conturul la click
        btnSalveaza.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));  // Contur negru gros
        btnSalveaza.addActionListener(new ActionListener() {
            /**
             * Gestionează acțiunea de salvare a contactului.
             *
             * @param e Evenimentul de click pe butonul "Salvează".
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String nume = textNume.getText().trim();
                String numarTelefon = textNumarTelefon.getText().trim();
                String email = textEmail.getText().trim();

                try {
                    // Validare nume
                    if (!nume.matches("^[a-zA-ZăâîșțĂÂÎȘȚ\\s]+$")) {
                        throw new IllegalArgumentException("Numele poate conține doar litere și spații.");
                    }

                    // Validare număr de telefon
                    if (!numarTelefon.matches("^\\d{10}$")) {
                        throw new IllegalArgumentException("Numărul de telefon trebuie să conțină exact 10 cifre.");
                    }

                    // Validare email (dacă este completat)
                    if (!email.isEmpty() && !email.matches("^[\\w._%+-]+@[\\w.-]+\\.(com|ro)$")) {
                        throw new IllegalArgumentException("Email-ul trebuie să fie valid și să conțină @ și domeniul .com sau .ro.");
                    }

                    // Creează un nou contact și îl adaugă în agenda principală
                    Contact contact = new Contact(nume, numarTelefon, email);
                    agenda.adaugaContact(contact); // Adaugă contactul în agendă
                    DataBase.incarcareDate(agenda);  // Actualizează baza de date
                    parinte.actualizeazaListaContacte("");  // Actualizează lista în interfața principală

                    dispose();  // Închide fereastra curentă

                } catch (IllegalArgumentException ex) {
                    // Afișează un mesaj de eroare dacă datele introduse sunt invalide
                    JOptionPane.showMessageDialog(EcranAdaugareContact.this,
                            ex.getMessage(),
                            "Eroare",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Adaugă butonul "Salvează" la un panou separat
        JPanel panelButon = new JPanel();
        panelButon.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centrare buton
        panelButon.add(btnSalveaza);

        // Adaugă panourile în fereastră
        add(panelMain, BorderLayout.CENTER);
        add(panelButon, BorderLayout.SOUTH);

        // Poziționează fereastra relativ la fereastra principală
        setLocationRelativeTo(parinte);
    }
}
