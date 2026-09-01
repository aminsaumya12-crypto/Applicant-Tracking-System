package ATS.model;

public class Notification {

    private int userId;
    private int applicationId;
    private String type;
    private boolean read;

    public Notification() {
    }

    public Notification(int userId, int applicationId, String type, boolean read) {
        this.userId = userId;
        this.applicationId = applicationId;
        this.type = type;
        this.read = read;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
