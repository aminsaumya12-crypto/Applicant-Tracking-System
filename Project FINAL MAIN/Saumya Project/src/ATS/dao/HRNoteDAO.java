package ATS.dao;

import ATS.database.DBConnection;
import ATS.model.HRNote;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class HRNoteDAO {

    private Connection con;

    public HRNoteDAO() {

        con = DBConnection.getConnection();

    }

    // Store a new HR note.
    public boolean addHRNote(HRNote note) {

        String sql = "INSERT INTO hr_notes(application_id, handled_by, remarks, rejection_reason, rejection_stage, created_at) VALUES (?, ?, ?, ?, ?, CURRENT_DATE())";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, note.getApplicationId());
            ps.setInt(2, note.getHandledBy());
            ps.setString(3, note.getRemarks());
            ps.setString(4, note.getRejectionReason());
            ps.setString(5, note.getRejectionStage());

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Update HR note details.
    public boolean updateHRNote(HRNote note) {

        String sql = "UPDATE hr_notes SET handled_by=?, remarks=?, rejection_reason=?, rejection_stage=? WHERE note_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, note.getHandledBy());
            ps.setString(2, note.getRemarks());
            ps.setString(3, note.getRejectionReason());
            ps.setString(4, note.getRejectionStage());
            ps.setInt(5, note.getNoteId());

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Get HR note using application ID.
    public HRNote getHRNoteByApplication(int applicationId) {

        String sql = "SELECT * FROM hr_notes WHERE application_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, applicationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HRNote note = new HRNote();

                note.setNoteId(rs.getInt("note_id"));
                note.setApplicationId(rs.getInt("application_id"));
                note.setHandledBy(rs.getInt("handled_by"));
                note.setRemarks(rs.getString("remarks"));
                note.setRejectionReason(rs.getString("rejection_reason"));
                note.setRejectionStage(rs.getString("rejection_stage"));
                String createdAtValue = rs.getString("created_at");
                if (createdAtValue != null && !createdAtValue.isEmpty()) {
                    note.setCreatedAt(LocalDate.parse(createdAtValue));
                }

                return note;

            }

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return null;

    }

    // Get all HR notes.
    public ArrayList<HRNote> getAllHRNotes() {

        ArrayList<HRNote> noteList = new ArrayList<>();

        String sql = "SELECT * FROM hr_notes";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                HRNote note = new HRNote();

                note.setNoteId(rs.getInt("note_id"));
                note.setApplicationId(rs.getInt("application_id"));
                note.setHandledBy(rs.getInt("handled_by"));
                note.setRemarks(rs.getString("remarks"));
                note.setRejectionReason(rs.getString("rejection_reason"));
                note.setRejectionStage(rs.getString("rejection_stage"));
                String createdAtValue = rs.getString("created_at");
                if (createdAtValue != null && !createdAtValue.isEmpty()) {
                    note.setCreatedAt(LocalDate.parse(createdAtValue));
                }

                noteList.add(note);

            }

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return noteList;

    }

    // Delete an HR note using note ID.
    public boolean deleteHRNote(int noteId) {

        String sql = "DELETE FROM hr_notes WHERE note_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, noteId);

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

}