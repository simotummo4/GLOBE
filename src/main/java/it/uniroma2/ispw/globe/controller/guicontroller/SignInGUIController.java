package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.bean.CredentialsBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.constants.ItineraryType.*;
import static it.uniroma2.ispw.globe.constants.UserType.*;


public class SignInGUIController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField paymentCredentialsField;
    @FXML
    private Label errorLabel;
    @FXML
    private VBox agencyForm;
    @FXML
    private CheckBox onTheRoadCheckBox;
    @FXML
    private CheckBox natureCheckBox;
    @FXML
    private CheckBox cultureCheckBox;
    @FXML
    private CheckBox relaxCheckBox;
    @FXML
    private CheckBox cityCheckBox;

    public void initialize() {
        agencyForm.setVisible(false);
    }

    public void getAgencyForm() {
        agencyForm.setVisible(!agencyForm.isVisible());
    }

    public void signIn(ActionEvent event) {
        errorLabel.setVisible(false);
        CredentialsBean credentials = new CredentialsBean();

        try {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                errorLabel.setVisible(true);
            } else {
                credentials.setUsername(usernameField.getText());
                credentials.setPassword(passwordField.getText());
                credentials.setPaymentCredentials(paymentCredentialsField.getText());
                
                if (agencyForm.isVisible()) {
                    credentials = getAgencyDetails(credentials);
                } else {
                    credentials.setType(USER);
                }

                new LogInController().signIn(credentials);
                goBack(event);
            }
        } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
        }
    }

    public CredentialsBean getAgencyDetails(CredentialsBean credentials) throws IncorrectDataException {
        List<String> preferences = new ArrayList<>();

        if (onTheRoadCheckBox.isSelected()) {
            preferences.add(ON_THE_ROAD);
        }
        if (natureCheckBox.isSelected()) {
            preferences.add(NATURE);
        }
        if (cultureCheckBox.isSelected()) {
            preferences.add(CULTURE);
        }
        if (relaxCheckBox.isSelected()) {
            preferences.add(RELAX);
        }
        if (cityCheckBox.isSelected()) {
            preferences.add(CITY);
        }


        credentials.setType(AGENCY);
        credentials.setDescription(descriptionField.getText());
        credentials.setPreferences(preferences);

        return credentials;
    }

    public void goBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewManager viewManager = new ViewManager();
        viewManager.goToLogInGUI(stage);
    }
}
