package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.dao.*;
import it.uniroma2.ispw.globe.engineering.session.Session;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.engineering.Persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;
import static it.uniroma2.ispw.globe.constants.ProposalState.ACCEPTED;


public class AcceptItineraryController {

    public ProposalBean getProposal(String proposalID, String sessionID) throws FailedOperationException, IncorrectDataException {
        Proposal proposal;
        User user;
        Agency agency;
        ProposalBean proposalBean = new ProposalBean();
        try {
            if (proposalID == null) {
                Session session = SessionManager.getInstance().getSession(sessionID);
                proposal = session.getPendingProposal();
                if (session.getPendingAccount() instanceof User pendingAccount) {
                    user = pendingAccount;
                    agency = (Agency) session.getAccount();
                } else {
                    user = (User) session.getAccount();
                    agency = (Agency) session.getPendingAccount();
                }

            } else {
                ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();
                AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
                proposal = proposalDao.getProposal(proposalID);
                user = accountDao.getUserByProposal(proposalID);
                agency = accountDao.getAgencyByProposal(proposalID);
            }

            proposalBean.setID(proposalID);
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

    public PaymentBean sendResponse(String proposalId, String response) throws FailedOperationException, IncorrectDataException {
        ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();
        try {
            Proposal proposal = proposalDao.getProposal(proposalId);
            PaymentBean paymentResult = null;

            if (response.equals(ACCEPTED)) {
                paymentResult = executePayment(proposal);
            }

            proposal.setAccepted(response);
            proposalDao.updateProposal(proposal);

            return paymentResult;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Send response");
        }
    }

    public PaymentBean executePayment(Proposal proposal) throws FailedOperationException, IncorrectDataException {

        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

        try {
            Agency payee = accountDao.getAgencyByProposal(proposal.getId());
            User payer = accountDao.getUserByProposal(proposal.getId());

            PaymentBean paymentResult = null;

            if (new PaymentApi().processPayment(proposal.getPrice(),payer.getPaymentCredential(),payee.getPaymentCredential())) {
                paymentResult = new PaymentBean();
                paymentResult.setAmount(proposal.getPrice());
                paymentResult.setPayeeUsername(payee.getUsername());
                paymentResult.setPayerUsername(payer.getUsername());
            }

            return paymentResult;

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Execute payment");
        }
    }

    public ItineraryBean getProposalItinerary(String proposalId) throws FailedOperationException, IncorrectDataException {
        ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();
        Proposal proposal;
        ItineraryBean itineraryBean = new ItineraryBean();
        try {
            proposal = proposalDao.getProposal(proposalId);
            if (proposal == null) {
                throw new FailedOperationException("Get proposal's itinerary - proposal not found");
            }
            Itinerary itinerary = proposal.getItinerary();

            itineraryBean.setId(itinerary.getItineraryID());
            itineraryBean.setName(itinerary.getName());
            itineraryBean.setDescription(itinerary.getDescription());
            itineraryBean.setTypes(itinerary.getTypes());
            itineraryBean.setDuration(itinerary.getDaysNumber());
            itineraryBean.setPhoto(itinerary.getPhotoFile());

            return itineraryBean;

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get proposal's itinerary");
        }
    }

    public void addRating(Double rating, String proposalId) throws FailedOperationException {
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        try {

            Agency agency = accountDao.getAgencyByProposal(proposalId);
            double curRating = agency.getRating();
            if (curRating == 0) {
                agency.setRating(rating);
            } else {
                agency.setRating((curRating + rating)/2);
            }

            accountDao.updateAgencyRating(agency);

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Add rating");
        }
    }

    public String getAccountType(String sessionId) {
        Account account = SessionManager.getInstance().getSession(sessionId).getAccount();
        return account.getType();
    }

    public CityBean getCity(int stepNum, String cityID, String sessionID) throws FailedOperationException {
        try {
            CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
            City city = null;

            if (sessionID != null) {
                Itinerary itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
                for (City savedCity : itinerary.getDays().get(stepNum).getCities()) {
                    if (cityID.equals(savedCity.getPlaceID())) {
                        city = savedCity;
                    }
                }
            } else {
                city = cityDao.getCity(cityID);
                if (city == null) {
                    city = cityDao.createCity(cityID);
                }
            }

            if (city != null) {
                CityBean cityBean = new CityBean();
                cityBean.setId(city.getPlaceID());
                cityBean.setName(city.getName());
                cityBean.setCountry(city.getCountry());
                return cityBean;
            } else {
                return null;
            }
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get city");
        }
    }

    public AttractionBean getAttraction(int stepNum, String attractionID, String sessionID) throws FailedOperationException {
        try {
            AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
            Attraction attraction = null;

            if (sessionID != null) {
                Itinerary itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
                for (Attraction savedeAttraction : itinerary.getDays().get(stepNum).getAttractions()) {
                    if (attractionID.equals(savedeAttraction.getPlaceID())) {
                        attraction = savedeAttraction;
                    }
                }
            } else {
                attraction = attractionDao.getAttraction(attractionID);
                if (attraction == null) {
                    attraction = attractionDao.createAttraction(attractionID);
                }
            }

            if (attraction != null) {
                AttractionBean attractionBean = new AttractionBean();
                attractionBean.setId(attraction.getPlaceID());
                attractionBean.setName(attraction.getName());
                attractionBean.setAddress(attraction.getAddress());
                attractionBean.setCity(attraction.getCity());

                return attractionBean;
            } else {
                return null;
            }
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get attraction");
        }
    }

    public List<StepBean> getSteps(String itineraryId, String sessionID) throws FailedOperationException, IncorrectDataException {
        try {
            List<StepBean> steps = new ArrayList<>();
            Itinerary itinerary;

            if (itineraryId == null) {
                itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
            } else {
                ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
                itinerary = itineraryDao.getItinerary(itineraryId);
            }

            List<Day> days = itinerary.getDays();
            for (Day day : days) {
                StepBean stepBean = getStepBean(day);
                steps.add(stepBean);
            }
            return steps;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get steps");
        }
    }

    private StepBean getStepBean(Day day) throws IncorrectDataException {
        List<String> attractions = new ArrayList<>();
        for (Attraction attraction : day.getAttractions()) {
            attractions.add(attraction.getPlaceID());
        }
        List<String> cities = new ArrayList<>();
        for (City city : day.getCities()) {
            cities.add(city.getPlaceID());
        }
        StepBean stepBean = new StepBean();
        stepBean.setNum(day.getDayNum()-1);
        stepBean.setAttractions(attractions);
        stepBean.setCity(cities);
        return stepBean;
    }
}