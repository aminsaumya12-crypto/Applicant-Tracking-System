package ATS.dao;

import ATS.database.DBConnection;
import ATS.model.JobSkill;
import java.sql.*;
import java.util.*;

public class JobSkillDAO {

    private Connection connection;

    public JobSkillDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean assignSkillToJob(JobSkill jobSkill) {
        boolean assigned = false;
        //Assigns a skill with points to a specific job.
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO job_skills (job_id, skill_id, skill_points) VALUES (?, ?, ?)");
            statement.setInt(1, jobSkill.getJobId());
            statement.setInt(2, jobSkill.getSkillId());
            statement.setInt(3, jobSkill.getSkillPoints());

            if (statement.executeUpdate() > 0) {
                assigned = true;
            }
        } catch (Exception e) {
            System.out.println("Error assigning skill to job: " + e.getMessage());
        }

        return assigned;
    }

    public boolean updateSkillPoints(JobSkill jobSkill) {
        boolean updated = false;
        //Updates the points of a skill assigned to a job.
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE job_skills SET skill_points = ? WHERE job_skill_id = ?");
            statement.setInt(1, jobSkill.getSkillPoints());
            statement.setInt(2, jobSkill.getJobSkillId());

            if (statement.executeUpdate() > 0) {
                updated = true;
            }
        } catch (Exception e) {
            System.out.println("Error updating skill points: " + e.getMessage());
        }

        return updated;
    }

    public boolean removeSkillFromJob(int jobSkillId) {
        boolean removed = false;
        //Removes a skill from a specific job.
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM job_skills WHERE job_skill_id = ?");
            statement.setInt(1, jobSkillId);

            if (statement.executeUpdate() > 0) {
                removed = true;
            }
        } catch (Exception e) {
            System.out.println("Error removing skill from job: " + e.getMessage());
        }

        return removed;
    }

    public ArrayList<JobSkill> getSkillsByJob(int jobId) {
        ArrayList<JobSkill> skillList = new ArrayList<>();

    //Retrieves all skills assigned to a specific job.
    try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM job_skills WHERE job_id = ?")) {
        statement.setInt(1, jobId);

        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                JobSkill jobSkill = new JobSkill();
                jobSkill.setJobSkillId(rs.getInt("job_skill_id"));
                jobSkill.setJobId(rs.getInt("job_id"));
                jobSkill.setSkillId(rs.getInt("skill_id"));
                jobSkill.setSkillPoints(rs.getInt("skill_points"));
                
                skillList.add(jobSkill);
            }
        }
    } catch (Exception e) {
        System.out.println("Error getting skills by job: " + e.getMessage());
    }

    return skillList;
}

    public JobSkill getJobSkillById(int jobSkillId) {
        JobSkill jobSkill = null;
        //Retrieves a specific job-skill mapping using its ID.
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM job_skills WHERE job_skill_id = ?");
            statement.setInt(1, jobSkillId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                jobSkill = new JobSkill();
                jobSkill.setJobSkillId(rs.getInt("job_skill_id"));
                jobSkill.setJobId(rs.getInt("job_id"));
                jobSkill.setSkillId(rs.getInt("skill_id"));
                jobSkill.setSkillPoints(rs.getInt("skill_points"));
            }
        } catch (Exception e) {
            System.out.println("Error getting job skill: " + e.getMessage());
        }

        return jobSkill;
    }


}
