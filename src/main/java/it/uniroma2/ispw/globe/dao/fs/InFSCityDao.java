package it.uniroma2.ispw.globe.dao.fs;

import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import it.uniroma2.ispw.globe.dao.CityDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.engineering.adapter.PlaceAdapter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InFSCityDao extends CityDao {
    private static final String FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/city.csv";

    @Override
    public void addCity(City city) throws DaoException {
        if (getCity(city.getPlaceID()) != null ) {
            return;
        }


        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(city));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    @Override
    public City getCity(String cityID) throws DaoException {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(cityID)) {
                    return fromCsv(nextLine);
                }
            }
            return null;
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public String[] toCSV(City city) {
        return new String[] {city.getPlaceID(),city.getName(),city.getCountry(), String.valueOf(city.getLatitude()), String.valueOf(city.getLongitude())};
    }

    public City fromCsv(String[] cityCsv) {

        JsonObject json = new JsonObject();

        json.addProperty("osm_type", cityCsv[0].substring(0,1));
        json.addProperty("osm_id", Integer.parseInt(cityCsv[0].substring(1)));

        json.addProperty("name", cityCsv[1]);
        json.addProperty("lat", cityCsv[3]);
        json.addProperty("lon", cityCsv[4]);

        JsonObject address = new JsonObject();
        address.addProperty("country", cityCsv[2]);
        address.addProperty("city", cityCsv[1]);

        json.add("address", address);

        return new PlaceAdapter(json);
    }
}
