/*
 * Author: Lois Mathew
 */
import java.util.ArrayList;

import OCSF.ClientUser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class lightController extends homeGuardianClientController {

    @FXML
    private Button activityLogButton;

    @FXML
    private Button devicesButton;

    @FXML
    private ImageView light1Button;

    @FXML
    private ToggleButton light1ToggleButton;

    @FXML
    private ImageView light2Button;

    @FXML
    private ToggleButton light2ToggleButton;

    @FXML
    private ImageView light3Button;

    @FXML
    private ToggleButton light3ToggleButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button settingsButton;

    @FXML
    public void initialize() {
        styleToggle(light1ToggleButton);
        styleToggle(light2ToggleButton);
        styleToggle(light3ToggleButton);
    }

    private void styleToggle(ToggleButton button) {
        if (button == null) return;

        boolean on = button.isSelected();
        if (on) {
            button.setText("ON");
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            button.setText("OFF");
            button.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }

    @FXML
    void light1ButtonClicked(MouseEvent event) {
        openLightDetail(event, 1);
    }

    @FXML
    void light2ButtonClicked(MouseEvent event) {
        openLightDetail(event, 2);
    }

    @FXML
    void light3ButtonClicked(MouseEvent event) {
        openLightDetail(event, 3);
    }

    private void openLightDetail(MouseEvent event, int lightId) {
        LightDetailController.setCurrentLightId(lightId);
        switchScene(event, "LightDetail.fxml");  
//log
        ClientUser user = getClientUser();
        if (user != null) {
            user.addActivity("Light " + lightId, "Opened light detail page");
        }
    }

    @FXML
    void light1ToggleButtonPressed(ActionEvent event) {
        handleLightToggle(1, light1ToggleButton);
    }

    @FXML
    void light2ToggleButtonPressed(ActionEvent event) {
        handleLightToggle(2, light2ToggleButton);
    }

    @FXML
    void light3ToggleButtonPressed(ActionEvent event) {
        handleLightToggle(3, light3ToggleButton);
    }

    private void handleLightToggle(int lightId, ToggleButton toggleButton) {
        boolean on = toggleButton.isSelected();
        if (on) {
            toggleButton.setText("ON");
            toggleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            toggleButton.setText("OFF");
            toggleButton.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-weight: bold;");
        }

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("TOGGLE_LIGHT");
        msg.add(lightId);
        msg.add(on);
        sendToServer(msg);

        ClientUser user = getClientUser();
        if (user != null) {
            user.addActivity("Light " + lightId,
                    "Light switched " + (on ? "ON" : "OFF"));
        }
    }

    @FXML
    void activityLogButtonPressed(ActionEvent event) {
        switchScene(event, "activityLogPage.fxml");
    }

    @FXML
    void devicesButtonPressed(ActionEvent event) {
        switchScene(event, "deviceMenu.fxml");
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
    void logoutButtonPressed(ActionEvent event) {
        sendToServer("logout");

        ClientUser user = getClientUser();
        if (user != null) {
            user.addActivity("System", "User logged out");
        }

        switchScene(event, "login.fxml");
    }
}
