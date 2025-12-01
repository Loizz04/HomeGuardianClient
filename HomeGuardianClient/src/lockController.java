import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class lockController extends homeGuardianClientController {

    @FXML
    private Button activityLogButton;

    @FXML
    private Button devicesButton;

    @FXML
    private ImageView lock1Button;

    @FXML
    private ToggleButton lock1ToggleButton;

    @FXML
    private ImageView lock2Button;

    @FXML
    private ToggleButton lock2ToggleButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button settingsButton;

    // ===================== INITIALIZE =====================

    @FXML
    public void initialize() {
        styleToggle(lock1ToggleButton);
        styleToggle(lock2ToggleButton);
    }

    // ===================== BOTTOM NAV =====================

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

    //@FXML
    //void settingsButtonPressed(ActionEvent event) {
      //  switchScene(event, "settings.fxml");
    //}

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        sendToServer("logout");
        switchScene(event, "login.fxml");
    }

    // ===================== ICON CLICKS → DETAIL PAGE =====================

    @FXML
    void lock1ButtonClicked(MouseEvent event) {
        LockDetailController.setCurrentLockId(1);
        switchScene(event, "LockDetail.fxml");
    }

    @FXML
    void lock2ButtonClicked(MouseEvent event) {
        LockDetailController.setCurrentLockId(2);
        switchScene(event, "LockDetail.fxml");
    }

    // ===================== TOGGLE HANDLERS =====================

    @FXML
    void lock1ToggleButtonPressed(ActionEvent event) {
        handleLockToggle(1, lock1ToggleButton);
    }

    @FXML
    void lock2ToggleButtonPressed(ActionEvent event) {
        handleLockToggle(2, lock2ToggleButton);
    }

    private void handleLockToggle(int lockId, ToggleButton toggleButton) {
        boolean engaged = toggleButton.isSelected(); // true = locked/engaged

        styleToggle(toggleButton);

        ArrayList<Object> msg = new ArrayList<>();
        msg.add("toggleLock");
        msg.add(lockId);
        msg.add(engaged);

        sendToServer(msg);

        System.out.println("Lock " + lockId + " is now " + (engaged ? "ENGAGED" : "DISENGAGED"));
    }

    // ===================== STYLE TOGGLES (ENGAGE / DISENGAGE) =====================

    private void styleToggle(ToggleButton button) {
        if (button == null) return;

        boolean engaged = button.isSelected();

        if (engaged) {
            // Currently locked → show red "Disengage" (to unlock)
            button.setText("Disengage");
            button.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            // Currently unlocked → show green "Engage" (to lock)
            button.setText("Engage");
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }
}
