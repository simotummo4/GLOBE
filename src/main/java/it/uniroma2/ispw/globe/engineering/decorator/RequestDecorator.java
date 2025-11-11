package it.uniroma2.ispw.globe.engineering.decorator;

import it.uniroma2.ispw.globe.model.*;

import java.util.List;

public abstract class RequestDecorator implements Request {
    private Request request;

    protected RequestDecorator(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public String getId() {
        return this.request.getId();
    }

    @Override
    public void setId(String id) {
        this.request.setId(id);
    }

    @Override
    public String getAccepted() {
        return this.request.getAccepted();
    }

    @Override
    public void setAccepted(String accepted) {
        this.request.setAccepted(accepted);
    }

    @Override
    public String getOtherRequest() {
        return this.request.getOtherRequest();
    }

    @Override
    public void setOtherRequest(String otherRequest) {
        this.request.setOtherRequest(otherRequest);
    }

    @Override
    public Boolean getFlightRequest() {
        return this.request.getFlightRequest();
    }

    @Override
    public void setFlightRequest(Boolean flightRequest) {
        this.request.setFlightRequest(flightRequest);
    }

    @Override
    public Boolean getAccommodationRequest() {
        return this.request.getAccommodationRequest();
    }

    @Override
    public void setAccommodationRequest(Boolean accommodationRequest) {
        this.request.setAccommodationRequest(accommodationRequest);
    }

    @Override
    public int getDayNum() {
        return this.request.getDayNum();
    }

    @Override
    public void setDayNum(int dayNum) {
        this.request.setDayNum(dayNum);
    }

    @Override
    public List<City> getCities() {
        return this.request.getCities();
    }

    @Override
    public void setCities(List<City> cities) {
        this.request.setCities(cities);
    }

    @Override
    public List<Attraction> getAttractions() {
        return this.request.getAttractions();
    }

    @Override
    public void setAttractions(List<Attraction> attractions) {
        this.request.setAttractions(attractions);
    }

    @Override
    public List<String> getItineraryType() {
        return this.request.getItineraryType();
    }

    @Override
    public void setItineraryType(List<String> itineraryType) {
        this.request.setItineraryType(itineraryType);
    }

}
