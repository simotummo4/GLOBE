package it.uniroma2.ispw.globe.model;

import java.io.File;
import java.util.List;

public class BaseItinerary implements Itinerary {
    private String itineraryID;
    private String name;
    private String description;
    private int daysNumber;
    private List<Day> days;
    private List<String> types;
    private File photoFile;

    @Override
    public String getItineraryID() {
        return itineraryID;
    }
    @Override
    public void setItineraryID(String itineraryID) {
        this.itineraryID = itineraryID;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public int getDaysNumber() {
        return daysNumber;
    }
    @Override
    public void setDaysNumber(int daysNumber) {
        this.daysNumber = daysNumber;
    }
    @Override
    public List<Day> getDays() {
        return days;
    }
    @Override
    public void setDays(List<Day> days) {
        this.days = days;
    }
    @Override
    public List<String> getTypes() {
        return types;
    }
    @Override
    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public File getPhotoFile() {
        return photoFile;
    }

    @Override
    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
    }


}
