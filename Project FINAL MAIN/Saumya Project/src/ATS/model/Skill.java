package ATS.model;

public class Skill {

    private int skillId;
    private String skillName;
    private int skillPoints;

    // Default constructor.
    // This constructor creates an empty Skill object.
    public Skill() {
    }

    // Parameterized constructor.
    // This constructor sets the skillId and skillName when creating the object.
    public Skill(int skillId, String skillName) {
        this.skillId = skillId;
        this.skillName = skillName;
    }

   
    public int getSkillId() {
        return skillId;
    }

    
    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }


    public String getSkillName() {
        return skillName;
    }

    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "skillId=" + skillId +
                ", skillName='" + skillName + '\'' +
                '}';
    }
}
// Every job using that skill automatically shows the new name because job_skills stores only the skill_id, not the text.
// this is made to do easy work like if HR change any thing eg . ADD skill then only one column will be added and that's all 
// all can see that changes of skill updated 

// but if HR changes the points for particular skill then we only have to change (job_skill) in  not skills 
// Maps jobs to skills with unique point values.
// Enables role-specific skill weighting without altering the master skills list.
