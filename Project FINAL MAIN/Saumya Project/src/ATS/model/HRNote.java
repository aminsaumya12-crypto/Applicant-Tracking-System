package ATS.model;

import java.time.LocalDate;

public class HRNote {

    private int noteId;
    private int applicationId;
    private int handledBy;
    private String remarks;
    private String rejectionReason;
    private String rejectionStage;
    private LocalDate createdAt;

    // Default constructor.
    // This constructor creates an empty HRNote object.
    public HRNote() {
    }

    // Parameterized constructor.
    // This constructor sets all fields when creating the object.
    public HRNote(int noteId,
                  int applicationId,
                  int handledBy,
                  String remarks,
                  String rejectionReason,
                  String rejectionStage,
                  LocalDate createdAt) {
        this.noteId = noteId;
        this.applicationId = applicationId;
        this.handledBy = handledBy;
        this.remarks = remarks;
        this.rejectionReason = rejectionReason;
        this.rejectionStage = rejectionStage;
        this.createdAt = createdAt;
    }

    
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    
    public int getApplicationId() {
        return applicationId;
    }

    
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    
    public int getHandledBy() {
        return handledBy;
    }

    
    public void setHandledBy(int handledBy) {
        this.handledBy = handledBy;
    }

    
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    
    public String getRejectionReason() {
        return rejectionReason;
    }

    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRejectionStage() {
        return rejectionStage;
    }

    
    public void setRejectionStage(String rejectionStage) {
        this.rejectionStage = rejectionStage;
    }

    
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    
    @Override
    public String toString() {
        return "HRNote{" +
                "noteId=" + noteId +
                ", applicationId=" + applicationId +
                ", handledBy=" + handledBy +
                ", remarks='" + remarks + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", rejectionStage='" + rejectionStage + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
