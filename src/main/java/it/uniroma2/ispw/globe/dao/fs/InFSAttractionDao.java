package it.uniroma2.ispw.globe.dao.fs;

import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import it.uniroma2.ispw.globe.dao.AttractionDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.engineering.adapter.PlaceAdapter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InFSAttractionDao extends AttractionDao {
    private static final String FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/attraction.csv";

    @Override
    public void addAttraction(Attraction attraction) throws DaoException {
        if (getAttraction(attraction.getPlaceID()) != null ) {
            return;
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(attraction));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    @Override
    public Attraction getAttraction(String attractionID) throws DaoException {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(attractionID)) {
                    return fromCsv(nextLine);
                }
            }
            return null;
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public String[] toCSV(Attraction attraction) {
        return new String[] {attraction.getPlaceID(),attraction.getName(),attraction.getCity(),attraction.getAddress(), String.valueOf(attraction.getLatitude()), String.valueOf(attraction.getLongitude())};
    }

    public Attraction fromCsv(String[] attractionCsv) {

        JsonObject json = new JsonObject();

        json.addProperty("osm_type", attractionCsv[0].substring(0,1));
        json.addProperty("osm_id", Integer.parseInt(attractionCsv[0].substring(1)));

        json.addProperty("name", attractionCsv[1]);
        json.addProperty("lat", attractionCsv[4]);
        json.addProperty("lon", attractionCsv[5]);

        JsonObject address = new JsonObject();
        address.addProperty("city", attractionCsv[2]);
        address.addProperty("road", attractionCsv[3]);

        json.add("address", address);

        return new PlaceAdapter(json);
    }
}
