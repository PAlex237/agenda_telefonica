package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.example.AdaugaIconFrame.setAppIcon;

/**
 * Clasa EcranDetaliiContact reprezintă interfața grafică pentru vizualizarea, editarea
 * și gestionarea unui contact existent în agenda.
 * Permite utilizatorului să vizualizeze informațiile unui contact, să le modifice,
 * să trimită emailuri sau să șteargă contactul.
 */
public class EcranDetaliiContact extends JFrame {
    private JTextField textNume;
    private JTextField textNumarTelefon;
    private JTextField textEmail;
    private JCheckBox checkBoxFavorit;
    private JButton btnSalveaza;
    private JButton btnSterge;
    private JButton btnTrimiteEmail;
    private Contact contact;
    private Agenda agenda;
    private EcranPrincipal parinte;

    /**
     * Constructorul pentru EcranDetaliiContact.
     *
     * @param parinte Fereastra principală care apelează acest ecran.
     * @param agenda  Obiectul de tip Agenda în care se află contactele.
     * @param contact Obiectul de tip Contact ale cărui detalii sunt gestionate.
     */
    public EcranDetaliiContact(EcranPrincipal parinte, Agenda agenda, Contact contact) {
        // Setează iconița aplicației
        setAppIcon(this, "images\\Icon.png");

        this.parinte = parinte;
        this.agenda = agenda;
        this.contact = contact;

        setTitle("Detalii Contact");
        setSize(900, 600);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spațiu între componente

        // Adăugare câmpuri pentru introducerea datelor
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nume:"), gbc);
        textNume = new JTextField(contact.getNume(), 20);
        gbc.gridx = 1;
        add(textNume, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Număr de Telefon:"), gbc);
        textNumarTelefon = new JTextField(contact.getNumarDeTelefon(), 20);
        gbc.gridx = 1;
        add(textNumarTelefon, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        textEmail = new JTextField(contact.getEmail(), 20);
        gbc.gridx = 1;
        add(textEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Favorit:"), gbc);
        checkBoxFavorit = new JCheckBox();
        checkBoxFavorit.setSelected("true".equals(contact.getFavorit()));
        gbc.gridx = 1;
        add(checkBoxFavorit, gbc);

        // Configurare buton pentru salvarea modificărilor
        btnSalveaza = new JButton("Salvează");
        configureButton(btnSalveaza, new Color(0, 123, 255), e -> salveazaContact());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(btnSalveaza, gbc);

        // Configurare buton pentru ștergerea contactului
        btnSterge = new JButton("Șterge");
        configureButton(btnSterge, new Color(255, 0, 0), e -> stergeContact());
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(btnSterge, gbc);

        // Configurare buton pentru trimiterea emailului
        btnTrimiteEmail = new JButton("Trimite Email");
        configureButton(btnTrimiteEmail, new Color(0, 255, 0), e -> deschideFereastraEmail());
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(btnTrimiteEmail, gbc);

        setLocationRelativeTo(parinte); // Poziționează fereastra relativ la fereastra principală
    }

    /**
     * Configurare generală pentru butoane.
     *
     * @param button     Butonul de configurat.
     * @param background Culoarea de fundal.
     * @param action     Acțiunea de efectuat la click.
     */
    private void configureButton(JButton button, Color background, ActionListener action) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFocusPainted(false);
        button.addActionListener(action);
    }

    /**
     * Deschide fereastra pentru trimiterea unui email către contactul selectat.
     */
    private void deschideFereastraEmail() {
        EcranEmail ecranEmail = new EcranEmail(contact.getEmail(), this);
        ecranEmail.setVisible(true);
    }

    /**
     * Salvează modificările făcute contactului și actualizează agenda.
     */
    private void salveazaContact() {
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

            // Actualizează datele contactului
            contact.setNume(nume);
            contact.setNumarDeTelefon(numarTelefon);
            contact.setEmail(email);
            contact.setFavorit(checkBoxFavorit.isSelected() ? "true" : "false");

            agenda.updateContact(contact);
            DataBase.incarcareDate(agenda); // Actualizează baza de date
            parinte.actualizeazaListaContacte("");

            JOptionPane.showMessageDialog(this, "Modificările au fost salvate cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Șterge contactul selectat din agendă și baza de date.
     */
    private void stergeContact() {
        int confirm = JOptionPane.showConfirmDialog(this, "Sigur dorești să ștergi acest contact?", "Confirmare Ștergere", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            agenda.stergereContact(contact);
            DataBase.incarcareDate(agenda);
            parinte.actualizeazaListaContacte("");

            JOptionPane.showMessageDialog(this, "Contactul a fost șters cu succes!", "Ștergere", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
