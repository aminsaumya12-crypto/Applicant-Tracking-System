package ATS.model;

public class Job {

    private int jobId;
    private String jobTitle;
    private String description;
    private int vacancies;
    private String status;
    private int cooldownMonths;
    private int testQuestions;
    private int durationMinutes;
    private int passingPercentage;
    private double minimumResumeScore;
    private String difficulty;
    private int createdBy;
    private String createdAt;

    
      //Default constructor.
      // is used to set all default value = null
    // means in starting we don't know that what is the details .
     
    public Job() {
    }

    
     // Parameterized constructor.
      // Parameterized constructor.
     // to fill the details in this columns .
    // made this for using this all things at same time .
    // like if in another class i call job b = new job();
    // then all values come together like group .
      
     
    public Job(int jobId, String jobTitle, String description, int vacancies,
               String status, int cooldownMonths, int testQuestions,
               int durationMinutes, int passingPercentage,
               double minimumResumeScore, String difficulty, int createdBy,
               String createdAt) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.description = description;
        this.vacancies = vacancies;
        this.status = status;
        this.cooldownMonths = cooldownMonths;
        this.testQuestions = testQuestions;
        this.durationMinutes = durationMinutes;
        this.passingPercentage = passingPercentage;
        this.minimumResumeScore = minimumResumeScore;
        this.difficulty = difficulty;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVacancies() {
        return vacancies;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCooldownMonths() {
        return cooldownMonths;
    }

    public void setCooldownMonths(int cooldownMonths) {
        this.cooldownMonths = cooldownMonths;
    }

    public int getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(int testQuestions) {
        this.testQuestions = testQuestions;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getPassingPercentage() {
        return passingPercentage;
    }

    public void setPassingPercentage(int passingPercentage) {
        this.passingPercentage = passingPercentage;
    }

    public double getMinimumResumeScore() {
        return minimumResumeScore;
    }

    public void setMinimumResumeScore(double minimumResumeScore) {
        this.minimumResumeScore = minimumResumeScore;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns a string representation of the job object.
     */
    @Override
    public String toString() {
        return "Job{" +
                "jobId=" + jobId +
                ", jobTitle='" + jobTitle + '\'' +
                ", description='" + description + '\'' +
                ", vacancies=" + vacancies +
                ", status='" + status + '\'' +
                ", cooldownMonths=" + cooldownMonths +
                ", testQuestions=" + testQuestions +
                ", durationMinutes=" + durationMinutes +
                ", passingPercentage=" + passingPercentage +
                ", minimumResumeScore=" + minimumResumeScore +
                ", difficulty='" + difficulty + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
