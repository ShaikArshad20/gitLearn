import java.sql.*;
import javax.swing.*;

public class BalanceEnquiry {
    public BalanceEnquiry(String accno) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM accounts WHERE accno = ?");
            ps.setString(1, accno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                JOptionPane.showMessageDialog(null, "Your current balance is: ₹" + balance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}