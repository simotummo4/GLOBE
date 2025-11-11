package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.engineering.session.NavigationData;
import it.uniroma2.ispw.globe.exception.AttractionNotAddedException;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.constants.ItineraryType.*;

public class CreateItineraryGUIController extends AbstractGUIController {
    @FXML
    private TextField cityField;
    @FXML
    private TextField itineraryField;
    @FXML
    private Label dayLabel;
    @FXML
    private VBox cityVBox;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField attractionField;
    @FXML
    private VBox attractionVBox;
    @FXML
    private VBox cityResultVBox;
    @FXML
    private VBox attractionResultVBox;
    @FXML
    private VBox requestVBox;
    @FXML
    private Label userLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private HBox typesHBox;
    @FXML
    private VBox requestCityVBox;
    @FXML
    private VBox requestAttractionVBox;
    @FXML
    private VBox accommodationVBox;
    @FXML
    private VBox flightVBox;
    @FXML
    private Button accommodationButton;
    @FXML
    private Button flightButton;
    @FXML
    private TextField accommodationField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField inDepartureTimeLabel;
    @FXML
    private TextField inArrivalTimeLabel;
    @FXML
    private TextField outDepartureTimeLabel;
    @FXML
    private TextField outArrivalTimeLabel;
    @FXML
    private VBox accommodationResultVBox;
    @FXML
    private Button onTheRoadButton;
    @FXML
    private Button natureButton;
    @FXML
    private Button cultureButton;
    @FXML
    private Button relaxButton;
    @FXML
    private Button cityButton;



    private String sessionId;
    private String requestId;

    private Node prev;

    private static final String DEFAULT_BUTTON = "button-default";
    private static final String SEARCH_BUTTON = "button-search";
    private static final String LABEL_LIGHT = "label-light";

    public void initialize(String sessionId) {

        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.requestId = data.getRequestID();
        this.prev = data.getPrev();


        accommodationVBox.setVisible(false);
        flightVBox.setVisible(false);

        onTheRoadButton.setUserData(false);
        natureButton.setUserData(false);
        cultureButton.setUserData(false);
        relaxButton.setUserData(false);
        cityButton.setUserData(false);

        if (requestId == null) {
            requestVBox.setVisible(false);
            return;
        }

        try {
            //create proposal use case
            RequestBean requestBean;
            requestBean = new ResponseRequestController().getAgencyRequest(requestId,sessionId);

            if (requestBean != null) {
                requestVBox.setVisible(true);
                userLabel.setText(requestBean.getUser());
                descriptionLabel.setText(requestBean.getOtherRequests());
                for (String type : requestBean.getTypes()) {
                    Label typeLabel = new Label(type+ " ");
                    typeLabel.getStyleClass().add(LABEL_LIGHT);
                    typesHBox.getChildren().add(typeLabel);
                }
                for (String city : requestBean.getCities()) {
                    CityBean cityBean = new AcceptItineraryController().getCity(0,city,null);
                    Label cityLabel = new Label(cityBean.getName()+" - "+cityBean.getCountry());
                    cityLabel.getStyleClass().add(LABEL_LIGHT);
                    requestCityVBox.getChildren().add(cityLabel);
                }
                for (String attraction : requestBean.getAttractions()) {
                    AttractionBean attractionBean = new AcceptItineraryController().getAttraction(0,attraction,null);
                    Label attractionLabel = new Label(attractionBean.getName()+" - "+attractionBean.getCity());
                    attractionLabel.getStyleClass().add(LABEL_LIGHT);
                    requestAttractionVBox.getChildren().add(attractionLabel);
                }
                if (requestBean.isFlight()) {
                    flightVBox.setVisible(true);
                    flightButton.setUserData(true);
                    flightButton.setVisible(false);
                } else {
                    flightVBox.setVisible(false);
                    flightButton.setUserData(false);
                    flightButton.setVisible(false);
                }
                if (requestBean.isAccommodation()) {
                    accommodationVBox.setVisible(true);
                    accommodationButton.setUserData(true);
                    accommodationButton.setVisible(false);
                } else {
                    accommodationVBox.setVisible(false);
                    accommodationButton.setUserData(false);
                    accommodationButton.setVisible(false);
                }
            }
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            goBack();
        }
    }

    public void generateItinerary(ActionEvent event) {
        String city;
        String attraction;

        String itinerary = itineraryField.getText();
        int day =  Integer.parseInt(dayLabel.getText());
        String description = descriptionField.getText();

        List<String> cities = new ArrayList<>();
        List<String> attractions = new ArrayList<>();

        for (int i = 0; i < cityVBox.getChildren().size(); i++) {
            city = (String) cityVBox.getChildren().get(i).getUserData();
            cities.add(city);
        }

        for (int i = 0; i < attractionVBox.getChildren().size(); i++) {
            attraction = (String) attractionVBox.getChildren().get(i).getUserData();
            attractions.add(attraction);
        }

        List<String> types = getItineraryTypes();

        ItineraryBean itineraryBean = new ItineraryBean();
        try {
            itineraryBean.setId(null);
            itineraryBean.setName(itinerary);
            itineraryBean.setDescription(description);
            itineraryBean.setTypes(types);
            itineraryBean.setCities(cities);
            itineraryBean.setAttractions(attractions);
            itineraryBean.setDuration(day);


            if (accommodationVBox.isVisible()) {
                List<Pair<String, String>> accommodations = new ArrayList<>();
                for (int i = 0; i < accommodationResultVBox.getChildren().size(); i++) {
                    accommodations.add((Pair<String, String>) accommodationResultVBox.getChildren().get(i).getUserData());
                }
                itineraryBean.setAccommodations(accommodations);
            }

            if (flightVBox.isVisible()) {
                itineraryBean.setInboundFlightDepartureTime(Double.parseDouble(inDepartureTimeLabel.getText()));
                itineraryBean.setInboundFlightArrivalTime(Double.parseDouble(inArrivalTimeLabel.getText()));

                itineraryBean.setOutboundFlightArrivalTime(Double.parseDouble(outArrivalTimeLabel.getText()));
                itineraryBean.setOutboundFlightDepartureTime(Double.parseDouble(outDepartureTimeLabel.getText()));

            }

            new CreateItineraryController().createItinerary(itineraryBean,sessionId);
        } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        } catch (AttractionNotAddedException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        if (requestId != null) {
            viewManager.goToDisplayItineraryGUI(sessionId,null,requestId,null, root);
        } else {
            viewManager.goToDisplayItineraryGUI(sessionId,null,null,null, root);
        }
    }

    public void searchCity() {
        List<CityBean> cities;
        cityResultVBox.getChildren().clear();

        String city = cityField.getText();

        try {
            cities = new CreateItineraryController().getCities(city);
        } catch (FailedOperationException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }
        if (!cities.isEmpty()) {
            for (CityBean cityResult : cities) {
                Button cityResultButton = new Button(cityResult.getName()+" - "+ cityResult.getCountry());
                cityResultButton.getStyleClass().add(SEARCH_BUTTON);
                cityResultButton.setOnAction(event -> addCity(cityResult));
                cityResultVBox.getChildren().add(cityResultButton);
                if (cityResultVBox.getChildren().size() == 3) {
                    break;
                }
            }
        } else {
            Label errorLabel = new Label("Error: no place");
            cityResultVBox.getChildren().add(errorLabel);
        }

    }

    public void addCity(CityBean city) {
        int count=0;
        if(cityVBox.getChildren().isEmpty()){
            Label cityLabel = new Label(city.getName());
            cityLabel.setUserData(city.getId());
            cityLabel.getStyleClass().add(LABEL_LIGHT);
            cityVBox.getChildren().add(cityLabel);
        } else {
            for (int i = 0; i < cityVBox.getChildren().size(); i++) {
                Label otherCity = (Label) cityVBox.getChildren().get(i);
                if(city.getName().equals(otherCity.getText())){
                    count++;
                }
            }
            if (count==0){
                Label cityLabel = new Label(city.getName());
                cityLabel.setUserData(city.getId());
                cityLabel.getStyleClass().add(LABEL_LIGHT);
                cityVBox.getChildren().add(cityLabel);
            }
        }
        cityField.clear();
        cityResultVBox.getChildren().clear();
    }

    public void searchAttraction() {
        List<AttractionBean> attractions;
        attractionResultVBox.getChildren().clear();

        String attraction = attractionField.getText();

        try {
            attractions = new CreateItineraryController().getAttractions(attraction);
        } catch (FailedOperationException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        if (!attractions.isEmpty()) {
            for (AttractionBean attractionResult : attractions) {
                Button attractionResultButton = new Button(attractionResult.getName()+" - "+attractionResult.getCity());
                attractionResultButton.getStyleClass().add(SEARCH_BUTTON);
                attractionResultButton.setOnAction(event -> addAttraction(attractionResult));
                attractionResultVBox.getChildren().add(attractionResultButton);
                if (attractionResultVBox.getChildren().size() == 3) {
                    break;
                }
            }
        } else {
            Label errorLabel = new Label("Error: no place");
            cityResultVBox.getChildren().add(errorLabel);
        }
    }

    public void addAttraction(AttractionBean attraction) {
        int count=0;
        if(attractionVBox.getChildren().isEmpty()){
            Label attractionLabel = new Label(attraction.getName());
            attractionLabel.setUserData(attraction.getId());
            attractionLabel.getStyleClass().add(LABEL_LIGHT);
            attractionVBox.getChildren().add(attractionLabel);
        } else {
            for (int i = 0; i < attractionVBox.getChildren().size(); i++) {
                Label otherAttraction = (Label) attractionVBox.getChildren().get(i);
                if(attraction.getName().equals(otherAttraction.getText())){
                    count++;

                }
            }
            if (count==0){
                Label attractionLabel = new Label(attraction.getName());
                attractionLabel.setUserData(attraction.getId());
                attractionLabel.getStyleClass().add(LABEL_LIGHT);
                attractionVBox.getChildren().add(attractionLabel);
            }
        }
        attractionField.clear();
        attractionResultVBox.getChildren().clear();
    }

    public void addFlight() {
        flightButton.setVisible(false);
        flightVBox.setVisible(true);
    }

    public void addAccommodation() {
        if (accommodationVBox.isVisible()) {
            if (!accommodationField.getText().isEmpty() && !addressField.getText().isEmpty()) {
                Label accommodationLabel = new Label(accommodationField.getText()+", "+addressField.getText());
                Pair<String,String> accommodation = new Pair<>(accommodationField.getText(),addressField.getText());
                accommodationLabel.setUserData(accommodation);
                accommodationLabel.getStyleClass().add(LABEL_LIGHT);
                accommodationResultVBox.getChildren().add(accommodationLabel);
                accommodationField.setText("");
                addressField.setText("");
            }
        } else {
            accommodationButton.setVisible(false);
            accommodationVBox.setVisible(true);
        }
    }

    public void addDay() {
        int dayNum = Integer.parseInt(dayLabel.getText());
        if (dayNum < 99) {
            dayLabel.setText(String.valueOf(dayNum + 1));
        }
    }

    public void removeDay() {
        int dayNum = Integer.parseInt(dayLabel.getText());
        if(dayNum>0){
            dayLabel.setText(String.valueOf(dayNum-1));}
    }

    public void setType(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setUserData(!((boolean)button.getUserData()));
        button.getStyleClass().removeAll("button-pressed", DEFAULT_BUTTON);
        if ((boolean)button.getUserData()) {
            button.getStyleClass().add("button-pressed");
        } else {
            button.getStyleClass().add(DEFAULT_BUTTON);
        }
    }

    public List<String> getItineraryTypes() {
        List<String> types = new ArrayList<>();

        if ((boolean)onTheRoadButton.getUserData()) {
            types.add(ON_THE_ROAD);
        }
        if ((boolean)natureButton.getUserData()) {
            types.add(NATURE);
        }
        if ((boolean)cultureButton.getUserData()) {
            types.add(CULTURE);
        }
        if ((boolean)relaxButton.getUserData()) {
            types.add(RELAX);
        }
        if ((boolean)cityButton.getUserData()) {
            types.add(CITY);
        }

        return types;
    }

    public void goBack() {
        BorderPane root = (BorderPane) dayLabel.getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
