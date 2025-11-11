package it.uniroma2.ispw.globe.dao.memory;

import it.uniroma2.ispw.globe.dao.AttractionDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Attraction;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAttractionDao extends AttractionDao {

    private static InMemoryAttractionDao instance = null;

    private List<Attraction> attractions = new ArrayList<>();

    private InMemoryAttractionDao() {}

    public static InMemoryAttractionDao getInstance() {
        if (instance == null) {
            instance = new InMemoryAttractionDao();
        }
        return instance;
    }

    @Override
    public void addAttraction(Attraction attraction) throws DaoException {
        if (getAttraction(attraction.getPlaceID()) == null) {
            attractions.add(attraction);
        }
    }

    @Override
    public Attraction getAttraction(String attractionID) throws DaoException {
        Attraction attractionResult = null;
        for (Attraction attraction : attractions) {
            if (attraction.getPlaceID().equals(attractionID)) {
                attractionResult = attraction;
            }
        }
        if (attractionResult == null) {
            attractionResult = createAttraction(attractionID);
            attractions.add(attractionResult);
        }
        return attractionResult;
    }
}
