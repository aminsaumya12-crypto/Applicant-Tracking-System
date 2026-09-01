package ATS.dao;

import ATS.database.DBConnection;
import ATS.model.Offer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class OfferDAO {

    private Connection con;

    public OfferDAO() {

        con = DBConnection.getConnection();

    }

    // Store a new offer.
    public boolean addOffer(Offer offer) {

        String sql = "INSERT INTO offers(application_id, offer_date, offer_status, accepted_date) VALUES (?, ?, ?, ?)";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, offer.getApplicationId());
            ps.setDate(2, Date.valueOf(offer.getOfferDate()));
            ps.setString(3, offer.getOfferStatus());

            if (offer.getAcceptedDate() != null) {
                ps.setDate(4, Date.valueOf(offer.getAcceptedDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Update offer details.
    public boolean updateOffer(Offer offer) {

        String sql = "UPDATE offers SET offer_date=?, offer_status=?, accepted_date=? WHERE offer_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDate(1, Date.valueOf(offer.getOfferDate()));
            ps.setString(2, offer.getOfferStatus());

            if (offer.getAcceptedDate() != null) {
                ps.setDate(3, Date.valueOf(offer.getAcceptedDate()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setInt(4, offer.getOfferId());

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Update offer status.
    public boolean updateOfferStatus(int offerId, String offerStatus) {

        String sql = "UPDATE offers SET offer_status=? WHERE offer_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, offerStatus);
            ps.setInt(2, offerId);

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

    // Get offer details using application ID.
    public Offer getOfferByApplication(int applicationId) {

        String sql = "SELECT * FROM offers WHERE application_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, applicationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Offer offer = new Offer();

                offer.setOfferId(rs.getInt("offer_id"));
                offer.setApplicationId(rs.getInt("application_id"));

                Date offerDate = rs.getDate("offer_date");
                if (offerDate != null) {
                    offer.setOfferDate(offerDate.toLocalDate());
                }

                offer.setOfferStatus(rs.getString("offer_status"));

                Date acceptedDate = rs.getDate("accepted_date");
                if (acceptedDate != null) {
                    offer.setAcceptedDate(acceptedDate.toLocalDate());
                }

                return offer;

            }

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return null;

    }

    // Get all offers.
    public ArrayList<Offer> getAllOffers() {

        ArrayList<Offer> offerList = new ArrayList<>();

        String sql = "SELECT * FROM offers";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Offer offer = new Offer();

                offer.setOfferId(rs.getInt("offer_id"));
                offer.setApplicationId(rs.getInt("application_id"));

                Date offerDate = rs.getDate("offer_date");
                if (offerDate != null) {
                    offer.setOfferDate(offerDate.toLocalDate());
                }

                offer.setOfferStatus(rs.getString("offer_status"));

                Date acceptedDate = rs.getDate("accepted_date");
                if (acceptedDate != null) {
                    offer.setAcceptedDate(acceptedDate.toLocalDate());
                }

                offerList.add(offer);

            }

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return offerList;

    }

    // Delete an offer using offer ID.
    public boolean deleteOffer(int offerId) {

        String sql = "DELETE FROM offers WHERE offer_id=?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, offerId);

            int rows = ps.executeUpdate();

            return rows > 0;

        }
        catch (SQLException e) {

            System.out.println("Database Error : " + e.getMessage());

        }

        return false;

    }

}