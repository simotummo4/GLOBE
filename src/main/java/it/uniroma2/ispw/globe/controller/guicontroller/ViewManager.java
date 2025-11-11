package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.engineering.session.NavigationData;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_VIEW;

public class ViewManager {

    public static final String LOGIN = "src/main/java/it/uniroma2/ispw/globe/view/LoginView.fxml";
    public static final String SIGNIN = "src/main/java/it/uniroma2/ispw/globe/view/SigninView.fxml";
    public static final String CREATE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml";
    public static final String CREATE_PROPOSAL = "src/main/java/it/uniroma2/ispw/globe/view/CreateProposalView.fxml";
    public static final String DISPALY_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml";
    public static final String DISPALY_PROPOSAL = "src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml";
    public static final String DISPALY_REQUEST = "src/main/java/it/uniroma2/ispw/globe/view/DisplayRequestView.fxml";
    public static final String MANAGE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml";
    public static final String MANAGE_REQUEST= "src/main/java/it/uniroma2/ispw/globe/view/ManageRequestView.fxml";
    public static final String REQUEST_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/RequestItineraryView.fxml";

    public static final String ERROR_MESSAGE = "page loading failed";

    private void loadView(String fxmlPath, NavigationData data, BorderPane root) {
        try {
            URL url = new File(fxmlPath).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            root.setCenter(loader.load());
            SessionManager.getInstance().getSession(data.getSessionID()).setNavigationData(data);
            AbstractGUIController controller = loader.getController();
            controller.initialize(data.getSessionID());
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_VIEW, e);
            new ErrorPopUpGUIController().createPopUp(ERROR_MESSAGE);
        }
    }

    public void goToLogInGUI(Stage stage) {
        loadAuthenticateGUI(stage, LOGIN);
    }

    public void goToSignInGUI(Stage stage) {
        loadAuthenticateGUI(stage, SIGNIN);
    }

    private void loadAuthenticateGUI(Stage stage, String type) {
        try {
            URL url = new File(type).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_VIEW, e);
            new ErrorPopUpGUIController().createPopUp(ERROR_MESSAGE);
        }
    }

    public void goToCreateItineraryGUI(String sessionID, String requestID, BorderPane root) {
        NavigationData data = new NavigationData(sessionID, null, null, requestID, root.getCenter());
        loadView(CREATE_ITINERARY, data, root);
    }

    public void goToCreateProposalGUI(String sessionID, String requestID, BorderPane root) {
        NavigationData data = new NavigationData(sessionID, null, null, requestID, root.getCenter());
        loadView(CREATE_PROPOSAL, data, root);
    }

    public void goToDisplayItineraryGUI(String sessionID, String itineraryID, String requestID, String proposalID, BorderPane root) {
        NavigationData data = new NavigationData(sessionID, itineraryID, proposalID, requestID, root.getCenter());
        loadView(DISPALY_ITINERARY, data, root);
    }

    public void goToDisplayProposalGUI(String sessionID, String requestID, String proposalID, BorderPane root) {
        NavigationData data = new NavigationData(sessionID, null, proposalID, requestID, root.getCenter());
        loadView(DISPALY_PROPOSAL, data, root);
    }

    public void goToDisplayRequestGUI(String sessionID, String requestID, BorderPane root) {
        NavigationData data = new NavigationData(sessionID, null, null, requestID, root.getCenter());
        loadView(DISPALY_REQUEST, data, root);
    }

    public void goToManageItineraryGUI(String sessionID, BorderPane root) {
        NavigationData data = new NavigationData(sessionID, null, null, null, null);
        loadView(MANAGE_ITINERARY, data, root);
    }

    public void goToManageRequestGUI(String sessionID, BorderPane root) {
        NavigationData data = new NavigationData(sessionID, null, null, null, null);
        loadView(MANAGE_REQUEST, data, root);
    }

    public void goToRequestItineraryGUI(String sessionID, BorderPane root) {
        NavigationData data = new NavigationData(sessionID, null, null, null, null);
        loadView(REQUEST_ITINERARY, data, root);
    }
}


