import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JTextField accnoField;
    JPasswordField pinField;
    JButton loginBtn, registerBtn;

    public Login() {
        setTitle("User Login");
        setSize(600, 600);
        setLayout(new GridLayout(4, 2, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Account Number:"));
        accnoField = new JTextField();
        add(accnoField);

        add(new JLabel("PIN (4 digits):"));
        pinField = new JPasswordField();
        add(pinField);

        loginBtn = new JButton("Login");
        loginBtn.addActionListener(this);
        add(loginBtn);

        registerBtn = new JButton("Register");
        registerBtn.addActionListener(this);
        add(registerBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accno = accnoField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if (e.getSource() == loginBtn) {
            if (!pin.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(this, "PIN must be exactly 4 digits.");
                return;
            }

            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement(
                    "SELECT name FROM accounts WHERE accno = ? AND pin = ?");
                ps.setString(1, accno);
                ps.setString(2, pin);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("name");
                    JOptionPane.showMessageDialog(this, "Welcome, " + name + "!");
                    dispose(); // Close login window

                    // Open menu or main dashboard after successful login
                    new UserDashboard(accno); // pass account number to dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Account Number or PIN.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        if (e.getSource() == registerBtn) {
            dispose(); // Close login window
            new RegisterUser(); // Open registration window
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
