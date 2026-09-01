package ATS.service;

import ATS.database.DBConnection;
import ATS.model.Application;
import ATS.model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicationTransactionService {

    private Connection connection;
    private ApplicationService applicationService;
    private ResumeService resumeService;
    private TestService testService;

    public ApplicationTransactionService() {
        this(DBConnection.getConnection(), new ApplicationService(),
                new ResumeService(), new TestService());
    }

    public ApplicationTransactionService(Connection connection,
                                         ApplicationService applicationService,
                                         ResumeService resumeService,
                                         TestService testService) {
        this.connection = connection;
        this.applicationService = applicationService;
        this.resumeService = resumeService;
        this.testService = testService;
    }

    public boolean processApplication(User user, int jobId, String resumePath) {
        boolean transactionStarted = false;
        boolean transactionCommitted = false;

        System.out.println("=========================================");
        System.out.println("Starting Transaction...");
        System.out.println("=========================================");

        try {
            if (connection == null) {
                throw new SQLException("Database connection is not available.");
            }

            connection.setAutoCommit(false);
            transactionStarted = true;

            if (!resumeService.isResumeFileValid(resumePath)) {
                System.out.println("Error Uploading Resume.");
                throw new SQLException("Resume file is not valid.");
            }
            System.out.println("Resume Uploaded Successfully.");

            if (!resumeService.validateResumeIdentity(user, resumePath)) {
                throw new SQLException("Resume identity validation failed.");
            }

            boolean applicationCreated = applicationService.applyForJob(
                    user.getUserId(), jobId, resumePath);
            if (!applicationCreated) {
                System.out.println("Error Creating Application.");
                throw new SQLException("Application record could not be created.");
            }
            System.out.println("Application Created Successfully.");

            Application application = applicationService.getApplication(user.getUserId(), jobId);
            if (application == null) {
                System.out.println("Error Loading Application.");
                throw new SQLException("Created application could not be loaded.");
            }

            boolean resumeProcessed = resumeService.processResume(
                    resumePath, application.getApplicationId(), jobId);
            if (!resumeProcessed) {
                throw new SQLException("Resume processing failed.");
            }

            boolean testCompleted = testService.startTest(
                    application.getApplicationId(), jobId);
            if (!testCompleted) {
                throw new SQLException("Online test processing failed.");
            }

            connection.commit();
            transactionCommitted = true;

            System.out.println("-----------------------------------------");
            System.out.println();
            System.out.println("Transaction Committed Successfully.");
            System.out.println();
            System.out.println("=========================================");
        } catch (Exception e) {
            System.out.println("-----------------------------------------");
            System.out.println();
            System.out.println("Rolling Back Transaction...");
            System.out.println();

            if (transactionStarted) {
                try {
                    connection.rollback();
                    System.out.println("Transaction Rolled Back Successfully.");
                    System.out.println();
                    System.out.println("No data has been saved.");
                } catch (SQLException rollbackError) {
                    System.out.println("Unable to roll back transaction: "
                            + rollbackError.getMessage());
                }
            } else {
                System.out.println("Transaction could not be started.");
            }

            System.out.println();
            System.out.println("=========================================");
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Unable to restore Auto Commit: " + e.getMessage());
                }
            }
        }

        return transactionCommitted;
    }
}
