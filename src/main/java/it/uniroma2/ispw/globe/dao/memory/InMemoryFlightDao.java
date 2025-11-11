package it.uniroma2.ispw.globe.dao.memory;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Flight;
import it.uniroma2.ispw.globe.dao.FlightDao;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;

public class InMemoryFlightDao extends FlightDao {

    private static InMemoryFlightDao instance = null;

        private List<Flight> flights = new ArrayList<>();

    private InMemoryFlightDao() {}

    public static InMemoryFlightDao getInstance() {
        if (instance == null) {
            instance = new InMemoryFlightDao();
        }
        return instance;
    }
    @Override
    public void addFlight(Flight flight) throws DaoException {
        if (getFlight(flight.getId()) == null) {
            flights.add(flight);
        } else {
            throw new DaoException("addFlight", DUPLICATE);
        }
    }

    @Override
    public Flight getFlight(String flightID) {
        for (Flight f : flights) {
            if (f.getId().equals(flightID)) {
                return f;
            }
        }
        return null;
    }
}
