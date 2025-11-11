package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.bean.ProposalBean;

import java.util.List;
import java.util.Scanner;

public class ManageItineraryCLIController {
    private String sessionId;

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";

    private static final String ENTER_CHOICE = "Please enter your choice: ";

    public ManageItineraryCLIController(String sessionId) {
        this.sessionId = sessionId;
    }


    public void start() {
        System.out.println("# MANAGE ITINERARY #");

        while(true) {
            int choice;
            choice = showMenu();

            switch(choice) {
                case 1 -> showItineraries();
                case 2 -> showProposals();
                case 3 -> createItinerary();
                case 4 -> createRequest();
                case 5 -> {
                    System.exit(0);
                    return;
                }
                default -> System.out.println(CHOICE_ERROR);
            }
        }
    }

    public int showMenu() {
        System.out.println("What do you want do?\n");
        System.out.println("1 -> Show Itineraries");
        System.out.println("2 -> Show Proposals");
        System.out.println("3 -> Create new Itinerary");
        System.out.println("4 -> Create new Request");
        System.out.println("5 -> Quit");

        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.print(ENTER_CHOICE);
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-5]")) {
                choice = Integer.parseInt(strChoice);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }

        return choice;
    }

    public void showItineraries() {
        List<ItineraryBean> itineraries;
        try {
            itineraries = new ManageItineraryController().getUserItineraries(sessionId);

            System.out.println("Itineraries:");
            for(ItineraryBean itinerary : itineraries) {
                System.out.println("> ID: " + itinerary.getId());
                System.out.println("    > Name: " + itinerary.getName());
                System.out.println("    > Days: " + itinerary.getDuration());
                System.out.println("    > Description: " + itinerary.getDescription());
            }

            String choice = getItineraryID(itineraries);

            if (!choice.equalsIgnoreCase("back")) {
                DisplayItineraryCLIController controller = new DisplayItineraryCLIController(sessionId,choice,null,null);
                controller.start();
            }
        } catch (IncorrectDataException | FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
    }

    public String getItineraryID(List<ItineraryBean> itineraries) {
        System.out.println("Which one do you want to see (insert ID to see or insert 'back' to go back)? ");

        Scanner input = new Scanner(System.in);
        String choice;
        while (true) {
            System.out.print(ENTER_CHOICE);
            choice = input.nextLine();
            if (choice.equalsIgnoreCase("back")) {
                return choice;
            } else {
                for (ItineraryBean itinerary : itineraries) {
                    if (itinerary.getId().equals(choice)) {
                        return itinerary.getId();
                    }
                }
                System.out.println(CHOICE_ERROR);
            }
        }
    }

    public void showProposals() {
        List<ProposalBean> proposals;
        try {
            proposals = new ManageItineraryController().getUserProposals(sessionId);
            System.out.println("Proposals:");
            for(ProposalBean proposal : proposals) {
                System.out.println("> ID: " + proposal.getID());
                System.out.println("    > Agency: " + proposal.getAgency());
            }
            String choice = getProposalID(proposals);

            if (!choice.equalsIgnoreCase("back")) {
                DisplayProposalCLIController controller = new DisplayProposalCLIController(sessionId, null, choice);
                controller.start();
            }

        } catch (FailedOperationException | IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }
    }

    public String getProposalID(List<ProposalBean> proposals) {
        System.out.println("Which proposal do you want to see (insert ID to see or insert 'back' to go back)? ");

        Scanner input = new Scanner(System.in);
        String choice;
        while (true) {
            System.out.print(ENTER_CHOICE);
            choice = input.nextLine();
            if (choice.equalsIgnoreCase("back")) {
                return choice;
            } else {
                for (ProposalBean proposal : proposals) {
                    if (proposal.getID().equals(choice)) {
                        return proposal.getID();
                    }
                }
                System.out.println(CHOICE_ERROR);
            }
        }
    }

    public void createItinerary() {
        CreateItineraryCLIController controller = new CreateItineraryCLIController(sessionId,null);
        controller.start();
    }

    public void createRequest() {
        CreateRequestCLIController controller = new CreateRequestCLIController(sessionId);
        controller.start();
    }
}
