package it.uniroma2.ispw.globe.dao.db;

import it.uniroma2.ispw.globe.dao.DayDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InDbDayDao extends DayDao {

    @Override
    public void addDay(Day day) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "insert into Day (dayNum,itineraryID) values (?,?)";
        String attractionQuery = "insert into dayAttraction (dayNum,itineraryID,attractionID) values (?,?,?)";
        String cityQuery = "insert into dayCity (dayNum,itineraryID,cityID) values (?,?,?)";

        PreparedStatement stmt = null;
        PreparedStatement attractionStmt = null;
        PreparedStatement cityStmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(2, day.getId());
            stmt.setInt(1, day.getDayNum());
            stmt.execute();

            attractionStmt = connection.prepareStatement(attractionQuery);

            for (Attraction attraction : day.getAttractions()) {
                InDbAttractionDao attractionDao = new InDbAttractionDao();
                attractionDao.addAttraction(attraction);

                attractionStmt.setInt(1, day.getDayNum());
                attractionStmt.setString(2, day.getId());
                attractionStmt.setString(3, attraction.getPlaceID());
                attractionStmt.addBatch();
            }
            attractionStmt.executeBatch();

            cityStmt = connection.prepareStatement(cityQuery);

            for (City city : day.getCities()) {
                InDbCityDao cityDao = new InDbCityDao();
                cityDao.addCity(city);

                cityStmt.setString(2, day.getId());
                cityStmt.setInt(1, day.getDayNum());
                cityStmt.setString(3, city.getPlaceID());
                cityStmt.addBatch();
            }
            cityStmt.executeBatch();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new DaoException("addDay: "+ e.getMessage(), DUPLICATE);
            }
            throw new DaoException("addDay: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
            DBConnection.getInstance().closeConnection(attractionStmt,null);
            DBConnection.getInstance().closeConnection(cityStmt,null);
        }
    }

    @Override
    public Day getDay(String itineraryID, int dayNum) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Day.itineraryID, Day.dayNum from Day where itineraryID = ? and dayNum = ?";
        String attractionQuery = "select dayAttraction.attractionID from dayAttraction where itineraryID = ? and dayNum = ?";
        String cityQuery = "select dayCity.cityID from dayCity where itineraryID = ? and dayNum = ?";

        PreparedStatement stmt = null;
        PreparedStatement attractionStmt = null;
        PreparedStatement cityStmt = null;
        ResultSet resultSet= null;

        Day day = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, itineraryID);
            stmt.setInt(2, dayNum);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                day = new Day();
                List<Attraction> attractions = new ArrayList<>();
                List<City> cities = new ArrayList<>();

                day.setId(resultSet.getString("itineraryID"));
                day.setDayNum(resultSet.getInt("dayNum"));

                attractionStmt = connection.prepareStatement(attractionQuery);

                attractionStmt.setString(1, itineraryID);
                attractionStmt.setInt(2, dayNum);

                resultSet = attractionStmt.executeQuery();

                while (resultSet.next()) {
                    InDbAttractionDao attractionDao = new InDbAttractionDao();
                    Attraction attraction = attractionDao.getAttraction(resultSet.getString("attractionID"));
                    attractions.add(attraction);
                }

                cityStmt = connection.prepareStatement(cityQuery);

                cityStmt.setString(1, itineraryID);
                cityStmt.setInt(2, dayNum);

                resultSet = cityStmt.executeQuery();

                while (resultSet.next()) {
                    InDbCityDao cityDao = new InDbCityDao();
                    City city = cityDao.getCity(resultSet.getString("cityID"));
                    cities.add(city);
                }

                day.setAttractions(attractions);
                day.setCities(cities);
            }
        } catch (SQLException e) {
            throw new DaoException("getDayNum: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
            DBConnection.getInstance().closeConnection(attractionStmt,null);
            DBConnection.getInstance().closeConnection(cityStmt,null);
        }

        return day;
    }
}
