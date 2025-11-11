package it.uniroma2.ispw.globe.dao.db;

import it.uniroma2.ispw.globe.dao.ItineraryDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.engineering.decorator.AccommodationDecorator;
import it.uniroma2.ispw.globe.engineering.decorator.FlightDecorator;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.engineering.decorator.ItineraryDecorator;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InDbItineraryDao extends ItineraryDao {

    @Override
    public void addItinerary(Itinerary itinerary, Account account) throws DaoException {
        if (account != null) {
            DBConnection connect = DBConnection.getInstance();

            String query = "insert into Itinerary (itineraryID,name,description,daysNumber,photo) values (?,?,?,?,?)";
            String finalQuery = "insert into accountItinerary (account,itineraryID) values (?,?)";

            PreparedStatement stmt = null;
            PreparedStatement finalStmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, itinerary.getItineraryID());
                stmt.setString(2, itinerary.getName());
                stmt.setString(3, itinerary.getDescription());
                stmt.setInt(4, itinerary.getDaysNumber());

                File photoFile = itinerary.getPhotoFile();
                if (photoFile != null) {
                    stmt.setString(5, photoFile.getAbsolutePath());
                } else {
                    stmt.setString(5, null);
                }

                stmt.execute();

                addDecorationsData(itinerary);

                addDays(itinerary);

                addTypes(itinerary);

                account.getItineraries().add(itinerary);

                finalStmt = connection.prepareStatement(finalQuery);
                finalStmt.setString(1,account.getUsername());
                finalStmt.setString(2, itinerary.getItineraryID());
                finalStmt.execute();


            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    throw new DaoException("addItinerary: "+ e.getMessage(), DUPLICATE);
                }
                throw new DaoException("addItinerary: " + e.getMessage(), GENERAL);
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
                DBConnection.getInstance().closeConnection(finalStmt,null);
            }
        }
    }

    @Override
    public Itinerary getItinerary(String id) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Itinerary.itineraryID, Itinerary.name, Itinerary.description, Itinerary.daysNumber, Itinerary.inFlight, Itinerary.outFlight, Itinerary.photo from Itinerary where itineraryID = ?";
        String dayQuery = "select Day.itineraryID, Day.dayNum from Day where itineraryID = ?";
        String accommodationQuery = "select itineraryAccommodation.accommodationID from itineraryAccommodation where itineraryID = ?";
        String typeQuery = "select itineraryType.type from itineraryType where itineraryID = ?";

        PreparedStatement stmt = null;
        PreparedStatement dayStmt = null;
        PreparedStatement accommodationStmt = null;
        PreparedStatement typeStmt = null;

        ResultSet resultSet= null;
        ResultSet otherResultSet= null;

        Itinerary itinerary = null;

        try {
            Connection connection = connect.getConnection();

            stmt = connection.prepareStatement(query);

            stmt.setString(1, id);
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {

                List<Day> days = new ArrayList<>();
                List<Accommodation> accommodations = new ArrayList<>();
                List<String> types = new ArrayList<>();

                dayStmt = connection.prepareStatement(dayQuery);

                dayStmt.setString(1, id);
                otherResultSet = dayStmt.executeQuery();

                while (otherResultSet.next()) {
                    InDbDayDao dayDao = new InDbDayDao();
                    Day day = dayDao.getDay(otherResultSet.getString("itineraryID"),otherResultSet.getInt("dayNum"));
                    days.add(day);
                }

                accommodationStmt = connection.prepareStatement(accommodationQuery);

                accommodationStmt.setString(1, id);
                otherResultSet = accommodationStmt.executeQuery();

                while (otherResultSet.next()) {
                    InDbAccommodationDao accommodationDao = new InDbAccommodationDao();
                    Accommodation accommodation = accommodationDao.getAccommodation(otherResultSet.getString("accommodationID"));
                    accommodations.add(accommodation);
                }

                typeStmt = connection.prepareStatement(typeQuery);

                typeStmt.setString(1, id);
                otherResultSet = typeStmt.executeQuery();

                while (otherResultSet.next()) {
                    types.add(otherResultSet.getString("type"));
                }

                itinerary = new BaseItinerary();

                itinerary.setItineraryID(resultSet.getString("itineraryID"));
                itinerary.setName(resultSet.getString("name"));
                itinerary.setDescription(resultSet.getString("description"));
                itinerary.setDaysNumber(resultSet.getInt("daysNumber"));
                itinerary.setDays(days);
                itinerary.setTypes(types);

                String photoUrl = resultSet.getString("photo");
                if (photoUrl != null) {
                    itinerary.setPhotoFile(new File(photoUrl));
                }

                if (!accommodations.isEmpty()) {
                    AccommodationDecorator accommodationItinerary = new AccommodationDecorator(itinerary);
                    accommodationItinerary.setAccommodations(accommodations);
                    itinerary = accommodationItinerary;
                }

                if (resultSet.getString("inFlight")!=null) {
                    InDbFlightDao flightDao = new InDbFlightDao();
                    Flight inFlight = flightDao.getFlight(resultSet.getString("inFlight"));
                    Flight outFlight = flightDao.getFlight(resultSet.getString("outFlight"));

                    FlightDecorator flightItinerary = new FlightDecorator(itinerary);
                    flightItinerary.setInFlight(inFlight);
                    flightItinerary.setOutFlight(outFlight);
                    itinerary = flightItinerary;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("addItinerary: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
            DBConnection.getInstance().closeConnection(dayStmt,otherResultSet);
            DBConnection.getInstance().closeConnection(accommodationStmt,null);
            DBConnection.getInstance().closeConnection(typeStmt,null);
        }

        return itinerary;
    }

    @Override
    public void addPhotoFile(File file, String itineraryID) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "update Itinerary set photo = ? where itineraryID = ?";

        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, file.getAbsolutePath());
            stmt.setString(2, itineraryID);

            stmt.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new DaoException("addPhotoFile: "+ e.getMessage(), DUPLICATE);
            }
            throw new DaoException("addPhotoFile: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }

    public void addDecorationsData(Itinerary itinerary) throws DaoException {

        Itinerary current = itinerary;

        while (current instanceof ItineraryDecorator itineraryDecorator) {
            if (current instanceof AccommodationDecorator accommodationDecorator) {
                addAccommodations(accommodationDecorator);
            }
            if (current instanceof FlightDecorator flightDecorator) {
                addFlight(flightDecorator);
            }
            current = itineraryDecorator.getItinerary();
        }
    }

    public void addAccommodations(AccommodationDecorator itinerary) throws DaoException {
        for (Accommodation accommodation : itinerary.getAccommodations()) {
            addAccommodation(accommodation,itinerary.getItineraryID());
        }
    }

    public void addAccommodation(Accommodation accommodation, String itineraryID) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        Connection connection = connect.getConnection();

        String accommodationQuery = "insert into itineraryAccommodation (itineraryID,accommodationID) values (?,?)";
        PreparedStatement accommodationStmt = null;

        InDbAccommodationDao accommodationDao = new InDbAccommodationDao();
        accommodationDao.addAccommodation(accommodation);

        try {
            accommodationStmt = connection.prepareStatement(accommodationQuery);

            accommodationStmt.setString(1, itineraryID);
            accommodationStmt.setString(2, accommodation.getId());
            accommodationStmt.execute();
        } catch (SQLException e) {
            throw new DaoException("addAccommodation: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(accommodationStmt, null);
        }
    }

    public void addFlight(FlightDecorator itinerary) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        Connection connection = connect.getConnection();

        String flightQuery = "update Itinerary set inFlight = ?, outFlight = ? where itineraryID = ?";
        PreparedStatement flightStmt = null;

        try {
            flightStmt = connection.prepareStatement(flightQuery);

            InDbFlightDao flightDao = new InDbFlightDao();

            flightDao.addFlight(itinerary.getInFlight());
            flightDao.addFlight(itinerary.getOutFlight());

            flightStmt.setString(1, itinerary.getInFlight().getId());
            flightStmt.setString(2, itinerary.getOutFlight().getId());
            flightStmt.setString(3, itinerary.getItineraryID());
            flightStmt.execute();
        } catch (SQLException e) {
            throw new DaoException("addFlight: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(flightStmt, null);
        }
    }

    public void addDays(Itinerary itinerary) throws DaoException {
        for (Day day : itinerary.getDays()) {
            InDbDayDao dayDao = new InDbDayDao();
            dayDao.addDay(day);
        }
    }

    public void addTypes(Itinerary itinerary) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        Connection connection = connect.getConnection();

        String typesQuery = "insert into ItineraryType (itineraryID,type) values (?,?)";
        PreparedStatement typesStmt = null;

        try {
            typesStmt = connection.prepareStatement(typesQuery);
            for (String type : itinerary.getTypes()) {
                typesStmt.setString(1, itinerary.getItineraryID());
                typesStmt.setString(2, type);
                typesStmt.addBatch();
            }
            typesStmt.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("addTypes: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(typesStmt, null);
        }
    }
}
