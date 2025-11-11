package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.engineering.session.NavigationData;
import it.uniroma2.ispw.globe.bean.ProposalBean;
import it.uniroma2.ispw.globe.bean.RequestBean;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateProposalGUIController extends AbstractGUIController{
    @FXML
    private Label userLabel;
    @FXML
    private Label requestLabel;
    @FXML
    private Label daysLabel;
    @FXML
    private TextField priceField;
    @FXML
    private TextField descriptionField;
    @FXML
    private HBox typesHBox;

    private String sessionId;
    private String requestId;

    private Node prev;

    public void initialize(String sessionId) {
        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.requestId = data.getRequestID();
        this.prev = data.getPrev();

        RequestBean request;
        ProposalBean proposal;
        try {
            request = new ResponseRequestController().getAgencyRequest(requestId, sessionId);
            proposal = new ResponseRequestController().getProposal(null,sessionId);
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            goBack();
            return;
        }

        userLabel.setText(request.getUser());
        requestLabel.setText(request.getOtherRequests());
        daysLabel.setText(String.valueOf(request.getDayNum()));
        for (String type : request.getTypes()){
            Label typeLabel = new Label(type);
            typeLabel.getStyleClass().add("rounded-label");
            typesHBox.getChildren().add(typeLabel);
        }

        if (proposal != null){
            priceField.setText(String.valueOf(proposal.getPrice()));
            descriptionField.setText(proposal.getDescription());
        }
    }

    public void createProposal(ActionEvent event) {

        int count = 0;

        if (priceField.getText().isEmpty()) {
            count ++;
        }
        if (descriptionField.getText().isEmpty()) {
            count ++;
        }
        if (count>0) {
            return;
        }

        ProposalBean proposalBean = new ProposalBean();
        try {
            proposalBean.setPrice(Double.parseDouble(priceField.getText()));
            proposalBean.setDescription(descriptionField.getText());

            new ResponseRequestController().createProposal(proposalBean,userLabel.getText(),requestId,sessionId);
        } catch (FailedOperationException | DuplicateItemException | NumberFormatException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayProposalGUI(sessionId,requestId,null, root);
    }

    public void goBack() {
        BorderPane root = (BorderPane) daysLabel.getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
