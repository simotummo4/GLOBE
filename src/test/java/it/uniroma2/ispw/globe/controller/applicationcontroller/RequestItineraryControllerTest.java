package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.dao.*;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.uniroma2.ispw.globe.constants.ItineraryType.CULTURE;
import static it.uniroma2.ispw.globe.constants.UserType.AGENCY;
import static it.uniroma2.ispw.globe.constants.UserType.USER;
import static org.junit.jupiter.api.Assertions.*;

class RequestItineraryControllerTest {

    // test by Simone Tummolo 0309116

    @Test
    void testCreateRequestCorrect() throws FailedOperationException, DuplicateItemException, DaoException, IncorrectDataException {
        RequestItineraryController controller = new RequestItineraryController();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

        RequestBean request = new RequestBean();

        request.setCities(List.of("r41485"));
        request.setAttractions(List.of("r1849830"));
        request.setDayNum(1);
        request.setOtherRequests("other requests");
        request.setAgencies(List.of("agencyTest"));
        request.setTypes(List.of(CULTURE));
        request.setFlight(true);
        request.setAccommodation(true);

        OnTheRoadBean onTheRoad = null;
        NatureBean nature = null;

        String sessionID = SessionManager.getInstance().addSession(accountDao.getAccount("userTest"));

        controller.createRequest(request, onTheRoad, nature, sessionID);
        assertNotNull(SessionManager.getInstance().getSession(sessionID).getPendingRequest());

        assertEquals(request.getAgencies().size(), SessionManager.getInstance().getSession(sessionID).getPendingAgencies().size());
        for (Agency agency: SessionManager.getInstance().getSession(sessionID).getPendingAgencies()) {
            assertNotNull(agency);
        }
    }

    @Test
    void testGetRequestCorrectID() throws FailedOperationException, DaoException, IncorrectDataException {
        RequestItineraryController controller = new RequestItineraryController();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        String sessionID = SessionManager.getInstance().addSession(accountDao.getAccount("userTest"));

        String requestId = "id1234";
        RequestBean request = controller.getRequest(requestId,sessionID);
        assertNotNull(request);
    }

    @Test
    void testGetRequestIncorrectID() throws DaoException {
        RequestItineraryController controller = new RequestItineraryController();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        String sessionID = SessionManager.getInstance().addSession(accountDao.getAccount("userTest"));

        String requestId = "id0000";
        String errorMess = "";

        try {
            controller.getRequest(requestId,sessionID);
        } catch (FailedOperationException | IncorrectDataException e) {
            errorMess = e.getMessage();
        }

        Assertions.assertEquals("Get Request operation failed", errorMess);
    }



    @Test
    void testSaveRequestCorrect() throws FailedOperationException, DuplicateItemException, DaoException {
        RequestItineraryController controller = new RequestItineraryController();

        CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
        AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        RequestDao requestDao = Persistence.getInstance().getFactory().getRequestDao();

        City city = cityDao.createCity("r41485");
        if (cityDao.getCity("r41485") == null) {
            cityDao.addCity(city);
        }

        Attraction attraction = attractionDao.createAttraction("r1849830");
        if (attractionDao.getAttraction("r1849830") == null) {
            attractionDao.addAttraction(attraction);
        }

        Request request = new BaseRequest();
        request.setId("id5678");
        request.setCities(List.of(city));
        request.setAttractions(List.of(attraction));
        request.setDayNum(1);
        request.setOtherRequest("other requests");
        request.setAccommodationRequest(true);
        request.setFlightRequest(true);
        request.setItineraryType(List.of(CULTURE));
        request.setAccepted("accepted");

        Agency agency = (Agency) accountDao.getAccount("agencyTest");

        String sessionID = SessionManager.getInstance().addSession(accountDao.getAccount("userTest"));
        SessionManager.getInstance().getSession(sessionID).setPendingAgencies(List.of(agency));
        SessionManager.getInstance().getSession(sessionID).setPendingRequest(request);

        controller.saveRequest(sessionID);

        assertNotNull(requestDao.getRequest(request.getId()));
    }

    @Test
    void testSaveRequestDuplicate() throws FailedOperationException, DaoException {
        RequestItineraryController controller = new RequestItineraryController();

        CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
        AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

        City city = cityDao.createCity("r41485");
        if (cityDao.getCity("r41485") == null) {
            cityDao.addCity(city);
        }

        Attraction attraction = attractionDao.createAttraction("r1849830");
        if (attractionDao.getAttraction("r1849830") == null) {
            attractionDao.addAttraction(attraction);
        }

        Request request = new BaseRequest();
        request.setId("id1234");
        request.setCities(List.of(city));
        request.setAttractions(List.of(attraction));
        request.setDayNum(1);
        request.setOtherRequest("other requests");
        request.setAccommodationRequest(true);
        request.setFlightRequest(true);
        request.setItineraryType(List.of(CULTURE));
        request.setAccepted("accepted");

        Agency agency = (Agency) accountDao.getAccount("agencyTest");

        String sessionID = SessionManager.getInstance().addSession(accountDao.getAccount("userTest"));
        SessionManager.getInstance().getSession(sessionID).setPendingAgencies(List.of(agency));
        SessionManager.getInstance().getSession(sessionID).setPendingRequest(request);

        String errorMess = "";

        try {
            controller.saveRequest(sessionID);
        } catch (DuplicateItemException e) {
            errorMess = e.getMessage();
        }
        Assertions.assertEquals("Duplicate item", errorMess);
    }

    @BeforeAll
    static void provideRequest() throws IncorrectDataException, DaoException {
        Persistence.getInstance().setType(Persistence.IN_MEMORY);
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
        AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
        RequestDao requestDao = Persistence.getInstance().getFactory().getRequestDao();

        CredentialsBean agencyCredentialsBean = new CredentialsBean();
        agencyCredentialsBean.setUsername("agencyTest");
        agencyCredentialsBean.setPassword("passwordA");
        agencyCredentialsBean.setType(AGENCY);
        agencyCredentialsBean.setDescription("agency description");
        agencyCredentialsBean.setPaymentCredentials("1234567890123456");
        agencyCredentialsBean.setPreferences(List.of(CULTURE));
        if (accountDao.getAccount("agencyTest") == null) {
            accountDao.addAccount(agencyCredentialsBean);
        }

        CredentialsBean userCredentialsBean = new CredentialsBean();
        userCredentialsBean.setUsername("userTest");
        userCredentialsBean.setPassword("passwordU");
        userCredentialsBean.setType(USER);
        userCredentialsBean.setPaymentCredentials("1234567890123456");
        if (accountDao.getAccount("userTest") == null) {
            accountDao.addAccount(userCredentialsBean);
        }

        City city = cityDao.createCity("r41485");
        if (cityDao.getCity("r41485") == null) {
            cityDao.addCity(city);
        }

        Attraction attraction = attractionDao.createAttraction("r1849830");
        if (attractionDao.getAttraction("r1849830") == null) {
            attractionDao.addAttraction(attraction);
        }

        Request request = new BaseRequest();
        request.setId("id1234");
        request.setCities(List.of(city));
        request.setAttractions(List.of(attraction));
        request.setDayNum(1);
        request.setOtherRequest("other requests");
        request.setAccommodationRequest(true);
        request.setFlightRequest(true);
        request.setItineraryType(List.of(CULTURE));
        request.setAccepted("accepted");
        requestDao.addAgencyRequest(request, (User) accountDao.getAccount("userTest"),List.of((Agency) accountDao.getAccount("agencyTest")));

    }
}