package it.uniroma2.ispw.globe.dao;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.Itinerary;

import static it.uniroma2.ispw.globe.constants.ProposalState.PENDING;

public abstract class ProposalDao {
    public Proposal createProposal(String id, double price, String description, Itinerary itinerary) {
        Proposal proposal = new Proposal();

        proposal.setId(id);
        proposal.setItinerary(itinerary);
        proposal.setPrice(price);
        proposal.setDescription(description);
        proposal.setAccepted(PENDING);

        return proposal;
    }
    public abstract void addProposal(Proposal proposal, User user, Agency agency) throws DaoException;
    public abstract Proposal getProposal(String proposalID) throws DaoException;
    public abstract void updateProposal(Proposal proposal) throws DaoException;
}
