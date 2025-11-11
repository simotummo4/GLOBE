package it.uniroma2.ispw.globe.engineering.adapter;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;

public class PlaceAdapter implements Attraction, City {
    private JsonObject place;

    private static final String ADDR = "address";
    private static final String UNKNOWN = "Unknown";

    public PlaceAdapter(JsonObject place) {
        this.place = place;
    }

    public String getPlaceID() {
        return (place.get("osm_type").getAsString()).charAt(0)+place.get("osm_id").getAsString();
    }

    public String getName() {
        return place.get("name").getAsString();
    }

    public String getCountry() {
        JsonObject address = place.getAsJsonObject(ADDR);
        try {
            return address.get("country").getAsString();
        } catch (NullPointerException e) {
            return UNKNOWN;
        }
    }

    public String getCity() {
        try {
            JsonObject address = place.getAsJsonObject(ADDR);
            return address.get("city").getAsString();
        } catch (NullPointerException e) {
            return UNKNOWN;
        }
    }

    public String getAddress() {
        JsonObject address = place.getAsJsonObject(ADDR);
        try {
            return address.get("road").getAsString();
        } catch (NullPointerException e) {
            return UNKNOWN;
        }
    }

    public double getLatitude() {
        return place.get("lat").getAsDouble();
    }

    public double getLongitude() {
        return place.get("lon").getAsDouble();
    }
}
