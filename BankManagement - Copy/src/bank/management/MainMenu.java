package bank.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;

public class MainMenu extends JFrame implements ActionListener, MouseListener {
    private JPanel mainPanel;
    private JToolBar toolBar;
    private JButton accountsButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton signOutButton;
    private JLabel nameLabel;
    private JLabel checkingLabel;
    private JLabel amountLabel;
    private JPanel middlePanel;
    private String username;
    private String password;
    private int userId;
    private boolean balancePageClicked = false;


    MainMenu(String username, String password, int userId){
        this.username = username;
        this.password = password;
        this.userId = userId;

        this.setContentPane(mainPanel);
        this.setSize(480,480);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this);

        displayBalanceAndName(username, password);

        signOutButton.addActionListener(this);
        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);
        middlePanel.addMouseListener(this);

        signOutButton.addMouseListener(this);
        depositButton.addMouseListener(this);
        withdrawButton.addMouseListener(this);
        middlePanel.addMouseListener(this);

        this.setVisible(true);
    }

    public void displayBalanceAndName (String username, String password) {

       try {
           ConnDB c = new ConnDB();
           String sql = "SELECT first_name, last_name, initial_deposit, user_id, transaction_total FROM users where username=? AND password=?";
           PreparedStatement preparedStatement = c.createPrepareStatement(sql);
           preparedStatement.setString(1,username);
           preparedStatement.setString(2,password);
           ResultSet rs =  preparedStatement.executeQuery();
           String firstName= "";
           String lastName= "";
           double amountTotal = 0;

           while (rs.next()){
               firstName = rs.getString("first_name");
               lastName = rs.getString("last_name");
               amountTotal = rs.getDouble("transaction_total");
               userId = rs.getInt("user_id");
           }
           preparedStatement.close();
           checkingLabel.setText(firstName + " " + lastName + " Checking...");
           nameLabel.setText(firstName);
           amountLabel.setText("$" + String.valueOf(amountTotal));
       }catch (Exception e){
           System.out.println(e);
       }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(signOutButton)){
            dispose();
            LoginForm loginForm = new LoginForm();
        } else if (e.getSource().equals(depositButton)){
            dispose();
            DepositPage depositPage = new DepositPage(username, password, userId);

        }else if (e.getSource().equals(withdrawButton)){
            dispose();
            WithdrawPage withdrawPage = new WithdrawPage(username, password, userId);

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
         if (e.getSource().equals(middlePanel) && !balancePageClicked)  {
             balancePageClicked = true;
             dispose();
             BalancePage balancePage = new BalancePage(username, password, userId);
         }
    }

    @Override
    public void mousePressed(MouseEvent e) {


    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource().equals(depositButton)){
            depositButton.setOpaque(true);
            depositButton.setBackground(new Color(230,230,230));
        }
        if (e.getSource().equals(withdrawButton)){
            withdrawButton.setOpaque(true);
            withdrawButton.setBackground(new Color(230,230,230));
        }

        if (e.getSource().equals(signOutButton)){
            signOutButton.setOpaque(true);
            signOutButton.setBackground(new Color(208,204,201));
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource().equals(depositButton)){
            depositButton.setOpaque(false);
        }
        if (e.getSource().equals(withdrawButton)){
            withdrawButton.setOpaque(false);
        }

        if (e.getSource().equals(signOutButton)){
            signOutButton.setOpaque(false);
        }
        if (e.getSource().equals(middlePanel)) {
            balancePageClicked = false;
        }

    }
}
