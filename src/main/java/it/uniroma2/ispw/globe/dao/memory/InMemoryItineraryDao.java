package it.uniroma2.ispw.globe.dao.memory;

import it.uniroma2.ispw.globe.dao.ItineraryDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.Itinerary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;

public class InMemoryItineraryDao extends ItineraryDao {

    private static InMemoryItineraryDao instance = null;

    private List<Itinerary> itineraries = new ArrayList<>();

    private InMemoryItineraryDao() {}

    public static InMemoryItineraryDao getInstance() {
        if (instance == null) {
            instance = new InMemoryItineraryDao();
        }
        return instance;
    }

    @Override
    public void addItinerary(Itinerary itinerary, Account account) throws DaoException {
        if (getItinerary(itinerary.getItineraryID()) == null) {
            for (Day day : itinerary.getDays()) {
                InMemoryDayDao.getInstance().addDay(day);
            }

            if (account != null) {
                itineraries.add(itinerary);
                account.getItineraries().add(itinerary);
            }
        } else {
            throw new DaoException("addItinerary", DUPLICATE);
        }
    }

    @Override
    public Itinerary getItinerary(String id) {
        for (Itinerary itinerary : itineraries) {
            if (itinerary.getItineraryID().equals(id)) {
                return itinerary;
            }
        }
        return null;
    }

    @Override
    public void addPhotoFile(File file, String itineraryID) throws DaoException {
        for (Itinerary itinerary : itineraries) {
            if (itinerary.getItineraryID().equals(itineraryID)) {
                itinerary.setPhotoFile(file);
            }
        }
    }
}
