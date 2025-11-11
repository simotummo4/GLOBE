package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.bean.ProposalBean;
import it.uniroma2.ispw.globe.dao.*;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.Session;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.model.Itinerary;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;

public class ManageItineraryController {

    public List<ItineraryBean> getUserItineraries(String sessionId) throws IncorrectDataException, FailedOperationException {
        Session session = SessionManager.getInstance().getSession(sessionId);
        if (session == null) {
            throw new FailedOperationException("GetUserItineraries - Session not found");
        }
        User user = (User) session.getAccount();
        List<Itinerary> itineraries = user.getItineraries();
        List<ItineraryBean> itineraryBeans = new ArrayList<>();
        for (Itinerary itinerary : itineraries) {
            ItineraryBean itineraryBean = new ItineraryBean();
            itineraryBean.setId(itinerary.getItineraryID());
            itineraryBean.setName(itinerary.getName());
            itineraryBean.setDescription(itinerary.getDescription());
            itineraryBean.setTypes(itinerary.getTypes());
            itineraryBean.setDuration(itinerary.getDaysNumber());
            itineraryBean.setPhoto(itinerary.getPhotoFile());


            itineraryBeans.add(itineraryBean);
        }
        return itineraryBeans;
    }

    public List<ProposalBean> getUserProposals(String sessionId) throws FailedOperationException, IncorrectDataException {
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        Session session = SessionManager.getInstance().getSession(sessionId);
        if (session == null) {
            throw new FailedOperationException("GetUserProposals - Session not found");
        }
        try {
            User user = (User) session.getAccount();
            List<Proposal> proposals = user.getProposals();
            List<ProposalBean> proposalBeans = new ArrayList<>();
            for (Proposal proposal : proposals) {
                ProposalBean proposalBean = new ProposalBean();

                proposalBean.setID(proposal.getId());
                proposalBean.setPrice(proposal.getPrice());

                Agency agency = accountDao.getAgencyByProposal(proposal.getId());

                proposalBean.setAgency(agency.getUsername());
                proposalBean.setUser(user.getUsername());
                proposalBean.setDescription(proposal.getDescription());
                proposalBean.setAccepted(proposal.getAccepted());

                proposalBeans.add(proposalBean);
            }
            return proposalBeans;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get user's proposal");
        }
    }
}
