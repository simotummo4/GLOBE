package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.bean.AttractionBean;
import it.uniroma2.ispw.globe.bean.CityBean;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.bean.StepBean;
import javafx.util.Pair;

import java.util.List;
import java.util.Scanner;

public class DisplayItineraryCLIController {
    private String sessionId;
    private String itineraryId;
    private String requestId;
    private String proposalId;

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";

    DisplayItineraryCLIController(String sessionId, String itineraryId, String requestId, String proposalId) {
        this.sessionId = sessionId;
        this.itineraryId = itineraryId;
        this.requestId = requestId;
        this.proposalId = proposalId;
    }

    public void start() {
        System.out.println("# DISPLAY ITINERARY #");

        ItineraryBean itinerary;
        List<StepBean> steps;
        try {
            itinerary = new CreateItineraryController().getItinerary(itineraryId,sessionId);
            steps = new AcceptItineraryController().getSteps(itineraryId,sessionId);

            System.out.println("> ID: " + itinerary.getId());
            System.out.println("    > Name: " + itinerary.getName());
            System.out.println("    > Days: " + itinerary.getDuration());
            System.out.println("    > Description: " + itinerary.getDescription());
            System.out.println("    > Types: " + itinerary.getTypes());

            if (itinerary.getAccommodations() != null && !itinerary.getAccommodations().isEmpty()) {
                System.out.println("    > Accommodations: ");
                for (int i = 0; i < itinerary.getAccommodations().size(); i++) {
                    Pair<String,String> accommodation = itinerary.getAccommodations().get(i);
                    System.out.println("        - " + accommodation.getKey() + ", " + accommodation.getValue());
                }
            }
            if (itinerary.getInboundFlightDepartureTime() != -1) {
                System.out.println("    > Inbound Flight: " + itinerary.getInboundFlightDepartureTime() + " - " + itinerary.getInboundFlightArrivalTime());
                System.out.println("    > Outbound Flight: " + itinerary.getOutboundFlightDepartureTime() + " - " + itinerary.getOutboundFlightArrivalTime());
            }

            showSteps(steps);

        } catch (FailedOperationException | IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }

        if (requestId != null) {
            return;
        }

        int choice;
        if (!(proposalId != null || itineraryId != null)) {
            choice = showUserMenu();
            if (choice == 1) {
                saveItinerary();
            }
        }
    }

    public int showUserMenu() {
        System.out.println("What do you want do?\n");

        System.out.println("1 -> Save Itinerary");
        System.out.println("2 -> Go Back ");

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

    public void showSteps(List<StepBean> steps) {
        try {
            System.out.println("    > Steps: ");
            for (StepBean step : steps) {
                CityBean city = new AcceptItineraryController().getCity(step.getNum(),step.getCity().get(0),null);
                System.out.println("        > Day " + step.getNum() + ": ");
                System.out.println("            > City: " + city.getName() + ", " + city.getCountry());
                System.out.println("            > Attractions: ");
                for (String attractionID : step.getAttractions()) {
                    AttractionBean attraction = new AcceptItineraryController().getAttraction(step.getNum(),attractionID,null);
                    System.out.println("                -" + attraction.getName() + ", " + attraction.getAddress());
                }
            }
        } catch (FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
    }

    public void saveItinerary() {
        if (itineraryId == null) {
            try {
                new CreateItineraryController().saveItinerary(sessionId);
            } catch (FailedOperationException | DuplicateItemException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
    }
}
