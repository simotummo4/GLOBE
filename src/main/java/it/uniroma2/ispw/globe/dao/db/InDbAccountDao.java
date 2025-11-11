package it.uniroma2.ispw.globe.dao.db;

import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;
import static it.uniroma2.ispw.globe.constants.UserType.AGENCY;

public class InDbAccountDao extends AccountDao {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ACCOUNT = "account";
    public static final String CREDENTIALS = "paymentCredential";

    @Override
    public Account authenticate(String username, String password) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Account.username, Account.password from Account where username = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        Account account = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            if (resultSet.next() && resultSet.getString(PASSWORD).equals(password)) {
                account = getAccount(username);
            }
            return account;
        } catch (SQLException e) {
            throw new DaoException("authenticate: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public void addAccount(CredentialsBean credentials) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        String query = "insert into Account (username, password, paymentCredential, rating, description, type) values (?,?,?,?,?,?)";
        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, credentials.getUsername());
            stmt.setString(2, credentials.getPassword());
            stmt.setString(3, credentials.getPaymentCredentials());
            stmt.setDouble(4, 0);
            stmt.setString(5, credentials.getDescription());
            stmt.setString(6, credentials.getType());
            stmt.execute();

            if (credentials.getType().equals(AGENCY)) {
                addAgencyPreferencese(credentials);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new DaoException("addAccount: "+ e.getMessage(), DUPLICATE);
            }
            throw new DaoException("addAccount: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }

    @Override
    public Account getAccount(String username) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Account.username, Account.password, Account.paymentCredential, Account.type, Account.description, Account.rating from Account where username = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        Account account = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                List<Proposal> proposals = getAccountProposals(username);
                List<Itinerary> itineraries = getAccountItineraries(username);
                List<Request> requests = getAccountRequests(username);

                if ((resultSet.getString("type")).equals(AGENCY)) {
                    List<String> types = getAgencyTypes(username);

                    account = new Agency();
                    account.setUsername(resultSet.getString(USERNAME));
                    account.setPassword(resultSet.getString(PASSWORD));
                    account.setPaymentCredential(resultSet.getString(CREDENTIALS));
                    account.setType(resultSet.getString("type"));
                    account.setProposals(proposals);
                    account.setItineraries(itineraries);
                    account.setRequests(requests);
                    ((Agency) account).setDescription(resultSet.getString("description"));
                    ((Agency) account).setPreferences(types);
                    ((Agency) account).setRating(resultSet.getDouble("rating"));

                } else {

                    account = new User();
                    account.setUsername(resultSet.getString(USERNAME));
                    account.setPassword(resultSet.getString(PASSWORD));
                    account.setPaymentCredential(resultSet.getString(CREDENTIALS));
                    account.setType(resultSet.getString("type"));
                    account.setProposals(proposals);
                    account.setItineraries(itineraries);
                    account.setRequests(requests);

                }
            }
            return account;
        } catch (SQLException e) {
            throw new DaoException("getAccount: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public void removeAccount(CredentialsBean credentials) throws DaoException {
        // not necessary (only in demo mode - InMemory)
    }

    @Override
    public List<Agency> getAgenciesByType(List<String> types) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        List<Agency> agencies = new ArrayList<>();
        List<Agency> correctAgencies = new ArrayList<>();

        if (types.isEmpty()) {
            return getAgencies();
        }

        String query = "select agencyType.agency from agencyType where type = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, types.get(0));
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString("agency"));
                if (account instanceof Agency agency) {
                    agencies.add(agency);
                }
            }

            for (Agency agency : agencies) {
                if (new HashSet<>(agency.getPreferences()).containsAll(types)) {
                    correctAgencies.add(agency);
                }
            }

            return correctAgencies;
        } catch (SQLException e) {
            throw new DaoException("getAgencyByProposal: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public Agency getAgencyByProposal(String proposalID) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select accountProposal.account from accountProposal where proposalID = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, proposalID);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString(ACCOUNT));
                if (account instanceof Agency agency) {
                    return agency;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DaoException("getAgencyByProposal: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public User getUserByProposal(String proposalID) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select accountProposal.account from accountProposal where proposalID = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, proposalID);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString(ACCOUNT));
                if (account instanceof User user) {
                    return user;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DaoException("getUserByProposal: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public Agency getAgencyByRequest(String requestID) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select accountRequest.account from accountRequest where requestID = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, requestID);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString(ACCOUNT));
                if (account instanceof Agency agency) {
                    return agency;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DaoException("getAgencyByRequest: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public User getUserByRequest(String requestID) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select accountRequest.account from accountRequest where requestID = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, requestID);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString(ACCOUNT));
                if (account instanceof User user) {
                    return user;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DaoException("getUserByRequest: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public void updateAgencyRating(Agency agency) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "update Account set rating = ? where username = ?";

        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setDouble(1, agency.getRating());
            stmt.setString(2, agency.getUsername());
            stmt.execute();

        } catch (SQLException e) {
            throw new DaoException("updateAgencyRating: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }

    public void addAgencyPreferencese(CredentialsBean credentialsBean) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        String query = "insert into agencyType (agency, type) values (?,?)";
        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            for (String preference : credentialsBean.getPreferences()) {
                stmt.setString(1, credentialsBean.getUsername());
                stmt.setString(2, preference);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new DaoException("addAgencyPreferences: "+ e.getMessage(), DUPLICATE);
            }
            throw new DaoException("addAgencyPreferences: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }

    public Account getAccountPrimaryData(String username) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Account.username, Account.password, Account.paymentCredential, Account.type, Account.description, Account.rating from Account where username = ?";
        String agencyTypeQuery = "select agencyType.type from agencyType where agency = ?";


        PreparedStatement stmt = null;
        PreparedStatement otherStmt = null;
        ResultSet resultSet = null;
        ResultSet otherResultSet = null;

        Account account = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, username);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                List<String> types = new ArrayList<>();
                otherStmt = connection.prepareStatement(agencyTypeQuery);

                otherStmt.setString(1, username);
                otherResultSet = otherStmt.executeQuery();

                while (otherResultSet.next()) {
                    types.add(otherResultSet.getString("type"));
                }


                if ((resultSet.getString("type")).equals(AGENCY)) {

                    account = new Agency();
                    account.setUsername(resultSet.getString(USERNAME));
                    account.setPassword(resultSet.getString(PASSWORD));
                    account.setPaymentCredential(resultSet.getString(CREDENTIALS));
                    account.setType(resultSet.getString("type"));
                    ((Agency) account).setDescription(resultSet.getString("description"));
                    ((Agency) account).setPreferences(types);
                    ((Agency) account).setRating(resultSet.getDouble("rating"));

                } else {

                    account = new User();
                    account.setUsername(resultSet.getString(USERNAME));
                    account.setPassword(resultSet.getString(PASSWORD));
                    account.setPaymentCredential(resultSet.getString(CREDENTIALS));
                    account.setType(resultSet.getString("type"));

                }
            }
            return account;
        } catch (SQLException e) {
            throw new DaoException("getAccountPrimaryData: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
            DBConnection.getInstance().closeConnection(otherStmt,otherResultSet);
        }
    }

    public List<Proposal> getAccountProposals(String username) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        Connection connection = connect.getConnection();

        String proposalQuery = "select accountProposal.proposalID from accountProposal where account = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Proposal> proposals = new ArrayList<>();


        try {
            stmt = connection.prepareStatement(proposalQuery);
            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                InDbProposalDao proposalDao = new InDbProposalDao();
                Proposal proposal = proposalDao.getProposal(resultSet.getString("proposalID"));
                proposals.add(proposal);
            }
        } catch (SQLException e) {
            throw new DaoException("getAccountProposals: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return proposals;
    }

    public List<Itinerary> getAccountItineraries(String username) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        Connection connection = connect.getConnection();

        String itineraryQuery = "select accountItinerary.itineraryID from accountItinerary where account = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Itinerary> itineraries = new ArrayList<>();
        try {
            stmt = connection.prepareStatement(itineraryQuery);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                InDbItineraryDao itineraryDao = new InDbItineraryDao();
                Itinerary itinerary = itineraryDao.getItinerary(resultSet.getString("itineraryID"));
                itineraries.add(itinerary);
            }
        } catch (SQLException e) {
            throw new DaoException("getAccountItineraries: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return itineraries;
    }

    public List<Request> getAccountRequests(String username) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        Connection connection = connect.getConnection();

        String requestQuery = "select accountRequest.requestID from accountRequest where account = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Request> requests = new ArrayList<>();
        try {
            stmt = connection.prepareStatement(requestQuery);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                InDbRequestDao requestDao = new InDbRequestDao();
                Request request = requestDao.getRequest(resultSet.getString("requestID"));
                requests.add(request);
            }
        } catch (SQLException e) {
            throw new DaoException("getAccountRequests: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return requests;
    }

    public List<String> getAgencyTypes(String username) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        Connection connection = connect.getConnection();

        String agencyTypeQuery = "select agencyType.type from agencyType where agency = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<String> types = new ArrayList<>();
        try {
            stmt = connection.prepareStatement(agencyTypeQuery);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                types.add(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            throw new DaoException("getAgencyTypes: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return types;
    }

    public List<Agency> getAgencies() throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        List<Agency> agencies = new ArrayList<>();

        String query = "select Account.username from Account where type = 'agency'";

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString(USERNAME));
                agencies.add((Agency) account);
            }

            return agencies;
        } catch (SQLException e) {
            throw new DaoException("getAgencyByProposal: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }


}
