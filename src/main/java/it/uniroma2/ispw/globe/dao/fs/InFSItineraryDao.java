package it.uniroma2.ispw.globe.dao.fs;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import it.uniroma2.ispw.globe.dao.ItineraryDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.engineering.decorator.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InFSItineraryDao extends ItineraryDao {
    private static final String FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/itinerary.csv";
    private static final String ACCOUNT_FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/account.csv";

    @Override
    public void addItinerary(Itinerary itinerary, Account account) throws DaoException {
        InFSDayDao dayDao = new InFSDayDao();
        InFSAccommodationDao accommodationDao = new InFSAccommodationDao();
        InFSFlightDao flightDao = new InFSFlightDao();

        if (getItinerary(itinerary.getItineraryID()) != null ) {
            throw new DaoException("addItinerary: ", DUPLICATE);
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(itinerary));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }

        for (Day day : itinerary.getDays()) {
            dayDao.addDay(day);
        }

        Itinerary current = itinerary;

        while (current instanceof ItineraryDecorator itineraryDecorator) {
            if (current instanceof AccommodationDecorator accommodationDecorator) {
                for (Accommodation accommodation : accommodationDecorator.getAccommodations()) {
                    accommodationDao.addAccommodation(accommodation);
                }
            }
            if (current instanceof FlightDecorator flightDecorator) {
                flightDao.addFlight(flightDecorator.getInFlight());
                flightDao.addFlight(flightDecorator.getOutFlight());
            }
            current = itineraryDecorator.getItinerary();
        }

        account.getItineraries().add(itinerary);
        updateAccount(account);
    }

    @Override
    public Itinerary getItinerary(String id) throws DaoException {
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

    @Override
    public void addPhotoFile(File file, String itineraryID) throws DaoException {
        Itinerary itinerary = getItinerary(itineraryID);
        itinerary.setPhotoFile(file);

        String[] itineraryCsv = toCSV(itinerary);
        List<String[]> allRows;

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            allRows = reader.readAll();
        } catch (CsvException | IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }

        for (int i = 0; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row[0].equals(itineraryID)) {
                allRows.set(i, itineraryCsv);
                break;
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeAll(allRows);
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public void updateAccount(Account account) throws DaoException {
        List<String[]> allRows;

        StringBuilder itineraryCsv = new StringBuilder();
        for (Itinerary accountItinerary : account.getItineraries()) {
            itineraryCsv.append(accountItinerary.getItineraryID()).append(";");
        }
        itineraryCsv.setLength(itineraryCsv.length() - 1);

        try (CSVReader reader = new CSVReader(new FileReader(ACCOUNT_FILE_PATH))) {
            allRows = reader.readAll();
        } catch (CsvException | IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }

        for (int i = 0; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row[0].equals(account.getUsername())) {
                row[4] = itineraryCsv.toString();
                allRows.set(i, row);
                break;
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(ACCOUNT_FILE_PATH, true))) {
            writer.writeAll(allRows);
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public String[] toCSV(Itinerary itinerary) {
        StringBuilder typesCsv = new StringBuilder();
        StringBuilder accommodationsCsv = new StringBuilder();
        String inFlight = "";
        String outFlight = "";
        String photoPath = null;

        if (itinerary.getTypes() != null && !itinerary.getTypes().isEmpty()) {
            for (String type : itinerary.getTypes()) {
                typesCsv.append(type).append(";");
            }
            typesCsv.setLength(typesCsv.length() - 1);
        }

        Itinerary current = itinerary;

        while (current instanceof ItineraryDecorator itineraryDecorator) {
            if (current instanceof AccommodationDecorator accommodationDecorator) {
                for (Accommodation accommodation : accommodationDecorator.getAccommodations()) {
                    accommodationsCsv.append(accommodation.getId()).append(";");
                }
                accommodationsCsv.setLength(accommodationsCsv.length() - 1);
            }
            if (current instanceof FlightDecorator flightDecorator) {
                inFlight = flightDecorator.getInFlight().getId();
                outFlight = flightDecorator.getOutFlight().getId();
            }
            current = itineraryDecorator.getItinerary();
        }
        if (itinerary.getPhotoFile() != null) {
            photoPath = itinerary.getPhotoFile().getAbsolutePath();
        }

        return new String[] {itinerary.getItineraryID(),itinerary.getName(),itinerary.getDescription(),String.valueOf(itinerary.getDaysNumber()),typesCsv.toString(),photoPath,accommodationsCsv.toString(),inFlight,outFlight};
    }

    public Itinerary fromCsv(String[] itineraryCsv) throws DaoException {
        InFSDayDao dayDao = new InFSDayDao();
        InFSAccommodationDao accommodationDao = new InFSAccommodationDao();
        InFSFlightDao flightDao = new InFSFlightDao();

        Itinerary itinerary = new BaseItinerary();

        itinerary.setItineraryID(itineraryCsv[0]);
        itinerary.setName(itineraryCsv[1]);
        itinerary.setDescription(itineraryCsv[2]);
        itinerary.setDaysNumber(Integer.parseInt(itineraryCsv[3]));
        List<Day> days = new ArrayList<>();
        for (int i = 0; i <= Integer.parseInt(itineraryCsv[3]); i++) {
            days.add(dayDao.getDay(itineraryCsv[0],i));
        }
        itinerary.setDays(days);
        List<String> types = new ArrayList<>(Arrays.asList(itineraryCsv[4].split(";")));
        itinerary.setTypes(types);
        itinerary.setPhotoFile(new File(itineraryCsv[5]));
        if (itineraryCsv[6] != null && !itineraryCsv[6].isEmpty()) {
            AccommodationDecorator accommodationItinerary = new AccommodationDecorator(itinerary);
            List<Accommodation> accommodations = new ArrayList<>();
            for (String accommodationID : itineraryCsv[6].split(";")) {
                accommodations.add(accommodationDao.getAccommodation(accommodationID));
            }
            accommodationItinerary.setAccommodations(accommodations);
            itinerary = accommodationItinerary;
        }
        if (itineraryCsv[7] != null && !itineraryCsv[7].isEmpty()) {
            FlightDecorator flightItinerary = new FlightDecorator(itinerary);
            flightItinerary.setInFlight(flightDao.getFlight(itineraryCsv[7]));
            flightItinerary.setOutFlight(flightDao.getFlight(itineraryCsv[8]));
            itinerary = flightItinerary;
        }

        return itinerary;
    }
}
