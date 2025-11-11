package it.uniroma2.ispw.globe.dao.fs;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import it.uniroma2.ispw.globe.dao.DayDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Day;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InFSDayDao extends DayDao {
    private static final String FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/day.csv";

    @Override
    public void addDay(Day day) throws DaoException {
        InFSCityDao cityDao = new InFSCityDao();
        InFSAttractionDao attractionDao = new InFSAttractionDao();

        if (getDay(day.getId(),day.getDayNum()) != null ) {
            throw new DaoException("addDay: ", DUPLICATE);
        }

        for (City city : day.getCities()) {
            cityDao.addCity(city);
        }
        for (Attraction attraction : day.getAttractions()) {
            attractionDao.addAttraction(attraction);
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(day));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    @Override
    public Day getDay(String itineraryID, int dayNum) throws DaoException {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(itineraryID) && nextLine[1].equals(String.valueOf(dayNum))) {
                    return fromCsv(nextLine);
                }
            }
            return null;
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public String[] toCSV(Day day) {
        StringBuilder cityCsv = new StringBuilder();
        StringBuilder attractionCsv = new StringBuilder();
        if (day.getCities() != null && !day.getCities().isEmpty()) {
            for (City city : day.getCities()) {
                cityCsv.append(city.getPlaceID()).append(";");
            }
            cityCsv.setLength(cityCsv.length() - 1);
        }
        if (day.getAttractions() != null && !day.getAttractions().isEmpty()) {
            for (Attraction attraction : day.getAttractions()) {
                attractionCsv.append(attraction.getPlaceID()).append(";");
            }
            attractionCsv.setLength(attractionCsv.length() - 1);
        }

        return new String[] {day.getId(),String.valueOf(day.getDayNum()),cityCsv.toString(),attractionCsv.toString()};
    }

    public Day fromCsv(String[] dayCsv) throws DaoException {
        InFSCityDao cityDao = new InFSCityDao();
        InFSAttractionDao attractionDao = new InFSAttractionDao();

        List<City> cities = new ArrayList<>();
        List<Attraction> attractions = new ArrayList<>();

        Day day = new Day();
        day.setId(dayCsv[0]);
        day.setDayNum(Integer.parseInt(dayCsv[1]));

        for (String cityID : dayCsv[2].split(";")) {
            cities.add(cityDao.getCity(cityID));
        }
        for (String attractionID : dayCsv[3].split(";")) {
            attractions.add(attractionDao.getAttraction(attractionID));
        }

        day.setCities(cities);
        day.setAttractions(attractions);

        return day;
    }
}
