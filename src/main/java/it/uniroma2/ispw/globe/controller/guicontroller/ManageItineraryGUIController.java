package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.bean.ProposalBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static it.uniroma2.ispw.globe.constants.ProposalState.ACCEPTED;
import static it.uniroma2.ispw.globe.constants.ProposalState.REJECTED;

public class ManageItineraryGUIController extends AbstractGUIController {
    @FXML
    private VBox itinerariesVBox;
    @FXML
    private VBox proposalsVBox;

    private String sessionId;

    public void initialize(String sessionId) {

        this.sessionId = sessionId;
        List<ItineraryBean> itineraries;
        List<ProposalBean> proposals;

        try {
            itineraries = new ManageItineraryController().getUserItineraries(sessionId);
            proposals = new ManageItineraryController().getUserProposals(sessionId);
        } catch (FailedOperationException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            Stage stage = (Stage) itinerariesVBox.getScene().getWindow();
            ViewManager viewManager = new ViewManager();
            viewManager.goToLogInGUI(stage);
            return;
        }
        for (ItineraryBean itinerary : itineraries) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/ItineraryElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button itineraryBox = loader.load();
                itineraryBox.setUserData(itinerary.getId());
                itineraryBox.setOnAction(this::viewItinerary);
                Label nameLabel = (Label) itineraryBox.getGraphic().lookup("#nameLabel");
                nameLabel.setText(itinerary.getName());
                Label descriptionLabel = (Label) itineraryBox.getGraphic().lookup("#descriptionLabel");
                descriptionLabel.setText(itinerary.getDescription());
                Label daysLabel = (Label) itineraryBox.getGraphic().lookup("#daysLabel");
                daysLabel.setText(String.valueOf(itinerary.getDuration()));
                File photoFile = itinerary.getPhoto();
                if (photoFile != null) {
                    itineraryBox.getGraphic().lookup("#imageView");
                    HBox photoHbox = (HBox) itineraryBox.getGraphic().lookup("#photoHbox");
                    photoHbox.getChildren().clear();
                    ImageView photo = new ImageView(photoFile.toURI().toString());
                    double totalWidth = 120;
                    double height = 100;
                    double circleRadius = height / 2; // 60

                    // Imposta dimensioni dell'immagine
                    photo.setFitWidth(totalWidth);
                    photo.setFitHeight(height);
                    photo.setPreserveRatio(false);

                    // Semicerchio a sinistra
                    Circle circle = new Circle(circleRadius, circleRadius, circleRadius);

                    // Rettangolo a destra
                    Rectangle rect = new Rectangle(circleRadius, 0, totalWidth - circleRadius, height);

                    // Unione delle due forme
                    Shape clipShape = Shape.union(circle, rect);

                    // Applica la maschera all'immagine
                    photo.setClip(clipShape);
                    photoHbox.getChildren().add(photo);
                }

                itinerariesVBox.getChildren().add(itineraryBox);
            } catch (IOException e) {
                new ErrorPopUpGUIController().createPopUp("'Manage Itinerary' page loading failed");
                Stage stage = (Stage) itinerariesVBox.getScene().getWindow();
                ViewManager viewManager = new ViewManager();
                viewManager.goToLogInGUI(stage);
                return;
            }
        }
        for (ProposalBean proposal : proposals) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/proposalElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button proposalBox = loader.load();
                proposalBox.setUserData(proposal.getID());
                proposalBox.setOnAction(this::viewProposal);
                Label nameLabel = (Label) proposalBox.getGraphic().lookup("#nameLabel");
                nameLabel.setText(proposal.getAgency());
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
                new ErrorPopUpGUIController().createPopUp("'Manage Itinerary' page loading failed");
                Stage stage = (Stage) itinerariesVBox.getScene().getWindow();
                ViewManager viewManager = new ViewManager();
                viewManager.goToLogInGUI(stage);
                return;
            }
        }
    }

    public void viewItinerary(ActionEvent event) {
        String itineraryId = (String) ((Button)event.getSource()).getUserData();
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayItineraryGUI(sessionId,itineraryId,null,null, root);
    }

    public void viewProposal(ActionEvent event) {
        String proposalId = (String) ((Button)event.getSource()).getUserData();
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayProposalGUI(sessionId, null, proposalId, root);
    }

    public void createItinerary(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToCreateItineraryGUI(sessionId,null, root);
    }
}
