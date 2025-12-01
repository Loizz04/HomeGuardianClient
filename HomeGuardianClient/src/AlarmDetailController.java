import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

public class AlarmDetailController extends homeGuardianClientController {

    // -------- which alarm is this screen for? (1, 2, or 3)
    private static int currentAlarmId = 1;

    public static void setCurrentAlarmId(int id) {
        currentAlarmId = id;
    }

    // For the loader pattern we used earlier:
    public void setAlarmNumber(int id) {
        currentAlarmId = id;
        alarmId = id;
        if (alarmTitleLabel != null) {
            alarmTitleLabel.setText("ALARM " + alarmId);
        }
    }

    private int alarmId = 1;

    // -------- FXML fields --------

    @FXML
    private Button activityLogButton;

    @FXML
    private Label alarmTitleLabel;

    @FXML
    private Button devicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private ToggleButton motionToggleButton;

    @FXML
    private ToggleButton recordOnCamButton;

    @FXML
    private Button settingsButton;

    // -------- initialize --------
    @FXML
    public void initialize() {
        // which alarm is this instance for
        if (alarmId <= 0) {
            alarmId = currentAlarmId;
        }

        if (alarmTitleLabel != null) {
            alarmTitleLabel.setText("ALARM " + alarmId);
        }

        // style both toggles based on their initial state
        styleToggle(motionToggleButton);
        styleToggle(recordOnCamButton);
    }

    // -------- bottom nav buttons --------

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
        switchScene(event, "settings.fxml");
    }

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        sendToServer("logout");
        switchScene(event, "login.fxml");
    }

    // -------- toggle handlers --------

    @FXML
    void motionToggleButtonPressed(ActionEvent event) {
        boolean on = motionToggleButton.isSelected();
        styleToggle(motionToggleButton);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("toggleAlarmMotion");
        msg.add(alarmId);
        msg.add(on);

        sendToServer(msg);
        System.out.println("Alarm " + alarmId + " motion sensor: " + (on ? "ON" : "OFF"));
    }

    @FXML
    void recordOnCamButtonPressed(ActionEvent event) {
        boolean on = recordOnCamButton.isSelected();
        styleToggle(recordOnCamButton);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("toggleAlarmRecordOnCam");
        msg.add(alarmId);
        msg.add(on);

        sendToServer(msg);
        System.out.println("Alarm " + alarmId + " record-on-camera: " + (on ? "ON" : "OFF"));
    }

    // -------- helper: style toggle buttons --------

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