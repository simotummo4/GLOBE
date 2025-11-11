package it.uniroma2.ispw.globe.dao.db;

import it.uniroma2.ispw.globe.dao.FlightDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InDbFlightDao extends FlightDao {

    @Override
    public void addFlight(Flight flight) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "insert into Flight (id,departureTime,arrivalTime) values (?,?,?)";

        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, flight.getId());
            stmt.setDouble(2, flight.getDepartureTime());
            stmt.setDouble(3, flight.getArrivalTime());
            stmt.execute();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new DaoException("addFlight: "+ e.getMessage(), DUPLICATE);
            }
            throw new DaoException("addFlight: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }

    @Override
    public Flight getFlight(String flightID) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Flight.id, Flight.departureTime, Flight.arrivalTime from Flight where id = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        Flight flight = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, flightID);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                flight = new Flight();

                flight.setId(resultSet.getString("id"));
                flight.setDepartureTime(resultSet.getDouble("departureTime"));
                flight.setArrivalTime(resultSet.getDouble("arrivalTime"));
            }
        } catch (SQLException e) {
            throw new DaoException("getFlight: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return flight;
    }
}
