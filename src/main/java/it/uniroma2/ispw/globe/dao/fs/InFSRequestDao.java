package it.uniroma2.ispw.globe.dao.fs;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import it.uniroma2.ispw.globe.dao.RequestDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.engineering.decorator.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InFSRequestDao extends RequestDao {
    private static final String FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/request.csv";


    @Override
    public void addAgencyRequest(Request request, User user, List<Agency> agencies) throws DaoException {
        InFSCityDao cityDao = new InFSCityDao();
        InFSAttractionDao attractionDao = new InFSAttractionDao();

        if (getRequest(request.getId()) != null) {
            throw new DaoException("addAgencyRequest", DUPLICATE);
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(request));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }

        for (City city : request.getCities()) {
            cityDao.addCity(city);
        }
        for (Attraction attraction : request.getAttractions()) {
            attractionDao.addAttraction(attraction);
        }

        user.getRequests().add(request);
        updateAccount(user);
        for (Agency agency : agencies) {
            agency.getRequests().add(request);
            updateAccount(agency);
        }
    }

    @Override
    public Request getRequest(String requestId) throws DaoException {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(requestId)) {
                    return fromCsv(nextLine);
                }
            }
            return null;
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    @Override
    public void updateRequest(Request request) throws DaoException {
        String[] requestCsv = toCSV(getRequest(request.getId()));
        List<String[]> allRows;

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            allRows = reader.readAll();
        } catch (CsvException | IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }

        for (int i = 0; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row[0].equals(request.getId())) {
                allRows.set(i, requestCsv);
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

        StringBuilder requestCsv = new StringBuilder();

        for (Request request : account.getRequests()) {
            requestCsv.append(request.getId()).append(";");
        }
        requestCsv.setLength(requestCsv.length() - 1);

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            allRows = reader.readAll();
        } catch (CsvException | IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }

        for (int i = 0; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row[0].equals(account.getUsername())) {
                row[6] = requestCsv.toString();
                allRows.set(i, row);
                break;
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeAll(allRows);
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public String[] toCSV(Request request) {
        StringBuilder typesCsv = new StringBuilder();
        StringBuilder cityCsv = new StringBuilder();
        StringBuilder attractionCsv = new StringBuilder();
        String trekkingDifficulty = "";
        double trekkingDistance = 0;
        String travelMode = "";
        double travelHours = 0;

        if (request.getItineraryType() != null && !request.getItineraryType().isEmpty()) {
            typesCsv = typesToCsv(request.getItineraryType());
        }
        if (request.getCities() != null && !request.getCities().isEmpty()) {
            for (City city : request.getCities()) {
                cityCsv.append(city.getPlaceID()).append(";");
            }
            cityCsv.setLength(cityCsv.length() - 1);
        }
        if (request.getAttractions() != null && !request.getAttractions().isEmpty()) {
            for (Attraction attraction : request.getAttractions()) {
                attractionCsv.append(attraction.getPlaceID()).append(";");
            }
            attractionCsv.setLength(attractionCsv.length() - 1);
        }

        Request current = request;

        while (current instanceof RequestDecorator requestDecorator) {

            if (current instanceof NatureRequestDecorator natureRequestDecorator) {
                trekkingDifficulty = natureRequestDecorator.getTrekkingDifficulty();
                trekkingDistance = natureRequestDecorator.getTrekkingDistance();
            }
            if (current instanceof OnTheRoadRequestDecorator onTheRoadRequestDecorator) {
                travelMode = onTheRoadRequestDecorator.getTravelMode();
                travelHours = onTheRoadRequestDecorator.getDayDrivingHours();
            }
            current = requestDecorator.getRequest();
        }

        return new String[] {request.getId(),request.getAccepted(),request.getOtherRequest(),String.valueOf(request.getFlightRequest()), String.valueOf(request.getAccommodationRequest()),String.valueOf(request.getDayNum()),cityCsv.toString(),attractionCsv.toString(),typesCsv.toString(),String.valueOf(trekkingDistance),trekkingDifficulty,travelMode,String.valueOf(travelHours)};
    }

    public Request fromCsv(String[] requestCsv) throws DaoException {
        InFSCityDao cityDao = new InFSCityDao();
        InFSAttractionDao attractionDao = new InFSAttractionDao();

        Request request = new BaseRequest();

        request.setId(requestCsv[0]);
        request.setAccepted(requestCsv[1]);
        request.setOtherRequest(requestCsv[3]);
        request.setFlightRequest(Boolean.valueOf(requestCsv[4]));
        request.setAccommodationRequest(Boolean.valueOf(requestCsv[5]));
        request.setDayNum(Integer.parseInt(requestCsv[6]));

        List<City> cities = new ArrayList<>();
        for (String cityID: requestCsv[7].split(";")) {
            cities.add(cityDao.getCity(cityID));
        }
        request.setCities(cities);

        List<Attraction> attractions = new ArrayList<>();
        for (String attractionID: requestCsv[8].split(";")) {
            attractions.add(attractionDao.getAttraction(attractionID));
        }
        request.setAttractions(attractions);

        List<String> types = new ArrayList<>(Arrays.asList(requestCsv[9].split(";")));
        request.setItineraryType(types);

        if (requestCsv[11] != null && !requestCsv[11].isEmpty()) {
            NatureRequestDecorator natureRequest = new NatureRequestDecorator(request);
            natureRequest.setTrekkingDistance(Double.parseDouble(requestCsv[10]));
            natureRequest.setTrekkingDifficulty(requestCsv[11]);
            request = natureRequest;
        }
        if (requestCsv[12] != null && !requestCsv[12].isEmpty()) {
            OnTheRoadRequestDecorator onTheRoadRequest = new OnTheRoadRequestDecorator(request);
            onTheRoadRequest.setTravelMode(requestCsv[12]);
            onTheRoadRequest.setDayDrivingHours(Double.parseDouble(requestCsv[13]));
            request = onTheRoadRequest;
        }

        return request;
    }

    public StringBuilder typesToCsv(List<String> types) {
        StringBuilder typesCsv = new StringBuilder();
        for (String type : types) {
            typesCsv.append(type).append(";");
        }
        typesCsv.setLength(typesCsv.length() - 1);
        return typesCsv;
    }
}
