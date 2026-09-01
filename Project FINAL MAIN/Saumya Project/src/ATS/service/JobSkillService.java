package ATS.service;

import ATS.dao.JobSkillDAO;
import ATS.model.JobSkill;
import java.util.ArrayList;

public class JobSkillService {

    private JobSkillDAO jobSkillDAO;

    public JobSkillService() {
        this.jobSkillDAO = new JobSkillDAO();
    }

    public boolean assignSkillToJob(JobSkill jobSkill) {
        return jobSkillDAO.assignSkillToJob(jobSkill);
    }

    public boolean updateSkillPoints(JobSkill jobSkill) {
        return jobSkillDAO.updateSkillPoints(jobSkill);
    }

    public boolean removeSkillFromJob(int jobSkillId) {
        return jobSkillDAO.removeSkillFromJob(jobSkillId);
    }

    public ArrayList<JobSkill> getSkillsByJob(int jobId) {
        return jobSkillDAO.getSkillsByJob(jobId);
    }

    public JobSkill getJobSkillById(int jobSkillId) {
        return jobSkillDAO.getJobSkillById(jobSkillId);
    }
}
