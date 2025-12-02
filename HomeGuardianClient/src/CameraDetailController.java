import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;

public class CameraDetailController extends homeGuardianClientController {

    // Will be set from cameraController before switching scenes
    private static int currentCameraId = 1;

    public static void setCurrentCameraId(int id) {
        currentCameraId = id;
    }

    private int cameraId = 1;

    @FXML
    private Button activityLogButton;

    @FXML
    private Label cameraTitleLabel;

    @FXML
    private Button devicesButton;

    @FXML
    private ComboBox<String> footageDropdown;

    @FXML
    private ImageView footageview;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private ToggleButton motionToggleButton;

    @FXML
    private ToggleButton recordToggleButton;

    @FXML
    private Button settingsButton;

    // ================== INITIALIZE ==================

    @FXML
    public void initialize() {
        // Set which camera this detail page is for
        cameraId = currentCameraId;

        // Update title: "CAMERA 1", "CAMERA 2", etc.
        if (cameraTitleLabel != null) {
            cameraTitleLabel.setText("CAMERA " + cameraId);
        }

        // Populate footage dropdown with sample options
        if (footageDropdown != null) {
            footageDropdown.getItems().clear();
            footageDropdown.getItems().addAll(
                    "Live Feed",
                    "Last Recording",
                    "Recording 1",
                    "Recording 2",
                    "Recording 3"
            );
            footageDropdown.setPromptText("Select Footage");
        }

        // Style toggles based on initial selected state
        styleToggle(recordToggleButton);
        styleToggle(motionToggleButton);
    }

    // ================== BOTTOM NAV BUTTONS ==================

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

    // ================== FOOTAGE DROPDOWN ==================

    @FXML
    void footageDropdownChanged(ActionEvent event) {
        String selected = footageDropdown.getValue();
        if (selected == null) return;

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("getCameraFootage");
        msg.add(cameraId);
        msg.add(selected);

        sendToServer(msg);

        System.out.println("Requested footage '" + selected + "' for camera " + cameraId);

        // Later, when the server responds with an image/frame,
        // you'll update 'footageview' in handleMessageFromServer().
    }

    // ================== TOGGLE BUTTONS ==================

    @FXML
    void recordToggleButtonPressed(ActionEvent event) {
        boolean on = recordToggleButton.isSelected();
        styleToggle(recordToggleButton);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("toggleCameraRecording");
        msg.add(cameraId);
        msg.add(on);

        sendToServer(msg);

        System.out.println("Recording " + (on ? "ON" : "OFF") +
                           " for camera " + cameraId);
    }

    @FXML
    void motionToggleButtonPressed(ActionEvent event) {
        boolean on = motionToggleButton.isSelected();
        styleToggle(motionToggleButton);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("toggleCameraMotion");
        msg.add(cameraId);
        msg.add(on);

        sendToServer(msg);

        System.out.println("Motion sensor " + (on ? "ON" : "OFF") +
                           " for camera " + cameraId);
    }

    // ================== HELPER: STYLE TOGGLES ==================

    private void styleToggle(ToggleButton button) {
        if (button == null) return;

        boolean on = button.isSelected();
        if (on) {
            button.setText("Turn off");
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            button.setText("Turn on");
            button.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }
}
