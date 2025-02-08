package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clasa care reprezinta agenda de contacte, avand functionalitati pentru gestionarea contactelor
 * si conectarea la o baza de date.
 */
public class Agenda {
    static ArrayList<Contact> contacte;

    /**
     * Conecteaza la baza de date si incarca toate contactele din tabelul "contacte".
     *
     * @return o instanta a clasei Agenda cu lista de contacte incarcata din baza de date
     */
    public Agenda conectare() {
        Agenda agenda = new Agenda(new ArrayList<>());
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/contacte",
                    "root",
                    "bidetejuro08"
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CONTACTE");

            while (resultSet.next()) {
                agenda.contacte.add(new Contact(
                        resultSet.getString("nume"),
                        resultSet.getString("numar_de_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("favorit")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agenda;
    }

    /**
     * Constructor implicit care initializeaza o lista vida de contacte.
     */
    public Agenda() {
        contacte = new ArrayList<>();
    }

    /**
     * Constructor care initializeaza agenda cu o lista data de contacte.
     *
     * @param contacte lista initiala de contacte
     */
    public Agenda(ArrayList<Contact> contacte) {
        this.contacte = contacte;
    }

    /**
     * Adauga un contact in agenda.
     *
     * @param contact contactul care trebuie adaugat
     */
    public void adaugaContact(Contact contact) {
        this.contacte.add(contact);
    }

    /**
     * Adauga un contact in agenda prin introducerea manuala a datelor.
     */
    public void adaugaContact() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nume:");
        String nume = scanner.nextLine();
        System.out.println("Numar de telefon:");
        String numarDeTelefon = scanner.next();
        System.out.println("Email:");
        String email = scanner.next();
        this.contacte.add(new Contact(nume, numarDeTelefon, email));
    }

    /**
     * Returneaza lista de contacte din agenda.
     *
     * @return lista de contacte
     */
    public ArrayList<Contact> getContacte() {
        return contacte;
    }

    /**
     * Actualizeaza un contact existent in agenda.
     *
     * @param contactActualizat contactul cu datele actualizate
     */
    public void updateContact(Contact contactActualizat) {
        for (int i = 0; i < contacte.size(); i++) {
            if (contacte.get(i).equals(contactActualizat)) {
                contacte.set(i, contactActualizat); // Inlocuieste contactul vechi cu cel actualizat
                break;
            }
        }
    }

    /**
     * Sterge un contact din agenda.
     *
     * @param contact contactul care trebuie sters
     */
    public void stergereContact(Contact contact) {
        this.contacte.remove(contact);
    }

    /**
     * Cauta un contact dupa nume.
     *
     * @param nume numele contactului cautat
     * @return contactul gasit sau null daca nu exista
     */
    public Contact cautaContact(String nume) {
        for (Contact contact : this.contacte) {
            if (contact.getNume().equals(nume)) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Returneaza o reprezentare sub forma de sir a obiectului Agenda.
     *
     * @return reprezentarea sub forma de sir a agendei
     */
    @Override
    public String toString() {
        return "Agenda{" +
                "contacte=" + contacte +
                '}';
    }
}

