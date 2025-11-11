package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.bean.RequestBean;
import it.uniroma2.ispw.globe.bean.ProposalBean;

import java.util.Scanner;

public class CreateProposalCLIController {
    private String sessionId;
    private String requestId;

    private static final String ERROR = "ERROR: ";

    CreateProposalCLIController(String sessionId, String requestId) {
        this.sessionId = sessionId;
        this.requestId = requestId;
    }

    public void start() {
        System.out.println("# CREATE PROPOSAL #");

        try {
            RequestBean request = new ResponseRequestController().getAgencyRequest(requestId, sessionId);

            if (request != null) {
                System.out.println("> User " + request.getUser() + " request:");
                System.out.println("    > Description: " + request.getOtherRequests());
                System.out.println("    > Types: " + request.getTypes());
                System.out.println("    > Duration: " + request.getDayNum());

                getProposalData(request.getUser());
            } else {
                System.out.println(ERROR + "No request found");
            }
        } catch (FailedOperationException | IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }
    }

    public void getProposalData(String user) {
        String strPrice;
        String description;
        double price;

        ProposalBean proposalBean = new ProposalBean();

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter Proposal description: ");
            description = input.nextLine();
            try {
                proposalBean.setDescription(description);
                break;
            } catch (IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
        while (true) {
            System.out.print("Please enter Proposal price duration: ");
            strPrice = input.nextLine();
            try {
                price = Double.parseDouble(strPrice);
                proposalBean.setPrice(price);
                break;
            } catch (NumberFormatException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }

        try {
            new ResponseRequestController().createProposal(proposalBean,user,requestId,sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            System.out.println(ERROR + e.getMessage());
            return;
        }

        DisplayProposalCLIController controller = new DisplayProposalCLIController(sessionId,requestId,null);
        controller.start();
    }
}
