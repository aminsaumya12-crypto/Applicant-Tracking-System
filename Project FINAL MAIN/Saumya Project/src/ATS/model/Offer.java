package ATS.model;

import java.time.LocalDate;

public class Offer {

    private int offerId;
    private int applicationId;
    private LocalDate offerDate;
    private String offerStatus;
    private LocalDate acceptedDate;

    // Default constructor.
    // This constructor creates an empty Offer object.
    public Offer() {
    }

    // Parameterized constructor.
    // This constructor sets all fields when creating the object.
    public Offer(int offerId,
                 int applicationId,
                 LocalDate offerDate,
                 String offerStatus,
                 LocalDate acceptedDate) {
        this.offerId = offerId;
        this.applicationId = applicationId;
        this.offerDate = offerDate;
        this.offerStatus = offerStatus;
        this.acceptedDate = acceptedDate;
    }

    
    public int getOfferId() {
        return offerId;
    }

    
    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    
    public int getApplicationId() {
        return applicationId;
    }

   
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    
    public LocalDate getOfferDate() {
        return offerDate;
    }

    
    public void setOfferDate(LocalDate offerDate) {
        this.offerDate = offerDate;
    }

    
    public String getOfferStatus() {
        return offerStatus;
    }

    
    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    
    public LocalDate getAcceptedDate() {
        return acceptedDate;
    }

   
    public void setAcceptedDate(LocalDate acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    
    @Override
    public String toString() {
        return "Offer{" +
                "offerId=" + offerId +
                ", applicationId=" + applicationId +
                ", offerDate=" + offerDate +
                ", offerStatus='" + offerStatus + '\'' +
                ", acceptedDate=" + acceptedDate +
                '}';
    }
}
