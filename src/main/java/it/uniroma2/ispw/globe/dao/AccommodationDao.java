package it.uniroma2.ispw.globe.dao;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Accommodation;

import java.util.UUID;

public abstract class AccommodationDao {
    public Accommodation createAccommodation(String name, String address) {

        String id = UUID.randomUUID().toString();
        Accommodation accommodation =  new Accommodation();
        accommodation.setId(id);
        accommodation.setName(name);
        accommodation.setAddress(address);

        return accommodation;
    }
    public abstract void addAccommodation(Accommodation accommodation) throws DaoException;
    public abstract Accommodation getAccommodation(String id) throws DaoException;
}
