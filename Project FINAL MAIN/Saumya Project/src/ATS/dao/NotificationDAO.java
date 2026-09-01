package ATS.dao;

import ATS.model.Application;
import ATS.model.Interview;
import ATS.model.HRNote;
import ATS.model.Notification;
import ATS.model.Offer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NotificationDAO {

    public static final String APPLICATION_RECEIVED = "APPLICATION_RECEIVED";
    public static final String TEST_COMPLETED = "TEST_COMPLETED";
    public static final String INTERVIEW_SCHEDULED = "INTERVIEW_SCHEDULED";
    public static final String OFFER_SENT = "OFFER_SENT";
    public static final String APPLICATION_REJECTED = "APPLICATION_REJECTED";

    private final Set<String> sessionReadNotifications = new HashSet<String>();

    private ApplicationDAO applicationDAO;
    private InterviewDAO interviewDAO;
    private OfferDAO offerDAO;
    private HRNoteDAO hrNoteDAO;
    private File readStatusFile;

    public NotificationDAO() {
        this(new ApplicationDAO(), new InterviewDAO(), new OfferDAO(), new HRNoteDAO(),
                new File("notification_read_status.txt"));
    }

    public NotificationDAO(ApplicationDAO applicationDAO,
                           InterviewDAO interviewDAO,
                           OfferDAO offerDAO,
                           HRNoteDAO hrNoteDAO,
                           File readStatusFile) {
        this.applicationDAO = applicationDAO;
        this.interviewDAO = interviewDAO;
        this.offerDAO = offerDAO;
        this.hrNoteDAO = hrNoteDAO;
        this.readStatusFile = readStatusFile;
    }

    // Notifications are derived from the existing application-related tables.
    // No notification table is required.
    public ArrayList<Notification> getNotifications(int userId) {
        ArrayList<Notification> notifications = new ArrayList<Notification>();
        ArrayList<Application> applications = applicationDAO.getAllApplications();
        Set<String> readNotifications = loadReadNotifications();

        if (applications == null) {
            return notifications;
        }

        for (Application application : applications) {
            if (application.getUserId() != userId) {
                continue;
            }

            int applicationId = application.getApplicationId();
            String status = application.getStatus();

            if ("Applied".equalsIgnoreCase(status)) {
                addIfUnread(notifications, readNotifications,
                        new Notification(userId, applicationId, APPLICATION_RECEIVED, false));
            }

            if ("Awaiting HR Review".equalsIgnoreCase(status)) {
                addIfUnread(notifications, readNotifications,
                        new Notification(userId, applicationId, TEST_COMPLETED, false));
            }

            Offer offer = offerDAO.getOfferByApplication(applicationId);
            boolean offerAccepted =
                    offer != null &&
                    "OfferAccepted".equalsIgnoreCase(offer.getOfferStatus());

            Interview interview = interviewDAO.getInterviewByApplication(applicationId);
            if (interview != null
                    && offer == null) {
                addIfUnread(notifications, readNotifications,
                        new Notification(userId, applicationId, INTERVIEW_SCHEDULED, false));
            }

            if (offer != null) {
                addIfUnread(notifications, readNotifications,
                        new Notification(userId, applicationId, OFFER_SENT, false));
            }

            HRNote note = hrNoteDAO.getHRNoteByApplication(applicationId);
            if (!offerAccepted
                    && ("Rejected".equalsIgnoreCase(status)
                    || hasRejectionReason(note))) {
                addIfUnread(notifications, readNotifications,
                        new Notification(userId, applicationId, APPLICATION_REJECTED, false));
            }
        }

        return notifications;
    }

    public boolean markAsRead(Notification notification) {
        if (notification == null) {
            return false;
        }

        String key = createKey(notification);
        Set<String> readNotifications = loadReadNotifications();

        notification.setRead(true);
        sessionReadNotifications.add(key);

        if (readNotifications.contains(key)) {
            return true;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(readStatusFile, true))) {
            writer.write(key);
            writer.newLine();
            return true;
        } catch (IOException e) {
            // The in-memory set still prevents duplicates during this program run.
            return false;
        }
    }

    private void addIfUnread(ArrayList<Notification> notifications,
                             Set<String> readNotifications,
                             Notification notification) {
        if (!readNotifications.contains(createKey(notification))) {
            notifications.add(notification);
        }
    }

    private Set<String> loadReadNotifications() {
        Set<String> readNotifications = new HashSet<String>();
        readNotifications.addAll(sessionReadNotifications);

        if (!readStatusFile.exists()) {
            return readNotifications;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(readStatusFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    readNotifications.add(line.trim());
                }
            }
        } catch (IOException e) {
            // Notifications remain available if the optional read-state file cannot be read.
        }

        return readNotifications;
    }

    private String createKey(Notification notification) {
        return notification.getUserId() + "|"
                + notification.getApplicationId() + "|"
                + notification.getType();
    }

    private boolean hasRejectionReason(HRNote note) {
        return note != null
                && note.getRejectionReason() != null
                && !note.getRejectionReason().trim().isEmpty();
    }
}
