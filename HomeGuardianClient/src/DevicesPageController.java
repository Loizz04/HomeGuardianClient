/*
 * Author: Lois Mathew
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class DevicesPageController extends homeGuardianClientController {

    @FXML
    private Button activityLogButton;

    @FXML
    private ImageView alarmButton;

    @FXML
    private ImageView cameraButton;

    @FXML
    private ImageView lightButton;

    @FXML
    private ImageView lockButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button settingsButton;

  
    @FXML
    void lightButtonClicked(MouseEvent event) {
        switchScene(event, "lightMenu.fxml");
    }

    @FXML
    void lockButtonClicked(MouseEvent event) {
        switchScene(event, "lockMenu.fxml");
    }

    @FXML
    void cameraButtonClicked(MouseEvent event) {
        switchScene(event, "cameraMenu.fxml");
    }

    @FXML
    void alarmButtonClicked(MouseEvent event) {
        switchScene(event, "alarmMenu.fxml");
    }
    @FXML
    void mainMenuButtonPressed(ActionEvent event) {
        switchScene(event, "mainMenu.fxml");
    }

   @FXML
   void settingsButtonPressed(ActionEvent event) {
	   switchScene(event, "settingsMenu.fxml");
   }

    @FXML
    void activityLogButtonPressed(ActionEvent event) {
        switchScene(event, "activityLogPage.fxml");
    }

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        sendToServer("logout");
        switchScene(event, "login.fxml");
    }
}
