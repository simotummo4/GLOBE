package it.uniroma2.ispw.globe.dao.db;

import it.uniroma2.ispw.globe.dao.RequestDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.engineering.decorator.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InDbRequestDao extends RequestDao {

    @Override
    public void addAgencyRequest(Request request, User user, List<Agency> agencies) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "insert into Request (id,accepted,description,days,flight,accommodation) values (?,?,?,?,?,?)";
        String accountQuery= "insert into accountRequest (account,requestID) values (?,?)";
        String cityQuery = "insert into requestCity (requestID,cityID) values (?,?)";
        String attractionQuery = "insert into requestAttraction (requestID,attractionID) values (?,?)";

        PreparedStatement stmt = null;
        PreparedStatement agencyStmt = null;
        PreparedStatement userStmt = null;
        PreparedStatement cityStmt = null;
        PreparedStatement attrStmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, request.getId());
            stmt.setString(2, request.getAccepted());
            stmt.setString(3, request.getOtherRequest());
            stmt.setInt(4, request.getDayNum());
            stmt.setBoolean(5,request.getFlightRequest());
            stmt.setBoolean(6,request.getAccommodationRequest());
            stmt.execute();

            addDecorationsData(request);

            userStmt = connection.prepareStatement(accountQuery);
            userStmt.setString(1, user.getUsername());
            userStmt.setString(2, request.getId());
            userStmt.execute();

            agencyStmt = connection.prepareStatement(accountQuery);
            for (Agency agency : agencies) {
                agencyStmt.setString(1, agency.getUsername());
                agencyStmt.setString(2, request.getId());
                agencyStmt.addBatch();
            }
            agencyStmt.executeBatch();

            cityStmt = connection.prepareStatement(cityQuery);
            for (City city : request.getCities()) {
                cityStmt.setString(1, request.getId());
                cityStmt.setString(2, city.getPlaceID());
                cityStmt.addBatch();
            }
            cityStmt.executeBatch();

            attrStmt = connection.prepareStatement(attractionQuery);
            for (Attraction attraction : request.getAttractions()) {
                attrStmt.setString(1, request.getId());
                attrStmt.setString(2, attraction.getPlaceID());
                attrStmt.addBatch();
            }
            attrStmt.executeBatch();

            addTypes(request);

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new DaoException("addAgencyRequest: "+ e.getMessage(), DUPLICATE);
            }
            throw new DaoException("addAgencyRequest: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
            DBConnection.getInstance().closeConnection(agencyStmt,null);
            DBConnection.getInstance().closeConnection(userStmt,null);
            DBConnection.getInstance().closeConnection(cityStmt,null);
            DBConnection.getInstance().closeConnection(attrStmt,null);
        }
    }

    @Override
    public Request getRequest(String requestId) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Request.id, Request.accepted, Request.description, Request.days, Request.flight, Request.accommodation, Request.trekkingDifficulty, Request.trekkingDistance, Request.travelMode, Request.dayDrivingHours from Request where id = ?";
        String cityQuery = "select requestCity.cityID from requestCity where requestID = ?";
        String attractionQuery = "select requestAttraction.attractionID from requestAttraction where requestID = ?";
        String typeQuery = "select requestType.type from requestType where requestID = ?";


        PreparedStatement stmt = null;
        PreparedStatement cityStmt = null;
        PreparedStatement attrStmt = null;
        PreparedStatement typeStmt = null;
        ResultSet resultSet= null;

        Request request = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, requestId);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                request = new BaseRequest();

                request.setId(resultSet.getString("id"));
                request.setAccepted(resultSet.getString("accepted"));
                request.setOtherRequest(resultSet.getString("description"));
                request.setDayNum(resultSet.getInt("days"));
                request.setFlightRequest(resultSet.getBoolean("flight"));
                request.setAccommodationRequest(resultSet.getBoolean("accommodation"));

                if (resultSet.getString("trekkingDifficulty") != null) {
                    NatureRequestDecorator decorator = new NatureRequestDecorator(request);
                    decorator.setTrekkingDistance(resultSet.getDouble("trekkingDistance"));
                    decorator.setTrekkingDifficulty(resultSet.getString("trekkingDifficulty"));
                    request = decorator;
                }
                if (resultSet.getString("travelMode") != null) {
                    OnTheRoadRequestDecorator decorator = new OnTheRoadRequestDecorator(request);
                    decorator.setTravelMode(resultSet.getString("travelMode"));
                    decorator.setDayDrivingHours(resultSet.getDouble("dayDrivingHours"));
                    request = decorator;
                }

                List<String> types = new ArrayList<>();
                typeStmt = connection.prepareStatement(typeQuery);
                typeStmt.setString(1, requestId);
                resultSet = typeStmt.executeQuery();
                while (resultSet.next()) {
                    types.add(resultSet.getString("type"));
                }
                request.setItineraryType(types);

                List<City> cities = new ArrayList<>();
                cityStmt = connection.prepareStatement(cityQuery);
                cityStmt.setString(1, requestId);
                resultSet = cityStmt.executeQuery();
                InDbCityDao cityDao = new InDbCityDao();
                while (resultSet.next()) {
                    City city = cityDao.getCity(resultSet.getString("cityID"));
                    cities.add(city);
                }
                request.setCities(cities);

                List<Attraction> attractions = new ArrayList<>();
                attrStmt = connection.prepareStatement(attractionQuery);
                attrStmt.setString(1, requestId);
                resultSet = attrStmt.executeQuery();
                InDbAttractionDao attractionDao = new InDbAttractionDao();
                while (resultSet.next()) {
                    Attraction attraction = attractionDao.getAttraction(resultSet.getString("attractionID"));
                    attractions.add(attraction);
                }
                request.setAttractions(attractions);
            }
        } catch (SQLException e) {
            throw new DaoException("getRequest: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
            DBConnection.getInstance().closeConnection(cityStmt,null);
            DBConnection.getInstance().closeConnection(attrStmt,null);
            DBConnection.getInstance().closeConnection(typeStmt,null);
        }

        return request;
    }

    @Override
    public void updateRequest(Request request) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "update Request set accepted = ? where id = ?";

        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, request.getAccepted());
            stmt.setString(2, request.getId());
            stmt.execute();

        } catch (SQLException e) {
            throw new DaoException("updateRequest: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }

    public void addDecorationsData(Request request) throws DaoException {
        Request current = request;

        while (current instanceof RequestDecorator requestDecorator) {
            if (current instanceof NatureRequestDecorator natureRequestDecorator) {
                addNatureData(natureRequestDecorator);
            }
            if (current instanceof OnTheRoadRequestDecorator onTheRoadRequestDecorator) {
                addOnTheRoadData(onTheRoadRequestDecorator);
            }
            current = requestDecorator.getRequest();
        }
    }

    public void addNatureData(NatureRequestDecorator natureRequestDecorator) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        String natureQuery = "update Request set trekkingDistance = ?, trekkingDifficulty = ? where id = ?";
        PreparedStatement natureStmt = null;

        Connection connection = connect.getConnection();
        try {
            natureStmt = connection.prepareStatement(natureQuery);

            natureStmt.setDouble(1, natureRequestDecorator.getTrekkingDistance());
            natureStmt.setString(2, natureRequestDecorator.getTrekkingDifficulty());
            natureStmt.setString(3, natureRequestDecorator.getId());
            natureStmt.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new DaoException("addDecorationData: "+ e.getMessage(), DUPLICATE);
            }
            throw new DaoException("addDecorationData: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(natureStmt,null);
        }
    }

    public void addOnTheRoadData(OnTheRoadRequestDecorator onTheRoadRequestDecorator) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        String onTheRoadQuery = "update Request set travelMode = ?, dayDrivingHours = ? where id = ?";
        PreparedStatement onTheRoadStmt = null;

        Connection connection = connect.getConnection();
        try {
            onTheRoadStmt = connection.prepareStatement(onTheRoadQuery);

            onTheRoadStmt.setString(1, onTheRoadRequestDecorator.getTravelMode());
            onTheRoadStmt.setDouble(2, onTheRoadRequestDecorator.getDayDrivingHours());
            onTheRoadStmt.setString(3, onTheRoadRequestDecorator.getId());
            onTheRoadStmt.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new DaoException("addDecorationData: "+ e.getMessage(), DUPLICATE);
            }
            throw new DaoException("addDecorationData: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(onTheRoadStmt,null);
        }
    }

    public void addTypes(Request request) throws DaoException {
        DBConnection connect = DBConnection.getInstance();
        Connection connection = connect.getConnection();

        String typesQuery = "insert into requestType (requestID,type) values (?,?)";
        PreparedStatement typesStmt = null;

        try {
            typesStmt = connection.prepareStatement(typesQuery);
            for (String type : request.getItineraryType()) {
                typesStmt.setString(1, request.getId());
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
