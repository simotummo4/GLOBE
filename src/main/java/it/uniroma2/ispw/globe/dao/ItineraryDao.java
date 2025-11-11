package it.uniroma2.ispw.globe.dao;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.BaseItinerary;
import it.uniroma2.ispw.globe.model.Itinerary;

import java.io.File;
import java.util.List;

public abstract class ItineraryDao {
    public BaseItinerary createItinerary(String id, String name, String description, int duration, List<String> types) {
        BaseItinerary itinerary = new BaseItinerary();

        itinerary.setItineraryID(id);
        itinerary.setName(name);
        itinerary.setDescription(description);
        itinerary.setDaysNumber(duration);
        itinerary.setTypes(types);

        return itinerary;
    }
    public abstract void addItinerary(Itinerary itinerary, Account account) throws DaoException;
    public abstract Itinerary getItinerary(String itineraryName) throws DaoException;
    public abstract void addPhotoFile(File file, String itineraryID) throws DaoException;
}
