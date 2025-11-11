package it.uniroma2.ispw.globe.dao.memory;

import it.uniroma2.ispw.globe.dao.ProposalDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;

public class InMemoryProposalDao extends ProposalDao {
    private static InMemoryProposalDao instance = null;

    List<Proposal> proposals = new ArrayList<>();

    private InMemoryProposalDao() {}

    public static InMemoryProposalDao getInstance() {
        if (instance == null) {
            instance = new InMemoryProposalDao();
        }
        return instance;
    }

    @Override
    public void addProposal(Proposal proposal, User user, Agency agency) throws DaoException {
        if (getProposal(proposal.getId()) == null) {
            proposals.add(proposal);
            user.getProposals().add(proposal);
            agency.getProposals().add(proposal);
        } else {
            throw new DaoException("addProposal", DUPLICATE);
        }
    }

    @Override
    public Proposal getProposal(String proposalID) throws DaoException {
        for (Proposal proposal : proposals) {
            if (proposal.getId().equals(proposalID)){
                return proposal;
            }
        }
        return null;
    }

    @Override
    public void updateProposal(Proposal proposal) {
        // not necessary
    }
}
