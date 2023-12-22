package bank.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterForm extends JFrame implements ActionListener{
    private JPanel registerPanel;
    private JTextField textFieldName;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JTextField textFieldInitialDe;
    private JButton registerButton;
    private JButton cancelButton;
    private JLabel usernameLabel;
    private JTextField lastNameTextField;

    RegisterForm() {
        this.setContentPane(registerPanel);
        this.setSize(480,480);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(this);

        textFieldName.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        lastNameTextField.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        textFieldInitialDe.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        textFieldUsername.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        passwordField.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));

        cancelButton.addActionListener(this);
        registerButton.addActionListener(this);

        this.setVisible(true);
    }

    public void createUser(){
        try {
            ConnDB c = new ConnDB();

            String firstName = textFieldName.getText();
            String lastName = lastNameTextField.getText();
            String username = textFieldUsername.getText();
            String password = String.valueOf(passwordField.getPassword());
            double initialDe = Double.parseDouble(textFieldInitialDe.getText());


            String sql = "INSERT INTO USERS (first_name, last_name, username, password, initial_deposit, transaction_total)VALUES(?,?,?,?,?,?)";

            PreparedStatement statement = c.createPrepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setDouble(5, initialDe);
            statement.setDouble(6, initialDe);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                this.dispose();
                LoginForm loginForm = new LoginForm();

            } else if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || initialDe == 0) {
                    JOptionPane.showMessageDialog(null, "Please fill out all the fields to continue.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(cancelButton)){
            dispose();
            LoginForm loginForm = new LoginForm();

        } else if (e.getSource().equals(registerButton)) {
            createUser();
        }
    }

}
