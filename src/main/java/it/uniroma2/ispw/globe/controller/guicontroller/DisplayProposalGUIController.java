package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.bean.PaymentBean;
import it.uniroma2.ispw.globe.engineering.session.NavigationData;
import it.uniroma2.ispw.globe.bean.ProposalBean;
import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import static it.uniroma2.ispw.globe.constants.ProposalState.*;
import static it.uniroma2.ispw.globe.constants.UserType.AGENCY;

public class DisplayProposalGUIController extends AbstractGUIController {
    @FXML
    private Label priceLabel;
    @FXML
    private Label agencyLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private HBox responseHBox;
    @FXML
    private Button saveButton;

    private String sessionId;
    private String requestID;
    private String proposalID;
    private Node prev;

    public void initialize(String sessionId) {
        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.requestID = data.getRequestID();
        this.proposalID = data.getProposalID();
        this.prev = data.getPrev();

        String type = new AcceptItineraryController().getAccountType(sessionId);

        ProposalBean proposal;
        try {
            proposal = new AcceptItineraryController().getProposal(proposalID, sessionId);
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            goBack();
            return;
        }
        descriptionLabel.setText(proposal.getDescription());
        agencyLabel.setText(proposal.getAgency());
        priceLabel.setText(String.valueOf(proposal.getPrice()));
        saveButton.setVisible(false);
        if (!proposal.getAccepted().equals(PENDING) || type.equals(AGENCY)){
            responseHBox.getChildren().clear();
        }
        if (type.equals(AGENCY)){
            agencyLabel.setText(proposal.getUser());
        }
        if (requestID != null) {
            responseHBox.getChildren().clear();
            saveButton.setVisible(true);
        }
    }

    public void showItinerary(ActionEvent event) {
        String itineraryId;

        if (proposalID != null) {
            try {
                itineraryId = new AcceptItineraryController().getProposalItinerary(proposalID).getId();
            } catch (FailedOperationException | IncorrectDataException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                return;
            }
        } else {
            itineraryId = null;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayItineraryGUI(sessionId,itineraryId,requestID,proposalID, root);
    }

    public void acceptProposal() {
        PaymentBean paymentResult;
        try {
            paymentResult = new AcceptItineraryController().sendResponse(proposalID,ACCEPTED);
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        if (paymentResult != null) {
            responseHBox.getChildren().clear();

            new PaymentGUIController().createPopUp(paymentResult, proposalID);

            BorderPane rootVM = (BorderPane) priceLabel.getScene().getRoot();
            ViewManager viewManager = new ViewManager();
            viewManager.goToManageItineraryGUI(sessionId, rootVM);
        } else {
            new ErrorPopUpGUIController().createPopUp("Payment failed");
        }
    }

    public void rejectProposal(ActionEvent event) {
        try {
            new AcceptItineraryController().sendResponse(proposalID,REJECTED);
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }
        responseHBox.getChildren().clear();

        BorderPane rootVM = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToManageItineraryGUI(sessionId, rootVM);
    }

    public void saveProposal(ActionEvent event) {
        try {
            new ResponseRequestController().saveProposal(sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToManageRequestGUI(sessionId, root);
    }

    public void goBack() {
        BorderPane root = (BorderPane) agencyLabel.getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
