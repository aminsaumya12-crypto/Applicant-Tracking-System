package ATS.dao;

import ATS.database.DBConnection;
import ATS.model.Interview;

import java.sql.*;

import java.util.ArrayList;

public class InterviewDAO {

    private Connection con;

    public InterviewDAO() {

        con = DBConnection.getConnection();

    }

    // Schedule an interview for an application.
    public boolean scheduleInterview(Interview interview) {

        String sql = "INSERT INTO interviews(application_id, interview_date, interview_time, location, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, interview.getApplicationId());
            ps.setString(2, interview.getInterviewDate());
            ps.setString(3, interview.getInterviewTime());
            ps.setString(4, interview.getLocation());
            ps.setString(5, interview.getStatus());

            int rows = ps.executeUpdate();
            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Update interview details.
    public boolean updateInterview(Interview interview) {

        String sql = "UPDATE interviews SET interview_date=?, interview_time=?, location=?, status=? WHERE interview_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, interview.getInterviewDate());
            ps.setString(2, interview.getInterviewTime());
            ps.setString(3, interview.getLocation());
            ps.setString(4, interview.getStatus());
            ps.setInt(5, interview.getInterviewId());

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Update interview status.
    public boolean updateInterviewStatus(int interviewId, String status) {

        String sql = "UPDATE interviews SET status=? WHERE interview_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "Completed");
            ps.setInt(2, interviewId);

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Get interview details using application ID.
    public Interview getInterviewByApplication(int applicationId) {

        String sql = "SELECT * FROM interviews WHERE application_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, applicationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Interview interview = new Interview();

                interview.setInterviewId(rs.getInt("interview_id"));
                interview.setApplicationId(rs.getInt("application_id"));
                interview.setInterviewDate(rs.getString("interview_date"));
                interview.setInterviewTime(rs.getString("interview_time"));
                interview.setLocation(rs.getString("location"));
                interview.setStatus(rs.getString("status"));

                return interview;

            }

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return null;

    }

    // Get all interviews.
    public ArrayList<Interview> getAllInterviews() {

        ArrayList<Interview> interviewList = new ArrayList<>();

        String sql = "SELECT * FROM interviews";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Interview interview = new Interview();

                interview.setInterviewId(rs.getInt("interview_id"));
                interview.setApplicationId(rs.getInt("application_id"));
                interview.setInterviewDate(rs.getString("interview_date"));
                interview.setInterviewTime(rs.getString("interview_time"));
                interview.setLocation(rs.getString("location"));
                interview.setStatus(rs.getString("status"));

                interviewList.add(interview);

            }

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return interviewList;

    }

    // Delete interview using interview ID.
    public boolean deleteInterview(int interviewId) {

        String sql = "DELETE FROM interviews WHERE interview_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, interviewId);

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

}
