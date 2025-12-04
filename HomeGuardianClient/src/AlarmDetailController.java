/*
 * Author: Lois Mathew
 */
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

public class AlarmDetailController extends homeGuardianClientController {

    private static int currentAlarmId = 1;
    public static void setCurrentAlarmId(int id) {
        currentAlarmId = id;
    }

    //loader pattern 
    public void setAlarmNumber(int id) {
        currentAlarmId = id;
        alarmId = id;
        if (alarmTitleLabel != null) {
            alarmTitleLabel.setText("ALARM " + alarmId);
        }
    }

    private int alarmId = 1;

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

    @FXML
    public void initialize() {
        if (alarmId <= 0) {
            alarmId = currentAlarmId;
        }
        if (alarmTitleLabel != null) {
            alarmTitleLabel.setText("ALARM " + alarmId);
        }
        styleToggle(motionToggleButton);
        styleToggle(recordOnCamButton);
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
    void motionToggleButtonPressed(ActionEvent event) {
        boolean on = motionToggleButton.isSelected();
        styleToggle(motionToggleButton);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("TOGGLE_ALARM_MOTION");
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
        msg.add("TOGGLE_ALARM_RECORD_ON_CAM");
        msg.add(alarmId);
        msg.add(on);

        sendToServer(msg);
        System.out.println("Alarm " + alarmId + " record-on-camera: " + (on ? "ON" : "OFF"));
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