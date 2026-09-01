package ATS.service;

import ATS.dao.ApplicationDAO;
import ATS.dao.HRNoteDAO;
import ATS.dao.InterviewDAO;
import ATS.dao.JobDAO;
import ATS.dao.NotificationDAO;
import ATS.model.Application;
import ATS.model.HRNote;
import ATS.model.Interview;
import ATS.model.Job;
import ATS.model.Notification;
import ATS.model.User;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class NotificationService {

    private NotificationDAO notificationDAO;
    private ApplicationDAO applicationDAO;
    private InterviewDAO interviewDAO;
    private HRNoteDAO hrNoteDAO;
    private JobDAO jobDAO;
    // Reused, not duplicated: routes the OFFER_SENT notification into the
    // existing candidate Accept / Reject Offer workflow in OfferService.
    private OfferService offerService;

    public NotificationService() {
        notificationDAO = new NotificationDAO();
        applicationDAO = new ApplicationDAO();
        interviewDAO = new InterviewDAO();
        hrNoteDAO = new HRNoteDAO();
        jobDAO = new JobDAO();
        offerService = new OfferService();
    }

    public NotificationService(NotificationDAO notificationDAO,
                               ApplicationDAO applicationDAO,
                               InterviewDAO interviewDAO,
                               HRNoteDAO hrNoteDAO,
                               JobDAO jobDAO) {
        this.notificationDAO = notificationDAO;
        this.applicationDAO = applicationDAO;
        this.interviewDAO = interviewDAO;
        this.hrNoteDAO = hrNoteDAO;
        this.jobDAO = jobDAO;
        this.offerService = new OfferService();
    }

    public ArrayList<Notification> getNotifications(User user) {
        if (user == null) {
            return new ArrayList<Notification>();
        }
        return notificationDAO.getNotifications(user.getUserId());
    }

    public void showNotifications(User user) {
        ArrayList<Notification> notifications = getNotifications(user);

        for (Notification notification : notifications) {
            if (displayNotification(notification)) {
                notificationDAO.markAsRead(notification);
            }
        }
    }

    private boolean displayNotification(Notification notification) {
        Application application = applicationDAO.getApplicationById(notification.getApplicationId());
        if (application == null) {
            return false;
        }

        Job job = jobDAO.getJobById(application.getJobId());
        String jobTitle = job == null ? "Not Available" : job.getJobTitle();
        String type = notification.getType();

        if (NotificationDAO.INTERVIEW_SCHEDULED.equals(type)) {
            Interview interview = interviewDAO.getInterviewByApplication(application.getApplicationId());
            if (interview == null) {
                return false;
            }
            System.out.println("-----------------------------------------");
            System.out.println("Interview Scheduled");
            System.out.println();
            System.out.println("Applied Job : " + jobTitle);
            System.out.println();
            System.out.println("Interview Date : " + interview.getInterviewDate());
            System.out.println();
            System.out.println("Interview Time : " + formatTime(interview.getInterviewTime()));
        } else if (NotificationDAO.OFFER_SENT.equals(type)) {
            // Only show the interactive Offer Notification while the
            // application is still in "Offer Sent" status. Any other
            // status (Awaiting HR Review, Interview Scheduled, Rejected,
            // Selected, Offer Declined, Test Completed) skips it.
            if (!"Offer Sent".equalsIgnoreCase(application.getStatus())) {
                return false;
            }

            // Delegate to OfferService, which prompts Accept/Reject,
            // confirms the decision, and performs the transaction.
            // Returns true only once the candidate has actually
            // responded, so the notification is marked read (and never
            // shown again) only after a real decision is made.
            return offerService.respondToOffer(application, job);
        } else if (NotificationDAO.APPLICATION_REJECTED.equals(type)) {
            HRNote note = hrNoteDAO.getHRNoteByApplication(application.getApplicationId());
            String reason = "Not provided";
            if (note != null && note.getRejectionReason() != null
                    && !note.getRejectionReason().trim().isEmpty()) {
                reason = note.getRejectionReason();
            }

            System.out.println("-----------------------------------------");
            System.out.println("Application Rejected");
            System.out.println();
            System.out.println("Applied Job : " + jobTitle);
            System.out.println();
            System.out.println("Reason : " + reason);
            System.out.println();
            System.out.println("Cooldown Ends On : "
                    + (application.getCooldownEnd() == null ? "Not Available" : application.getCooldownEnd()));
        } else if (NotificationDAO.APPLICATION_RECEIVED.equals(type)) {
            System.out.println("-----------------------------------------");
            System.out.println("Application Received Successfully");
            System.out.println();
            System.out.println("Current Status");
            System.out.println();
            System.out.println("Awaiting HR Review");
        } else if (NotificationDAO.TEST_COMPLETED.equals(type)) {
            System.out.println("-----------------------------------------");
            System.out.println("Online Test Completed Successfully");
            System.out.println();
            System.out.println("Current Status");
            System.out.println();
            System.out.println("Awaiting HR Review");
            System.out.println();
            System.out.println("Final score has been sent to HR.");
        } else {
            return false;
        }

        System.out.println("-----------------------------------------");
        return true;
    }

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
}
