package it.uniroma2.ispw.globe.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import javafx.util.Pair;

import java.io.File;
import java.util.List;

public class ItineraryBean {
    private String id;
    private String name;
    private String description;
    private List<String> types;
    private int duration;
    private List<String> cities;
    private List<String> attractions;
    private double outboundFlightDepartureTime;
    private double outboundFlightArrivalTime;
    private double inboundFlightDepartureTime;
    private double inboundFlightArrivalTime;
    private List<Pair<String, String>> accommodations;
    private File photo;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IncorrectDataException {
        if (name == null || name.isEmpty()) {
            throw new IncorrectDataException("Itinerary name not valid");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws IncorrectDataException {
        if (description == null || description.isEmpty()) {
            throw new IncorrectDataException("Itinerary description not valid");
        }
        this.description = description;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) throws IncorrectDataException {
        if (types == null || types.isEmpty()) {
            throw new IncorrectDataException("Itinerary type not valid");
        }
        this.types = types;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) throws IncorrectDataException {
        if (duration < 1 || duration > 99) {
            throw new IncorrectDataException("Itinerary duration not valid");
        }
        this.duration = duration;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) throws IncorrectDataException {
        if (cities == null || cities.isEmpty()) {
            throw new IncorrectDataException("Itinerary city not valid");
        }
        this.cities = cities;
    }

    public List<String> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<String> attractions) throws IncorrectDataException {
        if (attractions == null || attractions.isEmpty()) {
            throw new IncorrectDataException("Itinerary attraction not valid");
        }
        this.attractions = attractions;
    }

    public double getOutboundFlightDepartureTime() {
        return outboundFlightDepartureTime;
    }

    public void setOutboundFlightDepartureTime(double outboundFlightDepartureTime) throws IncorrectDataException {
        int hours = (int) outboundFlightDepartureTime;
        int minutes = (int) Math.round((outboundFlightDepartureTime - hours) * 100);

        if (outboundFlightDepartureTime == -1 || (hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59)) {
            this.outboundFlightDepartureTime = outboundFlightDepartureTime;
        } else {
            throw new IncorrectDataException("Itinerary outbound departure time not valid");
        }
    }

    public double getOutboundFlightArrivalTime() {
        return outboundFlightArrivalTime;
    }

    public void setOutboundFlightArrivalTime(double outboundFlightArrivalTime) throws IncorrectDataException {
        int hours = (int) outboundFlightArrivalTime;
        int minutes = (int) Math.round((outboundFlightArrivalTime - hours) * 100);

        if (outboundFlightArrivalTime == -1 || (hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59)) {
            this.outboundFlightArrivalTime = outboundFlightArrivalTime;
        } else {
            throw new IncorrectDataException("Itinerary outbound arrival time not valid");
        }
    }

    public double getInboundFlightDepartureTime() {
        return inboundFlightDepartureTime;
    }

    public void setInboundFlightDepartureTime(double inboundFlightDepartureTime) throws IncorrectDataException {
        int hours = (int) inboundFlightDepartureTime;
        int minutes = (int) Math.round((inboundFlightDepartureTime - hours) * 100);

        if (inboundFlightDepartureTime == -1 || (hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59)) {
            this.inboundFlightDepartureTime = inboundFlightDepartureTime;
        } else {
            throw new IncorrectDataException("Itinerary inbound departure time not valid");
        }
    }

    public double getInboundFlightArrivalTime() {
        return inboundFlightArrivalTime;
    }

    public void setInboundFlightArrivalTime(double inboundFlightArrivalTime) throws IncorrectDataException {
        int hours = (int) inboundFlightArrivalTime;
        int minutes = (int) Math.round((inboundFlightArrivalTime - hours) * 100);

        if (inboundFlightArrivalTime == -1 || (hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59)) {
            this.inboundFlightArrivalTime = inboundFlightArrivalTime;
        } else {
            throw new IncorrectDataException("Itinerary inbound arrival time not valid");
        }
    }

    public  List<Pair<String, String>> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations( List<Pair<String, String>> accommodations) throws IncorrectDataException {
        for (Pair<String,String> accommodation : accommodations) {
            if (!accommodation.getValue().matches("[\\p{L}\\d\\s'\\-]+") || !accommodation.getKey().matches("[\\p{L}\\d\\s,'\\.\\-]+")) {
                throw new IncorrectDataException("Accommodation not valid");
            }
        }
        this.accommodations = accommodations;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }
}