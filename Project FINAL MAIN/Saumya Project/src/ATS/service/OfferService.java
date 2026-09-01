package ATS.service;

import ATS.dao.InterviewDAO;
import ATS.dao.JobDAO;
import ATS.dao.OfferDAO;
import ATS.database.DBConnection;
import ATS.model.Application;
import ATS.model.Interview;
import ATS.model.Job;
import ATS.model.Offer;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class OfferService {

    private OfferDAO offerDAO;
    private Scanner scanner;

    // Added to support the candidate Accept / Reject Offer workflow.
    // Reused, not duplicated: ApplicationService, JobDAO, and InterviewDAO
    // are the same classes already used elsewhere in the project.
    private ApplicationService applicationService;
    private JobDAO jobDAO;
    private InterviewDAO interviewDAO;

    public OfferService() {

        offerDAO = new OfferDAO();
        scanner = new Scanner(System.in);
        applicationService = new ApplicationService();
        jobDAO = new JobDAO();
        interviewDAO = new InterviewDAO();

    }

    // Send Offer
    public void sendOffer() {

        System.out.println("\n===== Send Offer =====");

        System.out.print("Application ID : ");
        int applicationId = scanner.nextInt();
        scanner.nextLine();

        // Reuse the existing ApplicationService (backed by ApplicationDAO) to
        // check the application status before an offer is created, so HR
        // can never send a duplicate offer for the same application.
        Application application = applicationService.getApplicationById(applicationId);

        if (application == null) {

            System.out.println("Application not found.");
            return;

        }

//        Offer existingOffer = offerDAO.getOfferByApplication(applicationId);
//        if (existingOffer != null&& "Completed".equalsIgnoreCase(interview.getStatus())) {
//
//            System.out.println("=========================================");
//            System.out.println("Offer Already Exists");
//            System.out.println("=========================================");
//            System.out.println();
//            System.out.println("An offer has already been created for this application.");
//            System.out.println("=========================================");
//            return;
//
//        }
        Interview interview = interviewDAO.getInterviewByApplication(applicationId);

        if (interview == null) {

            System.out.println("=========================================");
            System.out.println("Interview Not Found");
            System.out.println("=========================================");
            System.out.println("This candidate does not have a scheduled interview.");
            System.out.println("=========================================");
            return;

        }


        if (!"Completed".equalsIgnoreCase(interview.getStatus())) {

            System.out.println("=========================================");
            System.out.println("Interview Not Completed");
            System.out.println("=========================================");
            System.out.println("HR must complete the interview before sending an offer.");
            System.out.println("Current Interview Status : " + interview.getStatus());
            System.out.println("=========================================");
            return;

        }


        Offer existingOffer = offerDAO.getOfferByApplication(applicationId);

        if (existingOffer != null) {

            System.out.println("=========================================");
            System.out.println("Offer Already Exists");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("An offer has already been created for this application.");
            System.out.println("=========================================");
            return;

        }

        if (!"Completed".equalsIgnoreCase(application.getStatus())) {
            System.out.println("=========================================");
            System.out.println("Offer Cannot Be Sent");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("This application is not ready for an offer.");
            System.out.println("Current Application Status : " + application.getStatus());
            System.out.println("=========================================");
            return;
        }

        if (!isApplicationEligibleForNewOffer(application.getStatus())) {
            return;
        }

        System.out.print("Offer Date (YYYY-MM-DD) : ");
        String offerDate = scanner.nextLine();

        System.out.print("Offer Status : ");
        String offerStatus = scanner.nextLine();

        if (offerDate.isEmpty() || offerStatus.isEmpty()) {

            System.out.println("Fields cannot be empty.");
            return;

        }

        Offer offer = new Offer();
        offer.setApplicationId(applicationId);
        offer.setOfferDate(LocalDate.parse(offerDate));
        offer.setOfferStatus(offerStatus);
        offer.setAcceptedDate(null);

        boolean result = offerDAO.addOffer(offer);

        if (result) {

            applicationService.updateApplicationStatus(applicationId, "Offer Sent");
            System.out.println("Offer sent successfully.");

        }

        else {

            System.out.println("Failed to send offer.");

        }

    }

    // Sends an offer for a given application, chosen elsewhere (e.g. the HR
    // candidate picker in HRMenu). Centralizes the same duplication check
    // used by sendOffer() so an offer can never be sent twice for the same
    // application, no matter which entry point HR uses.
    // Returns true only when a new offer was actually created.
//    public boolean sendOfferForApplication(int applicationId, LocalDate offerDate, String offerStatus) {
//
//        Application application = applicationService.getApplicationById(applicationId);
//
//        if (application == null) {
//
//            System.out.println("Application not found.");
//            return false;
//
//        }
//
//        Offer existingOffer = offerDAO.getOfferByApplication(applicationId);
//        if (existingOffer != null) {
//
//            System.out.println("=========================================");
//            System.out.println("Offer Already Exists");
//            System.out.println("=========================================");
//            System.out.println();
//            System.out.println("An offer has already been created for this application.");
//            System.out.println("=========================================");
//            return false;
//
//        }
//
//        if (!isApplicationEligibleForNewOffer(application.getStatus())) {
//            return false;
//        }
//
//        Offer offer = new Offer();
//        offer.setApplicationId(applicationId);
//        offer.setOfferDate(offerDate);
//        offer.setOfferStatus(offerStatus);
//        offer.setAcceptedDate(null);
//
//        boolean result = offerDAO.addOffer(offer);
//
//        if (result) {
//            applicationService.updateApplicationStatus(applicationId, "Offer Sent");
//        } else {
//            System.out.println("Unable to send offer. Please try again.");
//        }
//
//        return result;
//
//    }
    public boolean sendOfferForApplication(int applicationId, LocalDate offerDate, String offerStatus) {

        Application application = applicationService.getApplicationById(applicationId);

        if (application == null) {

            System.out.println("Application not found.");
            return false;

        }


        // Check interview completion before sending offer
        Interview interview = interviewDAO.getInterviewByApplication(applicationId);

        if (interview == null) {

            System.out.println("=========================================");
            System.out.println("Interview Not Found");
            System.out.println("=========================================");
            System.out.println("Candidate has no interview record.");
            System.out.println("=========================================");

            return false;

        }


        if (!"Completed".equalsIgnoreCase(interview.getStatus())) {

            System.out.println("=========================================");
            System.out.println("Interview Not Completed");
            System.out.println("=========================================");
            System.out.println("HR must complete the interview before sending offer.");
            System.out.println("Current Interview Status : "
                    + interview.getStatus());
            System.out.println("=========================================");

            return false;

        }


        if (!"Completed".equalsIgnoreCase(application.getStatus())) {
            System.out.println("=========================================");
            System.out.println("Offer Cannot Be Sent");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("This application is not ready for an offer.");
            System.out.println("Current Application Status : " + application.getStatus());
            System.out.println("=========================================");
            return false;
        }

        Offer existingOffer = offerDAO.getOfferByApplication(applicationId);

        if (existingOffer != null) {

            System.out.println("=========================================");
            System.out.println("Offer Already Exists");
            System.out.println("=========================================");
            System.out.println("An offer has already been created.");
            System.out.println("=========================================");

            return false;

        }


        if (!isApplicationEligibleForNewOffer(application.getStatus())) {
            return false;
        }


        Offer offer = new Offer();

        offer.setApplicationId(applicationId);
        offer.setOfferDate(offerDate);
        offer.setOfferStatus(offerStatus);
        offer.setAcceptedDate(null);


        boolean result = offerDAO.addOffer(offer);


        if (result) {

            applicationService.updateApplicationStatus(
                    applicationId,
                    "Offer Sent"
            );

            System.out.println("Offer sent successfully.");

        }
        else {

            System.out.println("Unable to send offer.");

        }


        return result;
    }

    // Checks whether a new offer may be created for an application in the
    // given status. Prints the matching "Already ..." message and returns
    // false when the application has already gone through the offer flow.
    private boolean isApplicationEligibleForNewOffer(String currentStatus) {

        if ("Offer Sent".equalsIgnoreCase(currentStatus)) {

            System.out.println("=========================================");
            System.out.println("Offer Already Sent");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("An offer has already been sent to this candidate.");
            System.out.println("Current Status : Offer Sent");
            System.out.println("No further action is required.");
            System.out.println("=========================================");
            return false;

        }

        if ("OfferAccepted".equalsIgnoreCase(currentStatus)) {

            System.out.println("=========================================");
            System.out.println("Offer Already Accepted");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("This candidate has already accepted the offer.");
            System.out.println("=========================================");
            return false;

        }

        if ("Offer Declined".equalsIgnoreCase(currentStatus)) {

            System.out.println("=========================================");
            System.out.println("Offer Already Declined");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("The candidate has already declined the offer.");
            System.out.println("=========================================");
            return false;

        }

        return true;

    }

    // Accept Offer
    public void acceptOffer() {

        System.out.println("\n===== Accept Offer =====");

        System.out.print("Application ID : ");
        int applicationId = scanner.nextInt();
        scanner.nextLine();

        Offer offer = offerDAO.getOfferByApplication(applicationId);

        if (offer == null) {

            System.out.println("Offer not found.");
            return;

        }

        if ("OfferAccepted".equalsIgnoreCase(offer.getOfferStatus())) {

            System.out.println("=========================================");
            System.out.println("Offer Already Accepted");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("You have already accepted this offer.");
            System.out.println("Current Status : Offer Accepted");
            System.out.println("=========================================");
            return;

        }

        // Reuse the existing DAO -> Service layer and the same transaction
        // mechanism as the candidate Accept Offer flow: this validates
        // vacancies before decreasing them, updates Offer Status, Application
        // Status, and vacancies in one transaction, and rolls back cleanly
        // (leaving Offer Status, Application Status, and vacancies
        // untouched) when no vacancies remain.
        Application application = applicationService.getApplicationById(offer.getApplicationId());

        if (application == null) {

            System.out.println("Application not found for this offer.");
            return;

        }

        Job job = jobDAO.getJobById(application.getJobId());
        String jobTitle = (job == null) ? "Not Available" : job.getJobTitle();

        processAcceptOffer(offer, application, jobTitle);

    }

    // Reject Offer
    public void rejectOffer() {

        System.out.println("\n===== Reject Offer =====");

        System.out.print("Application ID : ");
        int applicationId = scanner.nextInt();
        scanner.nextLine();

        Offer offer = offerDAO.getOfferByApplication(applicationId);

        if (offer == null) {

            System.out.println("Offer not found.");
            return;

        }

        if ("Rejected".equalsIgnoreCase(offer.getOfferStatus())) {

            System.out.println("=========================================");
            System.out.println("Offer Already Declined");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("You have already declined this offer.");
            System.out.println("=========================================");
            return;

        }

        // Reuse the same Application/Job lookups and transaction mechanism
        // used by the candidate Reject Offer flow, for a consistent UI.
        Application application = applicationService.getApplicationById(offer.getApplicationId());

        if (application == null) {

            System.out.println("Application not found for this offer.");
            return;

        }

        Job job = jobDAO.getJobById(application.getJobId());
        String jobTitle = (job == null) ? "Not Available" : job.getJobTitle();

        processRejectOffer(offer, application, jobTitle);

    }

    // ================================================================
    // Candidate Offer Notification workflow (Accept / Reject after login)
    // ================================================================
    // Uses the currently logged-in candidate's own Application/Offer,
    // so the candidate never types an Application ID, Offer ID, or Job ID.
    //
    // Returns true if the candidate resolved the offer (accepted or
    // rejected) so the caller can mark the notification as read.
    // Returns false if the offer was not resolved (e.g. vacancies ran
    // out), so the notification stays unread and is shown again on the
    // next login.
    public boolean respondToOffer(Application application, Job job) {

        if (application == null) {
            return false;
        }

        Offer offer = offerDAO.getOfferByApplication(application.getApplicationId());

        if (offer == null) {
            return false;
        }

        // Extra safety: even if this gets invoked when the offer was
        // already resolved, never let it be accepted or rejected twice.
        if ("OfferAccepted".equalsIgnoreCase(offer.getOfferStatus())) {

            System.out.println("=========================================");
            System.out.println("Offer Already Accepted");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("This candidate has already accepted the offer.");
            System.out.println("=========================================");
            return true;

        }

        if ("Rejected".equalsIgnoreCase(offer.getOfferStatus())) {

            System.out.println("=========================================");
            System.out.println("Offer Already Declined");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("You have already declined this offer.");
            System.out.println("=========================================");
            return true;

        }

        String jobTitle = (job == null) ? "Not Available" : job.getJobTitle();
        String offerDateDisplay = (offer.getOfferDate() == null) ? "Not Available" : offer.getOfferDate().toString();

        Interview interview = interviewDAO.getInterviewByApplication(application.getApplicationId());
        String interviewDateDisplay = (interview == null || interview.getInterviewDate() == null)
                ? "Not Scheduled" : interview.getInterviewDate();
        String interviewTimeDisplay = (interview == null || interview.getInterviewTime() == null)
                ? "Not Scheduled" : formatTime(interview.getInterviewTime());

        while (true) {

            System.out.println("=========================================");
            System.out.println("        OFFER NOTIFICATION");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("Congratulations!");
            System.out.println();
            System.out.println("You have received a Job Offer.");
            System.out.println();
            System.out.println("Job Title        : " + jobTitle);
            System.out.println("Offer Date       : " + offerDateDisplay);
            System.out.println("Interview Date   : " + interviewDateDisplay);
            System.out.println("Interview Time   : " + interviewTimeDisplay);
            System.out.println("Status           : Offer Sent");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("1. Accept Offer");
            System.out.println("2. Reject Offer");
            System.out.println("=========================================");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
                continue;
            }

            boolean wantsAccept = choice.equals("1");

            System.out.println("=========================================");
            System.out.println("Confirm Your Decision");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("You are about to " + (wantsAccept ? "ACCEPT" : "REJECT") + " the job offer.");
            System.out.println();
            System.out.println("This action cannot be undone.");
            System.out.println();
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("=========================================");
            System.out.print("Enter your choice: ");

            String confirm = scanner.nextLine().trim();

            if (confirm.equals("2")) {
                // Return to Offer Notification. Do NOT update the database.
                continue;
            }

            if (!confirm.equals("1")) {
                System.out.println("Invalid choice. Returning to Offer Notification.");
                continue;
            }

            if (wantsAccept) {
                return processAcceptOffer(offer, application, jobTitle);
            }

            return processRejectOffer(offer, application, jobTitle);
        }

    }

    // Formats a stored interview time (e.g. "14:30:00") into "HH:mm" for
    // display. Falls back to the raw value if it cannot be parsed.
    private String formatTime(String interviewTime) {
        if (interviewTime == null) {
            return "Not Available";
        }

        try {
            LocalTime time = LocalTime.parse(interviewTime);
            return time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            if (interviewTime.length() >= 5) {
                return interviewTime.substring(0, 5);
            }
            return interviewTime;
        }
    }

    // Accepts the offer in a single transaction:
    // Offer -> Accepted, Accepted Date -> today, Application -> Selected,
    // and vacancies decreased by one. Validates vacancies before decreasing
    // so they can never go negative.
    private boolean processAcceptOffer(Offer offer, Application application, String jobTitle) {

        if (!"Offer Sent".equalsIgnoreCase(application.getStatus())) {
            System.out.println("=========================================");
            System.out.println("Offer Cannot Be Accepted");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("The offer is no longer pending.");
            System.out.println("Current Application Status : " + application.getStatus());
            System.out.println("=========================================");
            return false;
        }

        Connection connection = DBConnection.getConnection();
        boolean transactionStarted = false;

        try {

            if (connection == null) {
                throw new SQLException("Database connection is not available.");
            }

            connection.setAutoCommit(false);
            transactionStarted = true;

            Job currentJob = jobDAO.getJobById(application.getJobId());
            int vacancies = (currentJob == null) ? 0 : currentJob.getVacancies();

            if (vacancies <= 0) {

                connection.rollback();

                System.out.println("=========================================");
                System.out.println("Offer Acceptance Failed");
                System.out.println("=========================================");
                System.out.println();
                System.out.println("Sorry.");
                System.out.println();
                System.out.println("This position is no longer available.");
                System.out.println("Please contact HR.");
                System.out.println("=========================================");

                // Application status is left unchanged; not resolved.
                return false;
            }

            offer.setOfferStatus("OfferAccepted");
            offer.setAcceptedDate(LocalDate.now());

            if (!offerDAO.updateOffer(offer)) {
                throw new SQLException("Unable to update offer.");
            }

            if (!applicationService.updateApplicationStatus(application.getApplicationId(), "OfferAccepted")) {
                throw new SQLException("Unable to update application status.");
            }

            if (!jobDAO.decreaseVacancy(application.getJobId())) {
                throw new SQLException("Unable to decrease job vacancies.");
            }

            connection.commit();

            // Read the accepted_date back from the database (the same
            // OfferDAO / Offer model already used elsewhere) rather than
            // trusting the in-memory value, so what is displayed is
            // exactly what was stored.
            Offer storedOffer = offerDAO.getOfferByApplication(application.getApplicationId());
            LocalDate acceptedDate = (storedOffer == null) ? offer.getAcceptedDate() : storedOffer.getAcceptedDate();

            System.out.println("=========================================");
            System.out.println("  OFFER ACCEPTED SUCCESSFULLY");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("Congratulations!");
            System.out.println();
            System.out.println("Your offer has been accepted.");
            System.out.println();
            System.out.println("Job Title        : " + jobTitle);
            System.out.println("Accepted On      : " + acceptedDate);
            System.out.println("Current Status   : Offer Accepted");
            System.out.println();
            System.out.println("Thank you for choosing our company.");
            System.out.println("We wish you a successful career.");
            System.out.println("=========================================");

            return true;

        }
        catch (SQLException e) {

            System.out.println("Unable to process offer acceptance: " + e.getMessage());

            if (transactionStarted) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    System.out.println("Unable to roll back transaction: " + rollbackError.getMessage());
                }
            }

            return false;

        }
        finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Unable to restore Auto Commit: " + e.getMessage());
                }
            }

        }

    }

    // Rejects the offer in a single transaction:
    // Offer -> Rejected, Accepted Date -> NULL, Application -> Offer Declined.
    // Vacancies remain unchanged.
    private boolean processRejectOffer(Offer offer, Application application, String jobTitle) {

        if (!"Offer Sent".equalsIgnoreCase(application.getStatus())) {
            System.out.println("=========================================");
            System.out.println("Offer Cannot Be Rejected");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("The offer is no longer pending.");
            System.out.println("Current Application Status : " + application.getStatus());
            System.out.println("=========================================");
            return false;
        }

        Connection connection = DBConnection.getConnection();
        boolean transactionStarted = false;

        try {

            if (connection == null) {
                throw new SQLException("Database connection is not available.");
            }

            connection.setAutoCommit(false);
            transactionStarted = true;

            offer.setOfferStatus("Rejected");
            offer.setAcceptedDate(null);

            if (!offerDAO.updateOffer(offer)) {
                throw new SQLException("Unable to update offer.");
            }

            if (!applicationService.updateApplicationStatus(application.getApplicationId(), "Offer Declined")) {
                throw new SQLException("Unable to update application status.");
            }

            connection.commit();

            System.out.println("=========================================");
            System.out.println("    OFFER DECLINED");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("You have declined the job offer.");
            System.out.println();
            System.out.println("Job Title        : " + jobTitle);
            System.out.println("Current Status   : Offer Declined");
            System.out.println();
            System.out.println("Thank you for your response.");
            System.out.println("We wish you success in your future opportunities.");
            System.out.println("=========================================");

            return true;

        }
        catch (SQLException e) {

            System.out.println("Unable to process offer rejection: " + e.getMessage());

            if (transactionStarted) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    System.out.println("Unable to roll back transaction: " + rollbackError.getMessage());
                }
            }

            return false;

        }
        finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Unable to restore Auto Commit: " + e.getMessage());
                }
            }

        }

    }

}