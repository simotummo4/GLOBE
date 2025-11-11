package it.uniroma2.ispw.globe.dao;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.controller.applicationcontroller.NominatimAPIClient;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.PlaceApiException;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.engineering.adapter.PlaceAdapter;

import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public abstract class AttractionDao {
    public Attraction createAttraction(String attractionID) throws DaoException {
        JsonObject jsonAttraction;
        try {
            jsonAttraction = new NominatimAPIClient().getPlaceByID(attractionID);
        } catch (PlaceApiException e) {
            throw new DaoException("createAttraction: " + e.getMessage(), GENERAL);
        }
        return new PlaceAdapter(jsonAttraction);
    }
    public abstract void addAttraction(Attraction attraction) throws DaoException;
    public abstract Attraction getAttraction(String attractionID) throws DaoException;
}
