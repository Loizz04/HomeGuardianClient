/*
 * Author: Lois Mathew
 */
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


public class CameraDetailController extends homeGuardianClientController {
    private static int currentCameraId = 1;
    public static void setCurrentCameraId(int id) {
        currentCameraId = id;}
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
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private ToggleButton motionToggleButton;

    @FXML
    private ToggleButton recordToggleButton;

    @FXML
    private Button settingsButton;
    
    @FXML
    private MediaView mediaView;

    private MediaPlayer currentPlayer;

    private final java.util.Map<String, String> footageFiles = new java.util.HashMap<>();

    @FXML
    public void initialize() {
        cameraId = currentCameraId;

        if (cameraTitleLabel != null) {
            cameraTitleLabel.setText("CAMERA " + cameraId);
        }
        //show options
        if (footageDropdown != null) {
            footageDropdown.getItems().clear();
            footageDropdown.getItems().addAll(
                    "Live Feed",
                    "Last Recording"
            );
            footageDropdown.setPromptText("Select Footage");
        }

        footageFiles.clear();
        footageFiles.put("Live Feed",
                "C:\\Users\\Lois\\git\\HomeGuardianClient\\HomeGuardianClient\\src\\footage2.mp4");
        footageFiles.put("Last Recording",
                "C:\\Users\\Lois\\git\\HomeGuardianClient\\HomeGuardianClient\\src\\footage1.mp4");

        styleToggle(recordToggleButton);
        styleToggle(motionToggleButton);
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
    void footageDropdownChanged(ActionEvent event) {
        String selected = footageDropdown.getValue();
        if (selected == null) return;
        String path = footageFiles.get(selected);
        if (path == null) {
            System.out.println("No video mapped for: " + selected);
            return;
        }

        try {
            // Convert Windows path -> URI -> Media
            File file = new File(path);
            String uri = file.toURI().toString();

            Media media = new Media(uri);
            currentPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(currentPlayer);
            currentPlayer.play();

            ArrayList<Object> msg = new ArrayList<>();
            msg.add("REQUEST_CAMERA_FOOTAGE");
            msg.add(cameraId);
            msg.add(selected);  
            sendToServer(msg);
            System.out.println("Playing '" + selected + "' from " + path);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not load video: " + path);
        }
    }

    @FXML
    void recordToggleButtonPressed(ActionEvent event) {
        boolean on = recordToggleButton.isSelected();
        styleToggle(recordToggleButton);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("TOGGLE_CAMERA_RECORDING");
        msg.add(cameraId);
        msg.add(on);

        sendToServer(msg);

        System.out.println("Recording " + (on ? "ON" : "OFF") +" for camera " + cameraId);
    }

    @FXML
    void motionToggleButtonPressed(ActionEvent event) {
        boolean on = motionToggleButton.isSelected();
        styleToggle(motionToggleButton);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("TOGGLE_CAMERA_MOTION");
        msg.add(cameraId);
        msg.add(on);
        sendToServer(msg);
        System.out.println("Motion sensor " + (on ? "ON" : "OFF") +" for camera " + cameraId);
    }

    private void styleToggle(ToggleButton button) {
        if (button == null) return;

        boolean on = button.isSelected();
        if (on) {
            button.setText("Turn off");
            button.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            button.setText("Turn on");
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }
}
