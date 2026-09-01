package ATS.model;

public class JobSkill {

    private int jobSkillId;
    private int jobId;
    private int skillId;
    private int skillPoints;

    // Default constructor.
    // This constructor creates an empty JobSkill object.
    public JobSkill() {
    }

    // Parameterized constructor.
    // This constructor sets all fields when creating the object.
    public JobSkill(int jobSkillId, int jobId, int skillId, int skillPoints) {
        this.jobSkillId = jobSkillId;
        this.jobId = jobId;
        this.skillId = skillId;
        this.skillPoints = skillPoints;
    }

    
    public int getJobSkillId() {
        return jobSkillId;
    }

    
    public void setJobSkillId(int jobSkillId) {
        this.jobSkillId = jobSkillId;
    }

    
    public int getJobId() {
        return jobId;
    }

   
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    
    public int getSkillId() {
        return skillId;
    }

    
    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    
    public int getSkillPoints() {
        return skillPoints;
    }

   
    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    
    @Override
    public String toString() {
        return "JobSkill{" +
                "jobSkillId=" + jobSkillId +
                ", jobId=" + jobId +
                ", skillId=" + skillId +
                ", skillPoints=" + skillPoints +
                '}';
    }
}
// This table is the heart of your resume scoring system .