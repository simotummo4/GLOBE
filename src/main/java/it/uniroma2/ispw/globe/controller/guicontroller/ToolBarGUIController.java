package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_IO;
import static it.uniroma2.ispw.globe.constants.UserType.GUEST;
import static it.uniroma2.ispw.globe.constants.UserType.USER;

public class ToolBarGUIController {
    private String sessionId;
    private String userType;
    private BorderPane root;

    @FXML
    private Button logOutButton;
    @FXML
    private Button requestButton;
    @FXML
    private Button agencyButton;
    @FXML
    private Button tripButton;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;

    private static final String DEFAULT_BUTTON = "button-default";
    private static final String PRESSED_BUTTON = "button-pressed";


    public ToolBarGUIController(String sessionId, String userType, BorderPane root) {
        this.sessionId = sessionId;
        this.userType = userType;
        this.root = root;
    }

    public void initialize() {
        if (userType.equals(GUEST)) {
            logOutButton.setText("Log In");
        }

        if (userType.equals(USER) || userType.equals(GUEST)) {
            agencyButton.setVisible(false);
        } else {
            tripButton.setVisible(false);
            requestButton.setVisible(false);
        }


    }

    public void requestItinerary()  {
        if (userType.equals(GUEST)) {
            new ErrorPopUpGUIController().createPopUp("You are not allowed to request itineraries.\nPlease Log In");
            return;
        }

        tripButton.getStyleClass().clear();
        requestButton.getStyleClass().clear();
        tripButton.getStyleClass().add(DEFAULT_BUTTON);
        requestButton.getStyleClass().add(PRESSED_BUTTON);
        ViewManager viewManager = new ViewManager();
        viewManager.goToRequestItineraryGUI(sessionId,root);

    }


    public void manageItinerary(){
        tripButton.getStyleClass().clear();
        requestButton.getStyleClass().clear();
        tripButton.getStyleClass().add(PRESSED_BUTTON);
        requestButton.getStyleClass().add(DEFAULT_BUTTON);
        ViewManager viewManager = new ViewManager();
        viewManager.goToManageItineraryGUI(sessionId,root);
    }

    public void manageRequest(){
        ViewManager viewManager = new ViewManager();
        viewManager.goToManageRequestGUI(sessionId,root);
    }

    public void logOut(ActionEvent event) {
        if (userType.equals(GUEST)) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/LogOutPopUp.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                loader.setController(this);
                Parent popUpRoot = loader.load();
                Scene scene = new Scene(popUpRoot);
                scene.setFill(Color.TRANSPARENT);

                Stage popupStage = new Stage();
                popupStage.setScene(scene);

                popupStage.initStyle(StageStyle.TRANSPARENT);
                popupStage.initModality(Modality.APPLICATION_MODAL);

                yesButton.setOnAction(e -> {
                    popupStage.close();
                    yesButton.setUserData(true);
                });
                noButton.setOnAction(e -> {
                    popupStage.close();
                    yesButton.setUserData(false);
                });

                popupStage.setScene(scene);
                popupStage.showAndWait();
            } catch (IOException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_IO, e);
            }
        }

        try {
            new LogInController().logOut(sessionId);
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        if (!userType.equals(GUEST) || (boolean) yesButton.getUserData()) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ViewManager viewManager = new ViewManager();
            viewManager.goToLogInGUI(stage);
        }

    }
}
