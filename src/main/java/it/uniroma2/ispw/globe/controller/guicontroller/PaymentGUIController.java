package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.bean.PaymentBean;
import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

public class PaymentGUIController {

    @FXML
    private Button closeButton;
    @FXML
    private Slider ratingSlider;
    @FXML
    private Label userLabel;
    @FXML
    private Label agencyLabel;

    public void createPopUp(PaymentBean paymentResult, String proposalID) {
        try {
            URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/PaymentPopUp.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            PaymentGUIController controller = loader.getController();

            Stage popupStage = new Stage();
            popupStage.setScene(scene);

            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.APPLICATION_MODAL);

            controller.userLabel.setText(paymentResult.getPayerUsername());
            controller.agencyLabel.setText(paymentResult.getPayeeUsername());

            controller.closeButton.setOnAction(e -> {
                try {
                    if (controller.ratingSlider.getValue() == 0) {
                        new ErrorPopUpGUIController().createPopUp("select rating");
                    } else {
                        new AcceptItineraryController().addRating(controller.ratingSlider.getValue(), proposalID);
                        popupStage.close();
                    }
                } catch (FailedOperationException ex) {
                    new ErrorPopUpGUIController().createPopUp(ex.getMessage());
                }
            });

            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_IO, e);
        }
    }
}
