package ATS.main;

import ATS.database.DBConnection;

public class Main {
    public static void main(String[] args) {
        try {
            // Connect to the database
            DBConnection.connectDB();

            // Show the main menu
            MainMenu.showMenu();
        } catch (Exception e) {
            // Handle any runtime error
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Close the database connection
            DBConnection.closeDB();
        }
    }
}

