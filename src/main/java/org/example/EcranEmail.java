package org.example;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.util.Properties;

import static org.example.AdaugaIconFrame.setAppIcon;

/**
 * Clasa EcranEmail reprezintă interfața grafică pentru trimiterea unui email unui contact din agendă.
 */
public class EcranEmail extends JFrame {

    /**
     * Câmp pentru introducerea destinatarului emailului.
     */
    private JTextField textDestinatar;

    /**
     * Câmp pentru introducerea subiectului emailului.
     */
    private JTextField textSubiect;

    /**
     * Zonă de text pentru compunerea mesajului emailului.
     */
    private JTextArea textMesaj;

    /**
     * Buton pentru trimiterea emailului.
     */
    private JButton btnTrimite;

    /**
     * Buton pentru anularea trimiterii emailului.
     */
    private JButton btnAnuleaza;

    /**
     * Adresa de email a destinatarului.
     */
    private String emailDestinatar;

    /**
     * Fereastra părinte care a deschis acest ecran.
     */
    private EcranDetaliiContact parinte;

    /**
     * Constructor pentru crearea interfeței de trimitere email.
     *
     * @param emailDestinatar Adresa de email a destinatarului.
     * @param parinte Referința la fereastra părinte (EcranDetaliiContact).
     */
    public EcranEmail(String emailDestinatar, EcranDetaliiContact parinte) {
        // Setează iconița aplicației.
        setAppIcon(this, "images\\Icon.png");

        this.emailDestinatar = emailDestinatar;
        this.parinte = parinte;

        setTitle("Trimitere Email");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Folosim BorderLayout pentru a organiza elementele UI.
        setLayout(new BorderLayout());

        // Panoul pentru câmpuri text și etichete.
        JPanel panouInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Câmpuri pentru introducerea datelor emailului.
        gbc.gridx = 0;
        gbc.gridy = 0;
        panouInput.add(new JLabel("Destinatar:"), gbc);
        textDestinatar = new JTextField(emailDestinatar, 20);
        gbc.gridx = 1;
        panouInput.add(textDestinatar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panouInput.add(new JLabel("Subiect:"), gbc);
        textSubiect = new JTextField(20);
        gbc.gridx = 1;
        panouInput.add(textSubiect, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panouInput.add(new JLabel("Mesaj:"), gbc);
        textMesaj = new JTextArea(5, 20);
        gbc.gridx = 1;
        panouInput.add(new JScrollPane(textMesaj), gbc);

        // Adăugăm panoul de input în centrul ferestrei.
        add(panouInput, BorderLayout.CENTER);

        // Creăm un panou pentru butoane.
        JPanel panouButoane = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Buton pentru trimiterea emailului.
        btnTrimite = new JButton("Trimite");
        btnTrimite.setBackground(Color.BLUE);
        btnTrimite.setForeground(Color.WHITE);
        btnTrimite.addActionListener(e -> trimiteEmail());
        panouButoane.add(btnTrimite);

        // Buton pentru anularea trimiterii.
        btnAnuleaza = new JButton("Anulează");
        btnAnuleaza.setBackground(Color.RED);
        btnAnuleaza.setForeground(Color.WHITE);
        btnAnuleaza.addActionListener(e -> dispose());  // Închide fereastra.
        panouButoane.add(btnAnuleaza);

        // Adăugăm panoul de butoane în partea de jos a ferestrei.
        add(panouButoane, BorderLayout.SOUTH);

        // Setăm locația ferestrei relativ la fereastra părinte.
        setLocationRelativeTo(parinte);  // Poziționează fereastra relativ la fereastra părinte.
    }

    /**
     * Metoda pentru trimiterea emailului utilizând protocoale SMTP.
     */
    private void trimiteEmail() {
        String destinatar = textDestinatar.getText().trim();
        String subiect = textSubiect.getText().trim();
        String mesaj = textMesaj.getText().trim();

        if (destinatar.isEmpty() || subiect.isEmpty() || mesaj.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Te rog să completezi toate câmpurile!", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Detalii cont expeditor.
        final String emailExpeditor = "alexmariopop@gmail.com";
        final String parolaExpeditor = "elst ytwx tgux pcag";

        // Configurare proprietăți pentru SMTP (Gmail în acest exemplu).
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Crearea unei sesiuni pentru trimiterea emailului.
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailExpeditor, parolaExpeditor);
            }
        });

        try {
            // Crearea unui obiect de tipul Message.
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailExpeditor));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatar));  // Destinatar.
            message.setSubject(subiect);  // Subiectul emailului.
            message.setText(mesaj);  // Corpul emailului.

            // Trimiterea emailului.
            Transport.send(message);

            JOptionPane.showMessageDialog(this, "Emailul a fost trimis cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
            dispose();  // Închide fereastra de email.

        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(this, "A apărut o eroare la trimiterea emailului!", "Eroare", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
