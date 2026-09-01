package ATS.service;

import ATS.dao.*;
import ATS.model.*;

public class SelectionService {

    private ApplicationDAO applicationDAO;
    private JobDAO jobDAO;

    // Constructor
    public SelectionService() {

        applicationDAO = new ApplicationDAO();
        jobDAO = new JobDAO();

    }

    // Calculate final score using Resume (40%) and Test (60%).
    public double calculateFinalScore(int applicationId) {

        Application application;

        application = applicationDAO.getApplicationById(applicationId);

        if (application == null) {

            return 0;

        }

        double resumeScore = application.getResumeScore();
        double testScore = application.getTestScore();

        double finalScore;

        finalScore = (resumeScore * 0.40) + (testScore * 0.60);

        // Save final score in database.
        applicationDAO.updateFinalScore(applicationId, finalScore);

        return finalScore;

    }

    // Check whether resume score is above cutoff.
    public boolean checkResumeCutoff(double resumeScore) {

        if (resumeScore >= 40) {

            return true;

        }

        return false;

    }

    // Check whether test score is above passing percentage.
    public boolean checkTestCutoff(double testScore,
                                   int jobId) {

        Job job;

        job = jobDAO.getJobById(jobId);

        if (job == null) {

            return false;

        }

        if (testScore >= job.getPassingPercentage()) {

            return true;

        }

        return false;

    }

    // Decide whether candidate is selected or rejected.
    public void processSelection(int applicationId) {

        Application application;

        application = applicationDAO.getApplicationById(applicationId);

        if (application == null) {

            System.out.println("Application not found.");

            return;

        }

        // Calculate and save final score.
        double finalScore;

        finalScore = calculateFinalScore(applicationId);

        boolean resumePassed;

        resumePassed = checkResumeCutoff(application.getResumeScore());

        boolean testPassed;

        testPassed = checkTestCutoff(application.getTestScore(),
                application.getJobId());

        if (resumePassed && testPassed) {

            applicationDAO.updateApplicationStatus(
                    applicationId,
                    "Interview Scheduled"
            );

            System.out.println("Candidate Selected");
            System.out.println("Final Score : " + finalScore);
            System.out.println("Status : Interview Scheduled");

        }

        else {

            applicationDAO.updateApplicationStatus(
                    applicationId,
                    "Rejected"
            );

            System.out.println("Candidate Rejected");
            System.out.println("Final Score : " + finalScore);
            System.out.println("Status : Rejected");

        }

    }

}