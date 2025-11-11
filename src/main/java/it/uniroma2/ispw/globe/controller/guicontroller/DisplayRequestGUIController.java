package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.controller.applicationcontroller.RequestItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.engineering.session.NavigationData;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class DisplayRequestGUIController extends AbstractGUIController {
    @FXML
    private Label daysLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Label otherRequestsLabel;
    @FXML
    private HBox typesHBox;
    @FXML
    private Button saveRequestButton;
    @FXML
    private Button createItineraryButton;
    @FXML
    private Label drivingHoursLabel;
    @FXML
    private Label modeLabel;
    @FXML
    private Label difficultLabel;
    @FXML
    private Label distanceLabel;
    @FXML
    private TabPane typeTabPane;
    @FXML
    private Tab onTheRoadTab;
    @FXML
    private Tab natureTab;
    @FXML
    private VBox attractionsVBox;
    @FXML
    private VBox citiesVBox;

    private static final String LIGHT = "label-light";


    private String sessionId;
    private String requestId;
    private Node prev;

    public void initialize(String sessionId) {
        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.requestId = data.getRequestID();
        this.prev = data.getPrev();

        RequestBean request;
        List<Object> optionals;

        if (requestId != null) {
            createItineraryButton.setVisible(true);
            saveRequestButton.setVisible(false);
            //create proposal use case

            try {
                request = new ResponseRequestController().getAgencyRequest(requestId, sessionId);
                optionals = new ResponseRequestController().getRequestOptional(requestId, sessionId);
            } catch (FailedOperationException | IncorrectDataException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                goBack();
                return;
            }
            userLabel.setText(request.getUser());
        } else {
            saveRequestButton.setVisible(true);
            createItineraryButton.setVisible(false);

            List<AgencyBean> agencies;
            try {
                request = new RequestItineraryController().getRequest(requestId, sessionId);
                optionals = new RequestItineraryController().getRequestOptional(requestId, sessionId);
                agencies = new RequestItineraryController().getAgencies(sessionId);
            } catch (FailedOperationException | IncorrectDataException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                goBack();
                return;
            }

            userLabel.setText("");
            StringBuilder agenciesString = new StringBuilder();
            for (AgencyBean agency : agencies) {
                agenciesString.append(agency.getName()).append(", ");
            }
            agenciesString.setLength(agenciesString.length() - 2);
            userLabel.setText(agenciesString.toString());
        }

        dispalyPlaces(request);

        otherRequestsLabel.setText(request.getOtherRequests());
        daysLabel.setText(String.valueOf(request.getDayNum()));
        for (String type : request.getTypes()) {
            Label typeLabel = new Label(type);
            typeLabel.getStyleClass().add("rounded-label");
            typesHBox.getChildren().add(typeLabel);
        }

        displayOptinal(optionals);
    }

    public void dispalyPlaces(RequestBean request) {
        for (String city: request.getCities()) {
            try {
                CityBean cityBean = new RequestItineraryController().getCity(city);
                Label cityLabel = new Label(cityBean.getName()+", "+cityBean.getCountry());
                cityLabel.getStyleClass().add(LIGHT);
                citiesVBox.getChildren().add(cityLabel);
            } catch (FailedOperationException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                goBack();
                return;
            }
        }

        for (String attraction: request.getAttractions()) {
            try {
                AttractionBean attractionBean = new RequestItineraryController().getAttraction(attraction);
                Label attractionLabel = new Label(attractionBean.getName()+", "+attractionBean.getCity());
                attractionLabel.getStyleClass().add(LIGHT);
                attractionsVBox.getChildren().add(attractionLabel);
            } catch (FailedOperationException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                goBack();
                return;
            }
        }
    }

    public void displayOptinal(List<Object> optionals) {
        if (optionals != null && !optionals.isEmpty()) {
            for (Object optional: optionals) {
                if (optional instanceof OnTheRoadBean onTheRoad) {
                    onTheRoadTab.setDisable(false);
                    typeTabPane.getSelectionModel().select(onTheRoadTab);
                    drivingHoursLabel.setText(String.valueOf((onTheRoad).getDayDrivingHours()));
                    modeLabel.setText(onTheRoad.getMode());
                } else if (optional instanceof NatureBean nature) {
                    natureTab.setDisable(false);
                    typeTabPane.getSelectionModel().select(natureTab);
                    difficultLabel.setText(nature.getDifficulty());
                    distanceLabel.setText(String.valueOf(nature.getTrekkingDistance()));
                }
            }
        }
    }

    public void createItinerary(ActionEvent event) {
        try {
            new ResponseRequestController().setPendingRequest(sessionId,requestId);
        } catch (FailedOperationException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToCreateItineraryGUI(sessionId,requestId,root);
    }
    public void saveRequest(ActionEvent event) {
        try {
            new RequestItineraryController().saveRequest(sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            goBack();
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToManageItineraryGUI(sessionId,root);
    }

    public void goBack() {
        BorderPane root = (BorderPane) userLabel.getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
