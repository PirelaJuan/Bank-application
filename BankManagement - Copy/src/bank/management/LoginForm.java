package bank.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;

public class LoginForm extends JFrame implements MouseListener, ActionListener{
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton signInButton;
    private JButton cancelButton;
    private JPanel loginPanel;
    private JLabel messageLabel;
    private JLabel signUpLabel;

    public LoginForm(){

        this.setTitle("Login");
        this.setContentPane(loginPanel);
        this.setSize(480,480);
        this.setLocationRelativeTo(this);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        tfUsername.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        pfPassword.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));

        signInButton.setFocusable(false);
        signUpLabel.addMouseListener(this);

        setVisible(true);
        signInButton.addActionListener(this);
    }

    public void aunthenticateUser(String username, String password) throws SQLException {
        ConnDB c = new ConnDB();
        String sql = "SELECT * FROM users where username=? AND password=?";
        PreparedStatement preparedStatement = c.createPrepareStatement(sql);
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        ResultSet rs =  preparedStatement.executeQuery();
        if (rs.next()){
            this.dispose();
            int userId = rs.getInt("user_id");
            MainMenu mainMenu = new MainMenu(username, password, userId);

        }else if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(null, "Username or password can not be empty.");
        }else {
            JOptionPane.showMessageDialog(null, "Username and password don’t match.");
        }
        preparedStatement.close();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.dispose();
        RegisterForm registerForm = new RegisterForm();

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        signUpLabel.setForeground(Color.CYAN);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        signUpLabel.setForeground(new Color(0,121,158));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(signInButton)){
            String username = tfUsername.getText();
            String password = String.valueOf(pfPassword.getPassword());

            try {
                aunthenticateUser(username,password);

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}




