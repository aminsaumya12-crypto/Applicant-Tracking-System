package ATS.dao;

import ATS.database.DBConnection;
import ATS.model.Job;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {

    private Connection connection;

    public JobDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean addJob(Job job) {
        boolean added = false;
        // used to insert new jobs by HR
        try {
            // this make easy to take copy from getJobTitle and then paste in tables .  
            PreparedStatement statement = connection.prepareStatement("INSERT INTO jobs (job_title, description, vacancies, status, cooldown_months, test_questions, duration_minutes, passing_percentage, minimum_resume_score, difficulty, created_by, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())");
            statement.setString(1, job.getJobTitle());
            statement.setString(2, job.getDescription());
            statement.setInt(3, job.getVacancies());
            statement.setString(4, job.getStatus());
            statement.setInt(5, job.getCooldownMonths());
            statement.setInt(6, job.getTestQuestions());
            statement.setInt(7, job.getDurationMinutes());
            statement.setInt(8, job.getPassingPercentage());
            statement.setDouble(9, job.getMinimumResumeScore());
            statement.setString(10, job.getDifficulty());
            statement.setInt(11, job.getCreatedBy());

        
            if (statement.executeUpdate()> 0) {
                added = true;
            }

           // statement.close();
        } catch (Exception e) {
            System.out.println("Error adding job: " + e.getMessage());
        }

        return added;
    }

    public boolean updateJob(Job job) {
        /* Number of vacancies ,Job description,Status (Open/Closed),Passing percentage,Minimum resume score
        Difficulty,Test duration*/
        boolean updated = false;
            //modify the details of an existing job in the jobs table
            // it only modify the row in which we want to modify the details of that row only not all rows
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE jobs SET job_title = ?, description = ?, vacancies = ?, status = ?, cooldown_months = ?, test_questions = ?, duration_minutes = ?, passing_percentage = ?, minimum_resume_score = ?, difficulty = ?, created_by = ? WHERE job_id = ?");
            statement.setString(1, job.getJobTitle());
            statement.setString(2, job.getDescription());
            statement.setInt(3, job.getVacancies());
            statement.setString(4, job.getStatus());
            statement.setInt(5, job.getCooldownMonths());
            statement.setInt(6, job.getTestQuestions());
            statement.setInt(7, job.getDurationMinutes());
            statement.setInt(8, job.getPassingPercentage());
            statement.setDouble(9, job.getMinimumResumeScore());
            statement.setString(10, job.getDifficulty());
            statement.setInt(11, job.getCreatedBy());
            statement.setInt(12, job.getJobId());

            if (statement.executeUpdate() > 0) {
                updated = true;
            }
        } catch (Exception e) {
            System.out.println("Error updating job: " + e.getMessage());
        }

        return updated;
    }

    public boolean deleteJob(int jobId) {
        boolean deleted = false;

        // Delete a job from the jobs table based on the provided job ID
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM jobs WHERE job_id = ?");
            statement.setInt(1, jobId);

            if (statement.executeUpdate() > 0) {
                deleted = true;
            }
        } catch (Exception e) {
            System.out.println("Error deleting job: " + e.getMessage());
        }

        return deleted;
    }

  
        

public List<Job> getAllJobs() {

    //It retrieves all jobs from the jobs table and returns them as an ArrayList<Job>
        // we are using arraylist because it should be 0 jobs , 100 jobs , 1000 jobs , so we don't know how many jobs are there so we use arraylist

    List<Job> jobList = new ArrayList<>();
    String query = "SELECT * FROM jobs";


    try (PreparedStatement statement = connection.prepareStatement(query);
         ResultSet rs = statement.executeQuery()) {

        while (rs.next()) {
            Job job = new Job();
            job.setJobId(rs.getInt("job_id"));
            job.setJobTitle(rs.getString("job_title"));
            job.setDescription(rs.getString("description"));
            job.setVacancies(rs.getInt("vacancies"));
            job.setStatus(rs.getString("status"));
            job.setCooldownMonths(rs.getInt("cooldown_months"));
            job.setTestQuestions(rs.getInt("test_questions"));
            job.setDurationMinutes(rs.getInt("duration_minutes"));
            job.setPassingPercentage(rs.getInt("passing_percentage"));
            job.setMinimumResumeScore(rs.getDouble("minimum_resume_score"));
            job.setDifficulty(rs.getString("difficulty"));
            job.setCreatedBy(rs.getInt("created_by"));
            job.setCreatedAt(rs.getString("created_at"));
            
            jobList.add(job);
        }
        
    } catch (Exception e) {
        System.out.println("Error getting jobs: " + e.getMessage());
    }

    return jobList;
}

    // Decrease vacancies by one for a job.
    // Only decreases when vacancies is greater than 0, so it can never go negative.
    public boolean decreaseVacancy(int jobId) {
        boolean updated = false;

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE jobs SET vacancies = vacancies - 1 WHERE job_id = ? AND vacancies > 0");
            statement.setInt(1, jobId);

            if (statement.executeUpdate() > 0) {
                updated = true;
            }
        } catch (Exception e) {
            System.out.println("Error decreasing vacancy: " + e.getMessage());
        }

        return updated;
    }

    public Job getJobById(int jobId) {
        Job job = null;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM jobs WHERE job_id = ?");
            statement.setInt(1, jobId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                job = new Job();
                job.setJobId(rs.getInt("job_id"));
                job.setJobTitle(rs.getString("job_title"));
                job.setDescription(rs.getString("description"));
                job.setVacancies(rs.getInt("vacancies"));
                job.setStatus(rs.getString("status"));
                job.setCooldownMonths(rs.getInt("cooldown_months"));
                job.setTestQuestions(rs.getInt("test_questions"));
                job.setDurationMinutes(rs.getInt("duration_minutes"));
                job.setPassingPercentage(rs.getInt("passing_percentage"));
                job.setMinimumResumeScore(rs.getDouble("minimum_resume_score"));
                job.setDifficulty(rs.getString("difficulty"));
                job.setCreatedBy(rs.getInt("created_by"));
                job.setCreatedAt(rs.getString("created_at"));
            }
        } catch (Exception e) {
            System.out.println("Error getting job: " + e.getMessage());
        }

        return job;
    }

}
