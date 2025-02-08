package org.example;

import java.util.Objects;

/**
 * Clasa Contact reprezintă un contact dintr-o agendă telefonică.
 * Fiecare contact are un nume, un număr de telefon, o adresă de email și un atribut pentru marcarea ca favorit.
 */
public class Contact {

    private String nume;
    private String numarDeTelefon;
    private String email;
    private String favorit;

    /**
     * Constructor care creează un contact cu nume, număr de telefon și email.
     * Contactul este implicit marcat ca "nefavorit".
     *
     * @param nume            Numele contactului.
     * @param numarDeTelefon  Numărul de telefon al contactului (exact 10 cifre).
     * @param email           Adresa de email a contactului (trebuie să fie validă).
     * @throws IllegalArgumentException Dacă numele, numărul de telefon sau email-ul sunt invalide.
     */
    public Contact(String nume, String numarDeTelefon, String email) {
        setNume(nume);
        setNumarDeTelefon(numarDeTelefon);
        setEmail(email);
        this.favorit = "false";
    }

    /**
     * Constructor care creează un contact cu nume, număr de telefon, email și statut favorit.
     *
     * @param nume            Numele contactului.
     * @param numarDeTelefon  Numărul de telefon al contactului (exact 10 cifre).
     * @param email           Adresa de email a contactului (trebuie să fie validă).
     * @param favorit         Statutul de favorit ("true" sau "false").
     * @throws IllegalArgumentException Dacă numele, numărul de telefon sau email-ul sunt invalide.
     */
    public Contact(String nume, String numarDeTelefon, String email, String favorit) {
        setNume(nume);
        setNumarDeTelefon(numarDeTelefon);
        setEmail(email);
        this.favorit = favorit;
    }

    /**
     * Marchează contactul ca favorit.
     */
    public void adaugareLaFavorite() {
        this.favorit = "true";
    }

    /**
     * Elimină contactul din lista de favorite.
     */
    public void stergereDeLaFavorite() {
        this.favorit = "false";
    }

    /**
     * Setează statutul favorit al contactului.
     *
     * @param favorit Statutul de favorit ("true" sau "false").
     */
    public void setFavorit(String favorit) {
        this.favorit = favorit;
    }

    /**
     * Returnează statutul favorit al contactului.
     *
     * @return Statutul de favorit ("true" sau "false").
     */
    public String getFavorit() {
        return favorit;
    }

    /**
     * Returnează numele contactului.
     *
     * @return Numele contactului.
     */
    public String getNume() {
        return nume;
    }

    /**
     * Setează numele contactului.
     *
     * @param nume Numele contactului (doar litere și spații).
     * @throws IllegalArgumentException Dacă numele conține caractere invalide.
     */
    public void setNume(String nume) {
        if (!nume.matches("^[a-zA-ZăâîșțĂÂÎȘȚ\\s]+$")) {
            throw new IllegalArgumentException("Numele poate conține doar litere și spații.");
        }
        this.nume = nume;
    }

    /**
     * Returnează numărul de telefon al contactului.
     *
     * @return Numărul de telefon (exact 10 cifre).
     */
    public String getNumarDeTelefon() {
        return numarDeTelefon;
    }

    /**
     * Setează numărul de telefon al contactului.
     *
     * @param numarDeTelefon Numărul de telefon (exact 10 cifre).
     * @throws IllegalArgumentException Dacă numărul de telefon nu are formatul corect.
     */
    public void setNumarDeTelefon(String numarDeTelefon) {
        if (!numarDeTelefon.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Numărul de telefon trebuie să conțină exact 10 cifre.");
        }
        this.numarDeTelefon = numarDeTelefon;
    }

    /**
     * Returnează adresa de email a contactului.
     *
     * @return Adresa de email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setează adresa de email a contactului.
     *
     * @param email Adresa de email (trebuie să fie validă și să conțină @ și domeniul .com sau .ro).
     * @throws IllegalArgumentException Dacă email-ul nu este valid.
     */
    public void setEmail(String email) {
        if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.(com|ro)$")) {
            throw new IllegalArgumentException("Email-ul trebuie să fie valid și să conțină @ și domeniul .com sau .ro.");
        }
        this.email = email;
    }

    /**
     * Verifică egalitatea dintre două obiecte de tip Contact pe baza numărului de telefon.
     *
     * @param o Obiectul de comparat.
     * @return True dacă cele două contacte au același număr de telefon, altfel false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return numarDeTelefon.equals(contact.numarDeTelefon);
    }

    /**
     * Returnează codul hash al obiectului bazat pe numărul de telefon.
     *
     * @return Codul hash al obiectului.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(numarDeTelefon);
    }

    /**
     * Returnează o reprezentare sub formă de String a obiectului Contact.
     *
     * @return Reprezentarea sub formă de String a contactului.
     */
    @Override
    public String toString() {
        return "Contact{" +
                "nume='" + nume + '\'' +
                ", numarDeTelefon='" + numarDeTelefon + '\'' +
                ", email='" + email + '\'' +
                ", favorit='" + favorit + '\'' +
                '}';
    }
}
