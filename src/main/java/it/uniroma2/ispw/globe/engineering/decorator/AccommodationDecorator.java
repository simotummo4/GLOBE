package it.uniroma2.ispw.globe.engineering.decorator;

import it.uniroma2.ispw.globe.model.Accommodation;
import it.uniroma2.ispw.globe.model.Itinerary;

import java.util.List;

public class AccommodationDecorator extends ItineraryDecorator {

    private List<Accommodation> accommodations;

    public AccommodationDecorator(Itinerary itinerary) {
        super(itinerary);
    }

    public List<Accommodation> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }
}
