package bank.management;

import java.sql.*;

public class ConnDB {
    private Connection con;
    private Statement st;
    private PreparedStatement preSt;

    public ConnDB() {
        final String username = "root";
        final String password = "jm28336030";
        final String connection = "jdbc:mysql://127.0.0.1:3306/bank";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection(connection, username, password);
            st = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    public void close() throws SQLException {
        if (this.con != null && !this.con.isClosed()) {
            this.con.close();
        }
    }

    public PreparedStatement createPrepareStatement(String sql) throws SQLException {
        preSt = con.prepareStatement(sql);
        return preSt;
    }

    // Add a method to close the PreparedStatement
    public void closePreparedStatement() throws SQLException {
        if (this.preSt != null && !this.preSt.isClosed()) {
            this.preSt.close();
        }
    }
}
