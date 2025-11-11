package it.uniroma2.ispw.globe.model;

import java.io.File;
import java.util.List;

public interface Itinerary {
    String getItineraryID();

    void setItineraryID(String itineraryID);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    int getDaysNumber();

    void setDaysNumber(int daysNumber);

    List<Day> getDays();

    void setDays(List<Day> days);

    List<String> getTypes();

    void setTypes(List<String> types);

    File getPhotoFile();

    void setPhotoFile(File photoFile);
}
