package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.bean.AttractionBean;
import it.uniroma2.ispw.globe.bean.CityBean;
import it.uniroma2.ispw.globe.engineering.adapter.PlaceAdapter;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.PlaceApiException;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NominatimAPIClient {

    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";

    private static final String BASE_URL = "https://nominatim.openstreetmap.org/";
    private static final Gson gson = new Gson();
    private final OkHttpClient httpClient;

    private static final String TYPE = "addresstype";

    public NominatimAPIClient() {
        this.httpClient = new OkHttpClient();
    }

    public List<JsonObject> getPlaces(String name, String type) throws FailedOperationException {
        String url = String.format("%ssearch?q=%s&format=json&addressdetails=1", BASE_URL, name.replace(" ", "+"));
        try {
            return getPlace(url, type);
        } catch (PlaceApiException e) {
            throw new FailedOperationException("Get agency by type");
        }
    }

    public JsonObject getPlaceByID(String id) throws PlaceApiException {
        String url = String.format("%slookup?osm_ids=%s&format=json&addressdetails=1", BASE_URL, id);
        List<JsonObject> places;
        places = getPlace(url,"id");
        if (!places.isEmpty()) {
            return places.get(0);
        }
        return null;
    }

    public List<JsonObject> getPlace(String url, String type) throws PlaceApiException {
        List<JsonObject> places;

        Request request = new Request.Builder().url(url).header("User-Agent", "Globe/1.0").build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = null;
            if (response.body() != null) {
                responseBody = response.body().string();
            }
            JsonArray results = gson.fromJson(responseBody, JsonArray.class);
            places = selectPlaces(results,type);
        } catch (IOException e) {
            throw new PlaceApiException(e.getMessage());
        }
        return places;
    }

    public List<JsonObject> selectPlaces(JsonArray results, String type) {
        List<JsonObject> places = new ArrayList<>();

        for (int i = 0; i < results.size() && i < 10; i++) {
            JsonObject place = results.get(i).getAsJsonObject();
            if (type.equals("id")) {
                places.add(place);
            } else if (type.equals(CITY)) {
                if (place.get(TYPE).getAsString().equals("city")||place.get(TYPE).getAsString().equals("town")||place.get(TYPE).getAsString().equals("village")) {
                    places.add(place);
                }
            } else {
                if (!place.get("type").getAsString().equals(type)) {
                    places.add(place);
                }
            }
        }

        return places;
    }

    public List<AttractionBean> getAttractions(String name) throws FailedOperationException {
        List<JsonObject> jsonAttractions = getPlaces(name, ATTRACTION);
        List<Attraction> attractions = new ArrayList<>();
        List<AttractionBean> attractionBeans = new ArrayList<>();

        for (JsonObject json_attraction : jsonAttractions) {
            Attraction attraction = new PlaceAdapter(json_attraction);
            attractions.add(attraction);
        }

        for (Attraction attraction : attractions) {
            AttractionBean attractionBean = new AttractionBean();
            attractionBean.setId(attraction.getPlaceID());
            attractionBean.setName(attraction.getName());
            attractionBean.setAddress(attraction.getAddress());
            attractionBean.setCity(attraction.getCity());

            attractionBeans.add(attractionBean);
        }

        return attractionBeans;
    }

    public List<CityBean> getCities(String name) throws FailedOperationException {
        List<JsonObject> jsonCities = getPlaces(name, CITY);
        List<City> cities = new ArrayList<>();
        List<CityBean> citiesBeans = new ArrayList<>();

        for (JsonObject json_city : jsonCities) {
            City city = new PlaceAdapter(json_city);
            cities.add(city);
        }

        for (City city : cities) {
            CityBean cityBean = new CityBean();
            cityBean.setId(city.getPlaceID());
            cityBean.setName(city.getName());
            cityBean.setCountry(city.getCountry());

            citiesBeans.add(cityBean);
        }
        return citiesBeans;
    }


}
