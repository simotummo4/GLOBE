package it.uniroma2.ispw.globe.dao.fs;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.Request;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;
import static it.uniroma2.ispw.globe.constants.UserType.AGENCY;
import static it.uniroma2.ispw.globe.constants.UserType.USER;

public class InFSAccountDao extends AccountDao {
    private static final String FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/account.csv";

    @Override
    public Account authenticate(String username, String password) throws DaoException {

        Account account = getAccount(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }

    @Override
    public void addAccount(CredentialsBean credentials) throws DaoException {
        if (getAccount(credentials.getUsername()) != null) {
            throw new DaoException("addAccount: ", DUPLICATE);
        }
        if (credentials.getType().equals(AGENCY)) {
            addAgency(credentials);
        } else {
            addUser(credentials);
        }

    }

    @Override
    public Account getAccount(String username) throws DaoException {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(username)) {
                    return fromCsv(nextLine);
                }
            }
            return null;
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    @Override
    public void removeAccount(CredentialsBean credentials) {
        // not necessary (only in demo mode - InMemory)
    }

    @Override
    public List<Agency> getAgenciesByType(List<String> types) throws DaoException {
        List<Agency> agencies = new ArrayList<>();
        List<String> preferences = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[3].equals(AGENCY)) {
                    preferences.addAll(Arrays.asList(nextLine[9].split(";")));
                    if (new HashSet<>(preferences).containsAll(types)) {
                        agencies.add((Agency) fromCsv(nextLine));
                    }
                }
            }
            return agencies;
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    @Override
    public Agency getAgencyByProposal(String proposalID) throws DaoException {
        List<String> proposalsID = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[3].equals(AGENCY)) {
                    proposalsID.addAll(Arrays.asList(nextLine[5].split(";")));
                    for (String proposal : proposalsID) {
                        if (proposalID.equals(proposal)) {
                            return (Agency) fromCsv(nextLine);
                        }
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
        return null;
    }

    @Override
    public User getUserByProposal(String proposalID) throws DaoException {
        List<String> proposalsID = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[3].equals(USER)) {
                    proposalsID.addAll(Arrays.asList(nextLine[5].split(";")));
                    for (String proposal : proposalsID) {
                        if (proposalID.equals(proposal)) {
                            return (User) fromCsv(nextLine);
                        }
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
        return null;
    }

    @Override
    public Agency getAgencyByRequest(String requestID) throws DaoException {
        List<String> requestsID = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[3].equals(AGENCY)) {
                    requestsID.addAll(Arrays.asList(nextLine[6].split(";")));
                    for (String proposal : requestsID) {
                        if (requestID.equals(proposal)) {
                            return (Agency) fromCsv(nextLine);
                        }
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
        return null;
    }

    @Override
    public User getUserByRequest(String requestID) throws DaoException {
        List<String> requestsID = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[3].equals(USER)) {
                    requestsID.addAll(Arrays.asList(nextLine[6].split(";")));
                    for (String proposal : requestsID) {
                        if (requestID.equals(proposal)) {
                            return (User) fromCsv(nextLine);
                        }
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
        return null;
    }

    @Override
    public void updateAgencyRating(Agency agency) throws DaoException {
        List<String[]> allRows;

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            allRows = reader.readAll();
        } catch (CsvException | IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }

        for (int i = 0; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row[0].equals(agency.getUsername())) {
                row[7] = String.valueOf(agency.getRating());
                allRows.set(i, row);
                break;
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeAll(allRows);
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public void addAgency(CredentialsBean credentials) throws DaoException {
        Agency agency = new Agency();
        agency.setUsername(credentials.getUsername());
        agency.setPassword(credentials.getPassword());
        agency.setPaymentCredential(credentials.getPaymentCredentials());
        agency.setType(credentials.getType());
        agency.setProposals(new ArrayList<>());
        agency.setItineraries(new ArrayList<>());
        agency.setRequests(new ArrayList<>());
        agency.setDescription(credentials.getDescription());
        agency.setPreferences(credentials.getPreferences());
        agency.setRating(0);

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(agency));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public void addUser(CredentialsBean credentials) throws DaoException {

        User user = new User();
        user.setUsername(credentials.getUsername());
        user.setPassword(credentials.getPassword());
        user.setPaymentCredential(credentials.getPaymentCredentials());
        user.setType(credentials.getType());
        user.setItineraries(new ArrayList<>());
        user.setProposals(new ArrayList<>());
        user.setRequests(new ArrayList<>());

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(user));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public String[] toCSV(Account account) {
        StringBuilder itineraryCsv = new StringBuilder();
        StringBuilder proposalCsv = new StringBuilder();
        StringBuilder requestCsv = new StringBuilder();

        if (account.getItineraries() != null && !account.getItineraries().isEmpty()) {
            for (Itinerary itinerary : account.getItineraries()) {
                itineraryCsv.append(itinerary.getItineraryID()).append(";");
            }
            itineraryCsv.setLength(itineraryCsv.length() - 1);
        }
        if (account.getProposals() != null && !account.getProposals().isEmpty()) {
            for (Proposal proposal : account.getProposals()) {
                proposalCsv.append(proposal.getId()).append(";");
            }
            proposalCsv.setLength(proposalCsv.length() - 1);
        }
        if (account.getRequests() != null && !account.getRequests().isEmpty()) {
            for (Request request : account.getRequests()) {
                requestCsv.append(request.getId()).append(";");
            }
            requestCsv.setLength(requestCsv.length() - 1);
        }

        if (account instanceof Agency agency) {
            return agencyToCsv(agency, itineraryCsv, proposalCsv, requestCsv);
        } else {
            return new String[] {account.getUsername(), account.getPassword(), account.getPaymentCredential(), account.getType(), itineraryCsv.toString(), proposalCsv.toString(), requestCsv.toString()};
        }
    }

    public String[] agencyToCsv(Agency agency, StringBuilder itineraryCsv, StringBuilder proposalCsv, StringBuilder requestCsv) {
        StringBuilder preferencesCsv = new StringBuilder();
        if (agency.getPreferences() != null && !agency.getPreferences().isEmpty()) {
            for (String preferences : agency.getPreferences()) {
                preferencesCsv.append(preferences).append(";");
            }
            preferencesCsv.setLength(preferencesCsv.length() - 1);
        }
        return new String[] {agency.getUsername(), agency.getPassword(), agency.getPaymentCredential(), agency.getType(), itineraryCsv.toString(), proposalCsv.toString(), requestCsv.toString(), String.valueOf(agency.getRating()), agency.getDescription(), preferencesCsv.toString()};

    }

    public Account fromCsv(String[] accountCsv) throws DaoException {
        InFSItineraryDao itineraryDao = new InFSItineraryDao();
        InFSProposalDao proposalDao = new InFSProposalDao();
        InFSRequestDao requestDao = new InFSRequestDao();

        List<Itinerary> itineraries = new ArrayList<>();
        List<Proposal> proposals = new ArrayList<>();
        List<Request> requests = new ArrayList<>();

        Account account = null;
        if (accountCsv[3].equals(AGENCY)) {
           account = new Agency();
        } else {
            account = new User();
        }
        account.setUsername(accountCsv[0]);
        account.setPassword(accountCsv[1]);
        account.setPaymentCredential(accountCsv[2]);
        account.setType(accountCsv[3]);

        for (String itineraryID : accountCsv[4].split(";")) {
            Itinerary itinerary = itineraryDao.getItinerary(itineraryID);
            if (itinerary != null) {
                itineraries.add(itinerary);
            }
        }
        account.setItineraries(itineraries);
        for (String proposalID : accountCsv[5].split(";")) {
            Proposal proposal = proposalDao.getProposal(proposalID);
            if (proposal != null) {
                proposals.add(proposal);
            }
        }
        account.setProposals(proposals);
        for (String requestID : accountCsv[6].split(";")) {
            Request request = requestDao.getRequest(requestID);
            if (request != null) {
                requests.add(request);
            }
        }
        account.setRequests(requests);

        if (account instanceof Agency agency) {
            agency.setRating(Double.parseDouble(accountCsv[7]));
            agency.setDescription(accountCsv[8]);

            List<String> preferences = new ArrayList<>(Arrays.asList(accountCsv[9].split(";")));
            agency.setPreferences(preferences);

            return agency;
        }
        return account;
    }
}
