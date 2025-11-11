package it.uniroma2.ispw.globe.dao;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.Request;

import java.util.ArrayList;
import java.util.List;

public abstract class RequestDao {

    public Request createRequest(String id, String accepted, String otherRequest, int dayNum, boolean flight, boolean accommodation, List<String> itineraryType) {

        BaseRequest request = new BaseRequest();

        request.setId(id);
        request.setAccepted(accepted);
        request.setOtherRequest(otherRequest);
        request.setDayNum(dayNum);
        request.setFlightRequest(flight);
        request.setAccommodationRequest(accommodation);
        request.setItineraryType(itineraryType);
        request.setCities(new ArrayList<>());
        request.setAttractions(new ArrayList<>());

        return request;
    }

    public abstract void addAgencyRequest(Request request, User user, List<Agency> agency) throws DaoException;
    public abstract Request getRequest(String requestId) throws DaoException;
    public abstract void updateRequest(Request request) throws DaoException;
}
