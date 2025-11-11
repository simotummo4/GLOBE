package it.uniroma2.ispw.globe.dao.memory;

import it.uniroma2.ispw.globe.dao.AccommodationDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Accommodation;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;

public class InMemoryAccommodationDao extends AccommodationDao {

    private static InMemoryAccommodationDao instance = null;

    private List<Accommodation> accommodations = new ArrayList<>();

    private InMemoryAccommodationDao() {}

    public static InMemoryAccommodationDao getInstance() {
        if (instance == null) {
            instance = new InMemoryAccommodationDao();
        }
        return instance;
    }

    @Override
    public void addAccommodation(Accommodation accommodation) throws DaoException {
        if (getAccommodation(accommodation.getId()) == null) {
            accommodations.add(accommodation);
        } else {
            throw new DaoException("addAccommodation", DUPLICATE);
        }
    }

    @Override
    public Accommodation getAccommodation(String id) {
        for (Accommodation a : accommodations) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }
}
