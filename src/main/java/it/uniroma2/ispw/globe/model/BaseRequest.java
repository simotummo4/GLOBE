package it.uniroma2.ispw.globe.model;

import java.util.List;

public class BaseRequest implements Request {
    private String id;
    private String accepted;
    private String otherRequest;
    private Boolean flightRequest;
    private Boolean accommodationRequest;
    private int dayNum;
    private List<City> cities;
    private List<Attraction> attractions;
    private List<String> itineraryType;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getAccepted() {
        return accepted;
    }

    @Override
    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    @Override
    public String getOtherRequest() {
        return otherRequest;
    }

    @Override
    public void setOtherRequest(String otherRequest) {
        this.otherRequest = otherRequest;
    }

    @Override
    public Boolean getFlightRequest() {
        return flightRequest;
    }

    @Override
    public void setFlightRequest(Boolean flightRequest) {
        this.flightRequest = flightRequest;
    }

    @Override
    public Boolean getAccommodationRequest() {
        return accommodationRequest;
    }

    @Override
    public void setAccommodationRequest(Boolean accommodationRequest) {
        this.accommodationRequest = accommodationRequest;
    }

    @Override
    public int getDayNum() {
        return dayNum;
    }

    @Override
    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    @Override
    public List<City> getCities() {
        return cities;
    }

    @Override
    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public List<Attraction> getAttractions() {
        return attractions;
    }

    @Override
    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    @Override
    public List<String> getItineraryType() {
        return itineraryType;
    }

    @Override
    public void setItineraryType(List<String> itineraryType) {
        this.itineraryType = itineraryType;
    }
}
