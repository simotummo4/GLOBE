package it.uniroma2.ispw.globe.dao.fs;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import it.uniroma2.ispw.globe.dao.FlightDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Flight;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InFSFlightDao extends FlightDao {
    private static final String FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/flight.csv";

    @Override
    public void addFlight(Flight flight) throws DaoException {
        if (getFlight(flight.getId()) != null ) {
            throw new DaoException("addFlight: ", DUPLICATE);
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(flight));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    @Override
    public Flight getFlight(String flightID) throws DaoException {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(flightID)) {
                    return fromCsv(nextLine);
                }
            }
            return null;
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public String[] toCSV(Flight flight) {
        return new String[] {flight.getId(), String.valueOf(flight.getDepartureTime()), String.valueOf(flight.getArrivalTime())};
    }

    public Flight fromCsv(String[] flightCsv) {
        Flight flight = new Flight();
        flight.setId(flightCsv[0]);
        flight.setDepartureTime(Double.parseDouble(flightCsv[1]));
        flight.setArrivalTime(Double.parseDouble(flightCsv[2]));

        return flight;
    }
}
