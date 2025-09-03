import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterUser extends JFrame implements ActionListener {
    JTextField nameField, genderField, countryField, stateField, cityField, pincodeField, balanceField, pinField;
    JButton registerBtn;

    public RegisterUser() {
        setTitle("New User Registration");
        setSize(400, 400);
        setLayout(new GridLayout(9, 2, 5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Full Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Gender:"));
        genderField = new JTextField();
        add(genderField);

        add(new JLabel("Country:"));
        countryField = new JTextField();
        add(countryField);

        add(new JLabel("State:"));
        stateField = new JTextField();
        add(stateField);

        add(new JLabel("City:"));
        cityField = new JTextField();
        add(cityField);

        add(new JLabel("Pincode:"));
        pincodeField = new JTextField();
        add(pincodeField);

        add(new JLabel("Initial Balance:"));
        balanceField = new JTextField();
        add(balanceField);

        add(new JLabel("Create 4-digit PIN:"));
        pinField = new JTextField();
        add(pinField);

        registerBtn = new JButton("Register");
        registerBtn.addActionListener(this);
        add(registerBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String gender = genderField.getText();
        String country = countryField.getText();
        String state = stateField.getText();
        String city = cityField.getText();
        String pincode = pincodeField.getText();
        String pin = pinField.getText();
        double balance = 0.0;

        try {
            balance = Double.parseDouble(balanceField.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid balance input");
            return;
        }

        if (!pin.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "PIN must be exactly 4 digits.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            // Generate new account number
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(CAST(accno AS UNSIGNED)) FROM accounts");
            int newAccNo = 100000;
            if (rs.next() && rs.getInt(1) != 0) {
                newAccNo = rs.getInt(1) + 1;
            }

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO accounts (accno, name, pin, gender, country, state, city, pincode, balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, String.valueOf(newAccNo));
            ps.setString(2, name);
            ps.setString(3, pin);
            ps.setString(4, gender);
            ps.setString(5, country);
            ps.setString(6, state);
            ps.setString(7, city);
            ps.setString(8, pincode);
            ps.setDouble(9, balance);

            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Account Created!\nYour Account Number is: " + newAccNo);
                dispose(); // close the window after successful registration
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register account.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegisterUser();
    }
}
