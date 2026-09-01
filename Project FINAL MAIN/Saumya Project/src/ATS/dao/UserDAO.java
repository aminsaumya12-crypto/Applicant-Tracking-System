package ATS.dao;

import ATS.database.DBConnection;
import ATS.model.User;
import java.sql.*;


public class UserDAO {

    private Connection connection;

    public UserDAO() {
        this.connection = DBConnection.getConnection();
    }


public boolean registerCandidate(User user) {

    /*Accept a User object.
    Insert the user's details into the users table.
    Return true if the record is inserted successfully.
    Return false if insertion fails. */
    try {

        PreparedStatement ps = connection.prepareStatement("INSERT INTO users(full_name, email, password, role,created_at) VALUES (?, ?, ?,?,NOW())");

        ps.setString(1, user.getFullName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ps.setString(4, "Candidate");
        // ps.close
        if (ps.executeUpdate() > 0) {
            return true;
        }

    } catch (Exception e) {
        System.out.println("Registration Failed : " + e.getMessage());
    }
    return false;
    }
   // registerCandidate class over

    public User loginUser(String email, String password) {
        /*Accept email and password.
    Check whether a matching user exists in the users table.
    If found, create and return a User object.
    If not found, return null.*/
        try {
             // we are taking User instead of boolean because we need userid , role,fullName later
            // to check who logged in? , Is the user an HR or a Candidate? , What is the user's ID?
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setAccountStatus(rs.getString("account_status"));

                return user;
            }

        } catch (Exception e) {
            System.out.println("Login Failed : " + e.getMessage());
        }

        return null;
    }

    public User getUserByEmail(String email) {

        /*Checking if an email is already registered.
        Retrieving a user's details.
        Preventing duplicate registrations.*/

        try {

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setAccountStatus(rs.getString("account_status"));

                return user;
            }

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());

        }

        return null; //No User object exists. because We are using User as return type it is give only two things
        // if exists then it will give User and if not exists then it will be null
    }

    public User getUserById(int userId) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE user_id = ?");
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setAccountStatus(rs.getString("account_status"));
                return user;
            }
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }

        return null;
    }

}

