package it.uniroma2.ispw.globe.dao.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_SQL;

public class DBConnection {
    private static DBConnection instance = null;
    private Connection conn = null;

    private DBConnection(){}

    public Connection getConnection() {
        try (InputStream input = new FileInputStream("src/main/resources/db.properties")){
            Properties properties = new Properties();
            properties.load(input);

            String dbUrl = properties.getProperty("db.url");
            String dbUsr = properties.getProperty("db.user");
            String dbPwd = properties.getProperty("db.password");

            conn = DriverManager.getConnection(dbUrl, dbUsr, dbPwd);
        } catch (IOException | SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL, e);
        }
        return conn;
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null)
            instance = new DBConnection();
        return instance;
    }

    public void closeConnection(Statement st, ResultSet rs) {
        try {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
            if(conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL, e);
        }
    }
}
