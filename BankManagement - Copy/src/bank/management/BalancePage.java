package bank.management;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BalancePage extends JFrame implements ActionListener, MouseListener {
    private JPanel BalancePanel;
    private JToolBar toolBar;
    private JButton accountsButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton signOffButton;
    private JPanel middlePanel;
    private JLabel amountLabel;
    private JLabel checkingLabel;
    private JLabel overviewLabel;
    private JScrollPane scrollPane;
    private JPanel infoPanel;
    private JPanel transactionsPanel;
    private int userId;
    private String username;
    private String password;
    private ConnDB c = new ConnDB();

    BalancePage(String username, String password, int userId){
        this.username = username;
        this.password = password;
        this.userId = userId;

        this.setContentPane(BalancePanel);
        this.setSize(480,480);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this);

        scrollPane.setViewportView(infoPanel);
        scrollPane.setBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.GREEN));
        overviewLabel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.GREEN));
        signOffButton.addActionListener(this);
        accountsButton.addActionListener(this);
        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);

        signOffButton.addMouseListener(this);
        depositButton.addMouseListener(this);
        withdrawButton.addMouseListener(this);


        transactionsPanel.setLayout(new GridLayout(0, 1));
        transactionsPanel.setBackground(Color.red);
        transactionsPanel.setOpaque(true);

        displayBalanceAndName(username, password);
        displayTransactions(transactionsPanel, userId);
        this.setVisible(true);
    }

    public void displayBalanceAndName(String username, String password){
        try {

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
            c.closePreparedStatement();


            checkingLabel.setText(firstName + " Checking...");
            amountLabel.setText("$" + String.valueOf(amountTotal));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayTransactions(JPanel transactionsPanel, int userId) {

        int highestTransId = maximumNumTransactions();
        int x = 0;
        int y = 0;
        double deposit = 0;
        double withdraw = 0;
        String transactionType = "";
        String amount = "";
        String sql = "";
        int sqlUserId = 0;

        if (c == null) {
            // Handle the case where c is null (print an error, throw an exception, etc.)
            return;
        }
        try{
            while(highestTransId > 999) {

                sql = "SELECT deposit, withdraw, user_id FROM transactions WHERE transaction_id=?";
                PreparedStatement preparedStatement = c.createPrepareStatement(sql);
                preparedStatement.setInt(1, highestTransId);

                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    deposit = rs.getDouble("deposit");
                    withdraw = rs.getDouble("withdraw");
                    sqlUserId = rs.getInt("user_id");
                }

                if (deposit != 0){
                    transactionType = "Deposit";
                    amount = String.valueOf("+$" + deposit);
                }else if(withdraw != 0){
                    transactionType = "Withdraw";
                    amount = String.valueOf("$" + withdraw);
                }
                preparedStatement.close();
                c.closePreparedStatement();

                if (userId == sqlUserId){
                    JPanel panel = getjPanel(x, y, transactionType, amount);
                    transactionsPanel.setLayout(new GridLayout(0, 1));  // Set GridLayout
                    transactionsPanel.setOpaque(false);
                    if (transactionsPanel != null) {
                        transactionsPanel.add(panel);
                        transactionsPanel.repaint();
                    }
                    y += 60;
                    highestTransId--;

                }else {

                    highestTransId--;
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private JPanel getjPanel(int x, int y, String transactionType, String amount) {
        JPanel panel = new JPanel();

        panel.setOpaque(false);
        panel.setLayout(new GridLayout(1, 2));
        panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        JLabel labelTransaction = new JLabel(transactionType);
        labelTransaction.setFont(new Font("Arial", Font.BOLD, 14));
        labelTransaction.setSize(new Dimension(100,30));
        labelTransaction.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.add(labelTransaction);

        JLabel amountLabel = new JLabel(amount);
        if (transactionType.equals("Deposit")) {
            amountLabel.setForeground(Color.green);
        }
        amountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        amountLabel.setHorizontalAlignment(JLabel.RIGHT);
        amountLabel.setSize(new Dimension(70,30));
        amountLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.add(amountLabel);

        return panel;
    }

    public int maximumNumTransactions(){
        int highestTransId = 0;
        try {
            String sql = "SELECT MAX(transaction_id) AS max_value FROM transactions;";
            PreparedStatement preparedStatement = c.createPrepareStatement(sql);
            ResultSet rs =  preparedStatement.executeQuery();
            while (rs.next()){
                highestTransId = rs.getInt("max_value");
            }
            preparedStatement.close();
            c.closePreparedStatement();

        }catch (Exception e){
            e.printStackTrace();
        }
        return highestTransId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource().equals(signOffButton)){
            dispose();
            new LoginForm();

        } else if (e.getSource().equals(accountsButton)) {
             dispose();
             new MainMenu(username, password, userId);

        } else if (e.getSource().equals(depositButton)) {
             dispose();
             new DepositPage(username, password, userId);

        } else if (e.getSource().equals(withdrawButton)) {
             dispose();
             new WithdrawPage(username, password, userId);

        }
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
        if (e.getSource().equals(depositButton)){
            depositButton.setOpaque(false);
        }
        if (e.getSource().equals(withdrawButton)){
            withdrawButton.setOpaque(false);
        }

        if (e.getSource().equals(signOffButton)){
            signOffButton.setOpaque(false);
        }

    }
}
