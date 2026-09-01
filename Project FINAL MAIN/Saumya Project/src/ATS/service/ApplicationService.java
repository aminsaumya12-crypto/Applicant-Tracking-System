package ATS.service;

import ATS.dao.ApplicationDAO;
import ATS.model.Application;
import java.time.LocalDate;
import java.util.ArrayList;

public class ApplicationService {

    private ApplicationDAO applicationDAO;

    public ApplicationService() {
        this.applicationDAO = new ApplicationDAO();
    }

    // Apply for a job
    public boolean applyForJob(int userId, int jobId, String resumePath) {
        return applicationDAO.applyForJob(userId, jobId, resumePath);
    }

    // Update resume score
    public boolean updateResumeScore(int applicationId, double resumeScore) {
        return applicationDAO.updateResumeScore(applicationId, resumeScore);
    }

    // Update test score
    public boolean updateTestScore(int applicationId, double testScore) {
        return applicationDAO.updateTestScore(applicationId, testScore);
    }

    // Update final score
    public boolean updateFinalScore(int applicationId, double finalScore) {
        return applicationDAO.updateFinalScore(applicationId, finalScore);
    }

    // Update application status
    public boolean updateApplicationStatus(int applicationId, String status) {
        return applicationDAO.updateApplicationStatus(applicationId, status);
    }

    // Get application by ID
    public Application getApplicationById(int applicationId) {
        return applicationDAO.getApplicationById(applicationId);
    }

    // Get all applications
    public ArrayList<Application> getAllApplications() {
        return applicationDAO.getAllApplications();
    }

    // Get applications by user ID
    public ArrayList<Application> getApplicationsByUserId(int userId) {
        return applicationDAO.getApplicationsByUserId(userId);
    }

    // Get application by user and job IDs
    public Application getApplication(int userId, int jobId) {
        return applicationDAO.getApplication(userId, jobId);
    }

    // Update cooldown end date
    public boolean updateCooldownEnd(int applicationId, LocalDate cooldownEnd) {
        return applicationDAO.updateCooldownEnd(applicationId, cooldownEnd);
    }
}
