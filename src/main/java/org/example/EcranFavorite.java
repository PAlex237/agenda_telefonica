package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Clasa EcranFavorite reprezintă interfața grafică pentru vizualizarea contactelor marcate ca favorite.
 * Permite afișarea unui tabel cu contacte favorite și accesul la detaliile fiecărui contact.
 */
public class EcranFavorite extends JFrame {
    private JTable tabelaFavorite;
    private DefaultTableModel modelTabelaFavorite;
    private JButton butonAdaugaFavorit;
    private Agenda agenda;
    private EcranPrincipal parinte;
    private JTextField searchField;  // Câmpul de căutare

    public EcranFavorite(EcranPrincipal parinte, Agenda agenda) {
        // Setează iconița aplicației
        AdaugaIconFrame.setAppIcon(this, "images\\Icon.png");

        this.parinte = parinte;
        this.agenda = agenda;

        setTitle("Contacte Favorite");
        setSize(900, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Creează tabelul pentru contactele favorite
        String[] coloane = {"Nume", "Număr de Telefon", "Email"};
        modelTabelaFavorite = new DefaultTableModel(coloane, 0);  // Inițializare model tabela
        tabelaFavorite = new JTable(modelTabelaFavorite);

        // Crează un câmp de căutare
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterContacts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterContacts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterContacts();
            }
        });

        // Panou pentru căutare și butoane
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelTop.add(new JLabel("Căutare Contact:"));
        panelTop.add(searchField);

        // Actualizează lista de contacte favorite
        actualizeazaListaFavorite();

        // Setare stil pentru tabel
        tabelaFavorite.setBackground(Color.WHITE);
        tabelaFavorite.setForeground(Color.BLACK);
        tabelaFavorite.setFont(new Font("Arial", Font.PLAIN, 14));  // Fontul și mărimea textului

        // Buton pentru a adăuga un contact la favorite
        butonAdaugaFavorit = new JButton("Adaugă Contact la Favorite");
        butonAdaugaFavorit.addActionListener(e -> {
            AdaugaContactFrame adaugaFrame = new AdaugaContactFrame(parinte, agenda, true); // true pentru a adăuga ca favorit
            adaugaFrame.setVisible(true);
        });

        // Stilizare buton
        butonAdaugaFavorit.setFont(new Font("Arial", Font.BOLD, 14)); // Fontul butonului
        butonAdaugaFavorit.setPreferredSize(new Dimension(250, 40));  // Dimensiunea butonului
        butonAdaugaFavorit.setBackground(Color.BLUE);  // Fundal albastru
        butonAdaugaFavorit.setForeground(Color.WHITE);  // Text alb
        butonAdaugaFavorit.setFocusPainted(false);  // Elimină conturul la click
        butonAdaugaFavorit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));  // Contur negru gros

        // Centrarea butonului folosind FlowLayout
        JPanel panelButoane = new JPanel();
        panelButoane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));  // Centrare și distanță între butoane
        panelButoane.add(butonAdaugaFavorit);

        // Permite deschiderea detaliilor contactului favorit la click
        tabelaFavorite.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tabelaFavorite.getSelectedRow();
                if (index != -1) {
                    Contact contactFavorit = obtineContactFavorite().get(index);
                    EcranDetaliiContact detaliiFrame = new EcranDetaliiContact(parinte, agenda, contactFavorit);
                    detaliiFrame.setVisible(true);
                }
            }
        });

        add(panelTop, BorderLayout.NORTH);  // Adaugă câmpul de căutare în partea de sus
        add(new JScrollPane(tabelaFavorite), BorderLayout.CENTER);
        add(panelButoane, BorderLayout.SOUTH);
        setLocationRelativeTo(parinte);  // Poziționează fereastra relativ la fereastra principală
    }

    // Funcție de căutare
    private void filterContacts() {
        String searchText = searchField.getText().toLowerCase();
        ArrayList<Contact> filteredContacts = new ArrayList<>();

        for (Contact contact : obtineContactFavorite()) {
            // Căutare după nume sau număr de telefon
            if (contact.getNume().toLowerCase().contains(searchText) || contact.getNumarDeTelefon().contains(searchText)) {
                filteredContacts.add(contact);
            }
        }

        // Actualizează tabelul cu contactele filtrate
        modelTabelaFavorite.setRowCount(0);  // Curăță tabelul
        for (Contact contact : filteredContacts) {
            Object[] rand = new Object[3];
            rand[0] = contact.getNume();
            rand[1] = contact.getNumarDeTelefon();
            rand[2] = contact.getEmail();
            modelTabelaFavorite.addRow(rand);
        }

        // Dacă nu există contacte favorite, adăugăm un mesaj în tabel
        if (filteredContacts.isEmpty()) {
            modelTabelaFavorite.addRow(new Object[]{"Nu există contacte care să corespundă căutării.", "", ""});
        }
    }

    /**
     * Obține lista de contacte favorite din agenda.
     *
     * @return o listă cu contacte marcate ca favorite.
     */
    private ArrayList<Contact> obtineContactFavorite() {
        ArrayList<Contact> favorite = new ArrayList<>();
        for (Contact contact : agenda.getContacte()) {
            if ("true".equals(contact.getFavorit())) {
                favorite.add(contact);
            }
        }
        return favorite;
    }

    /**
     * Actualizează tabela de contacte favorite afișate în fereastra curentă.
     * Aceasta va afișa un mesaj dacă nu există contacte favorite.
     */
    public void actualizeazaListaFavorite() {
        modelTabelaFavorite.setRowCount(0);  // Curăță tabelul

        ArrayList<Contact> favorite = obtineContactFavorite();
        for (Contact contact : favorite) {
            Object[] rand = new Object[3];
            rand[0] = contact.getNume();
            rand[1] = contact.getNumarDeTelefon();
            rand[2] = contact.getEmail();
            modelTabelaFavorite.addRow(rand);
        }

        if (favorite.isEmpty()) {
            modelTabelaFavorite.addRow(new Object[]{"Nu există contacte favorite.", "", ""});
        }
    }
}


