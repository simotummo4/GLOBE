package it.uniroma2.ispw.globe.dao.db;

import it.uniroma2.ispw.globe.dao.ProposalDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.Itinerary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InDbProposalDao extends ProposalDao {

    @Override
    public void addProposal(Proposal proposal,User user, Agency agency) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        String query = "insert into Proposal (id,itineraryID,price,description,user,agency,accepted) values (?,?,?,?,?,?,?)";
        String accountQuery= "insert into accountProposal (account,proposalID) values (?,?)";

        PreparedStatement stmt = null;
        PreparedStatement firstAccountStmt = null;
        PreparedStatement secondAccountStmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, proposal.getId());
            stmt.setString(2,proposal.getItinerary().getItineraryID());
            stmt.setDouble(3, proposal.getPrice());
            stmt.setString(4, proposal.getDescription());
            stmt.setString(5, user.getUsername());
            stmt.setString(6, agency.getUsername());
            stmt.setString(7, proposal.getAccepted());
            stmt.execute();

            firstAccountStmt = connection.prepareStatement(accountQuery);
            firstAccountStmt.setString(1, agency.getUsername());
            firstAccountStmt.setString(2, proposal.getId());
            firstAccountStmt.execute();

            secondAccountStmt = connection.prepareStatement(accountQuery);
            secondAccountStmt.setString(1, user.getUsername());
            secondAccountStmt.setString(2, proposal.getId());
            secondAccountStmt.execute();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new DaoException("addProposal: "+ e.getMessage(), DUPLICATE);
            }
            throw new DaoException("addProposal: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
            DBConnection.getInstance().closeConnection(firstAccountStmt,null);
            DBConnection.getInstance().closeConnection(secondAccountStmt,null);
        }
    }

    @Override
    public Proposal getProposal(String proposalID) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Proposal.id, Proposal.price, Proposal.description, Proposal.accepted, Proposal.itineraryID from Proposal where id = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        Proposal proposal = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, proposalID);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                proposal = new Proposal();

                proposal.setId(resultSet.getString("id"));
                proposal.setPrice(resultSet.getDouble("price"));
                proposal.setDescription(resultSet.getString("description"));
                proposal.setAccepted(resultSet.getString("accepted"));

                InDbItineraryDao itineraryDao = new InDbItineraryDao();
                Itinerary itinerary = itineraryDao.getItinerary(resultSet.getString("itineraryID"));

                proposal.setItinerary(itinerary);
            }
        } catch (SQLException e) {
            throw new DaoException("getProposal: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return proposal;
    }

    @Override
    public void updateProposal(Proposal proposal) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "update Proposal set accepted = ? where id = ?";

        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, proposal.getAccepted());
            stmt.setString(2,proposal.getId());
            stmt.execute();

        } catch (SQLException e) {
            throw new DaoException("updateProposal: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }
}
