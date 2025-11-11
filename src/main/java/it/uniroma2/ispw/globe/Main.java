package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.engineering.Persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {

        String persistence;
        String view;

        try (InputStream input = new FileInputStream("src/main/resources/start.properties")){
            Properties properties = new Properties();
            properties.load(input);

            persistence = properties.getProperty("dao.type");
            view = properties.getProperty("view.type");
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid dao type");
        }

        Persistence.getInstance().setDefaultType(persistence);
        Persistence.getInstance().setType(persistence);

        if (view.equals("GUI")) {
            GlobeApplicationGUI.run(args);
        } else if (view.equals("CLI")) {
            GlobeApplicationCLI.run();
        } else {
            throw new IllegalArgumentException("Invalid view type");
        }
    }
}