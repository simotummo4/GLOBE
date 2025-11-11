package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.engineering.session.NavigationData;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DisplayItineraryGUIController extends AbstractGUIController {

    @FXML
    private Label dayLabel;
    @FXML
    private TabPane daysTabPane;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Button nextUserButton;
    @FXML
    private Button nextAgencyButton;
    @FXML
    private VBox accommodationVBox;
    @FXML
    private VBox flightVBox;
    @FXML
    private HBox typesHBox;
    @FXML
    private ImageView itineraryPhoto;

    private String sessionId;
    private String itineraryId;
    private String requestId;
    private Node prev;

    private static final String LIGHT = "label-light";

    public void initialize(String sessionId) {
        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.itineraryId = data.getItineraryID();
        this.requestId = data.getRequestID();
        String proposalId = data.getProposalID();
        this.prev = data.getPrev();

        accommodationVBox.setVisible(false);
        flightVBox.setVisible(false);
        ItineraryBean itinerary;
        List<StepBean> steps;
        try {
            itinerary = new CreateItineraryController().getItinerary(itineraryId,sessionId);
            steps = new AcceptItineraryController().getSteps(itineraryId,sessionId);
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            goBack();
            return;
        }

        nameLabel.setText(itinerary.getName());
        descriptionLabel.setText(itinerary.getDescription());
        dayLabel.setText(String.valueOf(itinerary.getDuration()));
        if (itinerary.getPhoto() != null) {
            Image image = new Image(itinerary.getPhoto().toURI().toString());
            itineraryPhoto.setImage(image);
            Circle clip = new Circle(70, 70, 70);
            itineraryPhoto.setClip(clip);
        }

        for (String type : itinerary.getTypes()) {
            Label typeLabel = new Label(type);
            typeLabel.getStyleClass().add("rounded-label");
            typesHBox.getChildren().add(typeLabel);
        }

        int day = 1;
        for (StepBean step : steps) {
            drawDay(step, day);
            day ++;
        }


        if (itinerary.getAccommodations() != null && !itinerary.getAccommodations().isEmpty()) {
            accommodationVBox.setVisible(true);
            for (int i = 0; i < itinerary.getAccommodations().size(); i++) {
                Pair<String,String> accommodation = itinerary.getAccommodations().get(i);
                Label accommmodationLabel = new Label((i+1)+": "+accommodation.getKey()+", "+accommodation.getValue());
                accommmodationLabel.getStyleClass().add(LIGHT);
                accommodationVBox.getChildren().add(accommmodationLabel);
            }
        }
        if (itinerary.getInboundFlightDepartureTime() != -1) {
            flightVBox.setVisible(true);
            Label inFlightLabel = new Label("Inbound: "+itinerary.getInboundFlightDepartureTime()+" - "+itinerary.getInboundFlightArrivalTime());
            inFlightLabel.getStyleClass().add(LIGHT);
            Label outFlightLabel = new Label("Outbound: "+itinerary.getOutboundFlightDepartureTime()+" - "+itinerary.getOutboundFlightArrivalTime());
            outFlightLabel.getStyleClass().add(LIGHT);
            flightVBox.getChildren().add(inFlightLabel);
            flightVBox.getChildren().add(outFlightLabel);
        }

        nextAgencyButton.setVisible(false);
        if (proposalId != null) {
            nextUserButton.setVisible(false);
            nextAgencyButton.setVisible(false);
        }
        if (requestId != null) {
            nextUserButton.setVisible(false);
            nextAgencyButton.setVisible(true);
            try {
                nextAgencyButton.setVisible(new ResponseRequestController().getProposal(null, sessionId) == null);
            } catch (FailedOperationException | IncorrectDataException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                goBack();
                return;
            }
        }
        if (itineraryId != null) {
            nextUserButton.setVisible(false);
        }
    }

    public void drawDay(StepBean step, int day) {
        Tab tab = new Tab(String.valueOf(day));
        URL url;
        VBox dayVBox;
        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/DayTab.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            dayVBox = loader.load();

            ScrollPane scrollPane = (ScrollPane) dayVBox.lookup("#scrollPane");
            VBox attractionVBox = (VBox) scrollPane.getContent();
            Label cityLabel = (Label) dayVBox.lookup("#cityLabel");
            CityBean city = new AcceptItineraryController().getCity(step.getNum(),step.getCity().get(0),null);
            cityLabel.setText(city.getName()+", "+city.getCountry());
            for (String attractionID : step.getAttractions()) {
                AttractionBean attraction = new AcceptItineraryController().getAttraction(step.getNum(),attractionID,null);
                Label attractionLabel = new Label(attraction.getName());
                Label addressLabel = new Label(attraction.getCity()+", "+attraction.getAddress());
                attractionLabel.setMaxWidth(Double.MAX_VALUE);
                addressLabel.setMaxWidth(Double.MAX_VALUE);
                attractionLabel.getStyleClass().add(LIGHT);
                addressLabel.getStyleClass().add(LIGHT);
                HBox attractionHBox = new HBox(attractionLabel,addressLabel);
                HBox.setHgrow(attractionLabel, Priority.ALWAYS);
                HBox.setHgrow(addressLabel, Priority.ALWAYS);
                attractionVBox.getChildren().add(attractionHBox);
            }
            tab.setContent(dayVBox);
            daysTabPane.getTabs().add(tab);

        } catch (IOException e) {
            new ErrorPopUpGUIController().createPopUp("page loading failed");
            goBack();
        } catch ( FailedOperationException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            goBack();
        }
    }

    public void showItineraries(ActionEvent event) {
        if (itineraryId == null) {
            try {
                new CreateItineraryController().saveItinerary(sessionId);
            } catch (FailedOperationException | DuplicateItemException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                return;
            }
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToManageItineraryGUI(sessionId,root);
    }

    public void createProposal(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToCreateProposalGUI(sessionId, requestId, root);
    }

    public void addPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona una foto");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            itineraryPhoto.setImage(image);
            Circle clip = new Circle(55, 55, 55);
            itineraryPhoto.setClip(clip);
        }
        try {
            new CreateItineraryController().setItineraryPhoto(file, itineraryId, sessionId);
        } catch (FailedOperationException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
        }
    }

    public void goBack() {
        BorderPane root = (BorderPane) dayLabel.getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }

}
