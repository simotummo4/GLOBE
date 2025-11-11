package it.uniroma2.ispw.globe.bean;


import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import static it.uniroma2.ispw.globe.constants.ProposalState.*;

public class ProposalBean {
    private String id;
    private double price;
    private String agency;
    private String user;
    private String description;
    private String accepted;

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws IncorrectDataException {
        if (price <= 0) {
            throw new IncorrectDataException("Proposal price not valid");
        }
        this.price = price;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) throws IncorrectDataException {
        if (agency == null || agency.isEmpty()) {
            throw new IncorrectDataException("Proposal agency not valid");
        }
        this.agency = agency;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) throws IncorrectDataException {
        if (user == null || user.isEmpty()) {
            throw new IncorrectDataException("Proposal user not valid");
        }
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws IncorrectDataException {
        if (description == null || description.isEmpty() || description.length()>999) {
            throw new IncorrectDataException("Proposal description not valid");
        }
        this.description = description;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) throws IncorrectDataException {
        if (!(accepted.equals(ACCEPTED) || accepted.equals(REJECTED) || accepted.equals(PENDING))) {
            throw new IncorrectDataException("Proposal status not valid");
        }
        this.accepted = accepted;
    }
}
