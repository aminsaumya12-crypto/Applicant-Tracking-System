package ATS.service;

import ATS.dao.ApplicationDAO;
import ATS.dao.HRNoteDAO;
import ATS.model.Application;
import ATS.model.HRNote;

import java.util.Scanner;

public class RejectionService {

    private ApplicationDAO applicationDAO;
    private HRNoteDAO hrNoteDAO;
    private Scanner scanner;

    public RejectionService() {

        applicationDAO = new ApplicationDAO();
        hrNoteDAO = new HRNoteDAO();
        scanner = new Scanner(System.in);

    }

    // Store Rejection Reason
    public void storeRejectionReason() {

        System.out.println("\n===== Store Rejection Reason =====");

        System.out.print("Application ID : ");
        int applicationId = scanner.nextInt();
        scanner.nextLine();

        Application application =
                applicationDAO.getApplicationById(applicationId);

        if (application == null) {

            System.out.println("Application not found.");
            return;

        }

        System.out.print("HR ID : ");
        int handledBy = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Remarks : ");
        String remarks = scanner.nextLine();

        String rejectionReason = readRejectionReason();
        String rejectionStage = readRejectionStage();

        HRNote note = new HRNote();
        note.setApplicationId(applicationId);
        note.setHandledBy(handledBy);
        note.setRemarks(remarks);
        note.setRejectionReason(rejectionReason);
        note.setRejectionStage(rejectionStage);

        boolean noteSaved = hrNoteDAO.addHRNote(note);

        if (noteSaved) {

            boolean statusUpdated = applicationDAO.updateApplicationStatus(
                    applicationId,
                    "Rejected"
            );

            if (statusUpdated) {
                printTriggerSuccess();
            } else {
                System.out.println("Unable to reject candidate.");
            }

        }

        else {

            System.out.println("Unable to save rejection details.");

        }

    }

    public boolean rejectApplication(int applicationId,
                                     int handledBy,
                                     String remarks,
                                     String rejectionReason,
                                     String rejectionStage) {
        HRNote note = new HRNote();
        note.setApplicationId(applicationId);
        note.setHandledBy(handledBy);
        note.setRemarks(remarks);
        note.setRejectionReason(rejectionReason);
        note.setRejectionStage(rejectionStage);

        boolean noteSaved = hrNoteDAO.addHRNote(note);

        if (noteSaved) {
            boolean statusUpdated = applicationDAO.updateApplicationStatus(applicationId, "Rejected");
            if (statusUpdated) {
                printTriggerSuccess();
            }
            return statusUpdated;
        }

        return false;
    }

    // Matches applications.rejection_reason ENUM in the database exactly
    private static final String[] REJECTION_REASONS = {
            "Low Resume Score", "Low Test Score", "Failed Online Test",
            "Poor Communication", "Lack of Required Skills", "Insufficient Experience",
            "Position Filled", "Duplicate Application", "Missed Interview", "Offer Declined"
    };

    // Matches applications.rejection_stage ENUM in the database exactly
    private static final String[] REJECTION_STAGES = {
            "Resume Screening", "Online Test", "Interview", "Offer Stage", "Other"
    };

    private String readRejectionReason() {
        System.out.println("Select Rejection Reason :");
        for (int i = 0; i < REJECTION_REASONS.length; i++) {
            System.out.println((i + 1) + ". " + REJECTION_REASONS[i]);
        }
        return readMenuChoice(REJECTION_REASONS);
    }

    private String readRejectionStage() {
        System.out.println("Select Rejection Stage :");
        for (int i = 0; i < REJECTION_STAGES.length; i++) {
            System.out.println((i + 1) + ". " + REJECTION_STAGES[i]);
        }
        return readMenuChoice(REJECTION_STAGES);
    }

    private String readMenuChoice(String[] options) {
        while (true) {
            System.out.print("Enter choice number : ");
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= options.length) {
                    return options[choice - 1];
                }
            } catch (NumberFormatException e) {
                // fall through to error message
            }
            System.out.println("Invalid choice. Please enter a number between 1 and " + options.length + ".");
        }
    }

    private void printTriggerSuccess() {
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Candidate Rejected Successfully.");
        System.out.println();
        System.out.println("Database Trigger Executed.");
        System.out.println();
        System.out.println("Cooldown Date Generated Automatically.");
        System.out.println();
        System.out.println("-----------------------------------------");
    }

}
