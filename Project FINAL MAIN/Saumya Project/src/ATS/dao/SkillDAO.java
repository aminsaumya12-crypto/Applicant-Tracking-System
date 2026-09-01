package ATS.dao;

import ATS.database.DBConnection;
import ATS.model.Skill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SkillDAO {

    private Connection connection;

    public SkillDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean addSkill(Skill skill) {
        boolean added = false;

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO skills (skill_name) VALUES (?)");
            statement.setString(1, skill.getSkillName());

            if (statement.executeUpdate() > 0) {
                added = true;
            }
        } catch (Exception e) {
            System.out.println("Error adding skill: " + e.getMessage());
        }

        return added;
    }

    public boolean updateSkill(Skill skill) {
        boolean updated = false;

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE skills SET skill_name = ? WHERE skill_id = ?");
            statement.setString(1, skill.getSkillName());
            statement.setInt(2, skill.getSkillId());

            if (statement.executeUpdate() > 0) {
                updated = true;
            }
        } catch (Exception e) {
            System.out.println("Error updating skill: " + e.getMessage());
        }

        return updated;
    }

    public boolean deleteSkill(int skillId) {
        boolean deleted = false;

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM skills WHERE skill_id = ?");
            statement.setInt(1, skillId);

            if (statement.executeUpdate() > 0) {
                deleted = true;
            }
        } catch (Exception e) {
            System.out.println("Error deleting skill: " + e.getMessage());
        }

        return deleted;
    }

    public ArrayList<Skill> getAllSkills() {
        ArrayList<Skill> skillList = new ArrayList<Skill>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM skills");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Skill skill = new Skill();
                skill.setSkillId(rs.getInt("skill_id"));
                skill.setSkillName(rs.getString("skill_name"));
                skillList.add(skill);
            }
        } catch (Exception e) {
            System.out.println("Error getting skills: " + e.getMessage());
        }

        return skillList;
    }

    public Skill getSkillById(int skillId) {
        Skill skill = null;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM skills WHERE skill_id = ?");
            statement.setInt(1, skillId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                skill = new Skill();
                skill.setSkillId(rs.getInt("skill_id"));
                skill.setSkillName(rs.getString("skill_name"));
            }
        } catch (Exception e) {
            System.out.println("Error getting skill: " + e.getMessage());
        }

        return skill;
    }

}
