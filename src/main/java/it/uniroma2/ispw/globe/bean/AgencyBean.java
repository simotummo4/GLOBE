package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import java.util.List;

public class AgencyBean {
    private String name;
    private double rating;
    private List<String> itineraryTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) throws IncorrectDataException {
        if (name == null || name.isEmpty()) {
            throw new IncorrectDataException("Agency name not valid");
        }
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) throws IncorrectDataException {
        if (rating < 0 || rating > 5) {
            throw new IncorrectDataException("Agency rating not valid");
        }
        this.rating = rating;
    }

    public List<String> getItineraryTypes() {
        return itineraryTypes;
    }

    public void setItineraryTypes(List<String> itineraryTypes) throws IncorrectDataException {
        if (itineraryTypes == null || itineraryTypes.isEmpty()) {
            throw new IncorrectDataException("Agency itinerary types not valid");
        }
        this.itineraryTypes = itineraryTypes;
    }
}
