import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import javax.swing.*;

public class Deposit extends JFrame implements ActionListener {
    private JTextField amountField;
    private String accno;

    public Deposit(String accno) {
        this.accno = accno;
        setTitle("Deposit Money");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Enter Amount:"));
        amountField = new JTextField();
        add(amountField);

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(this);
        add(depositButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) throw new Exception("Amount must be greater than 0");

            Connection con = DBConnection.getConnection();

            // Update balance
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE accno = ?");
            ps.setDouble(1, amount);
            ps.setString(2, accno);
            int result = ps.executeUpdate();

            // Insert transaction
            if (result > 0) {
                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO transactions (accno, type, amount, timestamp) VALUES (?, ?, ?, ?)"
                );
                ps2.setString(1, accno);
                ps2.setString(2, "Deposit");
                ps2.setDouble(3, amount);
                ps2.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                ps2.executeUpdate();

                JOptionPane.showMessageDialog(this, "₹" + amount + " deposited successfully");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed. Account not found.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Deposit("123456789"); // For testing only, pass valid accno
    }
}
