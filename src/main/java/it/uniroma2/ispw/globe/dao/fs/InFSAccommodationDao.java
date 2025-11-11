package it.uniroma2.ispw.globe.dao.fs;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import it.uniroma2.ispw.globe.dao.AccommodationDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Accommodation;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InFSAccommodationDao extends AccommodationDao {
    private static final String FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/accommodation.csv";

    @Override
    public void addAccommodation(Accommodation accommodation) throws DaoException {
        if (getAccommodation(accommodation.getId()) != null ) {
            throw new DaoException("addAccommodation: ", DUPLICATE);
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(accommodation));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    @Override
    public Accommodation getAccommodation(String id) throws DaoException {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(id)) {
                    return fromCsv(nextLine);
                }
            }
            return null;
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public String[] toCSV(Accommodation accommodation) {
        return new String[] {accommodation.getId(),accommodation.getName(),accommodation.getAddress()};
    }

    public Accommodation fromCsv(String[] accommodationCsv) {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationCsv[0]);
        accommodation.setName(accommodationCsv[1]);
        accommodation.setAddress(accommodationCsv[2]);

        return accommodation;
    }
}
