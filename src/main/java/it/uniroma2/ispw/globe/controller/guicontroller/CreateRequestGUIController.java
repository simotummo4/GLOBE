package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.controller.applicationcontroller.RequestItineraryController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.constants.ItineraryType.*;
import static it.uniroma2.ispw.globe.constants.RequestOptional.*;

public class CreateRequestGUIController extends AbstractGUIController {
    @FXML
    private TextField citiesField;
    @FXML
    private TextField attractionsField;
    @FXML
    private TextField otherRequestsField;
    @FXML
    private TextField trekkingDistanceField;
    @FXML
    private TextField dayDrivingHoursField;
    @FXML
    private Label dayLabel;
    @FXML
    private VBox cityResultVBox;
    @FXML
    private VBox cityVBox;
    @FXML
    private VBox attractionResultVBox;
    @FXML
    private VBox attractionVBox;
    @FXML
    private VBox agencyResultVBox;
    @FXML
    private VBox agencyVBox;
    @FXML
    private Button flightButton;
    @FXML
    private Button accommodationButton;
    @FXML
    private Button onTheRoadButton;
    @FXML
    private Button natureButton;
    @FXML
    private Button relaxButton;
    @FXML
    private Button cultureButton;
    @FXML
    private Button cityButton;
    @FXML
    private Button normalDifficulty;
    @FXML
    private Button mediumDifficulty;
    @FXML
    private Button hardDifficulty;
    @FXML
    private Button morningMode;
    @FXML
    private Button lateAfternoonMode;
    @FXML
    private Button nightMode;
    @FXML
    private Tab onTheRoadTab;
    @FXML
    private Tab natureTab;
    @FXML
    private Tab relaxTab;
    @FXML
    private Tab cultureTab;
    @FXML
    private Tab cityTab;
    @FXML
    private Label errorLabel;
    @FXML
    private TabPane typeTabPane;

    private static final String SEARCH_BUTTON = "button-search";
    private static final String DEFAULT_BUTTON = "button-default";
    private static final String PRESSED_BUTTON = "button-pressed";

    private static final String LABEL_LIGHT = "label-light";

    private static final String CITY = "city";

    private String sessionId;

    public void initialize(String sessionId) {
        this.sessionId = sessionId;

        onTheRoadButton.setUserData(false);
        natureButton.setUserData(false);
        relaxButton.setUserData(false);
        cultureButton.setUserData(false);
        cityButton.setUserData(false);

        flightButton.setUserData(false);
        accommodationButton.setUserData(false);
    }

    public void removeDay() {
        int dayNum = Integer.parseInt(dayLabel.getText());
        if(dayNum>0){
            dayLabel.setText(String.valueOf(dayNum-1));}
    }

    public void addDay() {
        int dayNum = Integer.parseInt(dayLabel.getText());
        if (dayNum < 99) {
            dayLabel.setText(String.valueOf(dayNum + 1));
        }

    }

    public void searchCity() {
        List<CityBean> cities;
        cityResultVBox.getChildren().clear();
        String city = citiesField.getText();
        try {
            cities = new RequestItineraryController().getCities(city);
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
            Label errorCityLabel = new Label("Error: no place");
            cityResultVBox.getChildren().add(errorCityLabel);
        }

    }

    public void addCity(CityBean cityResult) {
        int count=0;
        if(cityVBox.getChildren().isEmpty()){
            Label cityLabel = new Label(cityResult.getName());
            cityLabel.getStyleClass().add(LABEL_LIGHT);
            cityLabel.setUserData(cityResult.getId());
            cityVBox.getChildren().add(cityLabel);
        } else {
            for (int i = 0; i < cityVBox.getChildren().size(); i++) {
                Label otherCity = (Label) cityVBox.getChildren().get(i);
                if(cityResult.getName().equals(otherCity.getText())){
                    count++;
                }
            }
            if (count==0){
                Label cityLabel = new Label(cityResult.getName());
                cityLabel.getStyleClass().add(LABEL_LIGHT);
                cityLabel.setUserData(cityResult.getId());
                cityVBox.getChildren().add(cityLabel);
            }
        }
        citiesField.clear();
        cityResultVBox.getChildren().clear();
    }

    public void searchAttraction() {
        List<AttractionBean> attractions;
        attractionResultVBox.getChildren().clear();

        String attraction = attractionsField.getText();

        try {
            attractions = new RequestItineraryController().getAttractions(attraction);
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
            Label errorAttractionLabel = new Label("Error: no place");
            attractionResultVBox.getChildren().add(errorAttractionLabel);
        }
    }

    public void addAttraction(AttractionBean attractionResult) {

        int count=0;
        if(attractionVBox.getChildren().isEmpty()){
            Label attractionLabel = new Label(attractionResult.getName());
            attractionLabel.setUserData(attractionResult.getId());
            attractionLabel.getStyleClass().add(LABEL_LIGHT);
            attractionVBox.getChildren().add(attractionLabel);
        } else {
            for (int i = 0; i < attractionVBox.getChildren().size(); i++) {
                Label otherAttraction = (Label) attractionVBox.getChildren().get(i);
                if(attractionResult.getName().equals(otherAttraction.getText())){
                    count++;

                }
            }
            if (count==0){
                Label attractionLabel = new Label(attractionResult.getName());
                attractionLabel.setUserData(attractionResult.getId());
                attractionLabel.getStyleClass().add(LABEL_LIGHT);
                attractionVBox.getChildren().add(attractionLabel);
            }
        }
        attractionsField.clear();
        attractionResultVBox.getChildren().clear();
    }

    public void addOptional(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setUserData(!((boolean)button.getUserData()));
        button.getStyleClass().removeAll(PRESSED_BUTTON, DEFAULT_BUTTON);
        if ((boolean)button.getUserData()) {
            button.getStyleClass().add(PRESSED_BUTTON);
        } else {
            button.getStyleClass().add(DEFAULT_BUTTON);
        }
    }

    public void chooseMode (ActionEvent event) {
        morningMode.setUserData(false);
        lateAfternoonMode.setUserData(false);
        nightMode.setUserData(false);
        Button mode = (Button) event.getSource();
        mode.setUserData(true);

    }

    public void chooseDifficulty (ActionEvent event) {
        normalDifficulty.setUserData(false);
        mediumDifficulty.setUserData(false);
        hardDifficulty.setUserData(false);
        Button difficulty = (Button) event.getSource();
        difficulty.setUserData(true);
    }

    public void setType(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setUserData(!((boolean)button.getUserData()));

        button.getStyleClass().removeAll(PRESSED_BUTTON, DEFAULT_BUTTON);
        if ((boolean)button.getUserData()) {
            button.getStyleClass().add(PRESSED_BUTTON);
            if (button.equals(onTheRoadButton)) {
                onTheRoadTab.setDisable(false);
                typeTabPane.getSelectionModel().select(onTheRoadTab);
            } else if (button.equals(natureButton)) {
                natureTab.setDisable(false);
                typeTabPane.getSelectionModel().select(natureTab);
            } else if (button.equals(relaxButton)) {
                relaxTab.setDisable(false);
                typeTabPane.getSelectionModel().select(relaxTab);
            } else if (button.equals(cultureButton)) {
                cultureTab.setDisable(false);
                typeTabPane.getSelectionModel().select(cultureTab);
            } else if (button.equals(cityButton)) {
                cityTab.setDisable(false);
                typeTabPane.getSelectionModel().select(cityTab);
            }
        } else {
            button.getStyleClass().add(DEFAULT_BUTTON);
            if (button.equals(onTheRoadButton)) {
                onTheRoadTab.setDisable(true);
            } else if (button.equals(natureButton)) {
                natureTab.setDisable(true);
            } else if (button.equals(relaxButton)) {
                relaxTab.setDisable(true);
            } else if (button.equals(cultureButton)) {
                cultureTab.setDisable(true);
            } else if (button.equals(cityButton)) {
                cityTab.setDisable(true);
            }
        }
    }

    public void searchAgency() {
        List<AgencyBean> agencies;
        List<String> types = new ArrayList<>();

        agencyVBox.getChildren().clear();
        agencyResultVBox.getChildren().clear();

        if ((boolean) onTheRoadButton.getUserData()) {
            types.add(ON_THE_ROAD);
        }
        if ((boolean) natureButton.getUserData()) {
            types.add(NATURE);
        }
        if ((boolean) relaxButton.getUserData()) {
            types.add(RELAX);
        }
        if ((boolean) cultureButton.getUserData()) {
            types.add(CULTURE);
        }
        if ((boolean) cityButton.getUserData()) {
            types.add(CITY);
        }

        try {
            agencies = new RequestItineraryController().getAgenciesByType(types);
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        if (!agencies.isEmpty()) {
            errorLabel.setVisible(false);
            for (AgencyBean agencyResult : agencies) {
                Button agencyButton = new Button(agencyResult.getName()+" - "+agencyResult.getRating());
                agencyButton.getStyleClass().add(SEARCH_BUTTON);
                agencyButton.setOnAction(event -> addAgency(agencyResult));
                agencyResultVBox.getChildren().add(agencyButton);
            }
        } else {
            errorLabel.setVisible(true);
        }


    }

    public void addAgency(AgencyBean agencyResult) {
        int count=0;
        if(agencyVBox.getChildren().isEmpty()){
            Label agencyLabel = new Label(agencyResult.getName());
            agencyLabel.setUserData(agencyResult.getName());
            agencyLabel.getStyleClass().add(LABEL_LIGHT);
            agencyVBox.getChildren().add(agencyLabel);
        } else {
            for (int i = 0; i < agencyVBox.getChildren().size(); i++) {
                Label otherAgency = (Label) agencyVBox.getChildren().get(i);
                if(agencyResult.getName().equals(otherAgency.getText())){
                    count++;
                }
            }
            if (count==0){
                Label agencyLabel = new Label(agencyResult.getName());
                agencyLabel.getStyleClass().add(LABEL_LIGHT);
                agencyLabel.setUserData(agencyResult.getName());
                agencyVBox.getChildren().add(agencyLabel);
            }
        }
    }

    public void createRequest(ActionEvent event) {

        String city;
        String attraction;
        String agency;
        List<String> cities = new ArrayList<>();
        List<String> attractions = new ArrayList<>();
        List<String> agencies = new ArrayList<>();
        List<String> itineraryType = new ArrayList<>();
        String otherRequests = otherRequestsField.getText();
        int dayNum =  Integer.parseInt(dayLabel.getText());
        boolean flight = (boolean) flightButton.getUserData();
        boolean accommodation = (boolean) accommodationButton.getUserData();
        OnTheRoadBean onTheRoadBean = new OnTheRoadBean();
        NatureBean natureBean = new NatureBean();

        if ((boolean) onTheRoadButton.getUserData()) {
            itineraryType.add(ON_THE_ROAD);
        }

        if ((boolean) natureButton.getUserData()) {
            itineraryType.add(NATURE);
        }
        if ((boolean) relaxButton.getUserData()) {
            itineraryType.add(RELAX);
        }
        if ((boolean) cultureButton.getUserData()) {
            itineraryType.add(CULTURE);
        }
        if ((boolean) cityButton.getUserData()) {
            itineraryType.add(CITY);
        }

        for (int i = 0; i < cityVBox.getChildren().size(); i++) {
            city = (String) cityVBox.getChildren().get(i).getUserData();
            cities.add(city);
        }

        for (int i = 0; i < attractionVBox.getChildren().size(); i++) {
            attraction = (String) attractionVBox.getChildren().get(i).getUserData();
            attractions.add(attraction);
        }

        for (int i = 0; i < agencyVBox.getChildren().size(); i++) {
            agency = (String) agencyVBox.getChildren().get(i).getUserData();
            agencies.add(agency);
        }

        try {
            RequestBean requestBean = new RequestBean();

            requestBean.setCities(cities);
            requestBean.setAttractions(attractions);
            requestBean.setOtherRequests(otherRequests);
            requestBean.setDayNum(dayNum);
            requestBean.setAgencies(agencies);
            requestBean.setFlight(flight);
            requestBean.setAccommodation(accommodation);
            requestBean.setTypes(itineraryType);

            onTheRoadBean = (OnTheRoadBean) addTypesDetails(onTheRoadBean);
            natureBean = (NatureBean) addTypesDetails(natureBean);

            new RequestItineraryController().createRequest(requestBean, onTheRoadBean, natureBean, sessionId);
        } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayRequestGUI(sessionId,null, root);
    }

    public Object addTypesDetails(Object type) throws IncorrectDataException {

        if (type instanceof OnTheRoadBean onTheRoadBean && (boolean) onTheRoadButton.getUserData()) {

            String mode = null;
            if((boolean) morningMode.getUserData()) {
                mode = MORNING_MODE;
            } else if ((boolean) lateAfternoonMode.getUserData()) {
                mode = AFTERNOON_MODE;
            } else if ((boolean) nightMode.getUserData()) {
                mode = NIGHT_MODE;
            }

            onTheRoadBean.setMode(mode);
            onTheRoadBean.setDayDrivingHours(Double.parseDouble(dayDrivingHoursField.getText()));

            return onTheRoadBean;

        } else if (type instanceof NatureBean natureBean && (boolean) natureButton.getUserData()) {

            String difficulty = null;
            if((boolean) normalDifficulty.getUserData()) {
                difficulty = NORMAL_DIFFICULT;
            } else if ((boolean) mediumDifficulty.getUserData()) {
                difficulty = MEDIUM_DIFFICULT;
            } else if ((boolean) hardDifficulty.getUserData()) {
                difficulty = HARD_DIFFICULT;
            }

            natureBean.setDifficulty(difficulty);
            natureBean.setTrekkingDistance(Double.parseDouble(trekkingDistanceField.getText()));

            return natureBean;

        } else {
            return null;
        }
    }
}
