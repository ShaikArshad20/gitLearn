import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDashboard extends JFrame implements ActionListener {
    String accno;
    JButton balanceBtn, depositBtn, withdrawBtn, historyBtn, logoutBtn;

    public UserDashboard(String accno) {
        this.accno = accno;
        setTitle("User Dashboard - Account: " + accno);
        setSize(400, 300);
        setLayout(new GridLayout(5, 1, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        balanceBtn = new JButton("Balance Enquiry"); balanceBtn.addActionListener(this);
        depositBtn = new JButton("Deposit"); depositBtn.addActionListener(this);
        withdrawBtn = new JButton("Withdraw"); withdrawBtn.addActionListener(this);
        historyBtn = new JButton("Transaction History"); historyBtn.addActionListener(this);
        logoutBtn = new JButton("Logout"); logoutBtn.addActionListener(this);

        add(balanceBtn); add(depositBtn); add(withdrawBtn); add(historyBtn); add(logoutBtn);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == balanceBtn) new BalanceEnquiry(accno);
        if (e.getSource() == depositBtn) new Deposit(accno);
        if (e.getSource() == withdrawBtn) new Withdraw(accno);
        if (e.getSource() == historyBtn) new TransactionHistory(accno);
        if (e.getSource() == logoutBtn) {
            dispose();
            new Login();
        }
    }
}
