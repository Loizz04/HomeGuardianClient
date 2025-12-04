/*
 * Author: Lois Mathew
 */
import java.util.ArrayList;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class cameraController extends homeGuardianClientController {

    @FXML
    private Button activityLogButton;

    @FXML
    private ImageView camera1Button;

    @FXML
    private ToggleButton camera1ToggleButton;

    @FXML
    private ImageView camera2Button;

    @FXML
    private ToggleButton camera2ToggleButton;

    @FXML
    private ImageView camera3Button;

    @FXML
    private ToggleButton camera3ToggleButton;

    @FXML
    private Button devicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button settingsButton;

    @FXML
    void camera1ButtonClicked(MouseEvent event) {
        openCameraDetail(event, 1);
    }

    @FXML
    void camera2ButtonClicked(MouseEvent event) {
        openCameraDetail(event, 2);
    }

    @FXML
    void camera3ButtonClicked(MouseEvent event) {
        openCameraDetail(event, 3);
    }

    private void openCameraDetail(MouseEvent event, int cameraId) {
        CameraDetailController.setCurrentCameraId(cameraId);
        switchScene(event, "CameraDetail.fxml");  // shared detail page
    }

    @FXML
    void camera1ToggleButtonPressed(ActionEvent event) {
        handleCameraToggle(1, camera1ToggleButton);
    }

    @FXML
    void camera2ToggleButtonPressed(ActionEvent event) {
        handleCameraToggle(2, camera2ToggleButton);
    }

    @FXML
    void camera3ToggleButtonPressed(ActionEvent event) {
        handleCameraToggle(3, camera3ToggleButton);
    }

    private void handleCameraToggle(int cameraId, ToggleButton toggleButton) {
        boolean on = toggleButton.isSelected();
        if (on) {
            toggleButton.setText("ON");
            toggleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            toggleButton.setText("OFF");
            toggleButton.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-weight: bold;");
        }

        //Send to server
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("TOGGLE_CAMERA");
        msg.add(cameraId);
        msg.add(on);

        sendToServer(msg);
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
        switchScene(event, "login.fxml");
    }

    @FXML
    public void initialize() {
        styleToggle(camera1ToggleButton);
        styleToggle(camera2ToggleButton);
        styleToggle(camera3ToggleButton);
    }

    private void styleToggle(ToggleButton button) {
        boolean on = button.isSelected();

        if (on) {
            button.setText("ON");
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            button.setText("OFF");
            button.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }
}
