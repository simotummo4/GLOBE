package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import java.util.List;

import static it.uniroma2.ispw.globe.constants.ProposalState.*;

public class RequestBean {
    private String id;
    private String user;
    private String agency;
    private String otherRequests;
    private int dayNum;
    private List<String> types;
    private List<String> cities;
    private List<String> attractions;
    private String accepted;
    private boolean flight;
    private boolean accommodation;
    private List<String> agencies;

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) throws IncorrectDataException {
        if (user == null || user.isEmpty()) {
            throw new IncorrectDataException("Request user not valid");
        }
        this.user = user;
    }

    public String getAgency() { return agency; }

    public void setAgency(String agency) throws IncorrectDataException{
        if (agency == null || agency.isEmpty()) {
            throw new IncorrectDataException("Request agency not valid");
        }
        this.agency = agency;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) throws IncorrectDataException {
        if (cities == null || cities.isEmpty()) {
            throw new IncorrectDataException("Request cities not valid");
        }
        this.cities = cities;
    }

    public List<String> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<String> attractions) throws IncorrectDataException {
        if (attractions == null || attractions.isEmpty()) {
            throw new IncorrectDataException("Request attractions not valid");
        }
        this.attractions = attractions;
    }

    public String getOtherRequests() {
        return otherRequests;
    }

    public void setOtherRequests(String otherRequests) throws IncorrectDataException {
        if (otherRequests == null || otherRequests.isEmpty() || otherRequests.length()>999) {
            throw new IncorrectDataException("Proposal other request not valid");
        }
        this.otherRequests = otherRequests;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) throws IncorrectDataException {
        if (dayNum < 1 || dayNum > 99) {
            throw new IncorrectDataException("Request dayNum not valid");
        }
        this.dayNum = dayNum;
    }

    public boolean isFlight() {
        return flight;
    }

    public void setFlight(boolean flight) {
        this.flight = flight;
    }

    public boolean isAccommodation() {
        return accommodation;
    }

    public void setAccommodation(boolean accommodation) {
        this.accommodation = accommodation;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) throws IncorrectDataException {
        if (types == null || types.isEmpty()) {
            throw new IncorrectDataException("Request itineraryType not valid");
        }
        this.types = types;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) throws IncorrectDataException {
        if (accepted.equals(ACCEPTED) || accepted.equals(REJECTED) || accepted.equals(PENDING)) {
            this.accepted = accepted;
            return;
        }
        throw new IncorrectDataException("Request accepted not valid");
    }

    public List<String> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<String> agencies) throws IncorrectDataException {
        if (agencies == null || agencies.isEmpty()) {
            throw new IncorrectDataException("Request agencies not valid");
        }
        this.agencies = agencies;
    }
}

