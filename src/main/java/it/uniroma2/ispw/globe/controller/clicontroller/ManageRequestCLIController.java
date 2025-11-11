package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.bean.RequestBean;
import it.uniroma2.ispw.globe.bean.ProposalBean;

import java.util.List;
import java.util.Scanner;

import static it.uniroma2.ispw.globe.constants.ProposalState.PENDING;

public class ManageRequestCLIController {
    private String sessionId;

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";

    private static final String ENTER_CHOICE = "Please enter your choice: ";

    public ManageRequestCLIController(String sessionId) {
        this.sessionId = sessionId;
    }

    public void start() {
        while(true) {
            System.out.println("# MANAGE REQUEST #");
            int choice;
            choice = showMenu();

            switch(choice) {
                case 1 -> showProposals();
                case 2 -> showRequests();
                case 3 -> {
                    System.exit(0);
                    return;
                }
                default -> System.out.println(CHOICE_ERROR);
            }
        }
    }

    public int showMenu() {
        System.out.println("What do you want do?\n");
        System.out.println("1 -> Show Proposals");
        System.out.println("2 -> Show Requests");
        System.out.println("3 -> Quit");

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
        return choice;
    }

    public void showProposals() {
        List<ProposalBean> proposals;
        try {
            proposals = new ResponseRequestController().getAgencyProposals(sessionId);

            System.out.println("Itineraries:");
            for(ProposalBean proposal : proposals) {
                System.out.println("> ID: " + proposal.getID());
                System.out.println("    > User: " + proposal.getUser());
                System.out.println("    > Description: " + proposal.getDescription());
                System.out.println("    > Price: " + proposal.getPrice());
                System.out.println("    > Status: " + proposal.getAccepted());
            }

            String choice = getProposalID(proposals);

            if (!choice.equalsIgnoreCase("back")) {
                DisplayProposalCLIController controller = new DisplayProposalCLIController(sessionId, null, choice);
                controller.start();
            }
        } catch (IncorrectDataException | FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
    }

    public void showRequests() {
        List<RequestBean> requests;
        int flag = 0;
        try {
            requests = new ResponseRequestController().getAgencyRequests(sessionId);

            System.out.println("Requests:");
            for(RequestBean request : requests) {
                if (request.getAccepted().equals(PENDING)) {
                    flag = 1;
                    System.out.println("> ID: " + request.getID());
                    System.out.println("    > User: " + request.getUser());
                    System.out.println("    > Description: " + request.getOtherRequests());
                    System.out.println("    > Duration: " + request.getDayNum());
                }
            }
            if (flag == 0) {
                System.out.println("No requests found");
            }

            String choice = getRequestID(requests);

            if (!choice.equalsIgnoreCase("back")) {
                new ResponseRequestController().setPendingRequest(sessionId,choice);
                DisplayRequestCLIController controller = new DisplayRequestCLIController(sessionId,choice);
                controller.start();
            }
        } catch (IncorrectDataException | FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());

        }
    }

    public String getProposalID(List<ProposalBean> proposals) {
        System.out.println("Which request do you want to see (insert ID to see or insert 'back' to go back)? ");

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

    public String getRequestID(List<RequestBean> requests) {
        System.out.println("Which one do you want to see (insert ID to see or insert 'back' to go back)? ");

        Scanner input = new Scanner(System.in);
        String choice;
        while (true) {
            System.out.print(ENTER_CHOICE);
            choice = input.nextLine();
            if (choice.equalsIgnoreCase("back")) {
                return choice;
            } else {
                for (RequestBean request : requests) {
                    if (request.getID().equals(choice)) {
                        return request.getID();
                    }
                }
                System.out.println(CHOICE_ERROR);
            }
        }
    }
}
