package ATS.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Application {

    private int applicationId;
    private int userId;
    private int jobId;
    private String resumePath;
    private double resumeScore;
    private double testScore;
    private double finalScore;
    private String status;
    private LocalDateTime appliedDate;
    private LocalDate cooldownEnd;

    // Default constructor.
    // This constructor creates an empty Application object.
    public Application() {
    }

    // Parameterized constructor.
    // This constructor sets all fields when creating the object.
    public Application(int applicationId,
                       int userId,
                       int jobId,
                       String resumePath,
                       double resumeScore,
                       double testScore,
                       double finalScore,
                       String status,
                       LocalDateTime appliedDate,
                       LocalDate cooldownEnd) {
        this.applicationId = applicationId;
        this.userId = userId;
        this.jobId = jobId;
        this.resumePath = resumePath;
        this.resumeScore = resumeScore;
        this.testScore = testScore;
        this.finalScore = finalScore;
        this.status = status;
        this.appliedDate = appliedDate;
        this.cooldownEnd = cooldownEnd;
    }


    public int getApplicationId() {
        return applicationId;
    }


    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }


    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getJobId() {
        return jobId;
    }


    public void setJobId(int jobId) {
        this.jobId = jobId;
    }


    public String getResumePath() {
        return resumePath;
    }


    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }


    public double getResumeScore() {
        return resumeScore;
    }


    public void setResumeScore(double resumeScore) {
        this.resumeScore = resumeScore;
    }


    public double getTestScore() {
        return testScore;
    }

    public void setTestScore(double testScore) {

        this.testScore = testScore;
    }

    public double getFinalScore() {

        return finalScore;
    }

    public void setFinalScore(double finalScore) {

        this.finalScore = finalScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }


    public LocalDate getCooldownEnd() {
        return cooldownEnd;
    }

    public void setCooldownEnd(LocalDate cooldownEnd) {
        this.cooldownEnd = cooldownEnd;
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", userId=" + userId +
                ", jobId=" + jobId +
                ", resumePath='" + resumePath + '\'' +
                ", resumeScore=" + resumeScore +
                ", testScore=" + testScore +
                ", finalScore=" + finalScore +
                ", status='" + status + '\'' +
                ", appliedDate=" + appliedDate +
                ", cooldownEnd=" + cooldownEnd +
                '}';
    }
}
