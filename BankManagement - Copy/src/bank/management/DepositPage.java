package bank.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepositPage extends JFrame implements ActionListener, KeyListener, MouseListener{
    private JToolBar toolBar;
    private JButton accountsButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton signOffButton;
    private JTextField amountTextField;
    private JLabel nameLabel;
    private JLabel amountLabel;
    private JLabel depositLabel;
    private JPanel mobileDepositPanel;
    private JPanel amountPanel;
    private JPanel depositPanel;
    private JButton cancelButton;
    private JButton depositButton1;
    private final int userId;
    private final String username;
    private final String password;
    private ConnDB c;

    DepositPage(String username, String password, int userId){

        this.username = username;
        this.password = password;
        this.userId = userId;

        this.setContentPane(depositPanel);
        this.setSize(480,480);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this);


        amountPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        mobileDepositPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        amountLabel.setBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.BLACK));
        amountTextField.setBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.BLACK));
        depositButton1.addActionListener(this);
        amountTextField.addKeyListener(this);
        cancelButton.addActionListener(this);
        signOffButton.addActionListener(this);
        accountsButton.addActionListener(this);
        withdrawButton.addActionListener(this);
        signOffButton.addMouseListener(this);
        accountsButton.addMouseListener(this);
        withdrawButton.addMouseListener(this);

        displayBalanceAndName(username, password);

        this.setVisible(true);
    }

    public void depositAmount() {
        try {
            c = new ConnDB();
            String depositString = amountTextField.getText();
            if (amountTextField.getText().isEmpty() || amountTextField.getText().equals("0")) {
                JOptionPane.showMessageDialog(null, "Please enter an amount to deposit");
            } else {
                double deposit = Double.parseDouble(depositString);

                String sql = "INSERT INTO transactions (deposit, user_id) VALUES(?,?)";
                PreparedStatement statement = c.createPrepareStatement(sql);
                statement.setDouble(1, deposit);
                statement.setInt(2, userId);
                statement.execute();
                JOptionPane.showMessageDialog(null, "You deposited $" + deposit + " successfully");
                amountTextField.setText("");

                statement.close();
                c.close();
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
            String firstName= "";
            double amountTotal = 0;

            while (rs.next()){
                firstName = rs.getString("first_name");
                amountTotal = rs.getDouble("transaction_total");
            }
            preparedStatement.close();
            c.close();

            nameLabel.setText(firstName + " Checking...");
            amountLabel.setText("Available Balance $" + String.valueOf(amountTotal));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(depositButton1)){
            System.out.println(userId);
            depositAmount();
            dispose();
            new DepositPage(username, password, userId);

        }else if (e.getSource().equals(cancelButton)){
            amountTextField.setText("");

        }else if (e.getSource().equals(signOffButton)){
            dispose();
            new LoginForm();

        } else if (e.getSource().equals(accountsButton)) {
            dispose();
            new MainMenu(username, password, userId);

        } else if (e.getSource().equals(withdrawButton)) {
            dispose();
            new WithdrawPage(username, password, userId);

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
        if (e.getSource().equals(accountsButton)){
            accountsButton.setOpaque(true);
            accountsButton.setBackground(new Color(230,230,230));
        }
        if (e.getSource().equals(withdrawButton)){
            withdrawButton.setOpaque(true);
            withdrawButton.setBackground(new Color(230,230,230));
        }

        if (e.getSource().equals(signOffButton)){
            signOffButton.setOpaque(true);
            signOffButton.setBackground(new Color(208,204,201));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource().equals(accountsButton)){
            accountsButton.setOpaque(false);
        }
        if (e.getSource().equals(withdrawButton)){
            withdrawButton.setOpaque(false);
        }

        if (e.getSource().equals(signOffButton)){
            signOffButton.setOpaque(false);
        }

    }
}
