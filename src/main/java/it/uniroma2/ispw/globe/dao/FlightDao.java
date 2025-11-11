package it.uniroma2.ispw.globe.dao;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Flight;

import java.util.UUID;

public abstract class FlightDao {
    public Flight createFlight(double departureTime, double arrivalTime) {
        Flight flight = new Flight();

        String id = UUID.randomUUID().toString();
        flight.setId(id);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);

        return flight;
    }
    public abstract void addFlight(Flight flight) throws DaoException;
    public abstract Flight getFlight(String flightID) throws DaoException;
}
