/*
 * Author: Lois Mathew
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class mainMenuController extends homeGuardianClientController {

    @FXML
    private ImageView activityLogButton;

    @FXML
    private ImageView devicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView settingsButton;

    @FXML
    void activityLogButtonClicked(MouseEvent event) {
        switchScene(event, "activityLogPage.fxml");
    }

    @FXML
    void devicesButtonClicked(MouseEvent event) {
        switchScene(event, "deviceMenu.fxml");
    }

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        sendToServer("logout");
        switchScene(event, "login.fxml");
    }

    @FXML
    void settingsButtonClicked(MouseEvent event) {
        switchScene(event, "settingsMenu.fxml");
    }
}
