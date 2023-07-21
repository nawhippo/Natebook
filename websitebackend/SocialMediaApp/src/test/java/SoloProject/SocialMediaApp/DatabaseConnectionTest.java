package SoloProject.SocialMediaApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        // Retrieve the values of the environment variables
        String dbUrl = System.getenv("DB_URL");
        String dbUsername = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");

        try {
            // Load the database driver
            Class.forName("org.postgresql.Driver");

            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(
                    dbUrl,
                    dbUsername,
                    dbPassword
            );

            // If the connection is successful, print a success message
            System.out.println("Connected to the database!");

            // Close the connection
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
        }
    }
}