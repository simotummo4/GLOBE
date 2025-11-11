package it.uniroma2.ispw.globe.dao.memory;

import it.uniroma2.ispw.globe.dao.DayDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Day;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;

public class InMemoryDayDao extends DayDao {

    private static InMemoryDayDao instance = null;

    private List<Day> days = new ArrayList<>();

    private InMemoryDayDao() {}

    public static InMemoryDayDao getInstance() {
        if (instance == null) {
            instance = new InMemoryDayDao();
        }
        return instance;
    }

    @Override
    public void addDay(Day day) throws DaoException {
        if (getDay(day.getId(), day.getDayNum()) == null) {
            for (City city : day.getCities()) {
                InMemoryCityDao.getInstance().addCity(city);
            }
            for (Attraction attraction : day.getAttractions()) {
                InMemoryAttractionDao.getInstance().addAttraction(attraction);
            }
            days.add(day);
        } else {
            throw new DaoException("addDay", DUPLICATE);
        }
    }

    @Override
    public Day getDay(String itineraryID, int dayNum) {
        for (Day day : days) {
            if (day.getId().equals(itineraryID) && day.getDayNum() == dayNum) {
                return day;
            }
        }
        return null;
    }
}
