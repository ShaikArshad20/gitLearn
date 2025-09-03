import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import javax.swing.*;

public class Withdraw extends JFrame implements ActionListener {
    JTextField amountField;
    JButton withdrawBtn;
    String accno;

    public Withdraw(String accno) {
        this.accno = accno;
        setTitle("Withdraw");
        setSize(300, 150);
        setLayout(new FlowLayout());

        add(new JLabel("Enter amount to withdraw:"));
        amountField = new JTextField(10); add(amountField);
        withdrawBtn = new JButton("Withdraw"); add(withdrawBtn);
        withdrawBtn.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) throw new Exception("Amount must be greater than 0");

            Connection con = DBConnection.getConnection();

            // Check balance
            PreparedStatement check = con.prepareStatement("SELECT balance FROM accounts WHERE accno = ?");
            check.setString(1, accno);
            ResultSet rs = check.executeQuery();

            if (rs.next() && rs.getDouble("balance") >= amount) {
                // Deduct balance
                PreparedStatement ps = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE accno = ?");
                ps.setDouble(1, amount); 
                ps.setString(2, accno);
                ps.executeUpdate();

                // Insert into transaction history
                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO transactions (accno, type, amount, timestamp) VALUES (?, ?, ?, ?)"
                );
                ps2.setString(1, accno);
                ps2.setString(2, "Withdraw");
                ps2.setDouble(3, amount);
                ps2.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                ps2.executeUpdate();

                JOptionPane.showMessageDialog(this, "₹" + amount + " withdrawn successfully");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient balance");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
