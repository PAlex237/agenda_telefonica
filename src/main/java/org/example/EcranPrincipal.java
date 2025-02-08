package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.example.AdaugaIconFrame.setAppIcon;

/**
 * Clasa EcranPrincipal reprezintă fereastra principală a aplicației de gestionare a contactelor.
 * Permite adăugarea de contacte, vizualizarea acestora și accesul la contactele favorite.
 */
public class EcranPrincipal extends JFrame {
    private JTable tabelaContacte;
    private JPanel panelMain;
    private JButton butonAdaugareContact;
    private JButton butonFavorite;
    private JPanel panelButoane;
    private Agenda agenda;
    private DefaultTableModel modelTabela;
    private JTextField searchBar;  // Adăugăm un câmp de text pentru căutare

    public EcranPrincipal() {
        setAppIcon(this, "images\\Icon.png");

        setTitle("Agenda Contacte");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());

        // Inițializare agenda și încărcare date
        agenda = new Agenda();
        agenda = agenda.conectare();  // Conectare și încărcare date din baza de date

        // Crearea tabelului pentru contacte
        String[] coloane = {"Nume", "Număr de Telefon", "Email"};
        modelTabela = new DefaultTableModel(coloane, 0); // Inițializare modelTabela
        tabelaContacte = new JTable(modelTabela);

        // Inițializare search bar
        searchBar = new JTextField();
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Căutare contact în funcție de textul introdus
                String searchText = searchBar.getText().toLowerCase();
                actualizeazaListaContacte(searchText);
            }
        });

        // Panou pentru căutare și butoane
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));  // Folosim FlowLayout
        searchPanel.setPreferredSize(new Dimension(900, 50)); // Înălțimea panoului de căutare
        searchPanel.setBackground(Color.WHITE); // Fundal alb pentru panoul de căutare

// Etichetă cu dimensiune fixă
        JLabel searchLabel = new JLabel("Căutare contact:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 12));  // Font mai mic pentru etichetă
        searchLabel.setForeground(Color.BLACK);  // Culoare text
        searchLabel.setPreferredSize(new Dimension(150, 30)); // Dimensiune fixă pentru etichetă

// Câmp de căutare (searchBar)
        searchBar.setPreferredSize(new Dimension(200, 30)); // Dimensiune fixă pentru câmpul de căutare

        searchPanel.add(searchLabel);  // Adăugăm eticheta în panoul de căutare
        searchPanel.add(searchBar);    // Adăugăm câmpul de căutare în panoul de căutare
        panelMain.add(searchPanel, BorderLayout.NORTH);  // Adăugăm panoul de căutare în cadrul principal

        // Actualizează lista de contacte
        actualizeazaListaContacte("");  // Actualizează lista de contacte fără filtru

        // Inițializare butoane
        butonAdaugareContact = new JButton("Adaugă Contact");
        butonFavorite = new JButton("Favorite");

        // Buton pentru adăugarea unui contact
        butonAdaugareContact.addActionListener(e -> {
            AdaugaContactFrame adaugaContactFrame = new AdaugaContactFrame(this, agenda);
            adaugaContactFrame.setVisible(true);
        });

        // Buton pentru afișarea contactelor favorite
        butonFavorite.addActionListener(e -> {
            EcranFavorite ecranFavorite = new EcranFavorite(this, agenda);
            ecranFavorite.setVisible(true);
        });

        // Stilizare butoane
        butonAdaugareContact.setFont(new Font("Arial", Font.BOLD, 14)); // Fontul butonului
        butonAdaugareContact.setPreferredSize(new Dimension(250, 40));  // Dimensiunea butonului
        butonAdaugareContact.setBackground(Color.BLUE);  // Fundal albastru
        butonAdaugareContact.setForeground(Color.WHITE);  // Text alb
        butonAdaugareContact.setFocusPainted(false);  // Elimină conturul la click
        butonAdaugareContact.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));  // Contur negru gros

        butonFavorite.setFont(new Font("Arial", Font.BOLD, 14));  // Fontul butonului
        butonFavorite.setPreferredSize(new Dimension(250, 40));   // Dimensiunea butonului
        butonFavorite.setBackground(Color.BLUE);  // Fundal verde
        butonFavorite.setForeground(Color.WHITE);  // Text alb
        butonFavorite.setFocusPainted(false);  // Elimină conturul la click
        butonFavorite.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));  // Contur negru gros

        // Centrarea butoanelor folosind FlowLayout
        panelButoane = new JPanel();
        panelButoane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));  // Centrare și distanță între butoane
        panelButoane.add(butonAdaugareContact);
        panelButoane.add(butonFavorite);

        panelMain.add(panelButoane, BorderLayout.SOUTH);

        // Stilizare tabel
        tabelaContacte.setBackground(Color.WHITE);  // Fundal alb
        tabelaContacte.setForeground(Color.BLACK);  // Text negru pentru contrast
        tabelaContacte.setFont(new Font("Arial", Font.PLAIN, 14));  // Fontul și mărimea textului

        JScrollPane tabelaPane = new JScrollPane(tabelaContacte);
        panelMain.add(tabelaPane, BorderLayout.CENTER); // Tabelul în centrul ferestrei

        add(panelMain);

        // Eveniment la click pe un contact pentru a vizualiza detaliile
        tabelaContacte.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tabelaContacte.getSelectedRow();
                if (index != -1) {
                    Contact selectedContact = agenda.contacte.get(index);
                    EcranDetaliiContact detaliiFrame = new EcranDetaliiContact(EcranPrincipal.this, agenda, selectedContact);
                    detaliiFrame.setVisible(true);
                }
            }
        });

        // Actualizarea datelor în baza de date
        DataBase.incarcareDate(agenda);
    }

    /**
     * Metodă pentru a actualiza lista de contacte în tabel.
     * Aceasta va popula tabelul cu numele, numărul de telefon și emailul contactelor.
     * @param searchText Textul de căutare
     */
    public void actualizeazaListaContacte(String searchText) {
        // Curățăm modelul tabelului înainte de a-l popula
        modelTabela.setRowCount(0);

        // Adăugăm contactele care se potrivesc cu searchText în tabel
        for (Contact contact : agenda.contacte) {
            if (contact.getNume().toLowerCase().startsWith(searchText) || contact.getNumarDeTelefon().startsWith(searchText)) {
                Object[] rand = new Object[3];
                rand[0] = contact.getNume();
                rand[1] = contact.getNumarDeTelefon();
                rand[2] = contact.getEmail();
                modelTabela.addRow(rand);
            }
        }
    }

    public static void main(String[] args) {
        EcranPrincipal ecranPrincipal = new EcranPrincipal();
        ecranPrincipal.setVisible(true);
    }
}
