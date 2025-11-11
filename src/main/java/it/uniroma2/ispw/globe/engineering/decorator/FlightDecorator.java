package it.uniroma2.ispw.globe.engineering.decorator;

import it.uniroma2.ispw.globe.model.Flight;
import it.uniroma2.ispw.globe.model.Itinerary;

public class FlightDecorator extends ItineraryDecorator {

    private Flight inFlight;
    private Flight outFlight;

    public FlightDecorator(Itinerary itinerary) {
        super(itinerary);
    }

    public Flight getInFlight() {
        return inFlight;
    }

    public void setInFlight(Flight inFlight) {
        this.inFlight = inFlight;
    }

    public Flight getOutFlight() {
        return outFlight;
    }

    public void setOutFlight(Flight outFlight) {
        this.outFlight = outFlight;
    }
}
