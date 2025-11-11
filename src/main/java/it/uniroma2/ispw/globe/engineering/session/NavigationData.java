package it.uniroma2.ispw.globe.engineering.session;

import javafx.scene.Node;

public class NavigationData {
    private final String sessionID;
    private final String itineraryID;
    private final String proposalID;
    private final String requestID;
    private Node prev;

    public NavigationData(String sessionID, String itineraryID, String proposalID, String requestID, Node prev) {
        this.sessionID = sessionID;
        this.itineraryID = itineraryID;
        this.proposalID = proposalID;
        this.requestID = requestID;
        this.prev = prev;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getItineraryID() {
        return itineraryID;
    }

    public String getProposalID() {
        return proposalID;
    }

    public String getRequestID() {
        return requestID;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
