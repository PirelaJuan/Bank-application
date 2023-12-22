package bank.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WithdrawPage extends JFrame implements ActionListener, KeyListener, MouseListener {
    private JButton signOffButton;
    private JTextField amountTextField;
    private JPanel enterAmountPanel;
    private JLabel enterLabel;
    private JButton cancelButton;
    private JButton withdrawButton1;
    private JToolBar toolBar;
    private JButton accountsButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JPanel withdrawPanel;
    private JLabel nameLabel;
    private JLabel amountLabel;
    private JPanel infoPanel;
    private final String username;
    private final String password;
    private final int userId;
    private ConnDB c;

    WithdrawPage(String username, String password, int userId){
        this.username = username;
        this.password = password;
        this.userId = userId;

        this.setContentPane(withdrawPanel);
        this.setSize(480,480);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this);
        
        amountTextField.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        enterAmountPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        infoPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0, Color.BLACK));

        displayBalanceAndName(username, password);
        withdrawButton1.addActionListener(this);
        amountTextField.addKeyListener(this);
        cancelButton.addActionListener(this);
        signOffButton.addActionListener(this);
        accountsButton.addActionListener(this);
        depositButton.addActionListener(this);
        signOffButton.addMouseListener(this);
        depositButton.addMouseListener(this);
        accountsButton.addMouseListener(this);

        this.setVisible(true);

    }
    public void withdrawAmount(){
        try {
            c = new ConnDB();
            String withdrawString = amountTextField.getText();

            if (amountTextField.getText().isEmpty() || amountTextField.getText().equals("0")) {
                JOptionPane.showMessageDialog(null, "Please enter an amount to withdraw");
            } else {
                double withdraw = Double.parseDouble(withdrawString);


                String sql = "INSERT INTO transactions (withdraw, user_id) VALUES(?,?)";
                PreparedStatement statement = c.createPrepareStatement(sql);
                statement.setDouble(1, withdraw);
                statement.setInt(2, userId);
                statement.execute();
                JOptionPane.showMessageDialog(null, "You withdraw $" + withdraw + " successfully");
                amountTextField.setText("");

                statement.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void displayBalanceAndName(String username, String password){
        try {
            c = new ConnDB();
            String sql = "SELECT first_name, transaction_total FROM users where username=? AND password=?";
            PreparedStatement preparedStatement = c.createPrepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet rs =  preparedStatement.executeQuery();
            String firtsName= "";
            double amountTotal = 0;

            while (rs.next()){
                firtsName = rs.getString("first_name");
                amountTotal = rs.getDouble("transaction_total");
            }
            preparedStatement.close();

            nameLabel.setText(firtsName + " Checking...");
            amountLabel.setText("Available Balance $" + String.valueOf(amountTotal));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(withdrawButton1)){
            withdrawAmount();
            dispose();
            new WithdrawPage(username, password, userId);

        }else if (e.getSource().equals(cancelButton)){
            amountTextField.setText("");

        }else if (e.getSource().equals(signOffButton)){
            dispose();
            new LoginForm();

        } else if (e.getSource().equals(accountsButton)) {
            dispose();
            new MainMenu(username, password, userId);

        } else if (e.getSource().equals(depositButton)) {
            dispose();
            new DepositPage(username, password, userId);

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isLetter(c)){
            amountTextField.setEditable(false);
        }else {
            amountTextField.setEditable(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
        if (e.getSource().equals(accountsButton)){
            accountsButton.setOpaque(true);
            accountsButton.setBackground(new Color(230,230,230));
        }

        if (e.getSource().equals(signOffButton)){
            signOffButton.setOpaque(true);
            signOffButton.setBackground(new Color(208,204,201));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource().equals(depositButton)){
            depositButton.setOpaque(false);
        }
        if (e.getSource().equals(accountsButton)){
            accountsButton.setOpaque(false);
        }

        if (e.getSource().equals(signOffButton)){
            signOffButton.setOpaque(false);
        }
    }
}
