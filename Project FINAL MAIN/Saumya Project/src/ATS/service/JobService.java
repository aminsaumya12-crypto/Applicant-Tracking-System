package ATS.service;

import ATS.dao.JobDAO;
import ATS.model.Job;
import java.util.List;

public class JobService {

    private JobDAO jobDAO;

    public JobService() {
        this.jobDAO = new JobDAO();
    }

    // Add a new job
    public boolean addJob(Job job) {
        return jobDAO.addJob(job);
    }

    // Update an existing job
    public boolean updateJob(Job job) {
        return jobDAO.updateJob(job);
    }

    // Delete a job by its ID
    public boolean deleteJob(int jobId) {
        return jobDAO.deleteJob(jobId);
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return jobDAO.getAllJobs();
    }

    // Get a job by its ID
    public Job getJobById(int jobId) {
        return jobDAO.getJobById(jobId);
    }
}
