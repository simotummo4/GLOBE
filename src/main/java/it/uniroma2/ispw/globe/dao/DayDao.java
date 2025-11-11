package it.uniroma2.ispw.globe.dao;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Day;

import java.util.ArrayList;

public abstract class DayDao {
    public Day createDay(String id, int dayNum) {
        Day day = new Day();
        day.setId(id);
        day.setDayNum(dayNum);
        day.setCities(new ArrayList<>());
        day.setAttractions(new ArrayList<>());
        return day;
    }
    public abstract void addDay(Day day) throws DaoException;
    public abstract Day getDay(String itineraryID, int dayNum) throws DaoException;
}
