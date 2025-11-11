package it.uniroma2.ispw.globe.model;

import java.util.List;

public abstract class Account {
    private String username;
    private String password;
    private String paymentCredential;
    private String type;
    private List<Itinerary> itineraries;
    private List<Proposal> proposals;
    private List<Request> requests;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaymentCredential() {
        return paymentCredential;
    }

    public void setPaymentCredential(String paymentCredential) {
        this.paymentCredential = paymentCredential;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Itinerary> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public List<Proposal> getProposals() {
        return proposals;
    }

    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
