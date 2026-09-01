package ATS.dao;

import ATS.database.DBConnection;
import ATS.model.Application;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ApplicationDAO {
    Connection con;

    public ApplicationDAO() {

        con = DBConnection.getConnection();

    }

    public ApplicationDAO(Connection con) {
        this.con = con;
    }

    // Apply for Job
    public boolean applyForJob(int userId, int jobId, String resumePath) {

        String sql = "INSERT INTO applications(user_id, job_id, resume_path, status, applied_date) VALUES ( ?, ?, ?, ?, NOW())";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, jobId);
            ps.setString(3, resumePath);
            ps.setString(4, "Applied");

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch(SQLException e) {

            System.out.println(e.getMessage());

        }
        return false;
    }


    // Save Resume Score
    public boolean updateResumeScore(int applicationId, double resumeScore) {

        String sql = "UPDATE applications SET resume_score=? WHERE application_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, resumeScore);
            ps.setInt(2, applicationId);

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch(SQLException e) {

            System.out.println(e.getMessage());

        }

        return false;

    }


    // Save Test Score
    public boolean updateTestScore(int applicationId, double testScore) {

        String sql = "UPDATE applications SET test_score=? WHERE application_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, testScore);
            ps.setInt(2, applicationId);

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch(SQLException e) {

            System.out.println(e.getMessage());

        }

        return false;

    }


    // Save Final Score
    public boolean updateFinalScore(int applicationId, double finalScore) {

        String sql = "UPDATE applications SET final_score=? WHERE application_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, finalScore);
            ps.setInt(2, applicationId);

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch(SQLException e) {

            System.out.println(e.getMessage());

        }

        return false;

    }

    //Update Application status
    public boolean updateApplicationStatus(int applicationId, String status) {

        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, status);
            ps.setInt(2, applicationId);

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;
    }

    //Get application by id
    public Application getApplicationById(int applicationId) {

        String sql = "SELECT * FROM applications WHERE application_id = ?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, applicationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Application application = new Application();

                application.setApplicationId(rs.getInt("application_id"));
                application.setUserId(rs.getInt("user_id"));
                application.setJobId(rs.getInt("job_id"));
                application.setResumePath(rs.getString("resume_path"));
                application.setResumeScore(rs.getDouble("resume_score"));
                application.setTestScore(rs.getDouble("test_score"));
                application.setFinalScore(rs.getDouble("final_score"));
                application.setStatus(rs.getString("status"));
                application.setAppliedDate(rs.getTimestamp("applied_date") != null ? rs.getTimestamp("applied_date").toLocalDateTime() : null);
                application.setCooldownEnd(rs.getDate("cooldown_end") != null ? rs.getDate("cooldown_end").toLocalDate() : null);

                return application;
            }

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return null;
    }

    public ArrayList<Application> getApplicationsByUserId(int userId) {
        ArrayList<Application> applicationList = new ArrayList<Application>();
        String sql = "SELECT * FROM applications WHERE user_id = ? ORDER BY application_id DESC";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Application application = new Application();
                application.setApplicationId(rs.getInt("application_id"));
                application.setUserId(rs.getInt("user_id"));
                application.setJobId(rs.getInt("job_id"));
                application.setResumePath(rs.getString("resume_path"));
                application.setResumeScore(rs.getDouble("resume_score"));
                application.setTestScore(rs.getDouble("test_score"));
                application.setFinalScore(rs.getDouble("final_score"));
                application.setStatus(rs.getString("status"));
                application.setAppliedDate(rs.getTimestamp("applied_date") != null ? rs.getTimestamp("applied_date").toLocalDateTime() : null);
                application.setCooldownEnd(rs.getDate("cooldown_end") != null ? rs.getDate("cooldown_end").toLocalDate() : null);
                applicationList.add(application);
            }

        } catch (SQLException e) {
            System.out.println("Database Error : " + e.getMessage());
        }

        return applicationList;
    }

    public Application getApplication(int user_Id, int jobId) {

        String sql = "SELECT * FROM applications WHERE user_id = ? AND job_id = ? "
                + "ORDER BY application_id DESC LIMIT 1";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, user_Id);
            ps.setInt(2, jobId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Application application = new Application();

                application.setApplicationId(rs.getInt("application_id"));
                application.setUserId(rs.getInt("user_id"));
                application.setJobId(rs.getInt("job_id"));
                application.setResumePath(rs.getString("resume_path"));
                application.setResumeScore(rs.getDouble("resume_score"));
                application.setTestScore(rs.getDouble("test_score"));
                application.setFinalScore(rs.getDouble("final_score"));
                application.setStatus(rs.getString("status"));
                application.setAppliedDate(rs.getTimestamp("applied_date") != null ? rs.getTimestamp("applied_date").toLocalDateTime() : null);
                application.setCooldownEnd(rs.getDate("cooldown_end") != null ? rs.getDate("cooldown_end").toLocalDate() : null);

                return application;
            }

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return null;
    }

    public ArrayList<Application> getAllApplications() {
        ArrayList<Application> applicationList = new ArrayList<Application>();
        String sql = "SELECT * FROM applications";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Application application = new Application();
                application.setApplicationId(rs.getInt("application_id"));
                application.setUserId(rs.getInt("user_id"));
                application.setJobId(rs.getInt("job_id"));
                application.setResumePath(rs.getString("resume_path"));
                application.setResumeScore(rs.getDouble("resume_score"));
                application.setTestScore(rs.getDouble("test_score"));
                application.setFinalScore(rs.getDouble("final_score"));
                application.setStatus(rs.getString("status"));
                application.setAppliedDate(rs.getTimestamp("applied_date") != null ? rs.getTimestamp("applied_date").toLocalDateTime() : null);
                application.setCooldownEnd(rs.getDate("cooldown_end") != null ? rs.getDate("cooldown_end").toLocalDate() : null);
                applicationList.add(application);
            }

        } catch (SQLException e) {
            System.out.println("Database Error : " + e.getMessage());
        }

        return applicationList;
    }

    // Update cooldown end date after rejection.
    public boolean updateCooldownEnd(int applicationId, LocalDate cooldownEnd) {

        String sql = "UPDATE applications " + "SET cooldown_end = ? " + "WHERE application_id = ?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDate(1, Date.valueOf(cooldownEnd));
            ps.setInt(2, applicationId);

            int rows = ps.executeUpdate();

            ps.close();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return false;

    }

}
