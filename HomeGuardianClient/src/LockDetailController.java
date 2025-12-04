import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public class LockDetailController extends homeGuardianClientController {

    // Which lock is this detail screen for? (1 or 2)
    private static int currentLockId = 1;

    public static void setCurrentLockId(int id) {
        currentLockId = id;
    }

    private int lockId = 1;

    @FXML
    private Button activityLogButton;

    @FXML
    private Button devicesButton;

    @FXML
    private ToggleButton linkToAlarmButton;

    @FXML
    private Label lockTitleLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Slider timeoutSlider;

    @FXML
    private TextField timeoutTextField;

    // =============== INITIALIZE ===============

    @FXML
    public void initialize() {
        // figure out which lock this page is for
        lockId = currentLockId;

        if (lockTitleLabel != null) {
            lockTitleLabel.setText("LOCK " + lockId);
        }

        // optional defaults
        if (timeoutTextField != null && timeoutTextField.getText().isEmpty()) {
            timeoutTextField.setText("0");
        }

        styleToggle(linkToAlarmButton);
    }

    // =============== BOTTOM NAV BUTTONS ===============

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
    void timeoutSliderMoved(MouseEvent event) {
        int minutes = (int) timeoutSlider.getValue();
        timeoutTextField.setText(Integer.toString(minutes));
        sendLockDurationToServer(minutes);
    }

    @FXML
    void timeoutTextFieldTyped(ActionEvent event) {
        try {
            int minutes = Integer.parseInt(timeoutTextField.getText().trim());
            if (minutes < 0) minutes = 0;
            timeoutSlider.setValue(minutes);
            sendLockDurationToServer(minutes);
        } catch (NumberFormatException e) {
            // reset to slider value if invalid
            timeoutTextField.setText(Integer.toString((int) timeoutSlider.getValue()));
        }
    }

    private void sendLockDurationToServer(int minutes) {
        ArrayList<Object> msg = new ArrayList<>();
        msg.add("SET_LOCK_DURATION");
        msg.add(lockId);
        msg.add(minutes);
        sendToServer(msg);

        System.out.println("Lock " + lockId + " duration set to " + minutes + " minutes");
    }

    // =============== LINK TO ALARM TOGGLE ===============

    @FXML
    void linkToAlarmButtonPressed(ActionEvent event) {
        boolean linked = linkToAlarmButton.isSelected();
        styleToggle(linkToAlarmButton);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("LINK_LOCK_TO_ALARM");
        msg.add(lockId);
        msg.add(linked);
        sendToServer(msg);

        System.out.println("Lock " + lockId + " linked to alarm: " + linked);
    }

    // =============== HELPER: STYLE TOGGLE ===============

    private void styleToggle(ToggleButton button) {
        if (button == null) return;

        boolean on = button.isSelected();

        if (on) {
            // currently linked → show red "Turn off"
            button.setText("Turn off");
            button.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            // currently not linked → show green "Turn on"
            button.setText("Turn on");
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }
}
