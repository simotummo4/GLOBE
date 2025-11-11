package it.uniroma2.ispw.globe.model;

import java.util.List;

public interface Request {

    String getId();
    void setId(String id);

    String getAccepted();
    void setAccepted(String accepted);

    String getOtherRequest();
    void setOtherRequest(String otherRequest);

    Boolean getFlightRequest();
    void setFlightRequest(Boolean flightRequest);

    Boolean getAccommodationRequest();
    void setAccommodationRequest(Boolean accommodationRequest);

    int getDayNum();
    void setDayNum(int dayNum);

    List<City> getCities();
    void setCities(List<City> cities);

    List<Attraction> getAttractions();
    void setAttractions(List<Attraction> attractions);

    List<String> getItineraryType();
    void setItineraryType(List<String> itineraryType);

}
