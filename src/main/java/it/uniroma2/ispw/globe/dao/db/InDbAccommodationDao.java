package it.uniroma2.ispw.globe.dao.db;

import it.uniroma2.ispw.globe.dao.AccommodationDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Accommodation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InDbAccommodationDao extends AccommodationDao {

    @Override
    public void addAccommodation(Accommodation accommodation) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "insert into Accommodation (id,name,address) values (?,?,?)";

        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, accommodation.getId());
            stmt.setString(2, accommodation.getName());
            stmt.setString(3, accommodation.getAddress());
            stmt.execute();

        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1062) {
                throw new DaoException("addAccommodation: "+ exception.getMessage(), DUPLICATE);
            }
            throw new DaoException("addAccommodation: "+ exception.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }

    @Override
    public Accommodation getAccommodation(String id) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Accommodation.id , Accommodation.name , Accommodation.address from Accommodation where id = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        Accommodation accommodation = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, id);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                accommodation = new Accommodation();
                accommodation.setId(resultSet.getString("id"));
                accommodation.setName(resultSet.getString("name"));
                accommodation.setAddress(resultSet.getString("address"));

            }
        } catch (SQLException e) {
            throw new DaoException("getAccommodation: "+ e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return accommodation;
    }
}
