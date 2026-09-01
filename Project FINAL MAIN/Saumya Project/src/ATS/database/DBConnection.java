package ATS.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Shared connection object
    private  static Connection con = null;

    // Method to connect to the database
    public static void connectDB() {

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/ats";
            String user = "root";         
            String password = "";
            // Create Connection
            con = DriverManager.getConnection(url, user, password);

            System.out.println((con != null)?"Database Connection Established.":"Failed to Connect Database.");

        }

        catch (ClassNotFoundException e) {

            System.out.println("MySQL JDBC Driver Not Found.");
            System.out.println(e.getMessage());

        }
        
        catch (SQLException e) {

            System.out.println("Unable to Connect to Database.");
            System.out.println(e.getMessage());
        }
        
    }
    public static Connection getConnection() {
        if (con == null) {
            connectDB();
        }
        return con;
    }
    // Method to close the database connection
    public static void closeDB() {

        try {

            if (con != null) {
                con.close();
                con = null;
                System.out.println("Database Connection Closed.");
            }

        } catch (SQLException e) {

            System.out.println("Error While Closing Database Connection.");
            System.out.println(e.getMessage());

        }
    }
}

// all other files can also use this
// for eg -> Connection con = DBConnection.con; for different files in same pacakage
// from this we have to not make more connections for different works