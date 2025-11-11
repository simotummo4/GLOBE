package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.controller.applicationcontroller.RequestItineraryController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static it.uniroma2.ispw.globe.constants.ItineraryType.*;

public class CreateRequestCLIController {
    private String sessionId;

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";

    private static final String ENTER_CHOICE = "Please enter your choice: ";

    CreateRequestCLIController(String sessionId) {
        this.sessionId = sessionId;
    }

    public void start() {
        System.out.println("# CREATE REQUEST #");
        getRequestData();
    }


    public void getRequestData() {
        String otherRequests;
        String strDuration;
        int duration;

        RequestBean request = new RequestBean();

        Scanner input = new Scanner(System.in);

        try {
            request.setCities(searchCities());
            request.setAttractions(searchAttractions());
        } catch (IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }

        while (true) {
            System.out.print("Please enter itinerary duration (1-99): ");
            strDuration = input.nextLine();
            try {
                duration = Integer.parseInt(strDuration);
                request.setDayNum(duration);
                break;
            } catch (NumberFormatException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }

        request = getOtherData(request);

        try {
            request.setTypes(getTypes());
        } catch (IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }
        OnTheRoadBean onTheRoadBean = null;
        NatureBean natureBean = null;
        for (String type : request.getTypes()) {
            switch (type) {
                case ON_THE_ROAD -> onTheRoadBean = getOnTheRoadData();
                case NATURE -> natureBean = getNatureData() ;
                case RELAX, CITY, CULTURE -> {
                    //not implemented yet
                }
                default -> {
                    // no default
                }
            }
        }


        while (true) {
            System.out.print("Please enter other request: ");
            otherRequests = input.nextLine();
            try {
                request.setOtherRequests(otherRequests);
                break;
            } catch (IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
        try {
            request.setAgencies(getAgencies(request.getTypes()));
        } catch (IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }

        try {
            new RequestItineraryController().createRequest(request, onTheRoadBean, natureBean, sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            System.out.println(ERROR + e.getMessage());
        }
        DisplayRequestCLIController controller = new DisplayRequestCLIController(sessionId,null);
        controller.start();
    }

    public List<String> searchCities() {
        String city;
        List<String> cities = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter City name (enter stop to terminate): ");
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
            citiesResult = new RequestItineraryController().getCities(city);
        } catch (FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
        if (!citiesResult.isEmpty()) {
            int i = 0;
            for (CityBean cityResult : citiesResult) {
                i++;
                System.out.println(i + " --> " + cityResult.getName()+" - "+ cityResult.getCountry());
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
            System.out.println("Please enter city number for your request: ");
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
            System.out.print("Please enter Attraction (enter stop to terminate): ");
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
            attractionsResult = new RequestItineraryController().getAttractions(attr);
        } catch (FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
        if (!attractionsResult.isEmpty()) {
            int i = 0;
            for (AttractionBean attractionResult : attractionsResult) {
                i++;
                System.out.println(i + "  -> " + attractionResult.getName()+" - "+ attractionResult.getCity());
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
            System.out.println("Please enter attraction number for your request: ");
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

    public List<String> getTypes() {
        List<String> types = new ArrayList<>();

        do {
            System.out.println("Please enter the number of the type for your request");
            System.out.println("1 --> " + ON_THE_ROAD);
            System.out.println("2 --> " + NATURE);
            System.out.println("3 --> " + CULTURE);
            System.out.println("4 --> " + RELAX);
            System.out.println("5 --> " + CITY);

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
            System.out.print(ENTER_CHOICE);
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

    public OnTheRoadBean getOnTheRoadData() {
        String mode;
        String strDuration;
        int duration;
        OnTheRoadBean onTheRoadBean = new OnTheRoadBean();

        System.out.println("travel mode?\n");
        System.out.println("1 -> Morning");
        System.out.println("2 -> Afternoon");
        System.out.println("3 -> Night");

        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.print(ENTER_CHOICE);
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-3]")) {
                choice = Integer.parseInt(strChoice);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }

        switch (choice) {
            case 1 -> mode = "morning";
            case 2 -> mode = "afternoon";
            case 3 -> mode = "night";
            default -> mode = null;
        }
        try {
            onTheRoadBean.setMode(mode);
        } catch (IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }
        while (true) {
            System.out.print("Please enter day driving hours (1-99): ");
            strDuration = input.nextLine();
            try {
                duration = Integer.parseInt(strDuration);
                onTheRoadBean.setDayDrivingHours(duration);
                break;
            } catch (NumberFormatException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }

        return onTheRoadBean;
    }

    public NatureBean getNatureData() {
        String trekkingDifficulty;
        String strDistance;
        int distance;

        NatureBean natureBean = new NatureBean();
        System.out.println("trekking difficulty?\n");
        System.out.println("1 -> Normal");
        System.out.println("2 -> Medium");
        System.out.println("3 -> Hard");

        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.print(ENTER_CHOICE);
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-3]")) {
                choice = Integer.parseInt(strChoice);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        switch (choice) {
            case 1 -> trekkingDifficulty = "Normal";
            case 2 -> trekkingDifficulty = "Medium";
            case 3 -> trekkingDifficulty = "Hard";
            default -> trekkingDifficulty = null;
        }
        try {
            natureBean.setDifficulty(trekkingDifficulty);
        } catch (IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }

        while (true) {
            System.out.print("Please enter trekking distance (1-99): ");
            strDistance = input.nextLine();
            try {
                distance = Integer.parseInt(strDistance);
                natureBean.setTrekkingDistance(distance);
                break;
            } catch (NumberFormatException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }

        return natureBean;

    }


    public RequestBean getOtherData(RequestBean requestBean) {

        System.out.println("Do you want to add an accommodation? (yes/no)");
        requestBean.setAccommodation(getAnother());

        System.out.println("Do you want to add flight? (yes/no)");
        requestBean.setFlight(getAnother());

        return requestBean;
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

    public List<String> getAgencies(List<String> types) {
        List<AgencyBean> agencies;
        String agency;
        List<String> agenciesSelected = new ArrayList<>();

        try {
            agencies = new RequestItineraryController().getAgenciesByType(types);
        } catch (FailedOperationException | IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
            return Collections.emptyList();
        }
        if (agencies != null && !agencies.isEmpty()) {
            for (AgencyBean agencyResult : agencies) {
                System.out.println(agencyResult.getName() + " - " + agencyResult.getRating());
            }
        } else {
            System.out.println(ERROR + "No agencies found");
            return Collections.emptyList();
        }

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter Agency (enter stop to terminate): ");
            agency = input.nextLine();
            if (!agency.isEmpty()) {
                if (agency.equalsIgnoreCase("stop")) {
                    break;
                }
                agenciesSelected = selectAgency(agencies, agency);
            }else {
                System.out.println(CHOICE_ERROR);
            }
        }

        return agenciesSelected;
    }

    public List<String> selectAgency(List<AgencyBean> agencies, String agency) {
        List<String> agenciesSelected = new ArrayList<>();
        for (AgencyBean agencyResult : agencies) {
            if (agencyResult.getName().equals(agency)) {
                agenciesSelected.add(agencyResult.getName());
            } else {
                System.out.println(ERROR + "Agency " + agency + " does not exist");
            }
        }

        return agenciesSelected;
    }
}
