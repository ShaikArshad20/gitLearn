import java.sql.*;
import javax.swing.*;

public class TransactionHistory {
    public TransactionHistory(String accno) {
        StringBuilder history = new StringBuilder("Transaction History:\n\n");
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM transactions WHERE accno = ? ORDER BY timestamp DESC");
            ps.setString(1, accno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                history.append(rs.getString("timestamp"))
                        .append(" - ").append(rs.getString("type"))
                        .append(" ₹").append(rs.getDouble("amount"))
                        .append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            history.append("Error loading history.");
        }
        JTextArea area = new JTextArea(history.toString());
        area.setEditable(false);
        JScrollPane pane = new JScrollPane(area);
        JFrame frame = new JFrame("Transaction History");
        frame.add(pane);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
