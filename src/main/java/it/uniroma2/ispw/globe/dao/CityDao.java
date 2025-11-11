package it.uniroma2.ispw.globe.dao;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.controller.applicationcontroller.NominatimAPIClient;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.PlaceApiException;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.engineering.adapter.PlaceAdapter;

import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public abstract class CityDao {
    public City createCity(String cityID) throws DaoException {
        JsonObject jsonCity;
        try {
            jsonCity= new NominatimAPIClient().getPlaceByID(cityID);
        } catch (PlaceApiException e) {
            throw new DaoException("createCity: " + e.getMessage(), GENERAL);
        }
        return new PlaceAdapter(jsonCity);
    }
    public abstract void addCity(City city) throws DaoException;
    public abstract City getCity(String cityID) throws DaoException;
}
