package org.example;

import java.sql.*;

/**
 * Clasa pentru gestionarea operatiilor cu baza de date a agendei.
 */
public class DataBase {

    /**
     * Salveaza contactele din agenda in baza de date, inlocuind datele existente.
     *
     * @param agenda obiectul {@link Agenda} care contine lista de contacte ce trebuie salvata
     */
    public static void incarcareDate(Agenda agenda) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/contacte", // URL-ul bazei de date
                "root", // Utilizatorul bazei de date
                "bidetejuro08" // Parola bazei de date
        )) {

            // Goleste tabelul CONTACTE pentru a preveni duplicarea datelor
            Statement statement = connection.createStatement();
            statement.executeUpdate("TRUNCATE TABLE CONTACTE");

            // Insereaza fiecare contact din lista agendei in tabel
            for (Contact contact : agenda.contacte) {
                String query = String.format(
                        "INSERT INTO CONTACTE (nume, numar_de_telefon, email, favorit) " +
                                "VALUES ('%s', '%s', '%s', '%s')",
                        contact.getNume(),
                        contact.getNumarDeTelefon(),
                        contact.getEmail(),
                        contact.getFavorit()
                );
                statement.executeUpdate(query);
            }

        } catch (SQLException e) {
            // Afiseaza eroarea in consola in cazul unei exceptii SQL
            e.printStackTrace();
        }
    }
}
