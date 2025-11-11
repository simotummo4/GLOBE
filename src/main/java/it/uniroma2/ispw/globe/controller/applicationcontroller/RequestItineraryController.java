package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.dao.AttractionDao;
import it.uniroma2.ispw.globe.dao.CityDao;
import it.uniroma2.ispw.globe.dao.RequestDao;
import it.uniroma2.ispw.globe.exception.*;
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
import static it.uniroma2.ispw.globe.constants.ProposalState.PENDING;

public class RequestItineraryController {

    public List<AttractionBean> getAttractions(String name) throws FailedOperationException {
        return new NominatimAPIClient ().getAttractions(name);
    }

    public List<CityBean> getCities(String name) throws FailedOperationException {
        return new NominatimAPIClient ().getCities(name);
    }

    public List<AgencyBean> getAgenciesByType(List<String> types) throws FailedOperationException, IncorrectDataException {
        try {
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
            List<Agency> agencies = accountDao.getAgenciesByType(types);
            return getAgencyBeans(agencies);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get agency by type");
        }
    }

    private List<AgencyBean> getAgencyBeans(List<Agency> agencies) throws IncorrectDataException {
        List<AgencyBean> agencyBeans = new ArrayList<>();

        for (Agency agency: agencies){
            AgencyBean agencyBean = new AgencyBean();
            agencyBean.setName(agency.getUsername());
            agencyBean.setRating(agency.getRating());
            agencyBean.setItineraryTypes(agency.getPreferences());
            agencyBeans.add(agencyBean);
        }
        return agencyBeans;
    }

    public RequestBean getRequest(String requestID, String sessionID) throws IncorrectDataException, FailedOperationException {
        Request request;
        Agency agency = null;
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
                throw new FailedOperationException("Get proposal");
            }
        } else {
            Session session = SessionManager.getInstance().getSession(sessionID);
            request = session.getPendingRequest();
            if (session.getAccount() instanceof Agency account) {
                agency = account;
                user = (User) session.getPendingAccount();
            } else {
                user = (User) session.getAccount();
            }
        }

        if (request == null) {
            throw new FailedOperationException("Get Request");
        }

        List<String> citiesID = new ArrayList<>();
        for (City city : request.getCities()) {
            citiesID.add(city.getPlaceID());
        }

        List<String> attractionsID = new ArrayList<>();
        for (Attraction attraction : request.getAttractions()) {
            attractionsID.add(attraction.getPlaceID());
        }

        RequestBean requestBean = new RequestBean();
        requestBean.setID(request.getId());
        requestBean.setUser(user.getUsername());
        requestBean.setCities(citiesID);
        requestBean.setAttractions(attractionsID);

        if (requestID != null) {
            requestBean.setAgency(agency.getUsername());
        } else {
            List<String> agencies = new ArrayList<>();
            for (Agency a: SessionManager.getInstance().getSession(sessionID).getPendingAgencies()) {
                agencies.add(a.getUsername());
            }
            requestBean.setAgencies(agencies);
        }
        requestBean.setOtherRequests(request.getOtherRequest());
        requestBean.setDayNum(request.getDayNum());
        requestBean.setTypes(request.getItineraryType());
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
                throw new FailedOperationException("Get proposal");
            }
        } else {
            request = SessionManager.getInstance().getSession(sessionID).getPendingRequest();
        }

        Request current = request;
        while (current instanceof RequestDecorator requestDecorator) {
            if (current instanceof OnTheRoadRequestDecorator onTheRoadRequestDecorator) {
                OnTheRoadBean onTheRoadBean = new OnTheRoadBean();
                onTheRoadBean.setMode(onTheRoadRequestDecorator.getTravelMode());
                onTheRoadBean.setDayDrivingHours(onTheRoadRequestDecorator.getDayDrivingHours());
                optionals.add(onTheRoadBean);
            }
            if (current instanceof NatureRequestDecorator natureRequestDecorator) {
                NatureBean natureBean = new NatureBean();
                natureBean.setDifficulty(natureRequestDecorator.getTrekkingDifficulty());
                natureBean.setTrekkingDistance(natureRequestDecorator.getTrekkingDistance());
                optionals.add(natureBean);
            }
            current = requestDecorator.getRequest();
        }

        return optionals;
    }

    public List<AgencyBean> getAgencies(String sessionID) throws IncorrectDataException {
        List<Agency> agencies = SessionManager.getInstance().getSession(sessionID).getPendingAgencies();
        return getAgencyBeans(agencies);
    }

    public void createRequest(RequestBean requestBean, OnTheRoadBean onTheRoadBean, NatureBean natureBean, String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
            RequestDao requestDao = Persistence.getInstance().getFactory().getRequestDao();
            CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
            AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();


            Request request = requestDao.createRequest(UUID.randomUUID().toString(),PENDING,requestBean.getOtherRequests(),requestBean.getDayNum(),requestBean.isFlight(),requestBean.isAccommodation(),requestBean.getTypes());

            List<City> cities = new ArrayList<>();
            List<Attraction> attractions = new ArrayList<>();

            for (String cityId : requestBean.getCities()) {
                City city = cityDao.createCity(cityId);
                cities.add(city);
            }

            for (String attractionId : requestBean.getAttractions()) {
                Attraction attraction = attractionDao.createAttraction(attractionId);
                attractions.add(attraction);
            }

            request.setAttractions(attractions);
            request.setCities(cities);

            if (onTheRoadBean != null) {
                OnTheRoadRequestDecorator onTheRoadRequest = new OnTheRoadRequestDecorator(request);
                onTheRoadRequest.setDayDrivingHours(onTheRoadBean.getDayDrivingHours());
                onTheRoadRequest.setTravelMode(onTheRoadBean.getMode());
                request = onTheRoadRequest;
            }
            if (natureBean != null) {
                NatureRequestDecorator natureRequest = new NatureRequestDecorator(request);
                natureRequest.setTrekkingDistance(natureBean.getTrekkingDistance());
                natureRequest.setTrekkingDifficulty(natureBean.getDifficulty());
                request = natureRequest;
            }

            SessionManager.getInstance().getSession(sessionID).setPendingRequest(request);

            List<Agency> agencies = new ArrayList<>();
            for (String agencyName : requestBean.getAgencies()){
                Agency agency = (Agency) accountDao.getAccount(agencyName);
                agencies.add(agency);
            }
            SessionManager.getInstance().getSession(sessionID).setPendingAgencies(agencies);

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Create request");
        }
    }

    public void saveRequest(String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
            RequestDao requestDao = Persistence.getInstance().getFactory().getRequestDao();
            AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
            CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();

            Session session = SessionManager.getInstance().getSession(sessionID);
            Account account = session.getAccount();

            Request request = session.getPendingRequest();
            List<Agency> agencies = session.getPendingAgencies();

            for (City city : request.getCities()) {
                cityDao.addCity(city);
            }
            for (Attraction attraction : request.getAttractions()) {
                attractionDao.addAttraction(attraction);
            }
            requestDao.addAgencyRequest(request, (User) account, agencies);

            session.setAccount(accountDao.getAccount(account.getUsername()));

            session.setPendingRequest(null);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Save request");
        }
    }

    public CityBean getCity(String cityId) throws FailedOperationException {
        try {
            CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
            City city = cityDao.getCity(cityId);

            if (city == null) {
                city = cityDao.createCity(cityId);
            }

            CityBean cityBean = new CityBean();
            cityBean.setName(city.getName());
            cityBean.setId(city.getPlaceID());
            cityBean.setCountry(city.getCountry());


            return cityBean;

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Save request");
        }
    }

    public AttractionBean getAttraction(String attractionId) throws FailedOperationException {
        try {
            AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
            Attraction attraction = attractionDao.getAttraction(attractionId);

            if (attraction == null) {
                attraction = attractionDao.createAttraction(attractionId);
            }

            AttractionBean attractionBean = new AttractionBean();
            attractionBean.setName(attraction.getName());
            attractionBean.setId(attraction.getPlaceID());
            attractionBean.setCity(attraction.getCity());
            attractionBean.setAddress(attraction.getAddress());

            return attractionBean;

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Save request");
        }
    }
}
