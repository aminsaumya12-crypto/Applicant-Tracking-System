package ATS.service;

import ATS.dao.ApplicationDAO;
import ATS.dao.InterviewDAO;
import ATS.model.Application;
import ATS.model.Interview;
import java.util.Scanner;

public class InterviewService {

    private InterviewDAO interviewDAO;
    private Scanner scanner;

    // Reused, not duplicated: ApplicationDAO is the same class already used
    // elsewhere in the project, added here to validate the application
    // before an interview is scheduled.
    private ApplicationDAO applicationDAO;

    public InterviewService() {
        interviewDAO = new InterviewDAO();
        scanner = new Scanner(System.in);
        applicationDAO = new ApplicationDAO();
    }

    // Schedule Interview
    public void scheduleInterview() {

        System.out.println("\n===== Schedule Interview =====");

        System.out.print("Application ID : ");
        int applicationId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Interview Date (YYYY-MM-DD): ");
        String interviewDate = scanner.nextLine();

        System.out.print("Interview Time (HH:MM:SS): ");
        String interviewTime = scanner.nextLine();

        System.out.print("Location : ");
        String location = scanner.nextLine();

        System.out.print("Status : ");
        String status = scanner.nextLine();

        if(interviewDate.isEmpty() || interviewTime.isEmpty() || location.isEmpty() || status.isEmpty()) {
            System.out.println("Fields cannot be empty.");
            return;
        }

        boolean alreadyValidated =
                applicationDAO.getApplicationById(applicationId) == null
                        || interviewDAO.getInterviewByApplication(applicationId) != null;

        if (!alreadyValidated) {
            Application application = applicationDAO.getApplicationById(applicationId);
            String currentStatus = (application == null) ? null : application.getStatus();
            alreadyValidated = "Rejected".equalsIgnoreCase(currentStatus)
                    || "Selected".equalsIgnoreCase(currentStatus)
                    || "Completed".equalsIgnoreCase(currentStatus)
                    || "Offer Sent".equalsIgnoreCase(currentStatus)
                    || "Offer Declined".equalsIgnoreCase(currentStatus);
        }

        boolean result = scheduleInterview(applicationId, interviewDate, interviewTime);

        if (!result && !alreadyValidated) {
            System.out.println("Failed to schedule interview.");
        }
    }

    public boolean scheduleInterview(int applicationId, String interviewDate, String interviewTime) {

        // Application Validation: reuse the existing
        // ApplicationDAO.getApplicationById(applicationId).
        Application application = applicationDAO.getApplicationById(applicationId);

        if (application == null) {
            System.out.println("=========================================");
            System.out.println("Invalid Application");
            System.out.println("=========================================");
            System.out.println("No application exists with the given Application ID.");
            System.out.println("=========================================");
            return false;
        }

        // Interview Validation: reuse the existing
        // InterviewDAO.getInterviewByApplication(applicationId).
        Interview existingInterview = interviewDAO.getInterviewByApplication(applicationId);

        if (existingInterview != null) {
            System.out.println("=========================================");
            System.out.println("Interview Already Scheduled");
            System.out.println("=========================================");
            System.out.println("An interview has already been scheduled for this candidate.");
            System.out.println("Interview Date : " + existingInterview.getInterviewDate());
            System.out.println("Interview Time : " + existingInterview.getInterviewTime());
            System.out.println("Status : " + existingInterview.getStatus());
            System.out.println("=========================================");
            return false;
        }

        // Application Status Validation: interviews can only be scheduled
        // for applications that are still in an eligible state.
        String currentStatus = application.getStatus();

        if ("Rejected".equalsIgnoreCase(currentStatus)
                || "OfferAccepted".equalsIgnoreCase(currentStatus)
                || "Completed".equalsIgnoreCase(currentStatus)
                || "Offer Sent".equalsIgnoreCase(currentStatus)
                || "Offer Declined".equalsIgnoreCase(currentStatus)) {

            System.out.println("=========================================");
            System.out.println("Interview Cannot Be Scheduled");
            System.out.println("=========================================");
            System.out.println("Current Application Status :");
            System.out.println(currentStatus);
            System.out.println("Only eligible candidates can be scheduled for interviews.");
            System.out.println("=========================================");
            return false;
        }

        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Scheduling Interview...");
        System.out.println();
        System.out.println("Calling Stored Procedure...");

        Interview interview = new Interview();
        interview.setApplicationId(applicationId);
        interview.setInterviewDate(interviewDate);
        interview.setInterviewTime(interviewTime);
        interview.setLocation("");
        interview.setStatus("Scheduled");
        boolean scheduled = interviewDAO.scheduleInterview(interview);

        if (scheduled) {
            boolean applicationUpdated = applicationDAO.updateApplicationStatus(applicationId, "Interview Scheduled");
            if (!applicationUpdated) {
                System.out.println("Unable to update application status.");
                return false;
            }

            System.out.println();
            System.out.println("Interview Scheduled Successfully");
            System.out.println();
            System.out.println("=========================================");
            System.out.println("Interview Scheduled Successfully");
            System.out.println("=========================================");
            System.out.println("Application ID : " + applicationId);
            System.out.println("Interview Date : " + interviewDate);
            System.out.println("Interview Time : " + interviewTime);
            System.out.println("=========================================");
        }

        System.out.println();
        System.out.println("-----------------------------------------");
        return scheduled;
    }

//    public boolean completeInterview(int applicationId) {
//        InterviewDAO interviewDAO = new InterviewDAO();
//        Interview interview = interviewDAO.getInterviewByApplication(applicationId);
//
//        if (interview == null) {
//            System.out.println("Interview not found.");
//            return false;
//        }
//
//        if ("Completed".equalsIgnoreCase(interview.getStatus())) {
//            System.out.println("=========================================");
//            System.out.println("Interview Already Completed");
//            System.out.println("=========================================");
//            return false;
//        }
//
//        interview.setStatus("Completed");
//
//        boolean interviewUpdated = interviewDAO.updateInterviewStatus(
//                interview.getInterviewId(),
//                "Completed"
//        );
//
//        if (!interviewUpdated) {
//            System.out.println("Unable to complete interview.");
//            return false;
//        }
//
//        ApplicationService applicationService = new ApplicationService();
//        boolean applicationUpdated = applicationService.updateApplicationStatus(applicationId, "Completed");
//
//        if (!applicationUpdated) {
//            System.out.println("Unable to complete interview.");
//            return false;
//        }
//
//        System.out.println("=========================================");
//        System.out.println("Interview completed successfully.");
//        System.out.println("=========================================");
//
//        return true;
//    }
public boolean completeInterview(int applicationId) {

    Interview interview = interviewDAO.getInterviewByApplication(applicationId);

    if (interview == null) {
        System.out.println("Interview not found.");
        return false;
    }

    if ("Completed".equalsIgnoreCase(interview.getStatus())) {
        System.out.println("=========================================");
        System.out.println("Interview Already Completed");
        System.out.println("=========================================");
        return false;
    }

    boolean interviewUpdated = interviewDAO.updateInterviewStatus(
            interview.getInterviewId(),
            "Completed"
    );

    if (!interviewUpdated) {
        System.out.println("Unable to complete interview.");
        return false;
    }

    ApplicationService applicationService = new ApplicationService();
    boolean applicationUpdated = applicationService.updateApplicationStatus(applicationId, "Completed");

    if (!applicationUpdated) {
        System.out.println("Unable to complete interview.");
        return false;
    }

    System.out.println("=========================================");
    System.out.println("Interview completed successfully.");
    System.out.println("=========================================");

    return true;
}

    // Update Interview Status
    public void updateInterviewStatus() {

        System.out.println("\n===== Update Interview Status =====");

        System.out.print("Interview ID : ");
        int interviewId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("New Status : ");
        String status = scanner.nextLine();


        boolean result = interviewDAO.updateInterviewStatus(interviewId, status);
        if(result)
            System.out.println("Interview status updated.");
        else
            System.out.println("Failed to update interview.");
    }
}
