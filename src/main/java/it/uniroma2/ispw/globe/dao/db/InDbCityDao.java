package it.uniroma2.ispw.globe.dao.db;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.dao.CityDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.engineering.adapter.PlaceAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InDbCityDao extends CityDao {

    @Override
    public void addCity(City city) throws DaoException {
        if (getCity(city.getPlaceID()) != null) {
            return;
        }

        DBConnection connect = DBConnection.getInstance();

        String query = "insert into City (placeID,name,country,latitude,longitude) values (?,?,?,?,?)";

        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, city.getPlaceID());
            stmt.setString(2, city.getName());
            stmt.setString(3, city.getCountry());
            stmt.setDouble(4, city.getLatitude());
            stmt.setDouble(5, city.getLongitude());
            stmt.execute();
        } catch (SQLException e) {
            throw new DaoException("addCity: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }

    @Override
    public City getCity(String cityID) throws DaoException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select City.placeID, City.name, City.country ,City.latitude, City.longitude from City where placeID = ?";



        PreparedStatement stmt = null;
        ResultSet rs = null;

        City city = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, cityID);

            rs = stmt.executeQuery();

            if (rs.next()) {
                JsonObject json = new JsonObject();

                json.addProperty("osm_type", rs.getString("placeID").substring(0,1));
                json.addProperty("osm_id", Integer.parseInt(rs.getString("placeID").substring(1)));

                json.addProperty("name", rs.getString("name"));
                json.addProperty("lat", String.valueOf(rs.getDouble("latitude")));
                json.addProperty("lon", String.valueOf(rs.getDouble("longitude")));

                JsonObject address = new JsonObject();
                address.addProperty("country", rs.getString("country"));
                address.addProperty("city", rs.getString("name"));

                json.add("address", address);

                city = new PlaceAdapter(json);
            }
        } catch (SQLException e) {
            throw new DaoException("getCity: " + e.getMessage(), GENERAL);
        } finally {
            DBConnection.getInstance().closeConnection(stmt, rs);
        }

        return city;
    }
}
