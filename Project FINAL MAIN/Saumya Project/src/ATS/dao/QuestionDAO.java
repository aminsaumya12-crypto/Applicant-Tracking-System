package ATS.dao;
import ATS.database.DBConnection;
import ATS.model.Question;
import java.sql.*;
import java.util.*;

public class QuestionDAO {

    Connection con;

    public QuestionDAO() {

        con = DBConnection.getConnection();

    }

       // Add a new question.
    public boolean addQuestion(Question question) {

        //Adds a new aptitude question to the database.
        String sql = "INSERT INTO questions(job_id, difficulty, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, question.getJobId());
            ps.setString(2, question.getDifficulty());
            ps.setString(3, question.getQuestionText());
            ps.setString(4, question.getOptionA());
            ps.setString(5, question.getOptionB());
            ps.setString(6, question.getOptionC());
            ps.setString(7, question.getOptionD());
            ps.setString(8, question.getCorrectAnswer());

            int rows = ps.executeUpdate();

            

            return rows > 0;

        }

        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Update an existing question.
    public boolean updateQuestion(Question question) {

        String sql = "UPDATE questions SET job_id=?, difficulty=?, question_text=?, option_a=?, option_b=?, option_c=?, option_d=?, correct_answer=? WHERE question_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, question.getJobId());
            ps.setString(2, question.getDifficulty());
            ps.setString(3, question.getQuestionText());
            ps.setString(4, question.getOptionA());
            ps.setString(5, question.getOptionB());
            ps.setString(6, question.getOptionC());
            ps.setString(7, question.getOptionD());
            ps.setString(8, question.getCorrectAnswer());
            ps.setInt(9, question.getQuestionId());

            int rows = ps.executeUpdate();

           

            return rows > 0;

        }

        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Delete a question using its ID.
    public boolean deleteQuestion(int questionId) {

        String sql = "DELETE FROM questions WHERE question_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, questionId);

            int rows = ps.executeUpdate();

            

            return rows > 0;

        }

        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Get a single question using its ID.
    public Question getQuestionById(int questionId) {

        String sql = "SELECT * FROM questions WHERE question_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, questionId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Question question = new Question();

                question.setQuestionId(rs.getInt("question_id"));
                question.setJobId(rs.getInt("job_id"));
                question.setDifficulty(rs.getString("difficulty"));
                question.setQuestionText(rs.getString("question_text"));
                question.setOptionA(rs.getString("option_a"));
                question.setOptionB(rs.getString("option_b"));
                question.setOptionC(rs.getString("option_c"));
                question.setOptionD(rs.getString("option_d"));
                question.setCorrectAnswer(rs.getString("correct_answer"));


                return question;

            }

            rs.close();
            ps.close();

        }

        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return null;

    }

    // Get Questions By Job
    public ArrayList<Question> getQuestionsByJob(int jobId) {

        ArrayList<Question> questionList = new ArrayList<>();

        String sql = "SELECT * FROM questions WHERE job_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, jobId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                Question question = new Question();

                question.setQuestionId(rs.getInt("question_id"));

                question.setJobId(rs.getInt("job_id"));

                question.setDifficulty(rs.getString("difficulty"));

                question.setQuestionText(rs.getString("question_text"));

                question.setOptionA(rs.getString("option_a"));

                question.setOptionB(rs.getString("option_b"));

                question.setOptionC(rs.getString("option_c"));

                question.setOptionD(rs.getString("option_d"));

                question.setCorrectAnswer(rs.getString("correct_answer"));

                questionList.add(question);

            }
            rs.close();
            ps.close();

        }
        catch(SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return questionList;

    }



}
