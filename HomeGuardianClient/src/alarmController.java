import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class alarmController extends homeGuardianClientController {

    // --- Styles for toggle buttons ---
    private static final String TOGGLE_ON_STYLE =
            "-fx-background-color: #28a745; -fx-text-fill: white;"; // green
    private static final String TOGGLE_OFF_STYLE =
            "-fx-background-color: #dc3545; -fx-text-fill: white;"; // red

    @FXML
    private Button activityLogButton;

    @FXML
    private ImageView alarm1Button;

    @FXML
    private ToggleButton alarm1ToggleButton;

    @FXML
    private ImageView alarm2Button;

    @FXML
    private ToggleButton alarm2ToggleButton;

    @FXML
    private ImageView alarm3Button;

    @FXML
    private ToggleButton alarm3ToggleButton;

    @FXML
    private Button devicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button settingsButton;

    @FXML
    private void initialize() {
        // Set all toggles to default OFF look at startup
        initToggle(alarm1ToggleButton);
        initToggle(alarm2ToggleButton);
        initToggle(alarm3ToggleButton);
    }

    private void initToggle(ToggleButton toggle) {
        if (toggle == null) return;
        toggle.setSelected(false);
        applyToggleStyle(toggle);
    }

    private void applyToggleStyle(ToggleButton toggle) {
        if (toggle.isSelected()) {
            toggle.setText("On");
            toggle.setStyle(TOGGLE_ON_STYLE);
        } else {
            toggle.setText("Off");
            toggle.setStyle(TOGGLE_OFF_STYLE);
        }
    }

    private void handleAlarmToggle(ToggleButton toggle, int alarmNumber) {
        if (toggle == null) return;
        applyToggleStyle(toggle);

        boolean on = toggle.isSelected();
        // Simple message format – adjust if your server expects something else
        String msg = "TOGGLE_ALARM" + alarmNumber + " " + (on ? "ON" : "OFF");
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
    void alarm1ButtonClicked(MouseEvent event) {
        openAlarmDetail(1, event);
    }

    @FXML
    void alarm2ButtonClicked(MouseEvent event) {
        openAlarmDetail(2, event);
    }

    @FXML
    void alarm3ButtonClicked(MouseEvent event) {
        openAlarmDetail(3, event);
    }

    private void openAlarmDetail(int alarmNumber, MouseEvent event) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("AlarmDetail.fxml"));
            Parent root = loader.load();

            AlarmDetailController controller = loader.getController();
            controller.setAlarmNumber(alarmNumber);
            
            Node sourceNode = (Node) event.getSource();
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void alarm1ToggleButtonPressed(ActionEvent event) {
        handleAlarmToggle(alarm1ToggleButton, 1);
    }

    @FXML
    void alarm2ToggleButtonPressed(ActionEvent event) {
        handleAlarmToggle(alarm2ToggleButton, 2);
    }

    @FXML
    void alarm3ToggleButtonPressed(ActionEvent event) {
        handleAlarmToggle(alarm3ToggleButton, 3);
    }


}
