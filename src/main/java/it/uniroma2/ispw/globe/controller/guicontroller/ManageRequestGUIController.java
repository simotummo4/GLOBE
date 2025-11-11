package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.bean.RequestBean;
import it.uniroma2.ispw.globe.bean.ProposalBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static it.uniroma2.ispw.globe.constants.ProposalState.*;

public class ManageRequestGUIController extends AbstractGUIController {

    @FXML
    private VBox proposalsVBox;
    @FXML
    private VBox requestsVBox;

    private String sessionId;

    public void initialize(String sessionId) {
        this.sessionId = sessionId;

        List<ProposalBean> proposals;
        List<RequestBean> requests;
        try {
            proposals = new ResponseRequestController().getAgencyProposals(sessionId);
            requests = new ResponseRequestController().getAgencyRequests(sessionId);
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            Stage stage = (Stage) proposalsVBox.getScene().getWindow();
            ViewManager viewManager = new ViewManager();
            viewManager.goToLogInGUI(stage);
            return;
        }

        for (ProposalBean proposal : proposals) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/proposalElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button proposalBox = loader.load();
                proposalBox.setUserData(proposal.getID());
                proposalBox.setOnAction(this::viewProposal);
                Label nameLabel = (Label) proposalBox.getGraphic().lookup("#nameLabel");
                nameLabel.setText(proposal.getUser());
                Label descriptionLabel = (Label) proposalBox.getGraphic().lookup("#descriptionLabel");
                descriptionLabel.setText(proposal.getDescription());
                Label priceLabel = (Label) proposalBox.getGraphic().lookup("#priceLabel");
                priceLabel.setText(String.valueOf(proposal.getPrice()));

                if (proposal.getAccepted().equals(ACCEPTED)) {
                    ImageView acceptedImage = (ImageView) proposalBox.getGraphic().lookup("#acceptedImage");
                    acceptedImage.setVisible(true);
                } else if (proposal.getAccepted().equals(REJECTED)){
                    ImageView acceptedImage = (ImageView) proposalBox.getGraphic().lookup("#rejectedImage");
                    acceptedImage.setVisible(true);
                }

                proposalsVBox.getChildren().add(proposalBox);
            } catch (IOException e) {
                new ErrorPopUpGUIController().createPopUp("'Manage Request' page loading failed");
                Stage stage = (Stage) proposalsVBox.getScene().getWindow();
                ViewManager viewManager = new ViewManager();
                viewManager.goToLogInGUI(stage);
                return;
            }
        }

        for (RequestBean request : requests) {
            if (request.getAccepted().equals(PENDING)) {
                try {
                    URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/requestElement.fxml").toURI().toURL();
                    FXMLLoader loader = new FXMLLoader(url);
                    Button requestsBox = loader.load();
                    requestsBox.setUserData(request.getID());
                    requestsBox.setOnAction(this::viewRequest);
                    Label nameLabel = (Label) requestsBox.getGraphic().lookup("#nameLabel");
                    nameLabel.setText(request.getUser());
                    Label descriptionLabel = (Label) requestsBox.getGraphic().lookup("#descriptionLabel");
                    descriptionLabel.setText(request.getOtherRequests());
                    Label daysLabel = (Label) requestsBox.getGraphic().lookup("#daysLabel");
                    daysLabel.setText(String.valueOf(request.getDayNum()));

                    requestsVBox.getChildren().add(requestsBox);

                } catch (IOException e) {
                    new ErrorPopUpGUIController().createPopUp("'Manage Request' page loading failed");
                    Stage stage = (Stage) proposalsVBox.getScene().getWindow();
                    ViewManager viewManager = new ViewManager();
                    viewManager.goToLogInGUI(stage);
                    return;
                }
            }
        }
    }

    public void viewProposal(ActionEvent event) {
        String proposalID = (String) ((Button)event.getSource()).getUserData();

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayProposalGUI(sessionId, null, proposalID, root);
    }

    public void viewRequest(ActionEvent event) {
        String requestID = (String) ((Button)event.getSource()).getUserData();

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayRequestGUI(sessionId,requestID, root);
    }
}
