package ATS.service;

import ATS.dao.SkillDAO;
import ATS.model.Skill;
import java.util.ArrayList;

public class SkillService {

    private SkillDAO skillDAO;

    public SkillService() {
        this.skillDAO = new SkillDAO();
    }

    // Add a new skill
    public boolean addSkill(Skill skill) {
        return skillDAO.addSkill(skill);
    }

    // Update an existing skill
    public boolean updateSkill(Skill skill) {
        return skillDAO.updateSkill(skill);
    }

    // Delete a skill by its ID
    public boolean deleteSkill(int skillId) {
        return skillDAO.deleteSkill(skillId);
    }

    // Get all skills
    public ArrayList<Skill> getAllSkills() {
        return skillDAO.getAllSkills();
    }

    // Get a skill by its ID
    public Skill getSkillById(int skillId) {
        return skillDAO.getSkillById(skillId);
    }
}
