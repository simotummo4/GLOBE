package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.controller.applicationcontroller.RequestItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisplayRequestCLIController {
    private String sessionId;
    private String requestId;

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";
    private static final String DASH = "        - ";
    private static final String DASH2 = ", ";


    DisplayRequestCLIController(String sessionId, String requestId) {
        this.sessionId = sessionId;
        this.requestId = requestId;
    }

    public void start() {
        System.out.println("# DISPLAY REQUEST #");

        RequestBean request;
        List<Object> optionals;
        List<AgencyBean> agencies = new ArrayList<>();

        try {
            if (requestId == null) {
                // requestItinerary use case
                request = new RequestItineraryController().getRequest(requestId, sessionId);
                optionals = new RequestItineraryController().getRequestOptional(requestId, sessionId);
                agencies = new RequestItineraryController().getAgencies(sessionId);
            } else {
                // createProposal use case
                request = new ResponseRequestController().getAgencyRequest(requestId, sessionId);
                optionals = new ResponseRequestController().getRequestOptional(requestId, sessionId);
            }
        } catch (FailedOperationException | IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
            return;
        }

        System.out.println("> ID: " + request.getID());

        if (requestId == null) {
            System.out.println("    > Agencies: ");
            for (AgencyBean agency : agencies) {
                System.out.println( DASH + agency.getName() + DASH2 + agency.getRating());
            }
        } else {
            System.out.println("    > User: "+ request.getUser());
        }
        System.out.println("    > Days: " + request.getDayNum());
        System.out.println("    > Requests: " + request.getOtherRequests());
        System.out.println("    > Types: " + request.getTypes());

        displayOptional(optionals);
        List<String> citiesID = request.getCities();
        List<String> attractionsID = request.getAttractions();
        displayCities(citiesID);
        displayAttractions(attractionsID);

        if (requestId == null) {
            int choice = showUserMenu();
            switch (choice) {
                case 1 -> saveRequest();
                case 2 -> {
                    // go back
                }
                default -> {
                    // no default case
                }
            }
        } else {
            int choice = showAgencyMenu();
            switch (choice) {
                case 1 -> createItinerary();
                case 2 -> {
                    // go back
                }
                default -> {
                    // no default case
                }
            }
        }
    }

    public void displayOptional(List<Object> optionals) {
        for (Object optional: optionals) {
            if (optional instanceof OnTheRoadBean onTheRoad) {
                System.out.println("    > Travel mode: " + onTheRoad.getMode());
                System.out.println("    > Day driving hours: " + onTheRoad.getDayDrivingHours());

            } else if (optional instanceof NatureBean nature) {
                System.out.println("    > Trekking difficulty: " + nature.getDifficulty());
                System.out.println("    > Trekking distance:   " + nature.getTrekkingDistance());
            }
        }
    }

    public void displayCities(List<String> citiesID) {
        System.out.println("    > Cities: ");
        for (String cityID: citiesID) {
            try {
                CityBean city =  new RequestItineraryController().getCity(cityID);
                System.out.println( DASH + city.getName() + DASH2 + city.getCountry());
            } catch (FailedOperationException e) {
                System.out.println(ERROR + e.getMessage());
                return;
            }
        }
    }

    public void displayAttractions(List<String> attractionsID) {
        System.out.println("    > Attractions: ");
        for (String attractionID: attractionsID) {
            try {
                AttractionBean attraction = new RequestItineraryController().getAttraction(attractionID);
                System.out.println( DASH + attraction.getName() + DASH2 + attraction.getAddress());
            } catch (FailedOperationException e) {
                System.out.println(ERROR + e.getMessage());
                return;
            }
        }
    }

    public int showUserMenu() {
        System.out.println("What do you want do?\n");

        System.out.println("1 -> Save Request");
        System.out.println("2 -> Go Back");

        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.print("Please enter your choice : ");
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-2]")) {
                choice = Integer.parseInt(strChoice);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        return choice;
    }

    public int showAgencyMenu() {
        System.out.println("What do you want do?\n");

        System.out.println("1 -> Create Itinerary");
        System.out.println("2 -> Go Back");

        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.print("Please enter your choice: ");
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-2]")) {
                choice = Integer.parseInt(strChoice);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        return choice;
    }

    public void createItinerary() {
        try {
            new ResponseRequestController().setPendingRequest(sessionId, requestId);
        } catch (FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
        CreateItineraryCLIController controller = new CreateItineraryCLIController(sessionId,requestId);
        controller.start();
    }


    public void saveRequest() {
        try {
            new RequestItineraryController().saveRequest(sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            System.out.println(ERROR + e.getMessage());
        }
    }
}
