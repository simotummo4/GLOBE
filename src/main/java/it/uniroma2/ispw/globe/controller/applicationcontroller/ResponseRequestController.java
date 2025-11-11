package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.bean.NatureBean;
import it.uniroma2.ispw.globe.bean.OnTheRoadBean;
import it.uniroma2.ispw.globe.bean.ProposalBean;
import it.uniroma2.ispw.globe.bean.RequestBean;
import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.dao.ItineraryDao;
import it.uniroma2.ispw.globe.dao.ProposalDao;
import it.uniroma2.ispw.globe.dao.RequestDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.Session;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.engineering.decorator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;
import static it.uniroma2.ispw.globe.constants.ProposalState.ACCEPTED;

public class ResponseRequestController {

    public void createProposal(ProposalBean proposalBean, String userUsername, String requestId, String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();
            RequestDao requestDao = Persistence.getInstance().getFactory().getRequestDao();
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

            User user = (User) accountDao.getAccount(userUsername);
            String proposalID = UUID.randomUUID().toString();
            proposalBean.setID(proposalID);

            Session session = SessionManager.getInstance().getSession(sessionID);

            Proposal proposal = proposalDao.createProposal(proposalID,proposalBean.getPrice(),proposalBean.getDescription(),session.getPendingItinerary());

            Request request = requestDao.getRequest(requestId);
            request.setAccepted(ACCEPTED);

            session.setPendingProposal(proposal);
            session.setPendingRequest(request);
            session.setPendingAccount(user);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Create proposal");
        }
    }

    public void saveProposal(String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
            ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();
            RequestDao requestDao = Persistence.getInstance().getFactory().getRequestDao();
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

            Session session = SessionManager.getInstance().getSession(sessionID);
            Agency agency = (Agency) session.getAccount();
            User user = (User) session.getPendingAccount();

            Proposal proposal = session.getPendingProposal();
            Itinerary itinerary = session.getPendingItinerary();
            itineraryDao.addItinerary(itinerary, agency);

            proposal.setItinerary(session.getPendingItinerary());
            proposalDao.addProposal(proposal,user,agency);

            Request request = session.getPendingRequest();
            request.setAccepted(ACCEPTED);
            requestDao.updateRequest(request);

            session.setAccount(accountDao.getAccount(agency.getUsername()));

            session.setPendingItinerary(null);
            session.setPendingProposal(null);
            session.setPendingRequest(null);
            session.setPendingAccount(null);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Save proposal");
        }
    }

    public ProposalBean getProposal(String proposalID, String sessionID) throws FailedOperationException, IncorrectDataException {
        Proposal proposal;
        User user;
        Agency agency;
        ProposalBean proposalBean = new ProposalBean();
        proposalBean.setID(proposalID);
        try {
            if (proposalID == null) {
                Session session = SessionManager.getInstance().getSession(sessionID);
                proposal = session.getPendingProposal();
                if (session.getAccount() instanceof Agency account) {
                    agency = account;
                    user = (User) session.getPendingAccount();
                } else {
                    user = (User) session.getAccount();
                    agency = (Agency) session.getPendingAccount();
                }

                if (proposal == null) {
                    return null;
                }
            } else {
                ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();
                AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
                proposal = proposalDao.getProposal(proposalID);
                agency = accountDao.getAgencyByProposal(proposalID);
                user = accountDao.getUserByProposal(proposalID);
            }

            proposalBean.setPrice(proposal.getPrice());
            proposalBean.setAgency(agency.getUsername());
            proposalBean.setUser(user.getUsername());
            proposalBean.setDescription(proposal.getDescription());
            proposalBean.setAccepted(proposal.getAccepted());

            return proposalBean;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get proposal");
        }
    }

    public RequestBean getAgencyRequest(String requestID, String sessionID) throws IncorrectDataException, FailedOperationException {
        Request request;
        Agency agency;
        User user;

        if (requestID != null) {
            RequestDao requestDao = Persistence.getInstance().getFactory().getRequestDao();
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

            try {
                request = requestDao.getRequest(requestID);
                agency = accountDao.getAgencyByRequest(requestID);
                user = accountDao.getUserByRequest(requestID);
            } catch (DaoException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
                throw new FailedOperationException("Get request");
            }
        } else {
            Session session = SessionManager.getInstance().getSession(sessionID);
            request = session.getPendingRequest();
            if (session.getAccount() instanceof Agency account) {
                agency = account;
                user = (User) session.getPendingAccount();
            } else {
                user = (User) session.getAccount();
                agency = (Agency) session.getPendingAccount();
            }
        }

        if (request == null) {
            throw new FailedOperationException("Get Request");
        }

        return getRequestBean(request, user, agency);
    }

    private RequestBean getRequestBean(Request request, User user, Agency agency) throws IncorrectDataException {
        RequestBean requestBean = new RequestBean();
        requestBean.setID(request.getId());
        requestBean.setUser(user.getUsername());
        requestBean.setAgency(agency.getUsername());
        requestBean.setOtherRequests(request.getOtherRequest());
        requestBean.setDayNum(request.getDayNum());
        requestBean.setTypes(request.getItineraryType());

        List<String> attractionsID = new ArrayList<>();
        for (Attraction attraction : request.getAttractions()) {
            attractionsID.add(attraction.getPlaceID());
        }
        requestBean.setAttractions(attractionsID);


        List<String> citiesID = new ArrayList<>();
        for (City city : request.getCities()) {
            citiesID.add(city.getPlaceID());
        }
        requestBean.setCities(citiesID);

        requestBean.setAccepted(request.getAccepted());
        requestBean.setFlight(request.getFlightRequest());
        requestBean.setAccommodation(request.getAccommodationRequest());
        return requestBean;
    }

    public List<Object> getRequestOptional(String requestID, String sessionID) throws FailedOperationException, IncorrectDataException {
        Request request;
        List<Object> optionals = new ArrayList<>();
        if (requestID != null) {
            RequestDao requestDao = Persistence.getInstance().getFactory().getRequestDao();
            try {
                request = requestDao.getRequest(requestID);
            } catch (DaoException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
                throw new FailedOperationException("Get request optional");
            }
        } else {
            request = SessionManager.getInstance().getSession(sessionID).getPendingRequest();
        }

        Request current = request;
        while (current instanceof RequestDecorator requestDecorator) {
            if (current instanceof NatureRequestDecorator natureRequestDecorator) {
                NatureBean natureBean = new NatureBean();
                natureBean.setDifficulty(natureRequestDecorator.getTrekkingDifficulty());
                natureBean.setTrekkingDistance(natureRequestDecorator.getTrekkingDistance());
                optionals.add(natureBean);
            }
            if (current instanceof OnTheRoadRequestDecorator onTheRoadRequestDecorator) {
                OnTheRoadBean onTheRoadBean = new OnTheRoadBean();
                onTheRoadBean.setMode(onTheRoadRequestDecorator.getTravelMode());
                onTheRoadBean.setDayDrivingHours(onTheRoadRequestDecorator.getDayDrivingHours());
                optionals.add(onTheRoadBean);
            }
            current = requestDecorator.getRequest();
        }

        return optionals;
    }

    public List<ProposalBean> getAgencyProposals(String sessionID) throws FailedOperationException, IncorrectDataException {
        try {
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

            Agency agency = (Agency) SessionManager.getInstance().getSession(sessionID).getAccount();
            List<Proposal> proposals = agency.getProposals();
            List<ProposalBean> proposalBeans = new ArrayList<>();
            for (Proposal proposal : proposals) {
                User user = accountDao.getUserByProposal(proposal.getId());
                ProposalBean proposalBean = new ProposalBean();
                proposalBean.setID(proposal.getId());
                proposalBean.setPrice(proposal.getPrice());
                proposalBean.setAgency(agency.getUsername());
                proposalBean.setUser(user.getUsername());
                proposalBean.setDescription(proposal.getDescription());
                proposalBean.setAccepted(proposal.getAccepted());

                proposalBeans.add(proposalBean);
            }

            return proposalBeans;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get agency's proposals");
        }
    }

    public List<RequestBean> getAgencyRequests(String sessionID) throws FailedOperationException, IncorrectDataException {
        try {
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

            Agency agency = (Agency) SessionManager.getInstance().getSession(sessionID).getAccount();
            List<Request> requests = agency.getRequests();
            List<RequestBean> requestBeans = new ArrayList<>();
            for (Request request : requests) {
                User user = accountDao.getUserByRequest(request.getId());

                RequestBean requestBean = new RequestBean();
                requestBean.setID(request.getId());
                requestBean.setUser(user.getUsername());
                requestBean.setAgency(agency.getUsername());
                requestBean.setOtherRequests(request.getOtherRequest());
                requestBean.setAccepted(request.getAccepted());
                requestBean.setDayNum(request.getDayNum());
                requestBean.setTypes(request.getItineraryType());

                requestBeans.add(requestBean);
            }
            return requestBeans;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get agency's requests");
        }
    }

    public void setPendingRequest(String sessionID,String requestID) throws FailedOperationException {
        try {
            RequestDao requestDao = Persistence.getInstance().getFactory().getRequestDao();
            Request request = requestDao.getRequest(requestID);

            SessionManager.getInstance().getSession(sessionID).setPendingRequest(request);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get pending request");
        }
    }

}
