package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.engineering.decorator.ItineraryDecorator;
import it.uniroma2.ispw.globe.exception.*;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.Accommodation;
import it.uniroma2.ispw.globe.model.Flight;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.bean.AttractionBean;
import it.uniroma2.ispw.globe.bean.CityBean;
import it.uniroma2.ispw.globe.dao.ItineraryDao;
import it.uniroma2.ispw.globe.dao.DayDao;
import it.uniroma2.ispw.globe.dao.CityDao;
import it.uniroma2.ispw.globe.dao.AttractionDao;
import it.uniroma2.ispw.globe.dao.AccommodationDao;
import it.uniroma2.ispw.globe.dao.FlightDao;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.Session;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.engineering.decorator.AccommodationDecorator;
import it.uniroma2.ispw.globe.engineering.decorator.FlightDecorator;
import it.uniroma2.ispw.globe.model.Itinerary;
import javafx.util.Pair;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;

public class CreateItineraryController {

    public ItineraryBean getItinerary(String itineraryId, String sessionID) throws FailedOperationException, IncorrectDataException {
        Itinerary itinerary;
        ItineraryBean itineraryBean = new ItineraryBean();
        try {

            if (itineraryId == null) {
                itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
                if (itinerary == null) {
                    return null;
                }
            } else {
                ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
                itinerary = itineraryDao.getItinerary(itineraryId);
            }

            if (itinerary == null) {
                throw new FailedOperationException("Get itinerary - itinerary not found");
            }

            itineraryBean.setId(itinerary.getItineraryID());
            itineraryBean.setName(itinerary.getName());
            itineraryBean.setDescription(itinerary.getDescription());
            itineraryBean.setTypes(itinerary.getTypes());
            itineraryBean.setDuration(itinerary.getDaysNumber());
            itineraryBean.setPhoto(itinerary.getPhotoFile());


            itineraryBean.setInboundFlightDepartureTime(-1);
            itineraryBean.setInboundFlightArrivalTime(-1);
            itineraryBean.setOutboundFlightDepartureTime(-1);
            itineraryBean.setOutboundFlightArrivalTime(-1);

            Itinerary current = itinerary;
            while (current instanceof ItineraryDecorator itineraryDecorator) {
                if (current instanceof AccommodationDecorator accommodationDecorator) {
                    List<Pair<String,String>> accommodations = new ArrayList<>();
                    for (Accommodation accommodation : accommodationDecorator.getAccommodations()) {
                        accommodations.add(new Pair<>(accommodation.getName(), accommodation.getAddress()));
                    }
                    itineraryBean.setAccommodations(accommodations);
                }
                if (current instanceof FlightDecorator flightDecorator) {
                    Flight inFlight = flightDecorator.getInFlight();
                    Flight outFlight = flightDecorator.getOutFlight();
                    double inDepartureTime = inFlight.getDepartureTime();
                    double inArrivalTime =inFlight.getArrivalTime();
                    double outDepartureTime = outFlight.getDepartureTime();
                    double outArrivalTime = outFlight.getArrivalTime();

                    itineraryBean.setInboundFlightDepartureTime(inDepartureTime);
                    itineraryBean.setInboundFlightArrivalTime(inArrivalTime);
                    itineraryBean.setOutboundFlightDepartureTime(outDepartureTime);
                    itineraryBean.setOutboundFlightArrivalTime(outArrivalTime);
                }
                current = itineraryDecorator.getItinerary();
            }

            return itineraryBean;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get itinerary");
        }
    }

    public void createItinerary(ItineraryBean itineraryBean, String sessionID) throws FailedOperationException, DuplicateItemException, AttractionNotAddedException {
        try {
            ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
            DayDao dayDao = Persistence.getInstance().getFactory().getDayDao();
            CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
            AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();

            String itineraryId = UUID.randomUUID().toString();
            itineraryBean.setId(itineraryId);

            Itinerary itinerary = itineraryDao.createItinerary(itineraryId,itineraryBean.getName(),itineraryBean.getDescription(), itineraryBean.getDuration(), itineraryBean.getTypes());

            List<Day> days = new ArrayList<>();

            Day day0 = dayDao.createDay(itineraryId,0);
            List<City> cities = new ArrayList<>();
            List<Attraction> attractions = new ArrayList<>();

            for (String cityId : itineraryBean.getCities()) {
                City city;
                city = cityDao.createCity(cityId);
                cities.add(city);
            }

            for (String attractionId : itineraryBean.getAttractions()) {
                Attraction attraction;
                attraction = attractionDao.createAttraction(attractionId);
                attractions.add(attraction);
            }
            day0.setCities(cities);
            day0.setAttractions(attractions);

            days.add(day0);
            for (int i=1; i<=itineraryBean.getDuration(); i++) {
                Day day = dayDao.createDay(itineraryId, i);
                days.add(day);
            }

            itinerary.setDays(days);

            calculateItinerary(itinerary);

            if (itineraryBean.getAccommodations() != null) {
                itinerary = getAccommodationDecorator(itineraryBean, itinerary);
            }

            if (itineraryBean.getInboundFlightArrivalTime() != 0) {
                itinerary = getFlightDecorator(itineraryBean, itinerary);
            }

            SessionManager.getInstance().getSession(sessionID).setPendingItinerary(itinerary);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Create itinerary");
        }
    }

    private FlightDecorator getFlightDecorator(ItineraryBean itineraryBean, Itinerary itinerary) {
        FlightDao flightDao = Persistence.getInstance().getFactory().getFlightDao();

        Flight inFlight = flightDao.createFlight(itineraryBean.getInboundFlightDepartureTime(), itineraryBean.getInboundFlightArrivalTime());
        Flight outFlight = flightDao.createFlight(itineraryBean.getOutboundFlightDepartureTime(), itineraryBean.getOutboundFlightArrivalTime());

        FlightDecorator flightItinerary = new FlightDecorator(itinerary);
        flightItinerary.setInFlight(inFlight);
        flightItinerary.setOutFlight(outFlight);
        return flightItinerary;
    }

    private AccommodationDecorator getAccommodationDecorator(ItineraryBean itineraryBean, Itinerary itinerary) {
        AccommodationDao accommodationDao = Persistence.getInstance().getFactory().getAccommodationDao();

        List<Accommodation> accommodations = new ArrayList<>();
        for (Pair<String,String> a : itineraryBean.getAccommodations()) {
            Accommodation accommodation = accommodationDao.createAccommodation(a.getKey(), a.getValue());
            accommodations.add(accommodation);
        }
        AccommodationDecorator accommodationItinerary = new AccommodationDecorator(itinerary);
        accommodationItinerary.setAccommodations(accommodations);
        return accommodationItinerary;
    }

    public void saveItinerary(String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();

            Session session = SessionManager.getInstance().getSession(sessionID);
            Account account = session.getAccount();

            Itinerary itinerary = session.getPendingItinerary();
            itinerary.getDays().removeIf(day -> day.getDayNum() == 0);
            itineraryDao.addItinerary(itinerary, account);

            session.setPendingItinerary(null);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Save itinerary");
        }
    }

    public void calculateItinerary(Itinerary itinerary) throws AttractionNotAddedException {
        Map<City,List<Attraction>> attractionsByCity = getAttractionsByCity(itinerary);
        itinerary.setDays(distributeAttraction(itinerary,attractionsByCity));
    }

    public Map<City, List<Attraction>> getAttractionsByCity(Itinerary itinerary) throws AttractionNotAddedException {
        List<City> cities = itinerary.getDays().get(0).getCities();
        List<Attraction> attractions = itinerary.getDays().get(0).getAttractions();
        List<Attraction> otherAttractions = new ArrayList<>();

        Map<City,List<Attraction>> attractionsByCity = new HashMap<>();

        for (City city : cities) {
            attractionsByCity.put(city, new ArrayList<>());
        }

        boolean flag = false;
        for (Attraction attraction : attractions) {
            for (Map.Entry<City, List<Attraction>> entry : attractionsByCity.entrySet()) {
                if (attraction.getCity().equals(entry.getKey().getName())) {
                    attractionsByCity.get(entry.getKey()).add(attraction);
                    flag = true;
                }
            }
            if (!flag) {
                otherAttractions.add(attraction);
            }
        }

        if (!otherAttractions.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Attraction attraction : otherAttractions) {
                stringBuilder.append("- ").append(attraction.getName()).append(", ").append(attraction.getCity()).append("\n");
            }
            throw new AttractionNotAddedException(stringBuilder.toString());
        }
        return attractionsByCity;
    }

    public List<Day> distributeAttraction(Itinerary itinerary, Map<City, List<Attraction>> attractionsByCity) {
        List<Day> newDays = new ArrayList<>();
        List<Integer> attrForCity = new ArrayList<>();

        for (List<Attraction> attractions : attractionsByCity.values()) {
            attrForCity.add(attractions.size());
        }
        List<Integer> daysForCity = assignDays(itinerary.getDaysNumber(),attractionsByCity.keySet().size(),attrForCity);

        int current = 0;
        int curDay = 1;
        for (Map.Entry<City, List<Attraction>> entry : attractionsByCity.entrySet()) {

            List<Attraction> attractionPath = getShortestPath(entry.getValue());

            int attrDayNum = (int)Math.ceil((double)attractionPath.size()/daysForCity.get(current));

            int curAttr = 0;

            for (int i = 0; i<daysForCity.get(current) ; i++) {
                Day day = itinerary.getDays().get(curDay);
                for (City city : itinerary.getDays().get(0).getCities()) {
                    if (city.getName().equals(entry.getKey().getName())) {
                        day.getCities().add(city);
                    }
                }

                day.setAttractions(new ArrayList<>(attractionPath.subList(curAttr, Math.min(curAttr + attrDayNum, attractionPath.size()))));
                curAttr += attrDayNum;
                newDays.add(day);
                curDay++;
            }
            current++;
        }
        return newDays;
    }

    public List<Integer> assignDays(int numDays, int numCity, List<Integer> attrForCity) { // n k h
        List<Integer> daysForCity = new ArrayList<>();
        for (int i = 0; i < numCity; i++) {
            daysForCity.add(1);
        }

        int daysLeft = numDays - numCity;

        while (daysLeft > 0) {
            int bestCity = -1;
            double bestGain = 0;

            for (int i = 0; i < numCity; i++) {
                double currentLoad = (double) attrForCity.get(i) / daysForCity.get(i);
                double projectedLoad = (double) attrForCity.get(i) / (daysForCity.get(i) + 1);
                double gain = currentLoad - projectedLoad;

                if (gain > bestGain) {
                    bestGain = gain;
                    bestCity = i;
                }
            }

            if (bestCity != -1) {
                int current = daysForCity.get(bestCity);
                daysForCity.set(bestCity, current + 1);
                daysLeft--;
            } else {
                int current = daysForCity.get(0);
                daysForCity.set(0, current + daysLeft);
                daysLeft = 0;
            }
        }

        return daysForCity;
    }

    public List<Attraction> getShortestPath(List<Attraction> attractions) {

        Map<Attraction,List<Pair<Attraction,Double>>> distances = new HashMap<>();

        for (Attraction attraction : attractions) {
            distances.put(attraction,new ArrayList<>());
            for (Attraction otherAttraction : attractions) {
                if (!attraction.equals(otherAttraction)) {
                    double latitudeDistance = attraction.getLatitude() - otherAttraction.getLatitude();
                    double longitudeDistance = attraction.getLongitude() - otherAttraction.getLongitude();
                    double distance = Math.sqrt(Math.pow(latitudeDistance, 2) + Math.pow(longitudeDistance, 2));
                    Pair<Attraction, Double> pair = new Pair<>(otherAttraction,distance);
                    List<Pair<Attraction,Double>> pairs = distances.get(attraction);
                    pairs.add(pair);
                    distances.put(attraction,pairs);
                }
            }
        }

        List<Attraction> path = new ArrayList<>();
        List<Attraction> visited = new ArrayList<>();

        Attraction current = attractions.get(0);

        while (visited.size()<attractions.size()) {
            path.add(current);
            visited.add(current);

            Attraction next = null;
            double minDistance = Double.MAX_VALUE;

            for (Pair<Attraction,Double> attrDistance : distances.get(current)) {
                if (!visited.contains(attrDistance.getKey()) && attrDistance.getValue()<minDistance) {
                    next = attrDistance.getKey();
                    minDistance = attrDistance.getValue();
                }
            }

            if (next == null) {
                break;
            }
            current = next;
        }
        return path;
    }

    public List<AttractionBean> getAttractions(String name) throws FailedOperationException {
        return new NominatimAPIClient ().getAttractions(name);
    }

    public List<CityBean> getCities(String name) throws FailedOperationException {
        return new NominatimAPIClient ().getCities(name);
    }

    public void setItineraryPhoto (File file, String itineraryID, String sessionID) throws FailedOperationException {
        if (itineraryID == null) {
            Itinerary itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
            itinerary.setPhotoFile(file);
        } else {
            ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
            try {
                itineraryDao.addPhotoFile(file, itineraryID);
            } catch (DaoException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
                throw new FailedOperationException("Set Photo");
            }
        }

    }
}
