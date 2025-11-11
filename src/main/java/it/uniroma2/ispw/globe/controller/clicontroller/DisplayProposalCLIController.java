package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.bean.PaymentBean;
import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.bean.ProposalBean;

import java.util.Scanner;

import static it.uniroma2.ispw.globe.constants.ProposalState.*;
import static it.uniroma2.ispw.globe.constants.UserType.AGENCY;

public class DisplayProposalCLIController {
    private String sessionId;
    private String requestId;
    private String proposalId;

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";

    DisplayProposalCLIController(String sessionId, String requestId, String proposalId) {
        this.sessionId = sessionId;
        this.requestId = requestId;
        this.proposalId = proposalId;
    }

    public void start() {
        System.out.println("# DISPLAY PROPOSAL #");

        ProposalBean proposal;
        String type = new AcceptItineraryController().getAccountType(sessionId);

        try {
            proposal = new AcceptItineraryController().getProposal(proposalId,sessionId);

            System.out.println("> ID: " + proposal.getID());
            System.out.println("    > Description: " + proposal.getDescription());
            System.out.println("    > Price: " + proposal.getPrice());
            if (type.equals(AGENCY)){
                System.out.println("    > User: " + proposal.getUser());
            } else {
                System.out.println("    > Agency: " + proposal.getAgency());
            }
            System.out.println("    > Status: " + proposal.getAccepted());
            System.out.println();

            while (true) {
                int choice;
                choice = showMenu(proposal.getAccepted());

                switch(choice) {
                    case 1 -> showItinerary();
                    case 2 -> {
                        if (proposal.getAccepted().equals(PENDING) && !type.equals(AGENCY)){
                            acceptProposal();
                        } else if (requestId != null) {
                            saveProposal();
                        }
                    }
                    case 3 -> {
                        if (proposal.getAccepted().equals(PENDING) && !type.equals(AGENCY)){
                            rejectProposal();
                        }
                    }
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println(CHOICE_ERROR);
                }
            }
        } catch (FailedOperationException | IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }
    }

    public int showMenu(String status) {

        String type = new AcceptItineraryController().getAccountType(sessionId);
        int maxChoice;

        System.out.println("What do you want do?\n");

        System.out.println("1 -> Show Itinerary");
        if (status.equals(PENDING) && !type.equals(AGENCY)){
            System.out.println("2 -> Accept Proposal");
            System.out.println("3 -> Refuse Proposal");
            System.out.println("4 -> Quit");
            maxChoice = 4;
        } else if (requestId != null) {
            System.out.println("2 -> Save Proposal");
            System.out.println("3 -> Quit");
            maxChoice = 3;
        } else {
            System.out.println("2 -> Quit");
            maxChoice = 2;
        }


        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.print("Please enter your choice: ");
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-" + maxChoice + "]")) {
                choice = Integer.parseInt(strChoice);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }

        return choice;
    }

    public void showItinerary() {
        String itineraryId;

        if (proposalId != null) {
            try {
                itineraryId = new AcceptItineraryController().getProposalItinerary(proposalId).getId();
            } catch (FailedOperationException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
                return;
            }
        } else {
            itineraryId = null;
        }
        DisplayItineraryCLIController controller = new DisplayItineraryCLIController(sessionId,itineraryId,requestId,proposalId);
        controller.start();
    }

    public void acceptProposal() {
        PaymentBean paymentResult;
        try {
            paymentResult = new AcceptItineraryController().sendResponse(proposalId,ACCEPTED);
        } catch (FailedOperationException | IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
            return;
        }

        if (paymentResult != null) {
            System.out.println("Payment from " + paymentResult.getPayerUsername() + " to " + paymentResult.getPayeeUsername() + " accepted");
        } else {
            System.out.println("Payment from rejected");
        }
    }

    public void rejectProposal() {
        try {
            new AcceptItineraryController().sendResponse(proposalId,REJECTED);
        } catch (FailedOperationException | IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }
    }

    public void saveProposal() {
        try {
            new ResponseRequestController().saveProposal(sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            System.out.println(ERROR + e.getMessage());
        }
    }
}
