package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.bean.AttractionBean;
import it.uniroma2.ispw.globe.bean.CityBean;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.bean.RequestBean;
import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.AttractionNotAddedException;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.uniroma2.ispw.globe.constants.ItineraryType.*;

public class CreateItineraryCLIController {
    private String sessionId;
    private String requestId;

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";

    CreateItineraryCLIController(String sessionId, String requestId) {
        this.sessionId = sessionId;
        this.requestId = requestId;
    }

    public void start() {
        System.out.println("# CREATE ITINERARY #");

        if (requestId != null) {
            try {
                //create proposal use case
                RequestBean requestBean;
                requestBean = new ResponseRequestController().getAgencyRequest(requestId, sessionId);

                if (requestBean != null) {
                    System.out.println("> User " + requestBean.getUser() + " request:");
                    System.out.println("    > Description: " + requestBean.getOtherRequests());
                    System.out.println("    > Types: " + requestBean.getTypes());
                    System.out.println("    > Cities: " + requestBean.getCities());
                    System.out.println("    > Attractions: " + requestBean.getAttractions());
                }
            } catch (FailedOperationException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
                return;
            }
        }

        getItineraryData();
    }

    public void getItineraryData() {
        String name;
        String strDuration;
        String description;
        int duration;

        ItineraryBean itineraryBean = new ItineraryBean();

        itineraryBean.setId(null);

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter Itinerary name: ");
            name = input.nextLine();
            try {
                itineraryBean.setName(name);
                break;
            } catch (IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
        while (true) {
            System.out.print("Please enter itinerary duration (1-99): ");
            strDuration = input.nextLine();
            try {
                duration = Integer.parseInt(strDuration);
                itineraryBean.setDuration(duration);
                break;
            } catch (NumberFormatException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
        while (true) {
            System.out.print("Please enter itinerary description: ");
            description = input.nextLine();
            try {
                itineraryBean.setDescription(description);
                break;
            } catch (IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }

        try {
            itineraryBean.setTypes(getTypes());
            itineraryBean.setCities(searchCities());
            itineraryBean.setAttractions(searchAttractions());
        } catch (IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }

        itineraryBean = getOtherData(itineraryBean);

        try {
            new CreateItineraryController().createItinerary(itineraryBean,sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            System.out.println(ERROR + e.getMessage());
            return;
        } catch (AttractionNotAddedException e) {
            System.out.println(ERROR + e.getMessage());
        }

        if (requestId != null) {
            CreateProposalCLIController controller = new CreateProposalCLIController(sessionId,requestId);
            controller.start();
        } else {
            DisplayItineraryCLIController controller = new DisplayItineraryCLIController(sessionId,null,null,null);
            controller.start();
        }
    }

    public List<String> getTypes() {
        List<String> types = new ArrayList<>();

        do {
            System.out.println("Please enter the number of the type for your itinerary");
            System.out.println("1 -> " + ON_THE_ROAD);
            System.out.println("2 -> " + NATURE);
            System.out.println("3 -> " + CULTURE);
            System.out.println("4 -> " + RELAX);
            System.out.println("5 -> " + CITY);

            types.add(getType(types));

            System.out.println("Do you want to add another type? (yes/no)");
        } while (getAnother());
        return types;
    }

    public String getType(List<String> types) {
        String strChoice;
        int choice;
        String type;

        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Please enter your choice : ");
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-5]")) {
                choice = Integer.parseInt(strChoice);
                switch (choice) {
                    case 1 -> type = ON_THE_ROAD;
                    case 2 -> type = NATURE;
                    case 3 -> type = CULTURE;
                    case 4 -> type = RELAX;
                    case 5 -> type = CITY;
                    default -> type = null;
                }
                if (!types.contains(type)) {
                    break;
                }
            }
            System.out.println(CHOICE_ERROR);
        }
        return type;
    }

    public List<String> searchCities() {
        String city;
        List<String> cities = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter City (enter stop to terminate): ");
            city = input.nextLine();
            if (!city.isEmpty()) {
                if (city.equalsIgnoreCase("stop")) {
                    break;
                }
                String cityID = getCity(city);
                if (cityID != null && !cities.contains(cityID)) {
                    cities.add(cityID);
                }
            }else {
                System.out.println(CHOICE_ERROR);
            }
        }

        return cities;
    }

    public String getCity(String city) {
        List<CityBean> citiesResult = new ArrayList<>();
        try {
            citiesResult = new CreateItineraryController().getCities(city);
        } catch (FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
        if (!citiesResult.isEmpty()) {
            int i = 0;
            for (CityBean cityResult : citiesResult) {
                i++;
                System.out.println(i + " -> " + cityResult.getName()+" - "+ cityResult.getCountry());
                if (i == 3) {
                    break;
                }
            }
            return addCity(i, citiesResult);
        } else {
            System.out.println("> no place");
        }
        return null;
    }

    public String addCity(int max, List<CityBean> cities) {
        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.println("Please enter the number of the city for your itinerary: ");
            strChoice = input.nextLine();
            try {
                choice = Integer.parseInt(strChoice);
                if (choice >= 1 && choice <= max) {
                    break;
                } else {
                    System.out.println(CHOICE_ERROR);
                }
            } catch (NumberFormatException e) {
                System.out.println(CHOICE_ERROR);
            }
        }
        return cities.get(choice-1).getId();
    }

    public List<String> searchAttractions() {
        String attraction;
        List<String> attractions = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter Attraction (enter stop to termiante): ");
            attraction = input.nextLine();
            if (!attraction.isEmpty()) {
                if (attraction.equalsIgnoreCase("stop")) {
                    break;
                }
                String attrID = getAttraction(attraction);
                if (attrID != null && !attractions.contains(attrID)) {
                    attractions.add(attrID);
                }
            } else {
                System.out.println(CHOICE_ERROR);
            }
        }

        return attractions;
    }

    public String getAttraction(String attr) {
        List<AttractionBean> attractionsResult = new ArrayList<>();
        try {
            attractionsResult = new CreateItineraryController().getAttractions(attr);
        } catch (FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
        if (!attractionsResult.isEmpty()) {
            int i = 0;
            for (AttractionBean attractionResult : attractionsResult) {
                i++;
                System.out.println(i + " -> " + attractionResult.getName()+" - "+ attractionResult.getCity());
                if (i == 3) {
                    break;
                }
            }
            return addAttraction(i,attractionsResult);
        } else {
            System.out.println("> no place");
        }
        return null;
    }

    public String addAttraction(int max, List<AttractionBean> attractions) {
        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.println("Please enter the number of the attraction for your itinerary: ");
            strChoice = input.nextLine();
            try {
                choice = Integer.parseInt(strChoice);
                if (choice >= 1 && choice <= max) {
                    break;
                } else {
                    System.out.println(CHOICE_ERROR);
                }
            } catch (NumberFormatException e) {
                System.out.println(CHOICE_ERROR);
            }
        }
        return attractions.get(choice-1).getId();
    }

    public ItineraryBean getOtherData(ItineraryBean itineraryBean) {

        System.out.println("Do you want to add an accommodation? (yes/no)");
        if (getAnother()) {
            itineraryBean = getAccommodations(itineraryBean);
        }

        System.out.println("Do you want to add flight? (yes/no)");
        if (getAnother()) {
            itineraryBean = getFlight(itineraryBean);
        }

        return itineraryBean;
    }

    public  ItineraryBean getAccommodations(ItineraryBean itineraryBean) {
        String accommodation;
        String address;

        Scanner input = new Scanner(System.in);

        if (itineraryBean.getAccommodations() == null) {
            try {
                itineraryBean.setAccommodations(new ArrayList<>());
            } catch (IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
        do {
            while (true) {
                System.out.print("Please enter accommodation name: ");
                accommodation = input.nextLine();
                if (!accommodation.isEmpty()) {
                    break;
                }
                System.out.println(CHOICE_ERROR);
            }
            while (true) {
                System.out.print("Please enter accommodation address: ");
                address = input.nextLine();
                if (!address.isEmpty()) {
                    break;
                }
                System.out.println(CHOICE_ERROR);
            }

            itineraryBean.getAccommodations().add(new Pair<>(accommodation, address));

            System.out.println("Do you want to add another type? (yes/no)");
        } while (getAnother());
        return itineraryBean;
    }

    public ItineraryBean getFlight(ItineraryBean itineraryBean) {
        String inDepartureTimeStr;
        String inArrivalTimeStr;
        String outDepartureTimeStr;
        String outArrivalTimeStr;

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter inbound departure time: ");
            inDepartureTimeStr = input.nextLine();
            try {
                itineraryBean.setInboundFlightDepartureTime(Double.parseDouble(inDepartureTimeStr));
                break;
            } catch (NumberFormatException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
        while (true){
            System.out.print("Please enter inbound arrival time: ");
            inArrivalTimeStr = input.nextLine();
            try {
                itineraryBean.setInboundFlightArrivalTime(Double.parseDouble(inArrivalTimeStr));
                break;
            } catch (NumberFormatException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
        while (true){
            System.out.print("Please enter outbound departure time: ");
            outDepartureTimeStr = input.nextLine();
            try {
                itineraryBean.setOutboundFlightDepartureTime(Double.parseDouble(outDepartureTimeStr));
                break;
            } catch (NumberFormatException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
        while (true){
            System.out.print("Please enter outbound arrival time: ");
            outArrivalTimeStr = input.nextLine();
            try {
                itineraryBean.setOutboundFlightArrivalTime(Double.parseDouble(outArrivalTimeStr));
                break;
            } catch (NumberFormatException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }

        return itineraryBean;
    }

    public boolean getAnother() {
        String response;
        Scanner input = new Scanner(System.in);

        while (true){
            response = input.nextLine();
            if (!response.isEmpty() && (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no"))) {
                break;
            }
            System.out.println(CHOICE_ERROR);
        }

        return !response.equalsIgnoreCase("no");
    }
}
