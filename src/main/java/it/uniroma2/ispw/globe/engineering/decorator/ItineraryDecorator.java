package it.uniroma2.ispw.globe.engineering.decorator;

import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.Itinerary;

import java.io.File;
import java.util.List;

public abstract class ItineraryDecorator implements Itinerary {
    private Itinerary itinerary;

    protected ItineraryDecorator(Itinerary itinerary){
        this.itinerary = itinerary;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    @Override
    public String getItineraryID(){
        return this.itinerary.getItineraryID();
    }

    @Override
    public void setItineraryID(String itineraryID) {
        this.itinerary.setItineraryID(itineraryID);
    }

    @Override
    public String getName() {
        return this.itinerary.getName();
    }

    @Override
    public void setName(String name) {
        this.itinerary.setName(name);
    }

    @Override
    public String getDescription() {
        return this.itinerary.getDescription();
    }

    @Override
    public void setDescription(String description) {
        this.itinerary.setDescription(description);
    }

    @Override
    public int getDaysNumber() {
        return this.itinerary.getDaysNumber();
    }

    @Override
    public void setDaysNumber(int daysNumber) {
        this.itinerary.setDaysNumber(daysNumber);
    }

    @Override
    public List<Day> getDays() {
        return this.itinerary.getDays();
    }

    @Override
    public void setDays(List<Day> days) {
        this.itinerary.setDays(days);
    }

    @Override
    public List<String> getTypes() {
        return this.itinerary.getTypes();
    }

    @Override
    public void setTypes(List<String> types) {
        this.itinerary.setTypes(types);
    }

    @Override
    public File getPhotoFile() {
        return this.itinerary.getPhotoFile();
    }

    @Override
    public void setPhotoFile(File photoFile) {
        this.itinerary.setPhotoFile(photoFile);
    }

}
